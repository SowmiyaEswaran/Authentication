package com.auth.data.response;

import static com.auth.common.Status.*;

import com.auth.common.Status;
import com.auth.exception.AppException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class GenericResponse <T>{
	@JsonIgnore
	private Integer httpStatusCode = 200;
	private String exCode;
	private int errorCode;
	private Status status = SUCCESS;
	private T data;
	private String message;
	@JsonIgnore
	private Throwable exception;
	
	public GenericResponse(Throwable exception) {
		if (exception == null) {
			return;
		}
		if(exception instanceof AppException) {
			errorCode = ((AppException) exception).getErrorCode();
		}
		exCode = exception.getClass().getSimpleName();
		status= FAILURE;
		
		this.setException(exception);
	}

	
}
