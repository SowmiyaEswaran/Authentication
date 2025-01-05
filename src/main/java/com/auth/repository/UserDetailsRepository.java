package com.auth.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.auth.data.entity.UserDetails;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, String> {
    
    @Query(value="select * from user_details where login_id=?1", nativeQuery = true)
    Optional<UserDetails> findbyLoginId(String loginId);
    @Query("SELECT ud FROM UserDetails ud JOIN ud.loginCredential lc WHERE lc.emailId = :emailId")
    UserDetails findByEmailId(@Param("emailId") String emailId);
}