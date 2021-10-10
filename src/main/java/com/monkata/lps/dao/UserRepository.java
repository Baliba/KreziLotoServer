package com.monkata.lps.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.monkata.lps.Game.Ticket;
import com.monkata.lps.entity.UserEntity;



@CrossOrigin("*")
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

	    UserEntity findByUsername(String username);
	    Optional<UserEntity> findById(Long id);
	    
	    Boolean existsByUsername(String username);
	    
	    @Query("Select T from UserEntity AS T WHERE T.role.name=:role AND enabled=true ")
	    List<UserEntity> getUserRole(@Param("role") String role);
	    
	    @Query("Select T from UserEntity AS T WHERE see_by_admin = 0 ")
	    List<UserEntity> getNewUser();
	    
	    @Query("Select T from UserEntity AS T WHERE email=:email ")
	    Optional<UserEntity> findByEmail(@Param("email") String email);
	    
	    @Query("Select T from UserEntity AS T WHERE username=:phone ")
	    Optional<UserEntity> getUserByPhone(String phone);
}
