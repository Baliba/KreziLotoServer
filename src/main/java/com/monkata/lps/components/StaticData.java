package com.monkata.lps.components;

import java.util.Arrays;
import java.util.List;

import com.monkata.lps.Game.GameMaster;
import com.monkata.lps.Game.ModeGame;
import com.monkata.lps.Game.WinName;
import com.monkata.lps.entity.Bank;
import com.monkata.lps.entity.PVBank;

public class StaticData {
	
public static List<GameMaster> gamemaster =  Arrays.asList(
		new GameMaster((long)1,"MNY","MIDI NEW YORK","07:00:00","13:30:00","14:00:00",1),
		new GameMaster((long)2,"SNY","SOIR NEW YORK","07:00:00","21:30:00","22:00:00",2),
		new GameMaster((long)3,"MFL","MIDI FLORIDA" ,"07:00:00","13:20:00","14:00:00",3),
		new GameMaster((long)4,"SFL","SOIR FLORIDA" ,"07:00:00","21:40:00","22:00:00",4)
		);

public static List<WinName> winNames = Arrays.asList(
		new WinName ("LOT_1","1*2","BOR"),
		new WinName ("LOT_2","3*4","BOR"),
		new WinName ("LOT_3","5*6","BOR"),
		new WinName ("3CH",  "0*1*2","3CH"),
		new WinName ("MAR","1*2*3*4|1*2*5*6|3*4*1*2|3*4*5*6|5*6*3*4|5*6*1*2","MAR"),
		new WinName ("4C1","3*4*5*6",  "4C1"),
		new WinName ("4C2","1*2*3*4",  "4C2"),
		new WinName ("4C3","1*2*5*6",  "4C3"),
		new WinName ("5C1","0*1*2*3*4","5C1"),
		new WinName ("5C2","0*1*2*5*6","5C2"),
		new WinName ("5C3","2*3*4*5*6","5C3")
		);

public static List<ModeGame> modeGames = Arrays.asList(
		new ModeGame("20","Borlette","BOR",5000,5,"", 2, "LOT_1=60/LOT_2=20/LOT_3=10", 3 , 1),
		new ModeGame("30","3-Chiffres","3CH",2500,5,"", 3,"3CH=500",1,2),
		new ModeGame("40","Mariage","MAR",1500, 5,"*", 4,2,true,"MAR=1000",1,3),
		new ModeGame("41","4-Chiffres-1","4C1",1200, 5,"",  4,"4C1=5000",1,4),
		new ModeGame("42","4-Chiffres-2","4C2",1000 ,5,"",  4,"4C2=5000",1,5),
		new ModeGame("43","4-Chiffres-3","4C3",750,  5,"",  4,"4C3=5000",1,6),
		new ModeGame("51","5-Chiffres-1","5C1",20,   5,"",  5,"5C1=25000",1,7),
		new ModeGame("52","5-Chiffres-2","5C2",20,   5,"",  5,"5C2=25000",1, 8),
		new ModeGame("53","5-Chiffres-3","5C3",20,   5,"",  5,"5C3=25000",1, 9) );




       public static Bank bank = new Bank(1L, "New York Lotto Center", "","","",""); 
       public static PVBank pvbank = new PVBank(1L, "NYLC FORT-JACQUES ", "","","",""); 
}






