package com.auth.common;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

	private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);


	@Pointcut("execution(* com.auth.*.*.*(..))")
	private void serviceImplMethods() {		
	}
	
	@Before(value = "serviceImplMethods()")
	public void logBefore(JoinPoint joinPoint) throws Throwable {
	   
		String methodName = joinPoint.getSignature().getName();
	   
	    logger.info("start - method {} () - ",methodName);
	}
	
	@After(value = "serviceImplMethods()")
	public void logAfter(JoinPoint joinPoint) throws Throwable {
	   
		String methodName = joinPoint.getSignature().getName();
	   
	    logger.info("end -  method {} () - ", methodName);   
	}
}
