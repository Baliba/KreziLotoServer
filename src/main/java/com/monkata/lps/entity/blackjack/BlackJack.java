package com.monkata.lps.entity.blackjack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.monkata.lps.Request.BJReq;
import com.monkata.lps.entity.KenoPayouts;
import com.monkata.lps.entity.UserEntity;
import com.monkata.lps.entity.cObj;

import lombok.Data;

@Data
@Entity
public class BlackJack extends cObj implements Serializable  {
	
	    private static final long serialVersionUID = 1L;
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    
	    @Column(nullable=false)
	    public  Long id_user;
	    
	    @Column(nullable=false)
	    public  String back;
	    
	    // mise blackjack
	    @Column(nullable=false, columnDefinition = "int default 10")
	    public  double mise_bj;
	    
	    // mise side bet
	    @Column(nullable=false, columnDefinition = "int default 0")
	    public  double mise_sb;
	    
	    // mise blackjack
	    @Column(nullable=false, columnDefinition = "int default 10")
	    public  double mise_total;
	    
	    @Column(nullable=true, columnDefinition = "boolean default false")
	    private boolean split; 
	    
	    @Column(nullable=true, columnDefinition = "boolean default false")
	    private boolean  doubler; 
	    
	    @Column(nullable=true, columnDefinition = "boolean default false")
	    private boolean side_bet; 
	    
	    @Column(nullable=true, columnDefinition ="int default 0" )
	    private int  side_bet_choose; 
	    
	   
	    // Nombre de main
	    @Column(nullable=false, columnDefinition = "int default 1")
	    public  double hand;
	    
	    @ElementCollection
	    private List<HCard> deal_card = new ArrayList<HCard>();
	    
	    @ElementCollection
	    private List<HCard> user_card_a = new ArrayList<HCard>();
	    
	    @ElementCollection
	    private List<HCard> user_card_b = new ArrayList<HCard>();
	    
	    @Column(nullable=false, columnDefinition = "int default 0")
	    public  int gameState;
	    
	 // mise blackjack
	    @Column(nullable=false, columnDefinition = "int default 0")
	    public  double win_total;
	    
	    @Column(nullable=false, columnDefinition = "int default 0")
	    public  double win_bj;
	    
	    @Column(nullable=false, columnDefinition = "int default 0")
	    public  double win_sb;
	    
	    @Column(nullable=false, columnDefinition = "int default 0")
	    public int dealer_hand_index;
	    
	    @Column(nullable=false, columnDefinition = "int default 0")
	    public int user_hand_index_a;
	    
	    @Column(nullable=false, columnDefinition = "int default 0")
	    public int user_hand_index_b;
	    
	    // data to ignore 
	    
	    @Column(nullable=false, columnDefinition = "int default 0")
	    @JsonIgnore()
	    public int state_bj;
	    
	    @Column(nullable=false, columnDefinition = "boolean default false")
	    @JsonIgnore()
	    public boolean state_sb;
	    
	    @Column(nullable=false,columnDefinition = "int default 0")
	    @JsonIgnore()
	    public double gain_sb;
	    
	    @Column(nullable=false, columnDefinition = "int default 0")
	    @JsonIgnore()
	    public double gain_bj;
	    
		@Column(columnDefinition = "boolean default false")
		private boolean over;
	    
	    public BlackJack(){}

		public BlackJack(BJReq b, UserEntity utt) {
		   this.id_user = utt.getId();
		   mise_bj = b.getBet_bj();
		   mise_sb = getMise_sb();
		   mise_total = mise_bj + mise_sb;
		   side_bet = b.isSb();
		   side_bet_choose = b.getType_sb();
		   gameState = 1;
		}

		public void addCardToDealHand(Card c1) {
			// TODO Auto-generated method stub
			dealer_hand_index++;
			HCard e = new HCard(dealer_hand_index, c1);
			deal_card.add(e);
		}

		public void addCardToUserHand(Card c) {
			// TODO Auto-generated method stub
			user_hand_index_a++;
			HCard e = new HCard(user_hand_index_a, c);
			user_card_a.add(e);
		}
		
		public HCard addCardToUserHandAndGet(Card c) {
			// TODO Auto-generated method stub
			user_hand_index_a++;
			HCard e = new HCard(user_hand_index_a, c);
			user_card_a.add(e);
			return e;
		}
		
	public void clearWin(){
		   win_total = 0;
		   win_bj = 0;
		   win_sb = 0;
    }
	
	
		
		
	    
   
}
