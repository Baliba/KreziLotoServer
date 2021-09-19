/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.monkata.lps.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.monkata.lps.Game.TicketClient;
import com.monkata.lps.Request.PayoutReq;
import com.monkata.lps.Tiraj.NumberFormater.FreeWinLots;
import com.monkata.lps.Tiraj.NumberFormater.WinLots;
import com.monkata.lps.dao.CaisseRepository;
import com.monkata.lps.dao.ComRepository;
import com.monkata.lps.dao.NotDao;
import com.monkata.lps.dao.PayoutRepository;
import com.monkata.lps.dao.TicketClientRepository;
import com.monkata.lps.entity.Commission;
import com.monkata.lps.entity.Notification;
import com.monkata.lps.entity.Payout;
import com.monkata.lps.entity.UserEntity;
import com.monkata.lps.response.JwtResponse;

import Bonis.Case;
import Bonis.Deck;
import dto.Sold;
import lombok.Data;

@Component
public class CaisseService {

    @Autowired
    JwtUserDetailsService users;
    //\\| *************|*********** |//\\
    @Autowired 
    CaisseRepository caisseRep;
	 
}
