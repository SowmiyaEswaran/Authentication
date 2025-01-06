package com.auth.data.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.auth.data.util.StringPrefixedSequenceIdGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="login_credential")
public class LoginCredential {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "login_id_seq")	
	@Column(name = "login_id")
	private String loginId;

	@Column(name = "email_id", unique = true)
	private String emailId;

	@Column(name = "password")
	private String password;
	
	@JoinColumn(referencedColumnName = "role_id", name ="role_id")
	@ManyToOne
	private Roles role;

	@Column(name = "created_by")
	private String createdBy;
	
	@Column(name = "creation_date")
	private LocalDateTime creationDate;
	
	@Column(name = "reset_password_token")
	private String resetPasswordToken;
	
	@Column(name = "email_verify_token")
	private String emailVerifyToken;
	
	@Column(name = "last_login_date")
	private LocalDateTime lastLoginDate;

	@Column(name = "attempt_count")
	private Integer attemptCount;

	@Column(name = "is_locked")
	private boolean accountLocked;

	@Column(name = "last_attempt_date")
	private LocalDateTime lastAttemptDate;
}
