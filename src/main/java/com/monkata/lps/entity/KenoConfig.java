package com.monkata.lps.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
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
public class KenoConfig  extends cObj implements Serializable {
	
	    private static final long serialVersionUID = 1L;
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    
	    @Column(nullable=true, columnDefinition = "int default 5")
	    private int ad_show_counter;
	    // -----------
	    @Column(nullable=true, columnDefinition = "int default 300")
	    private  int animation_speed = 300;
	    
	    // 
	    @Column(nullable=true, columnDefinition = "int default 500")
	    private  double bank_sold = 500;
	    
	    @Column(nullable=true, columnDefinition = "int default 10")
	    private  int global_occurrence  = 10;
	    // ---
	    @Column(nullable=true, columnDefinition = "boolean default false")
	    private boolean  audio_enable_on_startup;
	    //----
	    @Column(nullable=true, columnDefinition = "boolean default true")
	    private boolean  check_orientation;
	    //---
	    @Column(nullable=true, columnDefinition = "boolean default true")
	    private boolean fullscreen;
	    //---
	    @Column(nullable=true, columnDefinition = "boolean default true")
	    private boolean  show_credits = true;
	    //---
	    @ElementCollection
	    private List<Integer> win_occurrence = new ArrayList<Integer>();
	    
	    @ElementCollection
	    private List<Integer> bet = new ArrayList<Integer>();
	    
	    @ElementCollection
	    private List<KenoPayouts> payouts = new ArrayList<KenoPayouts>();
	    
	    public void ordered() {
			// TODO Auto-generated method stub
			bet = bet.stream().sorted().collect(Collectors.toList()); 
			win_occurrence = win_occurrence.stream().sorted().collect(Collectors.toList()); 
			Collections.reverse(win_occurrence);
			payouts = payouts.stream()
					  .sorted(Comparator.comparing(KenoPayouts::getIndex))
					  .collect(Collectors.toList());
			
		}
	    
	    
		public void setPayoutsNow() {
			// P 1
			List<Integer> h = Arrays.asList(0);
			List<Integer> p = Arrays.asList(0);
			List<Integer> o = Arrays.asList(0);
			KenoPayouts kp1 = new KenoPayouts(h, p, o, 1);
			payouts.add(kp1);

			 // P 2
			 h = Arrays.asList(2,1);
			 p = Arrays.asList(2,1);
			 o = Arrays.asList(10,90);
			 KenoPayouts kp2 = new KenoPayouts(h, p, o, 2);
			 payouts.add(kp2);
		
			 // P 3
			 h = Arrays.asList(3,2);
			 p = Arrays.asList(4,1);
			 o = Arrays.asList(10,90);
			 KenoPayouts kp3 = new KenoPayouts(h, p, o, 3);
			 payouts.add(kp3);
			 
			 // P 4
			 h = Arrays.asList(4,3,2);
			 p = Arrays.asList(8,3,1);
			 o = Arrays.asList(10,30,60);
			 KenoPayouts kp4 = new KenoPayouts(h, p, o, 4);
			 payouts.add(kp4);
		
			 // P 5
			 h = Arrays.asList(5,4,3);
			 p = Arrays.asList(20,10,2);
			 o = Arrays.asList(10,30,60);
			 KenoPayouts kp5 = new KenoPayouts(h, p, o, 5);
			 payouts.add(kp5);
			 
			 // P 6
			 h = Arrays.asList(6,5,4,3);
			 p = Arrays.asList(50,20,5,2);
			 o = Arrays.asList(10,15,20,55);
			 KenoPayouts kp6 = new KenoPayouts(h, p, o, 6);
			 payouts.add(kp6);
		
			 // P 7
			 h = Arrays.asList(7,6,5,4,3);
			 p = Arrays.asList(80,20,10,5,1);
			 o = Arrays.asList(5,10,20,30,35);
			 KenoPayouts kp7 = new KenoPayouts(h, p, o, 7);
			 payouts.add(kp7);
			 
			 // P 8
			 h = Arrays.asList(8,7,6,5,4);
			 p = Arrays.asList(300,165,100,12,2);
			 o = Arrays.asList(5,10,20,30,35);
			 KenoPayouts kp8 = new KenoPayouts(h, p, o, 8);
			 payouts.add(kp8);
		
			 // P 9
			 h = Arrays.asList(9,8,7,6,5,4);
			 p = Arrays.asList(400,200,80,25,10,3);
			 o = Arrays.asList(1,4,10,20,30,35);
			 KenoPayouts kp9 = new KenoPayouts(h, p, o, 9);
			 payouts.add(kp9);
           
			 
			 // P 10
			 h = Arrays.asList(10,9,8,7,5,5);
			 p = Arrays.asList(500,250,100,50,10,5);
			 o = Arrays.asList(1,4,10,15,30,40);
			 KenoPayouts kp10 = new KenoPayouts(h, p, o, 10);
			 payouts.add(kp10);
           
						
		}


		
		
	    
		
	 
}
