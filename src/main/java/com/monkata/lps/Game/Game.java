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
import com.monkata.lps.Helper.Log;
import com.monkata.lps.components.StaticData;
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
	     
	     @Column(nullable=true, columnDefinition = "varchar default 'default' ")
		 private String   name;
	     
	     @Column(nullable=true, columnDefinition = "varchar default 'ALL' ")
		 private String   cat_game;
		    
		 @Column(nullable=true, columnDefinition = "varchar default 'default.png' ")
		 private String   logo_game;
	    
		 @ManyToOne
		 @JsonIgnoreProperties("games")	
		 private ParamsGame paramsgame;
		 
		 @OneToMany(mappedBy = "game") 
		 @JsonIgnoreProperties({"game"})
		 private List<ModeGame> modegames = new ArrayList<>();
		 
	     public void  setOneGame(ModeGame mg){
			 modegames.add(mg);
		 }

		public boolean isNotExistModeGame(ModeGame nmg) {
			
			for(ModeGame mg : modegames) {   
				Log.d("++++++++++++ "+mg.getNo()+" ==="+nmg.getNo());
			    if(mg.getNo().equals(nmg.getNo())) {
			    	return true;
			    }
	        } 
			
			return false;
		}

		public boolean isNotExistModeGame(Game g, ModeGameMaster mg2) {
			
			if(g.getModegames().size()==0) {
				 return false;
			}
			
			for(ModeGame mg : g.getModegames()) {   
				//Log.d("++++++++++++ "+mg.getNo()+" ==="+mg2.getNo());
			    if(mg.getNo().equals(mg2.getNo())) {
			    	return true;
			    }
	        } 
			
			return false;
		}

		public boolean isNotExistModeGame(Game g, ModeGame mg2) {
			if(g.getModegames().size()==0) {
				 return false;
			}
			
			for(ModeGame mg : g.getModegames()) {   
				// Log.d("++++++++++++ "+mg.getNo()+" ==="+mg2.getNo());
			    if(mg.getNo().equals(mg2.getNo())) {
			    	return true;
			    }
	        } 
			
			return false;
		}
	  
}
