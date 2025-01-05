package com.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class GlobalCorsConfig implements WebMvcConfigurer {

	@Autowired
	private HeaderInterceptor headerInterceptor;

	@Value("${jwt.token.bearer.switch}") 
	private boolean bearerSwitch;


	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		if(bearerSwitch) {
			registry.addInterceptor(headerInterceptor)
			.addPathPatterns("/mytuitioncenter/**");
		}
		else {
			registry.addInterceptor(headerInterceptor)
			.excludePathPatterns("/**"); 
		}


	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
		.allowedOrigins("http://localhost:3000","http://139.144.4.104:8080","http://139.144.4.104:3000")
		.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
		.allowedHeaders("Content-Type", "Authorization")
		.allowCredentials(true);
	}
}
