package com.monkata.lps.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Winner {
	
	    private static final long serialVersionUID = 1L;
	    @Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private Long id;
	    
	    
	    @Column(nullable=false)
	    String name;
	   
	    @Column(nullable=true)
	    LocalDate date_debut;
	    
	    @Column(nullable=true)
	    LocalDate date_fin;
	    
	  
	    @Column(nullable=true)
	    Long id_winner;
	    
	    @Column(nullable=true)
	    String un_winner;
	    
	    @Column(nullable=true,  columnDefinition = "int default 100")
	    double cagnote;
	    
	    @Column(nullable=true,  columnDefinition = "int default 60")
	    int ticket_price;
	    
	   
	    boolean over;
	    
	    boolean pay;
	    
	    boolean finish;
	   
}


