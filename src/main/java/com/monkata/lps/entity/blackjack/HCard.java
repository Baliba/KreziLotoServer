package com.monkata.lps.entity.blackjack;

import java.io.Serializable;

import lombok.Data;

@Data
public class HCard implements Serializable  {
    int index;
    String name;
    Card card;
    public HCard() {}
	public HCard(int index2, Card c1) {
		this.card = c1;
		index = index2;
	}
	
}
