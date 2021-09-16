package com.monkata.lps.response;

import java.util.Date;

import lombok.Data;
@Data
public class RSTicket {
	    boolean crash;
	    int code;
	    Long id_ticket;
	    Date date_ticket;
	    String message;
	    Long id_user;
	    String sdate;
	    String sheure;
	    String date_exp;
	    public  RSTicket(){
		   crash = true;
	    }

}
