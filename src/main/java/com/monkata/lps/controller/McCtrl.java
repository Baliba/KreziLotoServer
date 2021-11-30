package com.monkata.lps.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.monkata.lps.Console;
import com.monkata.lps.Game.GameMaster;
import com.monkata.lps.Game.TicketClient;
import com.monkata.lps.Helper.DError;
import com.monkata.lps.Request.BankReq;
import com.monkata.lps.Request.PayoutReq;
import com.monkata.lps.Request.TirajReq;
import com.monkata.lps.Tiraj.Tiraj;
import com.monkata.lps.components.RoleName;
import com.monkata.lps.dao.BankRepository;
import com.monkata.lps.dao.BouleRepository;
import com.monkata.lps.dao.DepoDao;
import com.monkata.lps.dao.GameMasterRepository;
import com.monkata.lps.dao.GameRepository;
import com.monkata.lps.dao.ModeGameRepository;
import com.monkata.lps.dao.NotDao;
import com.monkata.lps.dao.ParamsGameRepository;
import com.monkata.lps.dao.RoleRepository;
import com.monkata.lps.dao.TicketClientRepository;
import com.monkata.lps.dao.TicketRepository;
import com.monkata.lps.dao.TirajRepository;
import com.monkata.lps.dao.UserRepository;
import com.monkata.lps.entity.Bank;
import com.monkata.lps.entity.Depot;
import com.monkata.lps.entity.Notification;
import com.monkata.lps.entity.Payout;
import com.monkata.lps.entity.UserEntity;
import com.monkata.lps.response.AppResponse;
import com.monkata.lps.response.JwtResponse;
import com.monkata.lps.service.JwtUserDetailsService;
import com.monkata.lps.service.KenoService;
import com.monkata.lps.service.PayoutService;
import com.monkata.lps.service.TicketService;

import dto.PayoutError;

@CrossOrigin("*")
@RestController
@RequestMapping({ "/api/moncash" })
public class McCtrl  extends BaseCtrl {
	
	    @Autowired
	    public UserRepository userRepository;
	  
	    @Autowired
	    private RoleRepository roleRepository;
	     
	    @Autowired
	    private GameRepository game;
	    
	    @Autowired
	    private GameMasterRepository mstgame;
	    
	    @Autowired
	    private ModeGameRepository mgame;
	    
	    @Autowired
	    private BankRepository bank;

	    @Autowired
	    private ParamsGameRepository pgame;
	    
	    @Autowired
	    private TicketRepository ticket;
	    
	    @Autowired
		private TicketClientRepository ticketc;
	    
	    @Autowired
	    private BouleRepository boule;
	    
	    @Autowired
	    private TirajRepository tiraj;
	    
	    @Autowired
		private JwtUserDetailsService UserDetails;
	    
	    
	  
	    @Transactional
	    @RequestMapping(value = "/addDepo/{token_order}", method = RequestMethod.GET)
	    public ResponseEntity<?> add(@PathVariable("token_order") String tko) throws Exception {
	    	DError ed = this.UserDetails.setDepoNow(tko);
	    	return ResponseEntity.ok(ed); 
	    }
	    
	    
	    @Transactional
	    @RequestMapping(value = "/addDepoFromWallet/{token_order}", method = RequestMethod.GET)
	    public ResponseEntity<?> addDepoFromWalet(@PathVariable("token_order") String tko) throws Exception {
	    	DError ed = this.UserDetails.setDepoNow(tko);
	    	return ResponseEntity.ok(ed); 
	    }
	    
	    
	    @Transactional
	    @RequestMapping(value = "/configPayment", method = RequestMethod.POST)
	    public ResponseEntity<?> setMoncashNumber(@RequestBody BankReq bank, Authentication auth) throws Exception {
	    	UserEntity user = this.getUser(auth);
	    	 //String epass = new BCryptPasswordEncoder().encode(bank.getPass());
	       if(new  BCryptPasswordEncoder().matches(bank.getPass(), user.getPassword())) {
	     	 if(bank.getType_ud()==0) {
	    	 user.setMoncashnumber(bank.getMoncash());
	    	 user =  userRepository.save(user);
	    	 } else{
	    	user.setSwift(bank.getSwitf());	
	    	user.setNombank(bank.getNombank());
	    	user.setNocompte(bank.getNocompte());
	    	user.setNomcompte(bank.getNomcompte());
	    	user = userRepository.save(user);
	    	}
	    	return ResponseEntity.ok(new JwtResponse<UserEntity>(false,user,"Success")); 
	       }
	      return ResponseEntity.ok(new JwtResponse<UserEntity>(true,user,"Kod sekre a pa bon.")); 
	    	 
	    }
	    
