package com.auth.data.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuccessResponse {
	
	private String loginId;
	private String emailId;
	private String role;
	private String status;
	private LocalDateTime lastLoginDate;
	private String userId;
	private String studentId;
	private String tutorId;
	private Long institutionId;

}