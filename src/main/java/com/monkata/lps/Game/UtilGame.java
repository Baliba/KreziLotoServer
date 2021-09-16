package com.monkata.lps.Game;

public class UtilGame {

	public static Game getGame(ParamsGame pg, Long id_game) {
	   for(Game g : pg.getGames()) {
		       if(g.getId()==id_game) {
		    	 return g;
		       }
	   }
	   return null;
	}

	public static ModeGame getModeGame(Game cGAME, Long id_mg) {
	 for(ModeGame g : cGAME.getModegames()) {
		       if(g.getId()==id_mg) {
		    	 return g;
		       }
	   }
	   return null;
	}

}
