package com.auth.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth.data.entity.LoginCredential;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${jwt.token.key.emailVerification}") 
    private String emailVerificationKey;
    
    @Value("${jwt.token.key.resetpassword}") 
    private String passwordResetkey;
    
    @Value("${jwt.token.expire.resetpassword}") 
    private int passwordExpire;
    
    @Value("${jwt.token.expire.emailverification}") 
    private int emailVerificationExpire;
    

    public String generateTokenPasswordReset(LoginCredential loginCredential) {
        return generateToken(loginCredential, passwordResetkey, passwordExpire);
    }

	public String generateValidateEmailToken(LoginCredential loginCredential) {
		return generateToken(loginCredential, emailVerificationKey, emailVerificationExpire);
	}
	
	public String generateToken(LoginCredential loginCredential, String secretKey, int expiryHr) {
		Map<String, Object> claims = new HashMap<>();
		  claims.put("emailId", loginCredential.getEmailId());
		return Jwts.builder()
				.setClaims(claims).setSubject(loginCredential.getEmailId()).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+ expiryHr*60*60*1000))
				.signWith(getSignInKey(secretKey), SignatureAlgorithm.HS256).compact();
	}
	
	private Key getSignInKey(String secretKey) {
		byte[] keys = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keys);
	}

    public boolean validateTokenPasswordReset(LoginCredential loginCredential) {
        return isTokenExpired(loginCredential.getResetPasswordToken(), passwordResetkey);
    }
    
    public boolean validateEmailToken(LoginCredential loginCredential) {
        return isTokenExpired(loginCredential.getEmailVerifyToken(), emailVerificationKey);
    }
	
	public boolean isTokenExpired(String token, String secretKey){
		 try {
	            Claims claims = Jwts.parserBuilder()
	                    .setSigningKey(getSignInKey(secretKey))
	                    .build()
	                    .parseClaimsJws(token)
	                    .getBody();
	            Date expiration = claims.getExpiration();
	            boolean isExpired = expiration.before(new Date());
	            
	            return isExpired;
		 } catch (Exception e) {
	            return true; 	       
	            }
		 }
}


