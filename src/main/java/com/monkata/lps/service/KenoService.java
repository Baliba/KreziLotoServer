/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.monkata.lps.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.monkata.lps.Game.TicketClient;
import com.monkata.lps.Request.PayoutReq;
import com.monkata.lps.Tiraj.NumberFormater.FreeWinLots;
import com.monkata.lps.Tiraj.NumberFormater.WinLots;
import com.monkata.lps.dao.ComRepository;
import com.monkata.lps.dao.KenoConfigRepository;
import com.monkata.lps.dao.KenoRepository;
import com.monkata.lps.dao.NotDao;
import com.monkata.lps.dao.PayoutRepository;
import com.monkata.lps.dao.TicketClientRepository;
import com.monkata.lps.entity.Commission;
import com.monkata.lps.entity.Keno;
import com.monkata.lps.entity.KenoConfig;
import com.monkata.lps.entity.Notification;
import com.monkata.lps.entity.Payout;
import com.monkata.lps.entity.UserEntity;
import com.monkata.lps.response.JwtResponse;

import Bonis.Case;
import Bonis.Deck;
import dto.Sold;
import lombok.Data;

@Component
public class KenoService {

    @Autowired
    JwtUserDetailsService users;
    @Autowired 
    TicketClientRepository tcRep;
    float gp = 5;
    
    @Autowired
    KenoConfigRepository kcr ;
    
    @Autowired
    KenoRepository kr ; 
	public JwtResponse  play(UserEntity user) {
	    user =  users.userId(user.getId()).get();
	    if(user.getBonus()>=gp) {
	       user = users.removeBonus(gp, user.getId());
	       Optional<Sold> swin = tcRep.maxWinClient(user.getId());
	       double win = 0;
	       if(swin.isPresent()) {
	         win  = swin.get().getSold();
	       }
	       Optional<Sold> slost =  tcRep.maxLostClient(user.getId());
	       double lost = 0;
	       if(slost.isPresent()) {
	              lost = slost.get().getSold();
	       }
	       List<Case> cases = new Deck().getCases();
	       GameDraw gd = new GameDraw();
	       gd.setCases(cases);
	       int h = getCaseDraw(win,lost,(float) user.getCompte());
	       user = setMoney(h,user.getId(), cases, user);
	       gd.setWinHole(h);
	       gd.setUser(user);
	       return   new JwtResponse<GameDraw>(false,gd,"siksè");
	    }
	    return   new JwtResponse<String>(true,"","Ou pa gen ase kob pou jwe.");
	}
	
	private UserEntity setMoney(int h, Long id,List<Case> cases, UserEntity u ) {
	             Case c = getCase(h, cases);
	             if(c.getSold()>0) {
	            	 
	            	 if(c.getType_win()==1) {
	            		u = users.addBonus(c.getSold(), id);
	            	  } else {
	            		u = users.addAmountV2(c.getSold(), id);
	            	 }
	            	 
	             }
	             return u;
		
	}

	public Case getCase(int i, List<Case> cases){
		
		for(Case c : cases ) {
			if( i== c.getId() ) {
				return c;
			}
		}
		return null;
	}
    
    public int getCaseDraw(double win, double lost, float sold) {
	  if(win==0 && lost==0 && sold==0) {
		  int c = chance(1,100);
		  if(c<90) {
			 return chance(2,4);
		  }
	  } else if(lost-win>=500 && lost-win<1000) {
		  int c = chance(1,100);
		  if(c<50) {
			 return chance(4,7);
		  }
	  }else if(lost-win>=1000 && lost-win<5000) {
		  int c = chance(1,100);
		  if(c<50) {
			 return chance(6,8);
		  }
	  } else if(lost-win>=10000) {
			 return 8;
	  }else {
		  int c = chance(1,100);
		  if(c<75) {
			 return chance(2,7);
		  }
	  }
	  return 1;
  }
  
	public int chance(int min, int max){
	      return  (int)Math.floor(Math.random()*(max-min+1)+min);
	}
     
	public KenoConfig getKC() {
		KenoConfig kc = null;
		List<KenoConfig> kcs = kcr.findAll();
		if(kcs.size()==0) {
			kc = new KenoConfig();
			List<Integer> wo = Arrays.asList(0,20,18,15,12,10,8,6,5,2);
			List<Integer> bet = Arrays.asList(10,15,25,50,75,100);
			kc.setWin_occurrence(wo);
			kc.setBet(bet);
			kc.setPayoutsNow();
			kc = kcr.save(kc);
		 } else {
			kc = kcs.get(0);
		 }
		
		return checkKC(kc);
	}
	
	
	public KenoConfig checkKC(KenoConfig kc) {
		
		if(kc.getPayouts().size()!=10 || kc.getWin_occurrence().size()!=10) {
			kcr.deleteById(kc.getId());
			kc = null;
			kc = new KenoConfig();
			List<Integer> wo = Arrays.asList(0,20,18,15,12,10,8,6,5,2);
			List<Integer> bet = Arrays.asList(10,15,25,50,75,100);
			kc.setWin_occurrence(wo);
			kc.setBet(bet);
			kc.setPayoutsNow();
			kc = kcr.save(kc);
		}
		
		return kc;
	}



	public List<Keno> getListGame(int day) {
		// TODO Auto-generated method stub
		return kr.getKenoGameByDay(day);
	}

	public List<Keno> getListGame(int day, Long id) {
		// TODO Auto-generated method stub
				return kr.getKenoGameByDay(day, id);
	}
    
}


@Data
class GameDraw {
	 int winHole;
	 List<Case> cases;
	 UserEntity user;
	public GameDraw(int winHole, List<Case> cases) {
		super();
		this.winHole = winHole;
		this.cases = cases;
	}
	public GameDraw() {
		super();
		// TODO Auto-generated constructor stub
	}
	 
}
