package com.monkata.lps.entity.rafle;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Rafle implements Serializable  {
	
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
	    

	    @ElementCollection
	    private List<Winner> winner = new ArrayList<Winner>();
	   
	    
	    @Column(nullable=true,  columnDefinition = "int default 100")
	    double cagnote;
	    
	    @Column(nullable=true,  columnDefinition = "int default 0")
	    int participant;
	    
	    @Column(nullable=true,  columnDefinition = "int default 3")
	    int max_winner;
	    
	    @Column(nullable=true,  columnDefinition = "int default 1000")
	    int max_participant;
	    
	    @Column(nullable=true,  columnDefinition = "int default 60")
	    int ticket_price;
	    
	    @Column(nullable=false,  columnDefinition = "boolean default false")
	    boolean over;
	    
	    @Column(nullable=false,  columnDefinition = "boolean default false")
	    boolean pay;
	    
	    @Column(nullable=false,  columnDefinition = "boolean default false")
	    boolean finish;
	    
	    @Column(nullable=false,  columnDefinition = "boolean default false")
	    boolean publish;
	   
}


