package com.monkata.lps.Tiraj;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.monkata.lps.Game.BouleClient;
import com.monkata.lps.Game.ModeGame;
import com.monkata.lps.Game.WinName;
import com.monkata.lps.Helper.Log;
import com.monkata.lps.dao.WinNameRepository;

import lombok.Data;

public class NumberFormater {
	
	public  Tiraj t;
	List<WinName> winNames;
    public NumberFormater(Tiraj t, List<WinName>  wn) {
    	this.t = t;
    	this.winNames = wn;
    }
    
    public List<FreeWinLots> getLots(){
    	List<FreeWinLots>  fwl = new ArrayList<FreeWinLots>();
    	for(WinName wn : winNames) {
    		String name = wn.getCode_mg()+"_"+wn.getName();
    		Log.d(name);
    		 fwl.add(new FreeWinLots(wn.getName(),wn.getCode_mg(), formatLot(wn.getSyntax())));
    	}
    	return fwl;
    }
    
    public WinLots checkNumberIsWin(BouleClient lot, ModeGame mg){
    	int pos = -1;
    	WinLots wl = null ; 
    	List<WinLots> wls = new ArrayList<>();
    	boolean win = false;
    	for(WinName wn : winNames) {
    		if(lot.getCode_mg().equals(wn.getCode_mg())) {
    			pos++;
    			String b = lot.getLot();
    			b = b.replace(mg.getMask(),"");
    			List<String> ll = formatLot(wn.getSyntax());
    			for(String s : ll ) {
    				if (s.equals(b)) {
    					wl = new WinLots(lot,pos, mg, wn.getName());
    					win = true;
    					break;
    				}
    			}
    			if(win) {
    				if(!lot.getCode_mg().equals("BOR")) {
    			     	break;
    				}
    			}
    		}
    	}
    	return wl;
    }
    
    public List<WinLots> checkNumberIsWinArray(BouleClient lot, ModeGame mg){
    	int pos = -1;
    	List<WinLots> wls = new ArrayList<>();
    	boolean win = false;
    	for(WinName wn : winNames) {
    		if(lot.getCode_mg().equals(wn.getCode_mg())) {
    			pos++;
    			String b = lot.getLot();
    			b = b.replace(mg.getMask(),"");
    			List<String> ll = formatLot(wn.getSyntax());
    			for(String s : ll ) {
    				if (s.equals(b)) {
    					wls.add(new WinLots(lot,pos, mg, wn.getName()));
    					win = true;
    					break;
    				}
    			}
    			if(win) {
    				if(!lot.getCode_mg().equals("BOR")) {
    			     	break;
    				}
    			}
    		}
    	}
    	return wls;
    }
    
    
    public List<String>   formatLot(String wn){
    	  List<String> lot = new ArrayList<>();
    	 if(wn.contains("|")) {
    		String[] seq = wn.split("\\|");
    		for(String s : seq) {
    			if(!s.isEmpty()) {
    			  String[] sl = s.split("\\*");
    			  lot.add(makeLot(sl));
    			}
    		}
    	  } else {
    		  String[] sl = wn.split("\\*");
  			  lot.add(makeLot(sl));
    	 }
    	 return lot;
    }

	private String makeLot(String[] sl) {
	    String lot ="";
	    for(String s : sl) {
	    	 lot=lot+getTirajLot(Integer.parseInt(s));
	    }
		return lot;
	}

	private String getTirajLot(int pos) {
		String c ="";
		switch(pos) {
		case 0 :
			c = t.getLot_0();
		break;
		case 1 :
			c = t.getLot_1();
		break;
		case 2 :
			c = t.getLot_2();
		break;
		case 3 :
			c = t.getLot_3();
		break;
		case 4 :
			c = t.getLot_4();
		break;
		case 5 :
			c = t.getLot_5();
		break;
		case 6 :
			c = t.getLot_6();
		break;
		}
		return c;
	}
	
	@Data
	public class WinLots{
		BouleClient bc;
		int pos;
		ModeGame mg;
		String name;
		public WinLots(BouleClient bc, int pos, ModeGame mg, String name) {
			this.bc = bc;
			this.pos = pos;
			this.mg = mg;
			this.name = name;
		}
	}
	
	
	@Data
	public class FreeWinLots{
		String name;
		String mg_name;
		public FreeWinLots(String name,String mg_name, List<String>  lot) {
			super();
			this.name = name;
			this.lot = lot;
			this.mg_name = mg_name;
		}
		List<String> lot;
	}
    
    
}
