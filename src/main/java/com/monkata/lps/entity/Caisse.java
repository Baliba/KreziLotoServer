package com.monkata.lps.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Caisse extends cObj implements Serializable {
	
	    private static final long serialVersionUID = 1L;
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    public  Long id;
	    
	    public int type_trans;
	    
	    public double sold;
	    
	    public Long  id_maker;
	    
	 
}
