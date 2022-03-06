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

import javax.persistence.ElementCollection;
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
import com.monkata.lps.Request.BJReq;
import com.monkata.lps.Request.DepoReq;
import com.monkata.lps.Request.PassRep;
import com.monkata.lps.Request.RegRequest;
import com.monkata.lps.components.RoleName;
import com.monkata.lps.controller.BaseCtrl;
import com.monkata.lps.dao.BJConfigRepository;
import com.monkata.lps.dao.BackJackRepository;
import com.monkata.lps.dao.CouponRepository;
import com.monkata.lps.dao.DepoDao;
import com.monkata.lps.dao.LoginUserDao;
import com.monkata.lps.dao.ModeGameMasterRepository;
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
import com.monkata.lps.entity.blackjack.BJBrain;
import com.monkata.lps.entity.blackjack.BJBrainForHit;
import com.monkata.lps.entity.blackjack.BjConfig;
import com.monkata.lps.entity.blackjack.BlackJack;
import com.monkata.lps.entity.blackjack.Card;
import com.monkata.lps.entity.blackjack.Deck;
import com.monkata.lps.entity.blackjack.HCard;
import com.monkata.lps.response.ApiResponse;
import com.monkata.lps.response.AppResponse;
import com.monkata.lps.response.JwtResponse;
import com.monkata.lps.response.Order;
import com.monkata.lps.service.GameService.BJGameState;

import dto.CouponDto;
import dto.DepoStat;
import dto.NParamsGame;
import dto.Sold;
import lombok.Data;
import net.minidev.json.JSONObject;

@Component
public class GameService  {

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
    private ModeGameMasterRepository mgamem;
    
    @Autowired
    BackJackRepository  bjr;
    
    @Autowired
    BJConfigRepository  bjcr;
    
   
    @Autowired
    JwtUserDetailsService users;
	public JwtResponse iniBlackJackNow() {
		
	   List<BjConfig>  bjs = bjcr.findAll();
	   BjConfig bj;
	   if(bjs.size()==0) {
		   bj = new BjConfig();
		   List<Integer> bet = Arrays.asList(10,15,25,50,75,100);
		   bj.setBet(bet);
		   bj.setWin_occurence(30);
		   bj.setBank_sold(0);
		   
		   bj = bjcr.save(bj);
	   } else {
		   bj = bjs.get(0);
	   }
	   bj.ordered();
	   return  new JwtResponse<BjConfig>(false,bj,"Siksè");
	}
	
	public BjConfig getBJC(){
		 List<BjConfig>  bjs = bjcr.findAll();
		 BjConfig bj = bjs.get(0);
		 bj.ordered();
		 return bj;
	}

	public JwtResponse getBjConfig() {
		 try {
		   BjConfig bj = getBJC();
		   return  new JwtResponse<BjConfig>(false,bj,"Siksè");
		   } catch(Exception e ) {
			 return  new JwtResponse<BjConfig>(true,null,e.getMessage()); 
		 }
	}

	public JwtResponse editBjc(BjConfig bjc) {
		  BjConfig bj = bjcr.save(bjc);
		  return  new JwtResponse<BjConfig>(false,bj,"Siksè");
	}

	public JwtResponse getSimpleDeck() {
		List<BjConfig>  bjs = bjcr.findAll();
		BjConfig bj =  getBJC();
		// TODO Auto-generated method stub
		Deck d = new Deck(1,13,bj.getBack());
		return new JwtResponse<Deck>(false,d,"Siksè");
	}

	public JwtResponse configUser() {
		  BjConfig bj = getBJC();
		  bj.setId(null);
		  ConfigUser cu = new ConfigUser();
		  cu.setBet(bj.getBet());
		  return  new JwtResponse<ConfigUser>(false,cu,"Siksè");
	}
	
	
	@Data
	public class ConfigUser {
		 private List<Integer> bet = new ArrayList<Integer>();
		 public ConfigUser() {}
	}

	public JwtResponse initGameBJ(BJReq b, UserEntity utt) {
		
		BjConfig bj = getBJC();
		
		if(!bj.isGame_block()) {
		int sizeb = bj.getBet().size();
		// TODO Auto-generated method stub
		if(sizeb>0) {
			if(b.getBet_bj()>=bj.getBet().get(0) && b.getBet_bj()<=bj.getBet().get(sizeb-1)) {
				if((b.getBet_sb()>=bj.getBet().get(0) && b.getBet_sb()<=bj.getBet().get(sizeb-1)) || !b.isSb()) {
				    long total = b.getBet_sb() + b.getBet_bj();	
				    if(total<= utt.getCompte()) {
				       if(users.removeAmount(utt.getId(),total)) {	
				       BlackJack	bjp = new BlackJack(b, utt);
				       bjp.setBack(bj.getBack());
				       BJBrain bjb = new BJBrain (bjp,bj, utt);
				       BJGameState bjgs = new BJGameState();
				       bjgs = bjb.init(bjgs);
				       bjp = this.bjr.save(bjgs.getBj());
				       bjgs.setBj(bjp);
				       return new JwtResponse<BJGameState>(false,bjgs,"");
				       } else {
							return new JwtResponse<>(true,"","Ou pa gen ase kob pou jwe");
					 }
				    } else {
						return new JwtResponse<>(true,"","Ou pa gen ase kob pou jwe");
					}
				} else {
					return new JwtResponse<String>(true,"","Montan pari sou jwet sou kote a pa valid");
				}
			} else {
				
				return new JwtResponse<String>(true,"","Montan pari sou blackjack la pa valid");
			}
		}
		}
		return new JwtResponse<String>(true,"","Jwet la pa disponib");
	
	}
	

	public GameResp blackJackHit(Long id, UserEntity utt) {
		// TODO Auto-generated method stub
		Optional<BlackJack> bjg = bjr.findById(id);
		GameResp gr = new GameResp();
		BjConfig bj =  getBJC();
		if(bjg.isPresent()) {
			BJBrainForHit bb = new BJBrainForHit(bjg.get() ,bj,utt);
			Card c = bb.init();
		    BlackJack bjo = bjg.get();
		    HCard hc = bjo.addCardToUserHandAndGet(c);
			gr.setCode(200);
			gr.setError(false);
			gr.setHcard(hc);
			return gr;
		}else {
		gr.setCode(404);
		gr.setError(true);
		return gr;
		}
	}
	
	@Data
	public class GameResp{
		int code;
		boolean error;
		HCard hcard;
		public GameResp() {}
	}
	
	@Data
	public class BJGameState {
	   BlackJack bj;
	}


   

}
