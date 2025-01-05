package com.auth.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth.common.CommonConstants;
import com.auth.data.dto.LoginCredentialDTO;
import com.auth.data.entity.LoginCredential;
import com.auth.data.entity.Roles;
import com.auth.data.entity.UserDetails;
import com.auth.data.response.SuccessResponse;
import com.auth.exception.MyTuitionCenterAppException.AccountLockedException;
import com.auth.exception.MyTuitionCenterAppException.PasswordIncorrectException;
import com.auth.exception.MyTuitionCenterAppException.SetupPasswordException;
import com.auth.exception.MyTuitionCenterAppException.TokenExpiredException;
import com.auth.exception.MyTuitionCenterAppException.TokenUnavailableException;
import com.auth.exception.MyTuitionCenterAppException.UserNotActiveException;
import com.auth.exception.MyTuitionCenterAppException.UserNotFoundException;
import com.auth.repository.AuthRepository;
import com.auth.repository.RolesRepository;
import com.auth.repository.UserDetailsRepository;
import com.auth.service.AuthService;
import com.auth.service.EmailService;
import com.auth.service.JwtService;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private AuthRepository authRepository;
	
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	
	@Autowired
	private RolesRepository roleRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtService jwtService;

	@Value("${mytuitioncenter.passwordReset.url}")
	private String passwordResetUrl;
	
	@Value("${mytuitioncenter.validateEmail.url}")
	private String ValidateEmailUrl;

	@Autowired
	private EmailService emailService;
	
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

	Integer loginAttemptCount = 0;

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);


	@Override
	public SuccessResponse signUp(LoginCredentialDTO loginCredentialsDTO) {
		Optional<LoginCredential> loginCredentials = authRepository.findByEmailId(loginCredentialsDTO.getEmailId());
		SuccessResponse response = new SuccessResponse();
		if (loginCredentials.isEmpty()) {
			Roles role = roleRepository.findByRoleName(loginCredentialsDTO.getRole());
			
			String encodedPassword = passwordEncoder.encode(loginCredentialsDTO.getPassword());
			LoginCredential loginCredential = modelMapper.map(loginCredentialsDTO, LoginCredential.class);
			
			if(!role.getRoleName().equalsIgnoreCase(CommonConstants.ADMIN)) {
				String token  = jwtService.generateValidateEmailToken(loginCredential);
				loginCredential.setEmailVerifyToken(token);
			}
			
			loginCredential.setPassword(encodedPassword);
			loginCredential.setCreationDate(LocalDateTime.now());
			loginCredential.setRole(role);
			loginCredential.setAccountLocked(false);
			loginCredential.setAttemptCount(0);
			loginCredential.setLastAttemptDate(null);
			loginCredential = authRepository.save(loginCredential);
			
			
			response.setLoginId(loginCredential.getLoginId());
			response.setEmailId(loginCredential.getEmailId());
			response.setRole(loginCredential.getRole().getRoleName());
			response.setStatus(CommonConstants.REGISTER);
			
		} else {
			LoginCredential loginCredential = loginCredentials.get();
			response.setLoginId(loginCredential.getLoginId());
			response.setEmailId(loginCredential.getEmailId());
			response.setRole(loginCredential.getRole().getRoleName());
			response.setStatus(CommonConstants.USER_ALREADY_EXSIST);
			}
		return response;
	}

	@Override
	public SuccessResponse signIn(LoginCredentialDTO loginCredentialsDTO) {
		LoginCredential loginCredential = authRepository.findByEmailId(loginCredentialsDTO.getEmailId())
				.orElseThrow(() -> new UserNotFoundException());

		//if user is admin directly process to login
		if(loginCredential.getRole().getRoleName().equalsIgnoreCase(CommonConstants.ADMIN)) {
			loginAttemptCount = loginCredential.getAttemptCount();

			if(!(loginCredential.isAccountLocked())) {
				
				if (passwordEncoder.matches(loginCredentialsDTO.getPassword(), loginCredential.getPassword())) {
					loginAttemptCount = 0;
					loginCredential.setAccountLocked(false);
					loginCredential.setAttemptCount(loginAttemptCount);
					loginCredential.setLastAttemptDate(null);
					authRepository.save(loginCredential);
					return processLogin(loginCredential, null);
				} else {
						loginAttemptCount++;
						loginCredential.setAttemptCount(loginAttemptCount);
						loginCredential.setLastAttemptDate(LocalDateTime.now());
						if(loginCredential.getAttemptCount() >= 3 ) {
							loginCredential.setAccountLocked(true);
						}
					}
					
					authRepository.save(loginCredential);

					throw new PasswordIncorrectException(loginAttemptCount +"");
				} else {
				throw new AccountLockedException();
			}
		}
		UserDetails userDetails = userDetailsRepository.findbyLoginId(loginCredential.getLoginId())
				.orElseThrow(() -> new UserNotFoundException());
		
		//first login, soft login to validate the email
		if(loginCredentialsDTO.getEmailVerifyToken() != null ) {
			//for testing purpose: to be removed
			logger.info("emailId:dto: "+loginCredentialsDTO.getEmailId()+": entity: "+loginCredential.getEmailId());
			logger.info("token:dto: "+loginCredentialsDTO.getEmailVerifyToken()+": entity: "+loginCredential.getEmailVerifyToken());

			return handleEmailVerification(loginCredential, userDetails);
		}
		
		//check if user isactive then proceed login
		if(userDetails != null && userDetails.isActive()) {
			loginAttemptCount = loginCredential.getAttemptCount();

			if(!(loginCredential.isAccountLocked())) {
				if(loginCredential.getPassword() == null) {
					throw new SetupPasswordException();
				}
				
				if (passwordEncoder.matches(loginCredentialsDTO.getPassword(), loginCredential.getPassword())) {
					loginAttemptCount = 0;
					loginCredential.setAccountLocked(false);
					loginCredential.setAttemptCount(loginAttemptCount);
					loginCredential.setLastAttemptDate(null);
					authRepository.save(loginCredential);
					return processLogin(loginCredential, userDetails.getUserId());
				} else {
						loginAttemptCount++;
						loginCredential.setAttemptCount(loginAttemptCount);
						loginCredential.setLastAttemptDate(LocalDateTime.now());
						if(loginCredential.getAttemptCount() >= 3 ) {
							loginCredential.setAccountLocked(true);
						}
					}
					
					authRepository.save(loginCredential);

					throw new PasswordIncorrectException(loginAttemptCount +"");
				} else {
				throw new AccountLockedException();
			}
		}else {
				throw new UserNotActiveException();
			}

	}

	private SuccessResponse handleEmailVerification(LoginCredential loginCredential, UserDetails userdetails) {
		if(!jwtService.validateEmailToken(loginCredential)) {
			
			loginCredential.setEmailVerifyToken(CommonConstants.VERIFIED);
			userdetails.setActive(true);
			userdetails.setModifiedDate(LocalDateTime.now());
			logger.info("EMAIL_VERIFIED : from email : "+loginCredential.getEmailId());

			userdetails.setStatus(com.auth.data.entity.Status.EMAIL_VERIFIED);
			userDetailsRepository.save(userdetails);
			
			return processLogin(loginCredential,userdetails.getUserId());
		}else {
			//if email verification token expired resend the email automatically
			String token  = jwtService.generateValidateEmailToken(loginCredential);
			loginCredential.setEmailVerifyToken(token);
			authRepository.save(loginCredential);
			
			StringBuffer verificationUrl = new StringBuffer();
			verificationUrl.append(ValidateEmailUrl)
			              .append(loginCredential.getEmailVerifyToken())
			              .append(CommonConstants.EMAIL_PATH)
			              .append(loginCredential.getEmailId())
			              .append(CommonConstants.ROLE_PATH)
			              .append(loginCredential.getRole().getRoleName());

			
			  executorService.submit(() -> {
				  			emailService.validateEmailTriggerMail(loginCredential.getEmailId(),verificationUrl.toString(),userdetails); 
				  			});
			SuccessResponse response = new SuccessResponse();
			response.setLoginId(loginCredential.getLoginId());
			response.setEmailId(loginCredential.getEmailId());
			response.setRole(loginCredential.getRole().getRoleName());
			response.setStatus(CommonConstants.VERIFICATION_EMAIL_FAILED);
			return response;
		}		
	}

	private SuccessResponse processLogin(LoginCredential loginCredential, String userId) {
		LocalDateTime lastLogin = loginCredential.getLastLoginDate();
		loginCredential.setLastLoginDate(LocalDateTime.now());
		authRepository.save(loginCredential);
		SuccessResponse response = new SuccessResponse();
	
		response.setLoginId(loginCredential.getLoginId());
		response.setEmailId(loginCredential.getEmailId());
		response.setRole(loginCredential.getRole().getRoleName());
		response.setLastLoginDate(lastLogin);
		response.setUserId(userId);
		response.setStatus(com.auth.common.Status.SUCCESS.toString());
		
		return response;
	}


	@Override
	
		
		public SuccessResponse resetPasswordLink(String emailId) {
		LoginCredential loginCredentials = authRepository.findByEmailId(emailId)
												.orElseThrow(() -> new UserNotFoundException());
		
		UserDetails userdetails =userDetailsRepository.findByEmailId(emailId);
		
		if(userdetails != null && userdetails.isActive()) {
			String token = jwtService.generateTokenPasswordReset(loginCredentials);
			loginCredentials.setResetPasswordToken(token);

			authRepository.save(loginCredentials);
			
			StringBuffer resetUrl = new StringBuffer();
			resetUrl.append(passwordResetUrl)
			              .append(loginCredentials.getResetPasswordToken())
			              .append(CommonConstants.EMAIL_PATH)
			              .append(loginCredentials.getEmailId());
			               
			executorService.submit(() -> {
				
				emailService.resetPasswordTriggerMail(loginCredentials.getEmailId(), resetUrl.toString(),userdetails);
			});
			
			SuccessResponse response = new SuccessResponse();
			response.setLoginId(loginCredentials.getLoginId());
			response.setEmailId(loginCredentials.getEmailId());
			response.setRole(loginCredentials.getRole().getRoleName());
			response.setStatus(CommonConstants.EMAIL_SENT_SUCCESSFULLY);
			return response;
		}else {
			throw new UserNotActiveException();
		}
	}

	@Override
	public SuccessResponse resetPassword(LoginCredentialDTO loginCredentialDTO) {
		LoginCredential loginCredentials = authRepository
				.findByResetPasswordToken(loginCredentialDTO.getPasswordResetToken());
		if (loginCredentials != null) {
			if (!jwtService.validateTokenPasswordReset(loginCredentials)) {
				//setup password if user created by admin, will receive only newPassword
				if(loginCredentials.getPassword()==null && loginCredentials.getCreatedBy().equalsIgnoreCase(CommonConstants.ADMIN)) {
					UserDetails user = userDetailsRepository.findbyLoginId(loginCredentials.getLoginId()).get();
					user.setActive(true);
					loginCredentials.setResetPasswordToken(null);
					loginCredentials.setEmailVerifyToken(CommonConstants.VERIFIED);
					if(loginCredentials.getRole().getRoleName().equalsIgnoreCase(CommonConstants.OPS_MANAGER)) {
						user.setStatus(com.auth.data.entity.Status.ACTIVE);
					}else if(user.getStatus().equals(com.auth.data.entity.Status.NEW)) {
						user.setStatus(com.auth.data.entity.Status.EMAIL_VERIFIED);
					}
					userDetailsRepository.save(user);
				} else {
					loginCredentials.setResetPasswordToken(null);
				}
				return encodePasswordUpdate(loginCredentialDTO,loginCredentials);

			} else {
				loginCredentials.setResetPasswordToken(null);
				loginCredentials = authRepository.save(loginCredentials);
				throw new TokenExpiredException();
			}
		} else {
			throw new TokenUnavailableException();
		}
	}
	@Override
	public SuccessResponse changePassword(LoginCredentialDTO loginCredentialsDTO) {
		if(loginCredentialsDTO.getUserId() != null && !loginCredentialsDTO.getUserId().isEmpty()) {

			UserDetails userDetails = userDetailsRepository.findById(loginCredentialsDTO.getUserId()).get();

			if(userDetails != null) {
				String login_id = userDetails.getLoginCredential().getLoginId();
				if (loginCredentialsDTO.getNewPassword() != null && !loginCredentialsDTO.getNewPassword().isEmpty()) {
					LoginCredential loginCredentials = authRepository.findByLoginId(login_id);
					if (loginCredentials != null && passwordEncoder.matches(loginCredentialsDTO.getPassword(), loginCredentials.getPassword())) {
						if (!(passwordEncoder.matches(loginCredentialsDTO.getNewPassword(), loginCredentials.getPassword()))) {
							return encodePasswordUpdate(loginCredentialsDTO,loginCredentials);
						} else {
							throw new PasswordIncorrectException(CommonConstants.PREVIOUS_PASSWORD_REPEAT); //previous password can't be given
						}
					}else {
						throw new PasswordIncorrectException(CommonConstants.PASSWORD_MISMATCH); //previous password can't be given
					}
					
				} else {
					throw new PasswordIncorrectException(CommonConstants.PASSWORD_INCORRECT); //newPassword has to be entered
				}
			}else throw new UserNotFoundException();
		}else {
			throw new UserNotFoundException();
		}
	}

	public SuccessResponse encodePasswordUpdate(LoginCredentialDTO loginCredentialsDTO, LoginCredential loginCredentials){
		String encodedPassword = passwordEncoder.encode(loginCredentialsDTO.getNewPassword());
		loginCredentials.setPassword(encodedPassword);
		loginCredentials.setAttemptCount(0);
		loginCredentials.setAccountLocked(false);
		loginCredentials.setLastAttemptDate(null);
		loginCredentials = authRepository.save(loginCredentials);
		
		SuccessResponse response = new SuccessResponse();
		response.setLoginId(loginCredentials.getLoginId());
		response.setEmailId(loginCredentials.getEmailId());
		response.setRole(loginCredentials.getRole().getRoleName());
		response.setStatus(CommonConstants.PASWORD_UPDATED);
		return response;
			
	}
}
