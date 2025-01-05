package com.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.auth.data.entity.Roles;

@Repository
public interface RolesRepository extends JpaRepository<Roles, String>{
	
	Roles findByRoleName(String roleName);

}

