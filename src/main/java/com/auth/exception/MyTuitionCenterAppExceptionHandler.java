package com.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.auth.data.response.GenericResponse;
import com.auth.exception.MyTuitionCenterAppException.*;

import jakarta.servlet.http.HttpServletResponse;

@RestControllerAdvice
public class MyTuitionCenterAppExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<GenericResponse<Object>> handleEmailException(UserNotFoundException ex) {
    	return HttpResponseUtils.prepareErrorResponse(ex,ex.getHttpErrorCode(),ex.getMessage());
    }
    
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(PasswordIncorrectException.class)
    public ResponseEntity<GenericResponse<Object>> handlePasswordIncorrectException(PasswordIncorrectException ex) {
    	return HttpResponseUtils.prepareErrorResponse(ex,ex.getHttpErrorCode(),ex.getMessage());
    }

    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<GenericResponse<Object>> handleTokenExpiredException(TokenExpiredException ex) {
    	return HttpResponseUtils.prepareErrorResponse(ex,ex.getHttpErrorCode(),ex.getMessage());

    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(TokenUnavailableException.class)
    public ResponseEntity<GenericResponse<Object>> handleUnavailableException(TokenUnavailableException ex) {
    	return HttpResponseUtils.prepareErrorResponse(ex,ex.getHttpErrorCode(),ex.getMessage());

    }
    

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<GenericResponse<Object>> StudentNotFoundException(StudentNotFoundException ex) {
    	return HttpResponseUtils.prepareErrorResponse(ex,ex.getHttpErrorCode(),ex.getMessage());

    }
    
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(UserNotActiveException.class)
    public ResponseEntity<GenericResponse<Object>> handleUserNotActiveException(UserNotActiveException ex) {
    	return HttpResponseUtils.prepareErrorResponse(ex,ex.getHttpErrorCode(),ex.getMessage());

    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EmailFailedException.class)
    public ResponseEntity<GenericResponse<Object>> handleEmailException(EmailFailedException ex) {
    	return HttpResponseUtils.prepareErrorResponse(ex,ex.getHttpErrorCode(),ex.getMessage());
    }
    
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(InvalidHeaderException.class)
    public ResponseEntity<GenericResponse<Object>> handleInvalidHeaderException(InvalidHeaderException ex) {
    	return HttpResponseUtils.prepareErrorResponse(ex,ex.getHttpErrorCode(),ex.getMessage());
    } 
    
    @ExceptionHandler(Throwable.class)
	private ResponseEntity<GenericResponse<Object>> handleException(Throwable ex, WebRequest request,
			HttpServletResponse response) throws Exception {
		return HttpResponseUtils.prepareErrorResponse(ex,HttpStatus.INTERNAL_SERVER_ERROR.value(),ex.getMessage());
	}
    
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(SetupPasswordException.class)
    public ResponseEntity<GenericResponse<Object>> handleSetupPasswordException(SetupPasswordException ex) {
    	return HttpResponseUtils.prepareErrorResponse(ex,ex.getHttpErrorCode(),ex.getMessage());
    }
}
