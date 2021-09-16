package com.monkata.lps.Request;

import java.util.List;

import com.monkata.lps.Game.Game;

import lombok.Data;

@Data
public class TicketRequest  {
	    private  List<RBoule> lots;
	    // private  RGame game;
	    private  Long   id_game;
	    private  Long   id_master;
	    private  String game_name;
	    private  String place;
	    private  String client;
	    private  int    type_ticket;
}
