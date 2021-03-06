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
public class Depot  extends cObj implements Serializable  {
	

		@Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private Long id;
	    
		@Column(nullable=true, columnDefinition = "int default 0")
	    public Long id_order;
	    
	    //
	    public Long id_user;
	    
	    //
	    @Column(nullable=true, columnDefinition = "int default 0")
	    public float montant, bonis;
	    
	    @Column(nullable=true, columnDefinition = "int default 0")
	    public String no_mc;
	    
	    
	    
	    @Column(unique=true)
	    public String token_order;
	    
	    @Column(nullable=true, columnDefinition = "int default 0")
	    public Long id_transaction;
	    
	    public Long see_by_admin;
	    
	    @Column(nullable=false, columnDefinition = "int default 0")
	    int type_depot;
	    
	    
	    @Column(nullable=true, columnDefinition = "int default 0")
	    Long id_deposant;
	    
	    @Column(nullable=true, columnDefinition = "varchar default 'Depo Automaik Sistem' ")
	    String details; 
	    
	    @CreationTimestamp
	    private LocalDateTime date_created;
	    
	    public Depot(Order o, Long id_user) {
	    	this.token_order = o.getToken_order();
			this.id_order = o.id_order;
			this.id_user = id_user;
			this.montant = o.getAmount();
			this.no_mc   = o.getPhone();
		    this.id_transaction = o.getId_transaction();
		}


		public Depot() {}
	    
	   
}
