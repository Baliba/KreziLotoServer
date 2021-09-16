package com.monkata.lps.Helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import com.monkata.lps.entity.UserEntity;
import com.monkata.lps.service.JwtUserDetailsService;

import lombok.Data;

@Data
public class LoginUser {
	@Autowired
	Authentication authentication;
	
	@Autowired
	private JwtUserDetailsService UserDetails;
	
    public UserEntity  getUser (Authentication authentication){
		   UserDetails me = (UserDetails) authentication.getPrincipal();
	       return  this.UserDetails.getUserInfo(me.getUsername());
	 }
	  
}
