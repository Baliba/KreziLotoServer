package com.monkata.lps.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jgit.transport.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import com.monkata.lps.Game.Game;
import com.monkata.lps.Game.ParamsGame;
import com.monkata.lps.dao.BankRepository;
import com.monkata.lps.entity.Bank;
import com.monkata.lps.entity.UserEntity;
import com.monkata.lps.service.JwtUserDetailsService;

import dto.Periode;

public class BaseCtrl {
	 @Autowired
	 private JwtUserDetailsService UserDetails;
	 
	 private static final String[] IP_HEADER_CANDIDATES = {
	            "X-Forwarded-For",
	            "Proxy-Client-IP",
	            "WL-Proxy-Client-IP",
	            "HTTP_X_FORWARDED_FOR",
	            "HTTP_X_FORWARDED",
	            "HTTP_X_CLUSTER_CLIENT_IP",
	            "HTTP_CLIENT_IP",
	            "HTTP_FORWARDED_FOR",
	            "HTTP_FORWARDED",
	            "HTTP_VIA",
	            "REMOTE_ADDR"
	        };
	 
	 public static LocalDateTime getLDT(String str) {
		 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		 return  LocalDateTime.parse(str, formatter);
	 }
	
	 public static String getClientIp(HttpServletRequest request) {

	      for (String header: IP_HEADER_CANDIDATES) {
	            String ipList = request.getHeader(header);
	            if (ipList != null && ipList.length() != 0 && !"unknown".equalsIgnoreCase(ipList)) {
	                String ip = ipList.split(",")[0];
	                return ip;
	            }
	        }
	        return request.getRemoteAddr();
	    } 
	 public UserEntity  getUser (Authentication authentication){
		   UserDetails me = (UserDetails) authentication.getPrincipal();
	       return  this.UserDetails.getUserInfo(me.getUsername());
	 }
	 
	 public static LocalDate nowLD() {
	  	   LocalDate  localDate = LocalDate.now();
	  	   return localDate;
	 }
	 public static LocalDateTime toDayFixe () {
	  	   LocalDateTime  localDateTime = LocalDateTime.now();
	  	//   LocalDate ld = localDateTime.toLocalDate();
	  	//   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	  	 //  String   sd =   localDateTime.format(formatter);  
	  	   return localDateTime;
	 }
	 public static String toDay () {
  	   LocalDateTime  localDateTime = LocalDateTime.now();
  	   LocalDate ld = localDateTime.toLocalDate();
  	   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
  	   String   sd =   localDateTime.format(formatter);  
  	   return sd.split(" ")[0];
     }
	 
	 public String time () {
	  	   LocalDateTime  localDateTime = LocalDateTime.now();
	  	   LocalDate ld = localDateTime.toLocalDate();
	  	   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	  	   String   sd =   localDateTime.format(formatter);  
	  	   return sd.split(" ")[1];
	 }
	 
	 public String yDay () {
	  	   LocalDateTime  localDateTime = LocalDateTime.now().minusDays(1);
	  	   LocalDate ld = localDateTime.toLocalDate();
	  	   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	  	   String   sd =   localDateTime.format(formatter);  
	  	   return sd.split(" ")[0];
	}
	   
	 public static LocalDateTime formatDate(String d, String h) {
		  
		  String str = d+" "+h;
		  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		  return  LocalDateTime.parse(str, formatter);
	  }
	  
     public static LocalDate formatDateOnly(String d) {
		  
		  String str = d+" ";
		  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		  return  LocalDate.parse(str, formatter);
	  }
	  
	 public static int getNewPin(int min, int max) {
		  Random rand = new Random(); 
		 return  rand.nextInt((max - min) + 1) + min;
	  }
	  
	  
	    @Autowired
	 BankRepository bank;
	    
    public Bank getBankConfig() {
	    	List<Bank>  bks = bank.findAll();
	    	if(bks.size()>0) {
	          return  bks.get(0);
	    	}
	    	return null;
	    }
	    
	public ParamsGame getAllValidGame(ParamsGame pg) {
			List<Game> gs = new ArrayList<>();
			LocalTime now = LocalTime.parse(time());
			for (Game g : pg.getGames()) {
				LocalTime tt = LocalTime.parse(g.getGamemaster().getHour_to_block());
				LocalTime ss = LocalTime.parse(g.getGamemaster().getHour_to_start_sell());
				if (now.isBefore(tt) && now.isAfter(ss) && g.getGamemaster().isEnabled()) {
					gs.add(g);
				}
			}
			pg.setGames(gs);
			return pg;
		}

	
	public static Periode getPeriodFromDay(int day) {
		Periode p = new Periode();
		LocalDateTime deb = null ;
		LocalDateTime fn = null;
	    if (day>1){
			 day--;
			 String date = LocalDate.now().toString();
			 String sd = LocalDate.now().minusDays(day).toString();
			 deb = getLDT(sd+" 00:00:00");
	         fn =  getLDT(date+" 23:59:59");
		} else {
			 String date =LocalDate.now().toString();
			 deb = getLDT(date+" 00:00:00");
	         fn =  getLDT(date+" 23:59:59");
		}
		p.setDebut(deb);
		p.setFin(fn);
		return p;
	}
		 
}
