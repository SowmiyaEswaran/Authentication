package com.auth.service;

import com.auth.data.dto.LoginCredentialDTO;
import com.auth.data.response.SuccessResponse;

public interface AuthService {

	SuccessResponse signUp(LoginCredentialDTO loginCredentialsDTO);

	SuccessResponse resetPassword(LoginCredentialDTO loginCredentialsDTO);

	SuccessResponse signIn(LoginCredentialDTO loginCredentialsDTO);
	
	SuccessResponse resetPasswordLink(String emailId);

	SuccessResponse changePassword(LoginCredentialDTO loginCredentialsDTO);

}
