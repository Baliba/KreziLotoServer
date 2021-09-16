package com.monkata.lps;

import java.util.Date;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


    @SpringBootApplication
   //public class LotteryPosSellerApplication extends SpringBootServletInitializer {
	public class LotteryPosSellerApplication {
	    public static void main(String[] args) {
	    	
		SpringApplication.run(LotteryPosSellerApplication.class, args);
		 
		}
	
	    @PostConstruct
	    public void init() {
	        TimeZone.setDefault(TimeZone.getTimeZone("GMT-5"));
	        System.out.println(" \n Date in UTC: " + new Date().toString()+"\n");
	    }
	    
//	    @Override
//	    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//	    	
//	       return application.sources(LotteryPosSellerApplication.class);
//	       
//	    }

}
