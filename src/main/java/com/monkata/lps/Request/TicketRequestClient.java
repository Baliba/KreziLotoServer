package com.monkata.lps.Request;

import java.util.List;

import javax.persistence.Column;

import com.monkata.lps.Game.Game;

import lombok.Data;

@Data
public class TicketRequestClient  {
	    private  List<RBoule> lots;
	    // private  RGame game;
	    private  Long   id_game;
	   
	    private boolean is_bonus;
	    
	    private  String coupon;
	  //  private  Long   id_master;
	  //  private  String game_name;
}
