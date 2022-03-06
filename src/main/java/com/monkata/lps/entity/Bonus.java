package com.monkata.lps.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import com.monkata.lps.response.Order;

import lombok.Data;

@Entity
@Data
public class Bonus  extends cObj implements Serializable  {
	

		@Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private Long id;
	    
	    //
	    public Long id_user;
	    
	    //
	    @Column(nullable=true, columnDefinition = "int default 0")
	    public float montant;
	    
	 
	    @Column(nullable=false, columnDefinition = "int default 0")
	    public Long see_by_admin;
	    
	    
	    @Column(nullable=true, columnDefinition = "int default 0")
	    Long id_deposant;
	    
	    @Column(nullable=true, columnDefinition = "varchar default 'Depo Automaik Sistem' ")
	    String details; 
	    
	    @CreationTimestamp
	    private LocalDateTime date_created;
	    
	    public Bonus(float mt, Long id_user, Long id_dep) {
			this.id_user = id_user;
			this.montant = mt;
			this.id_deposant=id_dep;
			this.date_created = LocalDateTime.now();
		}


		public Bonus() {}
	    
	   
}
