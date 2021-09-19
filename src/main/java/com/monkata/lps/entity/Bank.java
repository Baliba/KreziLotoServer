package com.monkata.lps.entity;

import java.io.Serializable;

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
public class Bank  extends cObj implements Serializable {
	
	    private static final long serialVersionUID = 1L;
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    private Long id_bank;
	    private String  nom , adress, phone, phone_b,  token, email;
	    private String dateTimeFormat="dd-MM-yyyy HH:mm:ss";
	    private String fuseau_horaire="";
	    private String slogan;
	    

	    
	    @Column(columnDefinition = "int default 1")
	    public  int lang;
	    
	    // BLOKER GAME, DEPO,  FICH, LOGIN, REG, 
	          
	    @Column(columnDefinition = "boolean default false")
	    private boolean block_depo;
	    
	    @Column(columnDefinition = "boolean default false")
	    private boolean block_payout;
	    
	    @Column(columnDefinition = "boolean default false")
	    private boolean block_make_ticket;
	    
	    @Column(columnDefinition = "boolean default false")
	    private boolean block_login;
	    
	    @Column(columnDefinition = "boolean default false")
	    private boolean block_register;
	    
	    @Column(columnDefinition = "boolean default false")
	    private boolean block_roulette;
	    
	    //**************************//
	    
	    
	    public Bank() {}
		public Bank(Long id, String nom, String adress, String phone, String phone_b, String email) {
			super();
			this.id = id;
			this.nom = nom;
			this.adress = adress;
			this.phone = phone;
			this.phone_b = phone_b;
			this.email = email;
			dateTimeFormat="dd-MM-yyyy HH:mm:ss";
			slogan = "Toujou Peye a lè";
			fuseau_horaire = "UTC-5";
		}
	    
	    
	 
}
