package com.monkata.lps.entity.blackjack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.monkata.lps.entity.UserEntity;
import com.monkata.lps.service.GameService.BJGameState;

public class BJBrain {
	BlackJack bjp; 
	BjConfig bj;
	UserEntity utt;
	Deck d;
	public BJBrain(BlackJack bjp, BjConfig bj, UserEntity utt) {
	this.bjp = bjp;	
	this.bj =  bj;
	this.utt = utt;
	d = new Deck(1,13,bj.getBack());
	}

	public BJGameState init( BJGameState bjgs) {
		// TODO Auto-generated method stub
	 d.shifleCards();
     int chance_a = random(0,100);	
     double bank = bj.getBank_sold();
     int chance_b = random(0,100);
     double pgain = 0;
     if(chance_a < bj.getWin_occurence()) {
    	  boolean exc =false;
    	  if(chance_b<=bj.getWin_occurence_bj() && (bank*0.7) >(bjp.getMise_bj()* bj.getPayout_bj()) ) {
    		  pgain =  bjp.getMise_bj()*bj.getPayout_bj();
    		  bjp.setState_bj(3);
    		  bjgs = makeCard(3,bjgs,bank, pgain );
    		  exc = true;
    	  }  
    	  if(chance_b<=bj.getWin_occurence_win()  && (bank*0.7) >(bjp.getMise_bj()* bj.getPayout()) ) {
    		  pgain =  bjp.getMise_bj()* bj.getPayout();
    		  bjp.setState_bj(2);
    		  bjgs = makeCard(2,bjgs, bank, pgain );
    		  exc = true;
    	   }
           if(!exc) {
        	 pgain =  bjp.getMise_bj()* bj.getPayout_push();
        	 bjp.setState_bj(1);
        	 bjgs = makeCard(1,bjgs, bank, pgain );
           }
         bjp.setGain_bj(pgain);
      } else {
    	  bjp.setState_bj(0);
    	  bjgs = makeCard(0, bjgs, bank,pgain);
     }
	 return bjgs;
	}
	


	private BJGameState makeCard(int state, BJGameState bjgs, double bank, double pgain) {
		  if(!bjp.isSide_bet()) {
			  return gameWithoutSb(state, bjgs);
		   } else {
			  return gameWithSb(state, bjgs, bank, pgain);
		 }
	}
	
	private BJGameState gameWithoutSb(int state, BJGameState bjgs) {
		
			 Card c1 = CombMaker.getFirstDealCard(state, bj.getBack());
			 bjp.addCardToDealHand(c1);
			 
			 List<Card> cards  = CombMaker.getCardforUser(state,bj.getBack());
			 // card user 
			 bjp.addCardToUserHand(cards.get(0));
			 
			 bjp.addCardToUserHand(cards.get(1));
			 bjgs.setBj(bjp);
	         return bjgs;
	}


	private BJGameState gameWithSb(int state, BJGameState bjgs, double bank, double pgain) {
		 int tsb = bjp.getSide_bet_choose();
		 int c = random(0,100);
		 int chance =  getChance(tsb);
		 int payouts = getPayouts_sb(tsb);
		 if(chance<=c && ((bank*0.7)-pgain) >= (bjp.getMise_bj()*payouts)) {
			  return playWithSb(state,true,bjgs,payouts, tsb);
		 } else {
			 return  playWithSb(state,false,bjgs, payouts, tsb);
		 }
	}

	private BJGameState playWithSb(int state, boolean state_sb, BJGameState bjgs, int payouts,int tsb) {
			 Card c1 = getRandomCardDeck(1, state);
			 bjp.addCardToDealHand(c1);
		     List<Card> cards = getListCardBySb(state_sb,tsb,state);
			 bjp.addCardToUserHand(cards.get(0));
			 bjp.addCardToUserHand(cards.get(1));
			 bjgs.setBj(bjp);
		     return bjgs;
	}




	
	private List<Card>  getListCardBySb(boolean state_sb, int tsb, int state) {
		 /*
		  * 1 = 21 + 3 
		  * 
		  * 2 = flush ak pe  / perfect pair
		  * 
		  * 3 = flush selman
		  * 
		  * 4 = pè selman 
		  * 
		  */
		 List<Card> cards = new ArrayList<Card>();
		
		switch(tsb) {
		 case 1:
			  cards = CombMaker.getwinsCardFor21_3(state_sb, state, bj.getBack());
		 break;
		 
		 case 2:
			  cards = CombMaker.getwinsCardForPerfect(state_sb, state,  bj.getBack());
		 break;
		  
		 case 3:
			 cards = CombMaker.getwinsCardForColour(state_sb, state,  bj.getBack());  
		 break;
		 case 4:
			 cards = CombMaker.getwinsCardForMix(state_sb, state,  bj.getBack());
		 break;
		}
		return cards;
	}


	private int getPayouts_sb(int tsb) {
		 /*
		  * 1 = 21 + 3 
		  * 
		  * 2 = flush ak pe  / perfect pair
		  * 
		  * 3 = flush selman
		  * 
		  * 4 = pè selman 
		  * 
		  */
		int chance = 0;
		
		switch(tsb) {
		 case 1:
			  chance =  bj.getSb_21_3();
		 break;
		 
		 case 2:
			  chance =  bj.getSb_perfect_pair();
		 break;
		 
		 case 3:
			  chance =  bj.getSb_colour_pair();
		 break;
		 case 4:
			  chance =  bj.getSb_mix_pair();
		 break;
		}
		return chance;
	}

	private int getChance(int tsb) {
		
		 /*
		  * 1 = 21 + 3 
		  * 
		  * 2 = flush ak pe  / perfect pair
		  * 
		  * 3 = flush selman
		  * 
		  * 4 = pè selman 
		  * 
		  */
		int chance = 0;
		
		switch(tsb) {
		 case 1:
			  chance =  bj.getWin_occurence_21_3();
		 break;
		 
		 case 2:
			  chance =  bj.getWin_occurence_perfect();
		 break;
		 
		 case 3:
			  chance =  bj.getWin_occurence_colour();
		 break;
		 case 4:
			  chance =  bj.getWin_occurence_mix();
		 break;
		}
		return chance;
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
	  

}
