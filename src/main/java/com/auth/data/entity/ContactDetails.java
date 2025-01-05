package com.auth.data.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="contact_details")
public class ContactDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="contact_id")
	private long contactId;
	
	@Column(name="email_id")
	private String emailId;
	
	@Column(name="phone_no")
	private long phoneNo;
	
	@JoinColumn(referencedColumnName = "address_id",name="address_id")
	@OneToOne(cascade = CascadeType.ALL)
	Address address;
	
}
