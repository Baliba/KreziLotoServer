/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.monkata.lps.service;

import java.util.HashMap;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.monkata.lps.Game.Game;
import com.monkata.lps.Game.GameMaster;
import com.monkata.lps.Game.ModeGame;
import com.monkata.lps.Game.ModeGameMaster;
import com.monkata.lps.Game.ParamsGame;
import com.monkata.lps.Game.Ticket;
import com.monkata.lps.Helper.Lang;
import com.monkata.lps.Helper.Log;
import com.monkata.lps.components.StaticData;
import com.monkata.lps.dao.BankRepository;
import com.monkata.lps.dao.GameMasterRepository;
import com.monkata.lps.dao.GameRepository;
import com.monkata.lps.dao.ModeGameMasterRepository;
import com.monkata.lps.dao.ModeGameRepository;
import com.monkata.lps.dao.ParamsGameRepository;
import com.monkata.lps.dao.TicketRepository;
import com.monkata.lps.entity.Bank;
import com.monkata.lps.response.JwtResponse;

import dto.BankAndLang;

@Component
public class BankService {

    @Autowired
    private BankRepository bank;
    
    @Autowired
    GameMasterRepository gmaster;
    
    @Autowired
    private GameRepository game;
    
    @Autowired
    private ModeGameRepository mgame;
    
    @Autowired
    private ModeGameMasterRepository mgamem;
    
    @Autowired
    private ParamsGameRepository pgame;


    public Bank getBank() {
    	
    	return bank.findAll().get(0);
    }
    
   public String  word(String key) {
	   HashMap<String,String>  lg = new  HashMap<String,String> ();
	   Bank b = bank.findAll().get(0);
	   if(b.getLang()==1) {
   	     lg = Lang.en();  
       } else {
   		 lg = Lang.kr();  
       }
	   return lg.get(key);
    }
    
   public BankAndLang  getBankLang() {
	   HashMap<String,String>  lg = new  HashMap<String,String> ();
	   Bank b = getBank();
	   if(b.getLang()==1) {
   	     lg = Lang.en();  
       } else {
   		 lg = Lang.kr();  
       }
	   BankAndLang bl = new BankAndLang();
	   bl.setLg(lg);
	   bl.setBank(b);
	   return bl;
    }

public JwtResponse initNewGame(Long id) {
	// TODO Auto-generated method stub
	GameMaster gms = gmaster.findById(id).get();
	
	if(gms.getGames().size()==0) {
		ParamsGame pg = pgame.findAll().get(0);
		 Game g = new Game();
		 g.setName(gms.getCode());
		 g.setCat_game(gms.getCat_game());
		 g.setLogo_game(gms.getLogo_game());
	     g.setGamemaster(gms);
		 g.setParamsgame(pg);
	     g = game.save(g);
	     for(ModeGame mg : StaticData.modeGames) {
			 // mgamem.save(new ModeGameMaster(mg));
			 mg.setPoint_per_price(1);
			 ModeGame mgs = new ModeGame(mg);
			 mgs.setGame(g); 
			 mgs.setPoint_per_price(1);
       	     ModeGame nmg = mgame.save(mgs);
       	     g.setOneGame(nmg);
            } 
		game.save(g);
		return  new JwtResponse<String>(false,"","siksè");
	} else { 
		
		     Game g = gms.getGames().get(0);
			 g.setName(gms.getCode());
			 g.setCat_game(gms.getCat_game());
			 g.setLogo_game(gms.getLogo_game());
			 g = game.save(g);
			 
			 int qg = g.getModegames().size();
			 int qgm = StaticData.modeGames.size();
			 
			 if(qg!=qgm || qg==0) { 
				 
				for(ModeGame mg : StaticData.modeGames) {
					
					     if(!g.isNotExistModeGame(g,mg)) {
							
							 mg.setPoint_per_price(1);
							 ModeGame mgs = new ModeGame(mg);
							 mgs.setGame(g); 
							 mgs.setPoint_per_price(1);
				       	     ModeGame nmg = mgame.save(mgs);
				       	     g.setOneGame(nmg);
				       	     g=game.save(g);
					     }
			    } 
				
			}
		
		return  new JwtResponse<String>(false,"","Siksè jwet sa update");
	}
	
}

	public JwtResponse addModeGameToGame(Long id, String no) {
		// TODO Auto-generated method stub
		ModeGameMaster mg = mgamem.findById(no).get();
		Optional<Game> og = game.findById(id);
         if(og.isPresent()) {
        	 Game g = og.get();
			 if(!g.isNotExistModeGame(g,mg)) {
							 ModeGame mgs = new ModeGame(mg);
							 mgs.setGame(g); 
							 mgs.setPoint_per_price(1);
				       	     ModeGame nmg = mgame.save(mgs);
				       	     g.setOneGame(nmg);
				       	     g = game.save(g);
				       	  return  new JwtResponse<ModeGame>(false,nmg,"Siksè jwet sa update");
			 }
		    return  new JwtResponse<String>(true,"","Modegame sa ajoute deja");
         } else {
        	 return  new JwtResponse<String>(true,"","Nou pa jwenn jwet sa a");
         }
	}
	

}
