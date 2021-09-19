package com.monkata.lps.Game;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.monkata.lps.entity.cObj;

import lombok.Data;

@Data
@Entity
public class Game  extends cObj  {
	
	    private static final long serialVersionUID = 1L;
	    @Id
	     @GeneratedValue(strategy = GenerationType.IDENTITY)
	     private Long id;
	    
	   
	     @ManyToOne
		 @JsonIgnoreProperties("games")	
		 private GameMaster gamemaster;
	    
		 @ManyToOne
		 @JsonIgnoreProperties("games")	
		 private ParamsGame paramsgame;
		 
		 @OneToMany(mappedBy = "game") 
		 @JsonIgnoreProperties({"game"})
		 private List<ModeGame> modegames = new ArrayList<>();
		 
	     public void  setOneGame(ModeGame mg){
			 modegames.add(mg);
		 }
	  
}
