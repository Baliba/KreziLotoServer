/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.monkata.lps.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.aspectj.apache.bcel.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.monkata.lps.Game.BouleClient;
import com.monkata.lps.Game.Game;
import com.monkata.lps.Game.GameMaster;
import com.monkata.lps.Game.ModeGame;
import com.monkata.lps.Game.ModeGameMaster;
import com.monkata.lps.Game.ParamsGame;
import com.monkata.lps.Game.Ticket;
import com.monkata.lps.Game.TicketClient;
import com.monkata.lps.Helper.Lang;
import com.monkata.lps.Helper.Log;
import com.monkata.lps.components.StaticData;
import com.monkata.lps.controller.BaseCtrl;
import com.monkata.lps.dao.BankRepository;
import com.monkata.lps.dao.BouleClientRepository;
import com.monkata.lps.dao.GameMasterRepository;
import com.monkata.lps.dao.GameRepository;
import com.monkata.lps.dao.ModeGameMasterRepository;
import com.monkata.lps.dao.ModeGameRepository;
import com.monkata.lps.dao.ParamsGameRepository;
import com.monkata.lps.dao.TicketClientRepository;
import com.monkata.lps.dao.TicketRepository;
import com.monkata.lps.entity.Bank;
import com.monkata.lps.response.JwtResponse;

import Specification.SearchCriteria;
import Specification.TicketClientSpecification;
import dto.BankAndLang;
import dto.SearchTicketRes;
import dto.TcDto;

import static org.springframework.data.jpa.domain.Specification.where;

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
    
    @Autowired
    private TicketClientRepository ticketc;

    @Autowired
    BouleClientRepository bouler;
    
    @Autowired
	private JwtUserDetailsService user;

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
		    return  new JwtResponse<String>(false,"","Modegame sa ajoute deja");
         } else {
        	 return  new JwtResponse<String>(true,"","Nou pa jwenn jwet sa a");
         }
	}

	public JwtResponse getAllTicketsNow(SearchTicketRes st) {
		// TODO Auto-generated method stub
		 String  deb =  st.getDate_debut()+" 00:00:00";
	     String  fn =  st.getDate_fin()+" 23:59:59";
	     
	     List<TicketClientSpecification> specs =  Arrays.asList(); 
	     SearchCriteria scd = new SearchCriteria ("date_ticket", "!", deb,fn);
	     TicketClientSpecification spec =  new TicketClientSpecification(scd);

	     TicketClientSpecification game, state, payment, verify;
	     
	     if(st.getId_game()!=0) {
	       SearchCriteria scd1 = new SearchCriteria ("id_gamemaster", ":", st.getId_game());
	        game =  new TicketClientSpecification(scd1);
	     } else {
	    	  SearchCriteria scd1 = new SearchCriteria ("id", ">", 0);
		    game =  new TicketClientSpecification(scd1);
	     }
	     
	     if(st.getPayment()!=0) {
	    	    boolean s = (st.getPayment()==1)? false : true;
		        SearchCriteria scd2 = new SearchCriteria ("is_bonus", ":", s);
		        payment =  new TicketClientSpecification(scd2);
		     } else {
		    	  SearchCriteria scd1 = new SearchCriteria ("id", ">", 0);
			    payment =  new TicketClientSpecification(scd1);
		   }
	     
	     if(st.getState()!=0) {
	    	    SearchCriteria scd2;
	    	    if(st.getState()==1) {
		           scd2 = new SearchCriteria ("win_pay", ">", 0);
	    	    }else {
	    	       scd2 = new SearchCriteria ("win_pay", "=", 0);
	    	    }
		        state =  new TicketClientSpecification(scd2);
		     } else {
		    	  SearchCriteria scd1 = new SearchCriteria ("id", ">", 0);
			    state =  new TicketClientSpecification(scd1);
		  }
	     
	     if(st.getVerify()!=0) {
	    	    boolean s = (st.getVerify()==1)? true : false;
	    	    SearchCriteria  scd2 = new SearchCriteria ("over", ":", s);
		        verify =  new TicketClientSpecification(scd2);
		     } else {
		    	  SearchCriteria scd1 = new SearchCriteria ("id", ">", 0);
			      verify =  new TicketClientSpecification(scd1);
		  }
	     List<TicketClient> tc =  ticketc.findAll(Specification.where(state).and(spec).and(game).and(payment).and(verify));
	     return  new JwtResponse<List<TicketClient>>(false,tc,"Siksè");
	}

	public JwtResponse delTicket(Long id) {
		Optional<TicketClient> tco = ticketc.findById(id);
		if(tco.isPresent()) {
		  TicketClient tc = tco.get();
		 try {
		  bouler.deleteAll(tc.getLots());
		  ticketc.deleteById(id);
		  if(!tc.is_bonus()) {
	            user.addAmount(tc.getTotal_price(), tc.getId_user());
			  } else {
				user.addBonus(tc.getTotal_price(), tc.getId_user());
		  }
		  return  new JwtResponse<String>(false,"","Siksè");
		 }catch(Exception e) {
			 return  new JwtResponse<String>(true,"",e.getMessage());
		 }
		}else {
			  return  new JwtResponse<String>(true,"","Nou pa jwenn fich sa ");
		}
	}
	
	
	

}
