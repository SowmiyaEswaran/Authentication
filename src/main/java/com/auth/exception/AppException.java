package com.auth.exception;

import static org.apache.http.HttpStatus.SC_OK;

import org.apache.http.HttpStatus;

import com.auth.common.CommonConstants;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppException  extends RuntimeException {

	private static final long serialVersionUID = -720664400716034134L;
	protected int httpErrorCode = SC_OK;
	protected int errorCode = Integer.MAX_VALUE;
	protected String errorCodeValue = null;
	
	public AppException() {
		super();
	}

	public AppException(String message, Throwable cause) {
		super(message, cause);
	}

	public AppException(String message) {
		super(message);
	}

	public AppException(Throwable cause) {
		super(cause);
	}
	
	public AppException(int errCode, int httpErrCode, String message) {
		this(message);
		errorCode = errCode;
		httpErrorCode = httpErrCode;
	}
	
	public static final class UserNotFoundException extends AppException {
		private static final long serialVersionUID = 1L;

		public UserNotFoundException() {
			super(5001, HttpStatus.SC_NOT_FOUND, CommonConstants.USER_NOT_FOUND);
		}
		public UserNotFoundException(String message) {
			super(5001, HttpStatus.SC_NOT_FOUND, message);
		}
	}
	public static final class PasswordIncorrectException extends AppException {
		private static final long serialVersionUID = 1L;

		public PasswordIncorrectException() {
			super(5002, HttpStatus.SC_NOT_FOUND, CommonConstants.PASSWORD_INCORRECT);
		}
		public PasswordIncorrectException(String message) {
			super(5002, HttpStatus.SC_NOT_FOUND, message +" " +CommonConstants.PASSWORD_INCORRET_ATTEMPT);
			}
	}
	public static final class TokenExpiredException extends AppException {
		private static final long serialVersionUID = 1L;

		public TokenExpiredException() {
			super(5003, HttpStatus.SC_NOT_FOUND, CommonConstants.TOKEN_EXPIRED);
		}
	}
	public static final class TokenUnavailableException extends AppException {
		private static final long serialVersionUID = 1L;

		public TokenUnavailableException() {
			super(5004, HttpStatus.SC_NOT_FOUND, CommonConstants.TOKEN_UNAVAILABLE);
		}
	}
	public static final class StudentNotFoundException extends AppException {
		private static final long serialVersionUID = 1L;

		public StudentNotFoundException() {
			super(5005, HttpStatus.SC_NOT_FOUND, CommonConstants.STUDENT_NOT_FOUND);
		}
	}
	public static final class UserNotActiveException extends AppException {
		private static final long serialVersionUID = 1L;

		public UserNotActiveException() {
			super(5006, HttpStatus.SC_NOT_FOUND, CommonConstants.USER_NOT_ACTIVE);
		}
	}
	public static final class EmailFailedException extends AppException {
		private static final long serialVersionUID = 1L;

		public EmailFailedException() {
			super(5007, HttpStatus.SC_SERVICE_UNAVAILABLE, CommonConstants.EMAIL_FAILED);
		}
	}
	public static final class InvalidHeaderException extends AppException {
		private static final long serialVersionUID = 1L;

		public InvalidHeaderException(String message) {
			super(5008, HttpStatus.SC_NOT_FOUND, message);
		}
	}

	public static final class AccountLockedException extends AppException {
		private static final long serialVersionUID = 1L;

		public AccountLockedException() {
			super(5009, HttpStatus.SC_NOT_FOUND, CommonConstants.ACCOUNT_LOCKED);
		}
		public AccountLockedException(String message) {
			super(5009, HttpStatus.SC_NOT_FOUND, message);
		}
	}
	public static final class InformationMismatchException extends AppException {
		private static final long serialVersionUID = 1L;

		public InformationMismatchException() {
			super(5010, HttpStatus.SC_NOT_FOUND, CommonConstants.INFORMATIONMISMATCH);
		}
		public InformationMismatchException(String message) {
			super(5010, HttpStatus.SC_NOT_FOUND, message);
		}
	}
	public static final class SetupPasswordException extends AppException {
		private static final long serialVersionUID = 1L;

		public SetupPasswordException() {
			super(5010, HttpStatus.SC_NOT_FOUND, CommonConstants.SETUP_PASSWORD);
		}
		public SetupPasswordException(String message) {
			super(5010, HttpStatus.SC_NOT_FOUND, message);
		}
	}

}