	    @Autowired
	    PayoutService payouts;
	    
	    @Transactional
	    @RequestMapping(value = "/payout", method = RequestMethod.POST)
	    public ResponseEntity<?> add(@RequestBody PayoutReq pay, Authentication auth) throws Exception {
	    	   UserEntity user = this.getUser(auth);
	    	   Bank bank = this.getBankConfig();
	  		   if(bank!=null && bank.isBlock_payout()) {
	  			return ResponseEntity.ok(new JwtResponse<String>(true,null,"Ou pa ka fè retrè pou kounya."));
	  		   }
	    	  if(new  BCryptPasswordEncoder().matches(pay.getPass(), user.getPassword())) {
	    	    JwtResponse pe =  payouts.setPay(user, pay);
	    	    return ResponseEntity.ok(pe); 
	    	  }
	    	  return ResponseEntity.ok(new JwtResponse<String>(true,null,"Kod sekre a pa bon.")); 
	    }
	    
	    
	    @RequestMapping(value = "/getCurrentPayout", method = RequestMethod.GET)
	    public ResponseEntity<?> getCurrentPayout(Authentication auth) throws Exception {
	    	   UserEntity user = this.getUser(auth);
	    	   JwtResponse pe =  payouts.getCurrentPayout(user.getId());
	    	    return ResponseEntity.ok(pe); 
	    	 
	    }
	    
	    
	    @RequestMapping(value = "/pastPayout/{day}", method = RequestMethod.GET)
	    public ResponseEntity<?> pastPayout(@PathVariable("day") int d, Authentication auth) throws Exception {
	    	   UserEntity user = this.getUser(auth);
	    	   JwtResponse pe =  payouts.pastPayout(user.getId(), d);
	    	    return ResponseEntity.ok(pe); 
	    	 
	    }
	    @Autowired
	    DepoDao dp;
	    
	    @RequestMapping(value = "/pastDepot/{day}", method = RequestMethod.GET)
	    public ResponseEntity<?> pastDepo(@PathVariable("day") int d, Authentication auth) throws Exception {
	    	   UserEntity user = this.getUser(auth);
	    	   List<Depot> dpt=  dp.getPastDepot(user.getId(), d);
	    	   return ResponseEntity.ok(new JwtResponse<List<Depot>>(false,dpt,"Depo ki gen "+d+" jou o plis")); 
	    
	    }
	    
	    
	    @RequestMapping(value = "/pastTicket/{day}", method = RequestMethod.GET)
	    public ResponseEntity<?> pastTicket(@PathVariable("day") int d, Authentication auth) throws Exception {
	    	   UserEntity user = this.getUser(auth);
	    	   List<TicketClient> dpt=  ticketc.getMyPastTickets(user.getId(), d);
	    	   return ResponseEntity.ok(new JwtResponse<List<TicketClient>>(false,dpt,"Tikèt ki gen "+d+" jou o plis")); 
	    
	    }
	    
	    @RequestMapping(value = "/pastTiraj/{day}", method = RequestMethod.GET)
	    public ResponseEntity<?> pastTiraj(@PathVariable("day") int day) throws Exception {
	    	   List<Tiraj> dpt=  tiraj.getLastTiraj(day);
	    	   return ResponseEntity.ok(new JwtResponse<List<Tiraj>>(false,dpt,"Tiraj ki gen "+day+" jou o plis")); 
	    
	    }
	    
	    
	    @RequestMapping(value = "/pastTicketById/{day}/{id}", method = RequestMethod.GET)
	    public ResponseEntity<?> pastTicket(@PathVariable("id") long id ,@PathVariable("day") int d, Authentication auth) throws Exception {
	    	   UserEntity user =  userRepository.findById(id).get();
	    	   List<TicketClient> dpt=  ticketc.getMyPastTickets(user.getId(), d);
	    	   return ResponseEntity.ok(new JwtResponse<List<TicketClient>>(false,dpt,"Tikèt ki gen "+d+" jou o plis")); 
	    
	    }
	    
