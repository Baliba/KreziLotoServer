package com.monkata.lps.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import dto.KenoReq;
import lombok.Data;

@Data
public class KenoPayouts implements Serializable {
	  
	  public List<Integer> hits;
	  public List<Integer> pays;
	  public List<Integer> occurrence;
	  public int index;
	  
	 public KenoPayouts() {}

	public KenoPayouts(List<Integer> h, List<Integer> p, List<Integer> o,int i ) {
		hits = h;
		pays = p;
		occurrence = o;
		index = i;
	}
	  
	    
}
