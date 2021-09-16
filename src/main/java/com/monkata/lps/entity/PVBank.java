package com.monkata.lps.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Columns;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@Entity
public class PVBank  extends cObj implements Serializable {
	
	    private static final long serialVersionUID = 1L;
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    @Column(unique=true)
	    private String  nom ;
	    public String adress, phone, phone_b,   email;
	    public PVBank() {
	    }
		public PVBank(Long id, String nom, String adress, String phone, String phone_b, String email) {
			super();
			this.id = id;
			this.nom = nom;
			this.adress = adress;
			this.phone = phone;
			this.phone_b = phone_b;
			this.email = email;
		}
	    
	    
	 
}
