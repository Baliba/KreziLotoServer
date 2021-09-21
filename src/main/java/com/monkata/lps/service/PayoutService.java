/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.monkata.lps.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.monkata.lps.Game.TicketClient;
import com.monkata.lps.Request.PayoutReq;
import com.monkata.lps.Tiraj.NumberFormater.FreeWinLots;
import com.monkata.lps.Tiraj.NumberFormater.WinLots;
import com.monkata.lps.controller.BaseCtrl;
import com.monkata.lps.dao.ComRepository;
import com.monkata.lps.dao.PayoutRepository;
import com.monkata.lps.entity.Commission;
import com.monkata.lps.entity.Payout;
import com.monkata.lps.entity.UserEntity;
import com.monkata.lps.response.JwtResponse;

import lombok.Data;

@Component
public class PayoutService {

    @Autowired
    private PayoutRepository payout;
    
    @Autowired
    ComRepository com;
    
    @Autowired
    NotService nots;
    
    @Autowired
    JwtUserDetailsService users;
    
    public JwtResponse  getCurrentPayout(Long id) {
    	try {
         	return   new JwtResponse<List<Payout>>(true,payout.getCurrentPayout(id),"");
    	} catch(Exception e) {
    		return   new JwtResponse<List<Payout>>(true,new ArrayList<>(),"");
    	}
    }
    
    public JwtResponse  pastPayout(Long id, int d) {
    	return   new JwtResponse<List<Payout>>(true,payout.getLastPayout(id, d),"");
    }
	public JwtResponse  setPay(UserEntity user, PayoutReq pay) {
        Payout po = new Payout();
        po.setId_user(user.getId());
        po.setType_pay(pay.getType_pay());
        po.setDate_created(LocalDateTime.now());
        po.setState("Ankou");
        Commission com = getCom(pay.getSold(), pay.getType_pay());
        if(com!=null) {
      	  po.setSold(pay.getSold()-com.getCom());
      	  po.setCom(com.getCom());
      	  po.setTaux_com(com.getTaux());
      	} else {
      		po.setSold(pay.getSold());
      		po.setCom(0);
      		po.setTaux_com(0);
      	}
        if(pay.getType_pay()==0) {
        	// moncash
        	po.setMoncashnumber(pay.getMoncash());
        	
        } else  if(pay.getType_pay()==1 || pay.getType_pay()==2 ) {
        	// bank 
        	po.setSwift(pay.getSwift());
        	po.setNombank(pay.getNom_bank());
        	po.setNomcompte(pay.getNom_compte());
        	po.setNocompte(pay.getNumero());
        } else  if(pay.getType_pay()==3) {
        	// chek
        	po.setNom_complet(pay.getNom_complet());
        } else  if(pay.getType_pay()==4) {
        	po.setPin(pay.getPin());
        	po.setState("Prè");
        }
        user =  users.userId(user.getId()).get();
       if(user.getCompte()>=pay.getSold()) {
    	   user.remain(pay.getSold());
    	   users.save(user);
    	   po = payout.save(po);
    	   nots.add(user.getId(),"Ou fèk sot fe on retrè "+pay.getSold()+"G.",1L);
    	   return new JwtResponse<PayoutRes>(false, new PayoutRes(user,po, com),"Siksè , ou retire "+pay.getSold()+"G sou kont ou.");   
       } else {
    	 return   new JwtResponse<String>(true,"","Ou pa gen ase kob pou fe retrè sa");
       }
	}
	
	
	public Commission getCom(float sold, int type_pay){
	   List<Commission> coms = com.getListCommissionByType(type_pay);	
	   if(coms!= null && coms.size()>0) {
		   for(Commission c : coms ) {
		      if(sold >= c.getMin() && sold < c.getMax()) {
			    return c;
		      } 
		   }
	   } 
	   return null;
	}

	public JwtResponse setPaymentByAdmin(Long id, UserEntity utt) {
		  Payout pay =	payout.findById(id).get();
		  pay.setPay_by_admin(utt.getId());
		  pay.setPay(1);
		  pay.setDate_pay_by_admin(BaseCtrl.toDayFixe());
		  String msg = "Retrè ou te mande an prè ou ka rele nan +(509) 3138-1388, Oubyen wap resevwal sou nimewo moncash ou te bay la.";
		  nots.add( pay.id_user, msg,utt.getId());
		  return   new JwtResponse<String>(true,"","Siksè");
	}
	
	
 
	

}
@Data
class PayoutRes {
  UserEntity user;	
  Payout payout ;	
  Commission  com;
  public PayoutRes(UserEntity user, Payout payout, Commission com) {
		super();
		this.user = user;
		this.payout = payout;
		this.com = com;
 }

    
}
