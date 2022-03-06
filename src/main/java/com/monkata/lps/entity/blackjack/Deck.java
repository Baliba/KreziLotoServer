package com.monkata.lps.entity.blackjack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lombok.Data;

@Data
public class Deck implements Serializable  {
 List<Card> cards = new ArrayList<Card>();
 
 public Deck(int a ,int v, String nd){
	           // Card 1 
	 for(int i = a  ; i<=v; i++ ) {
			  this.cards.add(new Card(i,1, nd));
			  this.cards.add(new Card(i,2, nd));
			  this.cards.add(new Card(i,3, nd));
			  this.cards.add(new Card(i,4, nd));
	 }	 
   }
 
 
 public void genGlobalCard(int v) {
	 List<Card> resultList = new ArrayList<Card>();
	 for(int i = 1  ; i<=v; i++ ) {
		 resultList.addAll(this.cards);
	 }
	 this.cards =  resultList;
 }
 




public void shifleCards() {
	 Collections.shuffle(cards);
	 Collections.shuffle(cards);
	 Collections.shuffle(cards);
	 Collections.shuffle(cards);
}

}
