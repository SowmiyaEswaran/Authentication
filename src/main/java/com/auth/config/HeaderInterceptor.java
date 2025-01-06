package com.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.auth.common.CommonConstants;
import com.auth.exception.AppException.InvalidHeaderException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class HeaderInterceptor implements HandlerInterceptor {
	
	@Value("${jwt.token.bearer.auth}") 
    private String bearerTokenProps;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	
    		final String bearerToken = request.getHeader(CommonConstants.AUTHORIZATION);
            String token = null;
        	if (bearerToken != null && bearerToken.startsWith(CommonConstants.BEARER)) {
        		token = bearerToken.substring(7);
        		if(token != null && !token.isEmpty() && token.equals(bearerTokenProps)) {
        			return true;
        		}
        		else {
                    throw new InvalidHeaderException(CommonConstants.INVALID_HEADER_KEY);
        		}
            }else {
                throw new InvalidHeaderException(CommonConstants.MISSING_HEADER_KEY);
            }    	
    }
}
