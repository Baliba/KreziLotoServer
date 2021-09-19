/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.monkata.lps.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.monkata.lps.Game.TicketClient;
import com.monkata.lps.Request.PayoutReq;
import com.monkata.lps.Tiraj.NumberFormater.FreeWinLots;
import com.monkata.lps.Tiraj.NumberFormater.WinLots;
import com.monkata.lps.controller.BaseCtrl;
import com.monkata.lps.dao.ComRepository;
import com.monkata.lps.dao.NotDao;
import com.monkata.lps.dao.PayoutRepository;
import com.monkata.lps.entity.Commission;
import com.monkata.lps.entity.Notification;
import com.monkata.lps.entity.Payout;
import com.monkata.lps.entity.UserEntity;
import com.monkata.lps.response.JwtResponse;

import lombok.Data;

@Component
public class NotService {

    @Autowired
    JwtUserDetailsService users;
    
    @Autowired
    NotDao notd;
    
    
    public void add(Long id_user, String msg, Long id) {
    	Notification not = new Notification();
    	not.setId_receiver(id_user);
    	not.setMessage(msg);
    	not.setVu(0L);
    	not.setId_sender(id);
    	not.setDate_not(LocalDateTime.now());
    	notd.save(not);
    }
    
  

    
}
