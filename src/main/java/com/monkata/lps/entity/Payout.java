package com.monkata.lps.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.monkata.lps.response.Order;

import lombok.Data;

@Entity
@Data
public class Payout  extends cObj implements Serializable  {
	

		@Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private Long id;
		
		
		 @Column(nullable=true, columnDefinition = "varchar default 'Ankou' ")
		 String  state;
	    
		 
		 @Column(nullable=true) 
	     LocalDateTime date_created;
	    //
	    public Long id_user;
	    
	    int pin;
	    
	    //
	    public float sold;
	    
	    
	    public float com;
	    
	    float  taux_com;
	    
	    public int type_pay;
	    
	    @Column(nullable=true)
	    String  nom_complet;
	    
	    @Column(nullable=true)
	    String  swift;
	    
	    @Column(nullable=true)
	    String  nombank;
	    
	    @Column(nullable=true)
	    String  nocompte;
	    
	    @Column(nullable=true)
	    String  nomcompte;
	  
	    
	    @Column(nullable=true)
	    String  moncashnumber;
	    
	    @Column(nullable=true)
	    LocalDateTime  date_pay_by_admin;
	  
	    @Column(nullable=true, columnDefinition = "int default 0")
	    Long pay_by_admin;
	    
	    
	    @Column(nullable=true, columnDefinition = "int default 0")
	    int pay;

		public Payout() {

		}
	    
	   
}
