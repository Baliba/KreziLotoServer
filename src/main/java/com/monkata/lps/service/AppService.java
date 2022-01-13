/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.monkata.lps.service;


import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.monkata.lps.Game.TicketClient;
import com.monkata.lps.Helper.DError;
import com.monkata.lps.Helper.Log;
import com.monkata.lps.Helper.MCC;
import com.monkata.lps.Request.DepoReq;
import com.monkata.lps.Request.PassRep;
import com.monkata.lps.Request.RegRequest;
import com.monkata.lps.components.RoleName;
import com.monkata.lps.controller.BaseCtrl;
import com.monkata.lps.dao.CouponRepository;
import com.monkata.lps.dao.DepoDao;
import com.monkata.lps.dao.LoginUserDao;
import com.monkata.lps.dao.ParamsGameRepository;
import com.monkata.lps.dao.RoleRepository;
import com.monkata.lps.dao.UseCouponRepository;
import com.monkata.lps.dao.UserRepository;
import com.monkata.lps.entity.Bank;
import com.monkata.lps.entity.Coupon;
import com.monkata.lps.entity.Depot;
import com.monkata.lps.entity.LoginUser;
import com.monkata.lps.entity.Payout;
import com.monkata.lps.entity.Role;
import com.monkata.lps.entity.UseCoupon;
import com.monkata.lps.entity.UserEntity;
import com.monkata.lps.entity.UserEntityReq;
import com.monkata.lps.response.ApiResponse;
import com.monkata.lps.response.AppResponse;
import com.monkata.lps.response.JwtResponse;
import com.monkata.lps.response.Order;

import dto.CouponDto;
import dto.DepoStat;
import dto.NParamsGame;
import dto.Sold;
import lombok.Data;
import net.minidev.json.JSONObject;

@Component
public class AppService  {

    @Autowired
    private UserRepository userInfoRepository;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private ParamsGameRepository pgame;
    
    @Autowired
    NotService nots;
    
    @Autowired
    CouponRepository cpRep;
    
    @Autowired 
    DepoDao dpDao;
    
    @Autowired 
    BankService banks;
    
    
    @Autowired
    private UseCouponRepository usecoupon; 
    
    public  Optional<UserEntity>  userId(Long id)  {
            Optional<UserEntity> user = userInfoRepository.findById(id);
            return user;
   
    }
    
    public UserEntity save(UserEntity u){
    	return userInfoRepository.save(u);
    }

	public JwtResponse getUseCoupon(String debut, String fin, int mg) {
		
		List<UseCoupon> ucs = Arrays.asList();
		//Log.d("****************| "+debut+" | "+fin);
		if(debut!=null && fin!=null ) {
         LocalDateTime deb = BaseCtrl.getLDT(debut+" 00:00:00");
         LocalDateTime fn =  BaseCtrl.getLDT(fin+" 23:59:59");
         if(mg>=0) {
		     ucs = usecoupon.getUseCoupon(deb,fn, mg);
         }else {
        	 ucs = usecoupon.getUseCoupon(deb,fn);
         }
		}
		
		return  new JwtResponse<List<UseCoupon>>(false,ucs,"Siksè");
	}

	public List<UseCoupon> getUseCouponByUser(Long id) {
		List<UseCoupon> ucs = Arrays.asList();
		ucs = usecoupon.getCurrentUC(id,0);
		return ucs;
	}
	
     @Transactional
	public JwtResponse payAgentNow(Long idu, Long idc) {
	  Optional<UseCoupon> uc=	usecoupon.findById(idc);
	    if(uc.isPresent()) {
	       UseCoupon ucc = uc.get();	
	       if(ucc.getIs_agent_pay()==0) {
	    	   ucc.setIs_agent_pay(1);
	    	   ucc.setDate_agent_pay(LocalDateTime.now());
	    	   UserEntity ut = userId(idu).get();
	    	   ut.add(ucc.getAgent_amount());
	    	   ucc = usecoupon.save(ucc);	
	    	   ut = userInfoRepository.save(ut);
	    	   return  new JwtResponse<UseCoupon>(false,ucc,"Siksè");
	       } else {
	    	   return  new JwtResponse<String>(true,null,"Ou touche kob sa deja");
	       }
	      
	    }
	    return  new JwtResponse<String>(true,null,"Nou pa jwenn koupon sa");
	}

	public List<UseCoupon> getUseCouponByUser(Long id, String debut, String fin) {
		List<UseCoupon> ucs = Arrays.asList();
		if(debut!=null && fin!=null ) {
	         LocalDateTime deb = BaseCtrl.getLDT(debut+" 00:00:00");
	         LocalDateTime fn =  BaseCtrl.getLDT(fin+" 23:59:59");
		ucs = usecoupon.getPastUC(id, 1, deb, fn); 
		}
		return ucs;
	}

}
