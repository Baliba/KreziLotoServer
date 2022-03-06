package com.monkata.lps.entity.blackjack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.monkata.lps.entity.UserEntity;
import com.monkata.lps.service.GameService.BJGameState;

import lombok.Data;

@Data
public class BJBrainForHit {
	BlackJack bjp; 
	BjConfig bj;
	UserEntity utt;
	Deck d;
	public BJBrainForHit(BlackJack bjp, BjConfig bj, UserEntity utt) {
	this.bjp = bjp;	
	this.bj =  bj;
	this.utt = utt;
	d = new Deck(1,13,bj.getBack());
	}
	
	public Card  init() {
		int st =  bjp.getState_bj();
		Card  c = new Card();
	    switch(st) {
	    case 0:
	    	//  lose 
	    	c = getCardsForLose();
	    	break;
	    case 1:
	    	 // push
	    c = getCardsForPush();
	    break;
	    case 2:
	    	// win 
	    c = getCardsForWin(); 
	    break;
	    case 3:
	     // blackjack 
	    c = getCardsForBlackjack();
	    break;
	    }
	    return c;
	}



	private Card getCardsForBlackjack() {
		// TODO Auto-generated method stub
		return null;
	}

	private Card getCardsForWin() {
		// TODO Auto-generated method stub
		return null;
	}

	private Card getCardsForPush() {
		// TODO Auto-generated method stub
		return null;
	}

	private Card getCardsForLose() {
		List<Integer> tt = getTotalPointForUser(); 
		Card cd = CombMaker.getCardsForLose(tt);
		     cd.setBack(bj.getBack());
		return cd;
	}

	public static int random(int min, int max) {
		  Random rand = new Random(); 
		 return  rand.nextInt((max - min) + 1) + min;
	}
	
	public Card   getRandomCardDeck() {
		 List<Card> cards = this.d.getCards();
		 int index = (int) Math.floor(Math.random() * cards.size()); 
		 return cards.get(index);
	} 
	public Card   getRandomCardDeck(int w, int mode) {
		 List<Card> cards = this.d.getCards();
		 int index = (int) Math.floor(Math.random() * cards.size()); 
		 return cards.get(index);
	}
	
	public List<Integer> getTotalPointForUser() {
		List<Integer>  ss = new ArrayList<Integer>();
	    int  tt = 0;
		List<Integer>  ts = new ArrayList<Integer>();
	    for (int i = 0; i < this.bjp.getUser_card_a().size();i++) {
	      HCard card = this.bjp.getUser_card_a().get(i);
	      if (card.getCard().getNumber() == 1) {
	          tt += 1;
	          ss.add(10);
	       } else if (card.getCard().getNumber()>=10) {
	          tt += 10;
	        } else {
	          tt += card.getCard().getNumber();
	       }
	    }
	    
	    if (ss.size() == 0) {
	    	ts.add(tt);
	        return ts;
	     } else {
	      for (int j = 0; j < ss.size(); j++) {
	        int i = tt + ss.get(j);
	        if(i<=21){
	           ts.add(i);
	        }
	        tt += ss.get(j);
	      }
	      return ts;
	    }

	  }
	  

}
