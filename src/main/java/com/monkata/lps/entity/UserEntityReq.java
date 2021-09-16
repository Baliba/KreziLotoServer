package com.monkata.lps.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import lombok.Data;

@Data
public class UserEntityReq{

	    Long id;
	    Long id_parent;
	   
	    String username, password, lastName,firstName, sex, token;
	   
	    LocalDate dob;
	    String email;
	    
	    String phone, phone_b, adress;
	    
	    Long supervisor , tech, paramgame, pin;
	   
	    Long pvbank;
	   
	   
	    boolean enabled;
	    boolean lock;
	    
	    // Bank transfert money 
	    
	    String  swift;
	    
	    String  nombank;
	    
	    String  nocompte;
	    
	    String  nomcompte;
	    
	    // 
	    
	    String  moncashnumber;
	  
	    // 
	   
	    Long bank;

		public UserEntityReq() {
			super();
		}
	    
	 
	     
	 
}
