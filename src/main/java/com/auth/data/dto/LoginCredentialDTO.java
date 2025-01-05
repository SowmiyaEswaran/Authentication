package com.auth.data.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginCredentialDTO {
	
	private String loginId;
	private String emailId;
	private String password;
	private String role;
	private String createdBy;
	private String creationDate;
	private String newPassword;
	private String passwordResetToken;
	private String emailVerifyToken;
	private String userId;
}
