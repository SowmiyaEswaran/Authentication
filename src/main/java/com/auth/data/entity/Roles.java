package com.auth.data.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="roles")
public class Roles {
	
	@Id
	@Column(name="role_id")
	private String roleId;
	
	@Column(name="role_name",unique = true)
	private String roleName;
	
	@Column(name="is_active")
	private boolean isActive;
	
	@Column(name="creation_date")
	private Date creationDate;
	
}
