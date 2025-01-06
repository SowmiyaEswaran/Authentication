package com.auth.data.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.auth.data.util.StringPrefixedSequenceIdGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
	@Column(name = "user_id")
	private String userId;

	@Column(name = "user_name")
	private String userName;

	@Column(name = "gender")
	private String gender;

	@Column(name="is_active")
	private boolean isActive;

	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private Status status;

	@Column(name = "joining_date")
	private LocalDateTime joiningDate;

	@Column(name = "creation_date")
	private LocalDateTime creationDate;

	@Column(name = "modified_date")
	private LocalDateTime modifiedDate;

	@OneToOne
	@JoinColumn(referencedColumnName = "login_id", name="login_id")
	private LoginCredential loginCredential;

	@JoinColumn(name ="contact_id", referencedColumnName = "contact_id")
	@OneToOne(cascade = CascadeType.ALL)
	private ContactDetails contactDetails;

}
