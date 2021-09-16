/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.monkata.lps.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.monkata.lps.Game.TicketClient;
import com.monkata.lps.Request.PayoutReq;
import com.monkata.lps.Tiraj.NumberFormater.FreeWinLots;
import com.monkata.lps.Tiraj.NumberFormater.WinLots;
import com.monkata.lps.dao.ComRepository;
import com.monkata.lps.dao.NotDao;
import com.monkata.lps.dao.PayoutRepository;
import com.monkata.lps.dao.TicketClientRepository;
import com.monkata.lps.entity.Commission;
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
