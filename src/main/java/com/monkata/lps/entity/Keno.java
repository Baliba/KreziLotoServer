package com.monkata.lps.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import dto.KenoReq;
import lombok.Data;

@Data
@Entity
public class Keno  extends cObj implements Serializable {
	
	    public Keno(KenoReq kr,Long id) {
		// TODO Auto-generated constructor stub
	    	bet = kr.bet;
	    	id_user= id;
	    	lot = kr.getLots();
	    	this.total_num = kr.getTotal_num();
	    }
		private static final long serialVersionUID = 1L;
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    
	    private Long id_user;
	    
	    private double bet;
	    
	    @Column(nullable=true, columnDefinition = "boolean default false")
	    private boolean  is_win;
	    
	    @Column(nullable=true)
	    private double win_sold;
	    
	    @Column(nullable=false, columnDefinition = "int default 0")
	    private double  bank_sold;
	    
	    @Column(nullable=true, columnDefinition = "varchar default 'Non Mentioné'")
	    private String  msg;
	    
	    @Column(nullable=true)
	    private double next_win_sold;
	    
	    private int total_num;
	    
	    @Column(nullable=true)
	    @ElementCollection
	    @Cascade(value={CascadeType.ALL})
	    private List<Integer> draw = new ArrayList<Integer>();
	    
	    @ElementCollection
	    @Cascade(value={CascadeType.ALL})
	    private List<Integer> lot = new ArrayList<Integer>();
	    
	    @Column(nullable=true, columnDefinition = "boolean default false")
	    private boolean over;
	    
	    @Column(nullable=true)
	    LocalDateTime date_ticket;
	    
	    public Keno() {
	    	
	    }
	    
	    public void setDate(){
	    	 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		     String str = LocalDateTime.now().format(formatter);
			 date_ticket =  LocalDateTime.parse(str, formatter);
	    }
	    
	 
}
