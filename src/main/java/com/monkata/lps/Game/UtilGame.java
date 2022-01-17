package com.monkata.lps.Game;

import com.monkata.lps.Helper.Log;

public class UtilGame {

	public static Game getGame(ParamsGame pg, Long id_game) {
	   for(Game g : pg.getGames()) {
		       if((long) g.getId()== (long)id_game) {
		    	 return g;
		       }
	   }
	   return null;
	}

	public static ModeGame getModeGame(Game cGAME, Long id_mg) {
		ModeGame ng = null;	
	 for(ModeGame g : cGAME.getModegames()) {
		       if((long) g.getId()== (long) id_mg) {
		    	  return g;
		       } 
	   }
	   return null;
	}

}