	    @RequestMapping(value = "/currentTicketById/{day}/{id}", method = RequestMethod.GET)
	    public ResponseEntity<?> currentTicket(@PathVariable("id") long id ,@PathVariable("day") int d, Authentication auth) throws Exception {
	    	   UserEntity user =  userRepository.findById(id).get();
	    	   List<TicketClient> dpt=  ticketc.getMyCurrentTickets(user.getId(), d);
	    	   return ResponseEntity.ok(new JwtResponse<List<TicketClient>>(false,dpt,"Tikèt ki gen "+d+" jou o plis")); 
	    
	    }
	    
	    
	    @RequestMapping(value = "/pastDepotById/{day}/{id}", method = RequestMethod.GET)
	    public ResponseEntity<?> pastDepotById(@PathVariable("id") long id  , @PathVariable("day") int d, Authentication auth) throws Exception {
	    	   UserEntity user =  userRepository.findById(id).get();
	    	   List<Depot> dpt=  dp.getPastDepot(user.getId(), d);
	    	   return ResponseEntity.ok(new JwtResponse<List<Depot>>(false,dpt,"Depo ki gen "+d+" jou o plis")); 
	    }
	    
	    
	    @RequestMapping(value = "/pastPayoutById/{day}/{id}", method = RequestMethod.GET)
	    public ResponseEntity<?> pastPayoutById(@PathVariable("id") long id , @PathVariable("day") int d, Authentication auth) throws Exception {
	    	   UserEntity user =  userRepository.findById(id).get();
	    	   JwtResponse pe =  payouts.pastPayout(user.getId(), d);
	    	   return ResponseEntity.ok(pe); 
	    	 
	    }
	 
	    

		@Autowired
		private TicketService sticket;
	    
	    @RequestMapping(value = "/winToDay", method = RequestMethod.GET)
	    public ResponseEntity<?> winToDay( Authentication auth) throws Exception {
	    	UserEntity utt = getUser(auth);
		    if(utt.getRole().getName().equals(RoleName.ADMIN) || utt.getRole().getName().equals(RoleName.MASTER) ) {
		    	JwtResponse jr = sticket.getWinToDay();
		    	return ResponseEntity.ok(jr);
		    }
			return ResponseEntity.ok(new JwtResponse<String>(true,"","Ou pa gen dwa sa. "+utt.getRole().getName()));
			
	    }
	    
	    @RequestMapping(value = "/winToGlobal", method = RequestMethod.GET)
	    public ResponseEntity<?> winToGlobal( Authentication auth) throws Exception {
	    	UserEntity utt = getUser(auth);
		    if(utt.getRole().getName().equals(RoleName.ADMIN) || utt.getRole().getName().equals(RoleName.MASTER) ) {
		    	JwtResponse jr = sticket.getWinGlobal();
		    	return ResponseEntity.ok(jr);
		    }
			return ResponseEntity.ok(new JwtResponse<String>(true,"","Ou pa gen dwa sa. "+utt.getRole().getName()));
			
	    }
	    
	    
	    @RequestMapping(value = "/lostToDay", method = RequestMethod.GET)
	    public ResponseEntity<?> lostToDay( Authentication auth) throws Exception {
	    	UserEntity utt = getUser(auth);
		    if(utt.getRole().getName().equals(RoleName.ADMIN) || utt.getRole().getName().equals(RoleName.MASTER) ) {
		    	JwtResponse jr = sticket.getLostToDay();
		    	return ResponseEntity.ok(jr);
		    }
			return ResponseEntity.ok(new JwtResponse<String>(true,"","Ou pa gen dwa sa. "+utt.getRole().getName()));
			
	    }
	    
	    @RequestMapping(value = "/lostToGlobal", method = RequestMethod.GET)
	    public ResponseEntity<?> lostToGlobal( Authentication auth) throws Exception {
	    	UserEntity utt = getUser(auth);
		    if(utt.getRole().getName().equals(RoleName.ADMIN) || utt.getRole().getName().equals(RoleName.MASTER) ) {
		    	JwtResponse jr = sticket.getLostGlobal();
		    	return ResponseEntity.ok(jr);
		    }
			return ResponseEntity.ok(new JwtResponse<String>(true,"","Ou pa gen dwa sa. "+utt.getRole().getName()));
			
	    }
	    
	    @Autowired
	    NotDao nots;
	    
	    @Transactional
		@Modifying
	    @RequestMapping(value = "/getNotByUser", method = RequestMethod.GET)
		public ResponseEntity<?> getNotByUser(Authentication auth) throws Exception {
			UserEntity utt = getUser(auth);
			List<Notification> ns = nots.getNotByUser(utt.getId());
			for(Notification n : ns) {
				nots.seeAll(n.getId());
			}
			return ResponseEntity.ok(new JwtResponse<List<Notification>>(true,ns,"Lis notifikasyon."));
			
		}
	
		 
	   
	       
}
