package com.monkata.lps.dao;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.monkata.lps.entity.Bank;
import com.monkata.lps.entity.Role;
import com.monkata.lps.entity.UserEntity;

import dto.NRole;
import lombok.Data;



@CrossOrigin("*")
@RepositoryRestResource
public interface RoleRepository extends JpaRepository<Role, Long> {
	
	    Role findByName( String name);
	    
	    
	    @Query("Select new dto.NRole(r.id,r.name) from Role AS r")
	    List<NRole> getRoles();
	    
	    

}
