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
import com.monkata.lps.Game.ModeGameMaster;
import com.monkata.lps.Game.TicketClient;
import com.monkata.lps.Helper.DError;
import com.monkata.lps.Helper.Log;
import com.monkata.lps.Helper.MCC;
import com.monkata.lps.Request.DepoReq;
import com.monkata.lps.Request.PassRep;
import com.monkata.lps.Request.RegRequest;
import com.monkata.lps.components.RoleName;
import com.monkata.lps.controller.BaseCtrl;
import com.monkata.lps.dao.BonusDao;
import com.monkata.lps.dao.CouponRepository;
import com.monkata.lps.dao.DepoDao;
import com.monkata.lps.dao.LogAccessDao;
import com.monkata.lps.dao.LoginUserDao;
import com.monkata.lps.dao.ModeGameMasterRepository;
import com.monkata.lps.dao.ParamsGameRepository;
import com.monkata.lps.dao.PayoutRepository;
import com.monkata.lps.dao.RoleRepository;
import com.monkata.lps.dao.TicketClientRepository;
import com.monkata.lps.dao.UseCouponRepository;
import com.monkata.lps.dao.UserRepository;
import com.monkata.lps.entity.Bank;
import com.monkata.lps.entity.Bonus;
import com.monkata.lps.entity.Coupon;
import com.monkata.lps.entity.Depot;
import com.monkata.lps.entity.LogAccess;
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
import dto.UserRapport;
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
    BonusDao bDao;
    
    @Autowired
    private ModeGameMasterRepository mgamem;

    
    @Autowired
    private UseCouponRepository usecoupon; 
    
    @Autowired
    private LogAccessDao laDao;
    
    @Autowired
    PayoutRepository  pDao;
    
    @Autowired
    TicketClientRepository tcDao;
    
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
	    	   if(ucc.getAgent_amount()>0) {
	    		 UserEntity ut = userId(idu).get();  
	    	     ut.add(ucc.getAgent_amount());
	    	     ucc = usecoupon.save(ucc);	
	    	     ut = userInfoRepository.save(ut);
	    	   }
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

	public JwtResponse getModeGameMaster() {
	  List<ModeGameMaster> mgm = mgamem.findAll();
	  return  new JwtResponse<List<ModeGameMaster>>(false,mgm,"Siksè");
	}

	public JwtResponse getLogAccess(String debut, String fin, int mg, Long id) {
		List<LogAccess> ucs = Arrays.asList();
		  if (mg>-1) {
			if(id>0) {
				return this.getFullLogAccess(debut, fin, mg, id);
			}else {
				return this.getStateLogAccess(debut, fin, mg);
			}
		   } else {
			   if(id>0) {
					return this.getUserLogAccess(debut, fin, id);
				}else {
					return this.getLogAccess(debut, fin);
		       }
		  }
	}
	
	

	private JwtResponse getFullLogAccess(String debut, String fin, int mg, Long id) {
		List<LogAccess> ucs = Arrays.asList();
		boolean state = (mg==1) ? true : false; 
		if(debut!=null && fin!=null ) {
	         LocalDateTime deb = BaseCtrl.getLDT(debut+" 00:00:00");
	         LocalDateTime fn =  BaseCtrl.getLDT(fin+" 23:59:59");
		     ucs = laDao.getFullLogAccess(id, state, deb, fn); 
		}
		return  new JwtResponse<List<LogAccess>>(false,ucs,"Siksè");
	}
	
	private JwtResponse getUserLogAccess(String debut, String fin,Long id) {
		List<LogAccess> ucs = Arrays.asList();
		// TODO Auto-generated method stub
		if(debut!=null && fin!=null ) {
	         LocalDateTime deb = BaseCtrl.getLDT(debut+" 00:00:00");
	         LocalDateTime fn =  BaseCtrl.getLDT(fin+" 23:59:59");
		     ucs = laDao.getUserLogAccess(id, deb, fn); 
		}
		return  new JwtResponse<List<LogAccess>>(false,ucs,"Siksè");
	}
	
	private JwtResponse getStateLogAccess(String debut, String fin, int mg) {
		List<LogAccess> ucs = Arrays.asList();
		boolean state = (mg==1) ? true : false; 
		if(debut!=null && fin!=null ) {
	         LocalDateTime deb = BaseCtrl.getLDT(debut+" 00:00:00");
	         LocalDateTime fn =  BaseCtrl.getLDT(fin+" 23:59:59");
		     ucs = laDao.getStateLogAccess(state, deb, fn); 
		}
		// TODO Auto-generated method stub
		return  new JwtResponse<List<LogAccess>>(false,ucs,"Siksè");
	}
	
	private JwtResponse getLogAccess(String debut, String fin) {
		List<LogAccess> ucs = Arrays.asList();
		if(debut!=null && fin!=null ) {
	         LocalDateTime deb = BaseCtrl.getLDT(debut+" 00:00:00");
	         LocalDateTime fn =  BaseCtrl.getLDT(fin+" 23:59:59");
		     ucs = laDao.getLogAccess(deb, fn); 
		}
		// TODO Auto-generated method stub
		return  new JwtResponse<List<LogAccess>>(false,ucs,"Siksè");
	}

	public JwtResponse getBonus(String debut, String fin, Long id) {
		// TODO Auto-generated method stub
		List<Bonus> ucs = Arrays.asList();
		if(debut!=null && fin!=null ) {
	         LocalDateTime deb = BaseCtrl.getLDT(debut+" 00:00:00");
	         LocalDateTime fn =  BaseCtrl.getLDT(fin+" 23:59:59");
		     ucs = (id==0) ? bDao.getBonus(deb, fn) : bDao.getBonusForUser(deb, fn, id) ; 
		}
		return new JwtResponse<List<Bonus>>(false,ucs,"Siksè");
	}

	public JwtResponse validerBonus(Long id, Long pin,  UserEntity utt) {
		
		if(utt.getPin().equals(pin)) {
			Bonus bonus = bDao.findById(id).get();
			bonus.setSee_by_admin(utt.getId());
			bDao.save(bonus);
		  return new JwtResponse<String>(false,null,"Siksè");           
		}
		
		return new JwtResponse<String>(true,"","Pin nan pa bon");
	}

	public JwtResponse validerAll(Long pin, UserEntity utt) {
		if(utt.getPin().equals(pin)) {
			List<Bonus> bonuses = bDao.getAllNonValider();
			for(Bonus bonus : bonuses) {
				Long d =bonus.getSee_by_admin();
				if(d==null || d==0) {
			          bonus.setSee_by_admin(utt.getId());
			          bDao.save(bonus);
				}
			}
		  return new JwtResponse<String>(false,null,"Siksè");           
		}
		return new JwtResponse<String>(true,"","Pin nan pa bon");
	}

	public JwtResponse validerUser(Long id, Long pin, UserEntity utt) {
		if(utt.getPin().equals(pin)) {
			UserEntity user  = userInfoRepository.findById(id).get();
	        saveUser(user,utt);
		  return new JwtResponse<String>(false,null,"Siksè");           
		}
		
		return new JwtResponse<String>(true,"","Pin nan pa bon");
	}
	
	public JwtResponse validerAllUser (Long pin, UserEntity utt) {
		if(utt.getPin().equals(pin)) {
			List<UserEntity> users = userInfoRepository.getNewUser();
			for(UserEntity user : users) {
				Long d = user.getSee_by_admin();
				if(d==null || d==0) {
					  saveUser(user,utt);
				}
			}
		  return new JwtResponse<String>(false,null,"Siksè");           
		}
		return new JwtResponse<String>(true,"","Pin nan pa bon");
	}
	
	public void saveUser(UserEntity user , UserEntity utt){
		user.setSee_by_admin(utt.getId());
		userInfoRepository.save(user);
	}

	public JwtResponse getUserStat(Long id) {
		List<UserRapport> urs = new ArrayList<UserRapport>();
		if ((long) id==0) {
			List<UserEntity> users = userInfoRepository.getPlayUser();
			// Log.d("+++______________(Nombre user : "+users.size()+")___________+++");
			if(users.size()>0) {
				for(UserEntity user : users) {
					urs.add(getAllInfo(user));
				}
			}
			
		 } else {
			//  Log.d("+++______________(ID User : "+id+")___________+++");
			 Optional<UserEntity> ouser = userInfoRepository.findById(id);
			 if(ouser.isPresent()) {
			    UserEntity user = ouser.get(); 
				urs.add(getAllInfo(user));
			 }
		}
		
		return new JwtResponse<List<UserRapport>>(false,urs,"");
	}

	private UserRapport getAllInfo(UserEntity user) {
		
		UserRapport ur = new UserRapport();
		ur.setId(user.getId());
		ur.setUsername(user.getUsername());
		ur.setFullName(user.getFirstName() + " " +user.getLastName());
		//  
		try {
		Optional<Sold> depot = dpDao.getTotalUserDepot(user.getId());
		if(depot.isPresent()) {
			ur.setDepot(depot.get().getSold());
		}
		}catch(Exception e) {}
		//
		try {
		Optional<Sold> retrait = pDao.getTotalUserRetrait(user.getId());
		if(retrait.isPresent()) {
			ur.setRetrait(retrait.get().getSold());
		}
		}catch(Exception e) {}
		
		try {
		Optional<Sold> play = tcDao.getTotalUser(user.getId());
		if(play.isPresent()) {
			ur.setPlay(play.get().getSold());
		} 
		}catch(Exception e) {}
		
		 try {
			Optional<Sold> win = tcDao.getTotalWinUser(user.getId());
			if(win.isPresent()) {
				ur.setWin(win.get().getSold());
			}
		 }catch(Exception e) {}
		
		return ur;
	}

}
