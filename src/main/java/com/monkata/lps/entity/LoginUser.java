package com.monkata.lps.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class LoginUser {

	
	 private static final long serialVersionUID = 1L;
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id_user;
	    
	    @Column(nullable=false)
	    LocalDateTime date_log;
	    
	    @Column(nullable=true)
	    String ip;

		public LoginUser() {
			super();
		}
	    
	    
}
