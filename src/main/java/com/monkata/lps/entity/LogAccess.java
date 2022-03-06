package com.monkata.lps.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@Entity
public class LogAccess  extends cObj implements Serializable {
	
	    private static final long serialVersionUID = 1L;
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    
	    @Column(nullable=true)
	    private Long id_user;
	    
	    private String device;
	    private String ip;
	    
	    private LocalDateTime date_emis;
	    @Column(nullable=true, columnDefinition = "boolean default false")
	    public boolean state;
	    @Column(nullable=true, columnDefinition = "varchar default 'Success'")
	    public String message;
	    public LogAccess() {}
	    public LogAccess(Long id, String ip, String dev,boolean state, String msg ) {
	    	id_user = id;
	    	this.ip = ip;
	    	device  = dev;
	    	date_emis = LocalDateTime.now();
	    	this.state = state;
	    	this.message = msg;
	    }
	    
	    
	 
}
