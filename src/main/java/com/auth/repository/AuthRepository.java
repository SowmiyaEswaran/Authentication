package com.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.auth.data.entity.LoginCredential;

@Repository
public interface AuthRepository extends JpaRepository<LoginCredential, String> {

	Optional<LoginCredential> findByEmailId(String emailId);
	
	LoginCredential findByResetPasswordToken(String token);

	LoginCredential findByLoginId(String login_id);

}
