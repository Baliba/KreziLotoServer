/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.monkata.lps.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.monkata.lps.Game.Ticket;
import com.monkata.lps.Helper.Lang;
import com.monkata.lps.dao.BankRepository;
import com.monkata.lps.dao.TicketRepository;
import com.monkata.lps.entity.Bank;

import dto.BankAndLang;

@Component
public class BankService {

    @Autowired
    private BankRepository bank;


    public Bank getBank() {
    	
    	return bank.findAll().get(0);
    }
    
   public String  word(String key) {
	   HashMap<String,String>  lg = new  HashMap<String,String> ();
	   Bank b = bank.findAll().get(0);
	   if(b.getLang()==1) {
   	     lg = Lang.en();  
       } else {
   		 lg = Lang.kr();  
       }
	   return lg.get(key);
    }
    
   public BankAndLang  getBankLang() {
	   HashMap<String,String>  lg = new  HashMap<String,String> ();
	   Bank b = getBank();
	   if(b.getLang()==1) {
   	     lg = Lang.en();  
       } else {
   		 lg = Lang.kr();  
       }
	   BankAndLang bl = new BankAndLang();
	   bl.setLg(lg);
	   bl.setBank(b);
	   return bl;
    }
	

}
