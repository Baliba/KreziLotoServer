package com.monkata.lps.entity.blackjack;

import java.io.Serializable;

import lombok.Data;

@Data
public class Card implements Serializable  {
    int number;
    String snumber;
    int color;
    String scolor;
    String back;
    boolean visibility;
    public Card() {}
	public Card(int i, int j, String b) {
		number = i; 
		color  = j;	
		back = b;
		setSNumberbyInt(i);
		setScolorbyInt(j);
	}
	
	public Card(int z, int c) {
		// TODO Auto-generated constructor stub
		number = z; 
		color  = c;	
		setSNumberbyInt(z);
		setScolorbyInt(c);
	}
	public void setSNumberbyInt(int i ){
		if(i==1) {
			snumber = "A";
		} else  if(i==11) {
			snumber = "J";
		} else  if(i==12) {
			snumber = "Q";
		} else  if(i==13) {
			snumber = "K";
		}else{
			snumber = i+"";
		}
	}
	
	public void setScolorbyInt(int j ){
		switch(j) {
		case 1 :
			scolor = "C";
			break;
		case 2 :
			scolor = "S";
			break;
		case 3 :
			scolor = "D";
			break;
		case 4 :
			scolor = "H";
			break;
		}
	}
}
