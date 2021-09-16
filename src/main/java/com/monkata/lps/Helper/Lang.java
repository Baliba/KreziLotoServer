package com.monkata.lps.Helper;

import java.util.HashMap;

public class Lang {
	
	public static HashMap<String,String>   en() {
		HashMap<String,String> p =new HashMap<String,String>();
		                       p.put("LOGIN_NAME","Login now");
		                       p.put("GAME","game");
		                       p.put("TICKET","ticket");
		                       p.put("Client","Client");
		                       p.put("MSG_PHONE_EXIST","Phone already exist.");
		                       return p;
		 
	}
	
	public static HashMap<String,String>   kr() {
		HashMap<String,String> p =new HashMap<String,String>();
		                       p.put("LOGIN_NAME","Konekte kounya");
		                       p.put("LOGIN_MSG","Konekte ak kont ou kounya");
		                       p.put("USER_INPUT_PH","Non itilisatè ou nimero ou");
		                       p.put("PASS_INPUT_PH","Paswod");
		                       p.put("GAME","Jwèt");
		                       p.put("TICKET","Fich");
		                       p.put("LOGIN_BTN","Konekte");
		                       p.put("MSG_BAD_USER","Non itilizatè sa pa bon");
		                       p.put("MSG_USER_EXIST","Email sa pa disponib");
		                       p.put("MSG_PHONE_EXIST","Nimero telefòn sa pa disponib");
		                       p.put("MSG_BAD_PASS","Modpass la pa bon");
		                       p.put("MSG_BLOCK_ACCOUNT","Kont sa block");
		                       p.put("MSG_SUCCESS","Siksè"); 
		                       p.put("Entity","Entité yo");
		                       p.put("MSG_FAIL","Echwe");
		                       p.put("GameAndSetting","Jwèt ak Paramèt");
		                       p.put("Bank","Bank yo");
		                       p.put("User","Itilizatè yo");
		                       p.put("NO_GAME","Pa gen jwet disponib");
		                       p.put("Client","Kliyan yo");
		                       return p;
		 
	}


}
