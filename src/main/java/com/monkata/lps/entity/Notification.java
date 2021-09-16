package com.monkata.lps.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

@Data
@Entity
public class Notification extends cObj  {

	    @Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private Long id;
	    
	    @Column(nullable=true)
	    Long id_receiver;
	    
	    @Column(nullable=true)
	    Long id_sender;
	    
	    String message;
	    
        @Column(nullable = true, updatable = false)
	    private LocalDateTime    date_not;
	    
	    @Column(nullable=true)
	    Long vu;
}
