package com.monkata.lps;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.monkata.lps.dao.BankRepository;
import com.monkata.lps.entity.Bank;


    @SpringBootApplication
   //public class LotteryPosSellerApplication extends SpringBootServletInitializer {
	public class LotteryPosSellerApplication {
    	
    	
	    public static void main(String[] args) {
	    	
		SpringApplication.run(LotteryPosSellerApplication.class, args);
		 
		}
	    
	    @Autowired
	    BankRepository bank;
	    
	    @PostConstruct
	    public void init() {
	    	String utc ="America/New_York";
	    	try {
	    	List<Bank>  bks = bank.findAll();
	    	if(bks.size()>0) {
	           Bank bk = bks.get(0);	
	           if(!bk.getFuseau_horaire().equals("")) {
	        	   utc = bk.getFuseau_horaire();
	              TimeZone.setDefault(TimeZone.getTimeZone(bk.getFuseau_horaire()));
	           } else {
	        	   TimeZone.setDefault(TimeZone.getTimeZone(utc)); 
	           }
	    	  } else {
	    		 TimeZone.setDefault(TimeZone.getTimeZone(utc));
	    	 }
	        System.out.println(" \n Date in ("+utc+") ====> " + LocalDateTime.now() +"\n");
	    	} catch(Exception e) {
	    		 TimeZone.setDefault(TimeZone.getTimeZone(utc));
	    		System.out.println("TIME => ***********("+ e.getMessage()+" )********");
	    	}
	    	
	    	System.out.println("\n DEFAULT TIME ZONE========================> ("+TimeZone.getDefault()+") <==============================\n");
	    	
	    }
	    
//	    @Override
//	    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//	    	
//	       return application.sources(LotteryPosSellerApplication.class);
//	       
//	    }

}
