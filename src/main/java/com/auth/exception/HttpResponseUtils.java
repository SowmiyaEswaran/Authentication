package com.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.auth.data.response.GenericResponse;


public class HttpResponseUtils {


	private static final MultiValueMap<String, String> COMMON_HTTP_HEADERS_MAP = new LinkedMultiValueMap<String, String>();

	static {
		COMMON_HTTP_HEADERS_MAP.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

	}
	
	public static final MultiValueMap<String, String> getCommonHttpHeaders() {
		return COMMON_HTTP_HEADERS_MAP;
	}

	public static final <T> ResponseEntity<byte[]> prepareResponse(final byte[] body,
			MultiValueMap<String, String> header) {
		return new ResponseEntity<byte[]>(body, header, HttpStatus.OK);
	}


	public static final  <T>ResponseEntity<GenericResponse<T>> prepareErrorResponse(final Throwable ex, final int statusCode) {
		return prepareErrorResponse(ex, statusCode, null);
	}

	public static final <T> ResponseEntity<GenericResponse<T>> prepareErrorResponse(final Throwable ex, final int statusCode,
			final String message) {
		GenericResponse<T> genericResponse = new GenericResponse<T>(ex);
		if (message != null) { // case when message has to be overridden by custom message
			genericResponse.setMessage(message);
		}
		return new ResponseEntity<GenericResponse<T>>(genericResponse, getCommonHttpHeaders(), HttpStatus.valueOf(statusCode));
		
	}

	public static final <T> ResponseEntity<GenericResponse<T>> prepareSuccessResponse(final T data) {
	    return prepareSuccessResponse(data, null, null);
	}

	public static final <T> ResponseEntity<GenericResponse<T>> prepareSuccessResponse(final T data, final String message) {
		return prepareSuccessResponse(data, message, null);
	}

	public static final <T> ResponseEntity<GenericResponse<T>> prepareSuccessResponse(final T data,
			final MultiValueMap<String, String> headers) {
		return prepareSuccessResponse(data, null, headers);
	}

	public static final <T> ResponseEntity<GenericResponse<T>> prepareSuccessResponse(final T data, final String message,
			final MultiValueMap<String, String> headers) {
		final GenericResponse<T> response = new GenericResponse<>();
		response.setData(data);
		response.setMessage(message);
		if (headers != null && !headers.isEmpty()) {
			headers.putAll(getCommonHttpHeaders());
			return new ResponseEntity<GenericResponse<T>>(response, headers, HttpStatus.OK);
		}
		return new ResponseEntity<GenericResponse<T>>(response, getCommonHttpHeaders(), HttpStatus.OK);
	}

}
