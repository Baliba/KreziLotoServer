package com.monkata.lps.entity.blackjack;

import java.io.Serializable;
import java.util.ArrayList;
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

import com.monkata.lps.entity.KenoPayouts;
import com.monkata.lps.entity.cObj;

import lombok.Data;

@Data
@Entity
public class BjConfig extends cObj implements Serializable  {
	
	    private static final long serialVersionUID = 1L;
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    
	    @Column(columnDefinition = "int default 50")
	    public  int win_occurence;
	    
	    @Column(columnDefinition = "int default 10")
	    public  int win_occurence_bj;
	    
	    @Column(columnDefinition = "int default 30")
	    public  int win_occurence_win;
	    
	    @Column(columnDefinition = "int default 60")
	    public  int win_occurence_push;
	    
	    @Column(columnDefinition = "int default 50")
	    public  double bank_sold;
	    
	    
	    @Column(columnDefinition = "int default 0")
	    public  double win_sold;
	    
	    @Column(columnDefinition = "int default 0")
	    public  double lost_sold;
	    
	    
	    @ElementCollection
	    private List<Integer> bet = new ArrayList<Integer>();
	    
	    //
	    
	    
	    //
	    /// PAYOUT AND COTE 
	    
	    @Column(columnDefinition = "int default 2")
	    public  int payout;
	    
	    @Column(columnDefinition = "int default 3")
	    public  int payout_bj;
	    
	    @Column(columnDefinition = "int default 1")
	    public  int payout_push;
	    
	    
	    //*********************
	    
	    @Column(columnDefinition = "int default 25")
	    public  int sb_perfect_pair;
	    
	    @Column(columnDefinition = "int default 12")
	    public  int sb_colour_pair;
	    
	    @Column(columnDefinition = "int default 6")
	    public  int sb_mix_pair;
	    
	    @Column(columnDefinition = "int default 100")
	    public  int sb_21_3;
	    
	    // #########################
	    
	    @Column(columnDefinition = "int default 10")
	    public  int win_occurence_mix;
	    
	    @Column(columnDefinition = "int default 5")
	    public  int win_occurence_colour;
	    
	    @Column(columnDefinition = "int default 3")
	    public  int win_occurence_perfect;
	    
	    @Column(columnDefinition = "int default 2")
	    public  int win_occurence_21_3;
	    
	    // *******************
	    
	
	    @Column(columnDefinition = "varchar default 'default.png' ")
	    public  String back;
	    
	    
	    // BLOKER GAME, DEPO,  FICH, LOGIN, REG, 
	          
	    @Column(columnDefinition = "boolean default false")
	    private boolean game_block;
	    
	    
	    public void ordered() {
			// TODO Auto-generated method stub
			bet = bet.stream().sorted().collect(Collectors.toList()); 
		}
	    
}
