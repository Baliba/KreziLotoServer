package com.monkata.lps.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.monkata.lps.components.RoleName;
import com.monkata.lps.dao.BankRepository;
import com.monkata.lps.dao.BouleClientRepository;
import com.monkata.lps.dao.BouleRepository;
import com.monkata.lps.dao.DepoDao;
import com.monkata.lps.dao.GameMasterRepository;
import com.monkata.lps.dao.GameRepository;
import com.monkata.lps.dao.ModeGameRepository;
import com.monkata.lps.dao.NotDao;
import com.monkata.lps.dao.PVBankRepository;
import com.monkata.lps.dao.ParamsGameRepository;
import com.monkata.lps.dao.PayoutRepository;
import com.monkata.lps.dao.RoleRepository;
import com.monkata.lps.dao.TicketClientRepository;
import com.monkata.lps.dao.TicketRepository;
import com.monkata.lps.dao.TirajRepository;
import com.monkata.lps.dao.UserRepository;
import com.monkata.lps.entity.Coupon;
import com.monkata.lps.entity.UserEntity;
import com.monkata.lps.response.JwtResponse;
import com.monkata.lps.service.AppService;
import com.monkata.lps.service.BankService;
import com.monkata.lps.service.JwtUserDetailsService;
import com.monkata.lps.service.NotService;
import com.monkata.lps.service.TicketService;

import dto.CouponDto;

@RestController
public class AdminCtrl extends BaseCtrl {
	
	@Autowired
	NotService nots;

	@Autowired
	public UserRepository userRepository, user;

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
	private TicketService sticket;

	@Autowired
	private BouleRepository boule;

	@Autowired
	private BouleClientRepository boulec;

	@Autowired
	private JwtUserDetailsService UserDetails;

	@Autowired
	private TirajRepository tiraj;

	@Autowired
	private RoleRepository role;
	@Autowired
	private PVBankRepository pvBank;
	
	@Autowired
	private NotDao notDao;
	
	@Autowired
	PayoutRepository payDao;
	
	@Autowired
	DepoDao depoDoa;
	
	@Autowired 
	BankService banks;
	
	@Autowired
	AppService apps;

	public UserEntity getUser(Authentication authentication) {
		UserDetails me = (UserDetails) authentication.getPrincipal();
		return this.UserDetails.getUserInfo(me.getUsername());
	}

	public String now() {
		Date date = new Date();
		String strDateFormat = "hh:mm:ss a";
		DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
		String formattedDate = dateFormat.format(date);
		return formattedDate.trim();
	}
	
	    @RequestMapping(value = "/api/getTicketOfTodayByGame/{game}/{state}", method = RequestMethod.GET)
	    public ResponseEntity<?> getTicketOfTodayByGame (@PathVariable("game") Long game, @PathVariable("state") int state , Authentication auth) throws Exception {
		        UserEntity utt = getUser(auth);
		        if(utt.getRole().getName().equals(RoleName.ADMIN) || utt.getRole().getName().equals(RoleName.MASTER) ) {
		        	JwtResponse jr;
		        	if(state==0) {
			    	  jr = sticket.getTicketOfTodayByGame(game);
		        	} else if(state==1) {
			    	  jr = sticket.getTicketOfTodayByGame(game,true);
		        	}else {
		              jr = sticket.getTicketOfTodayByGame(game, false);
		        	}
			    	return ResponseEntity.ok(jr);
			    }
				return ResponseEntity.ok(new JwtResponse<String>(true,"","Ou pa gen dwa sa. "+utt.getRole().getName()));
		        
	    }
	    
	    @RequestMapping(value = "/api/getTicketByGame/{game}/{day}/{state}", method = RequestMethod.GET)
	    public ResponseEntity<?> getTicketByGame (@PathVariable("state") int state, @PathVariable("day") int day , @PathVariable("game") Long game, Authentication auth) throws Exception {
		        UserEntity utt = getUser(auth);
		        if(utt.getRole().getName().equals(RoleName.ADMIN) || utt.getRole().getName().equals(RoleName.MASTER) ) {
			    	JwtResponse jr = sticket.getTicketByGame(game, day, state);
			    	return ResponseEntity.ok(jr);
			    }
				return ResponseEntity.ok(new JwtResponse<String>(true,"","Ou pa gen dwa sa. "+utt.getRole().getName()));
		        
	    }
	    
	    
	    @RequestMapping(value = "/api/addCouponNow", method = RequestMethod.POST)
	    public ResponseEntity<?> addCouponNow (@RequestBody CouponDto cp, Authentication auth) throws Exception {
		        UserEntity utt = getUser(auth);
		        if(utt.getRole().getName().equals(RoleName.ADMIN) || utt.getRole().getName().equals(RoleName.MASTER) ) {
		        	
		        	JwtResponse jr = UserDetails.addCouponNow(cp, this.nowLD());
		        	
		            return ResponseEntity.ok(jr);
			    }
				return ResponseEntity.ok(new JwtResponse<String>(true,"","Ou pa gen dwa sa. "+utt.getRole().getName()));
		        
	    }
	    
	    
	    @RequestMapping(value = "/api/NTracking/{game}/{day}/{mode}/{live}", method = RequestMethod.GET)
	    public ResponseEntity<?> Ntracking (@PathVariable("live") int live, @PathVariable("mode") int mode, @PathVariable("day") int day, @PathVariable("game") Long game, Authentication auth) throws Exception {
		        UserEntity utt = getUser(auth);
		        if(utt.getRole().getName().equals(RoleName.ADMIN) || utt.getRole().getName().equals(RoleName.MASTER) ) {
		        	JwtResponse jr = sticket.getTrackingNumber(game,day,mode, live);
		            return ResponseEntity.ok(jr);
			    }
			return ResponseEntity.ok(new JwtResponse<String>(true,"","Ou pa gen dwa sa. "+utt.getRole().getName()));
		        
	    }
	    
	    @RequestMapping(value = "/api/getDepotAdmin/{index}/{day}", method = RequestMethod.GET)
	    public ResponseEntity<?> getPastDepotByAdmin (@PathVariable("day") int day, @PathVariable("index") int index, Authentication auth) throws Exception {
		        UserEntity utt = getUser(auth);
		        if(utt.getRole().getName().equals(RoleName.ADMIN) || utt.getRole().getName().equals(RoleName.MASTER) ) {
		        	
		        	      JwtResponse jr = UserDetails.getPastDepotByAdmin(day,index);
		                  return ResponseEntity.ok(jr);
			    }
			return ResponseEntity.ok(new JwtResponse<String>(true,"","Ou pa gen dwa sa. "+utt.getRole().getName()));
		        
	    }
	    
	    @RequestMapping(value = "/api/depoByAdmin/{id}/{sold}/{pin}", method = RequestMethod.GET)
	    public ResponseEntity<?> depoByAdmin (@PathVariable("id") Long id,@PathVariable("sold") int sold, @PathVariable("pin") Long pin, Authentication auth) throws Exception {
		        UserEntity utt = getUser(auth);
		        if(utt.getRole().getName().equals(RoleName.ADMIN) || utt.getRole().getName().equals(RoleName.MASTER) ) {
		        	      JwtResponse jr = UserDetails.depoByAdmin(utt, id,pin,sold);
		                  return ResponseEntity.ok(jr);
			    }
			return ResponseEntity.ok(new JwtResponse<String>(true,"","Ou pa gen dwa sa. "+utt.getRole().getName()));
		        
	    }
	    
	    @RequestMapping(value = "/api/getDepotStat", method = RequestMethod.GET)
	    public ResponseEntity<?> getDepotStat (Authentication auth) throws Exception {
		        UserEntity utt = getUser(auth);
		        if(utt.getRole().getName().equals(RoleName.ADMIN) || utt.getRole().getName().equals(RoleName.MASTER) ) {
		        	      JwtResponse jr = UserDetails.getDepoStat();
		                  return ResponseEntity.ok(jr);
			    }
			return ResponseEntity.ok(new JwtResponse<String>(true,"","Ou pa gen dwa sa. "+utt.getRole().getName()));
		        
	    }
	    
	    @RequestMapping(value = "/api/getUserByPhone/{phone}", method = RequestMethod.GET)
	    public ResponseEntity<?> getUserByPhone (@PathVariable("phone") String phone, Authentication auth) throws Exception {
		        UserEntity utt = getUser(auth);
		        if(utt.getRole().getName().equals(RoleName.ADMIN) || utt.getRole().getName().equals(RoleName.MASTER) ) {
		        	      JwtResponse jr = UserDetails.getUserByPhone(phone);
		                  return ResponseEntity.ok(jr);
			    }
			return ResponseEntity.ok(new JwtResponse<String>(true,"","Ou pa gen dwa sa. "+utt.getRole().getName()));
		        
	    }
	    
	    @RequestMapping(value = "/api/changeUserPass/{id}/{pass}/{pin}", method = RequestMethod.GET)
	    public ResponseEntity<?> changeUserPass (@PathVariable("id") Long id,@PathVariable("pass") String pass, @PathVariable("pin") Long pin, Authentication auth) throws Exception {
		        UserEntity utt = getUser(auth);
		        if(utt.getRole().getName().equals(RoleName.ADMIN) || utt.getRole().getName().equals(RoleName.MASTER) ) {
		        	      JwtResponse jr = UserDetails.changeUserPass(utt, id,pin,pass);
		                  return ResponseEntity.ok(jr);
			    }
			return ResponseEntity.ok(new JwtResponse<String>(true,"","Ou pa gen dwa sa. "+utt.getRole().getName()));
		        
	    }
	
	    @Transactional
	    @RequestMapping(value = "/api/initNewGame/{id}", method = RequestMethod.GET)
	    public ResponseEntity<?> initNewGame (@PathVariable("id") Long id, Authentication auth) throws Exception {
		        UserEntity utt = getUser(auth);
		        if(utt.getRole().getName().equals(RoleName.ADMIN) || utt.getRole().getName().equals(RoleName.MASTER) ) {
		        	      JwtResponse jr = banks.initNewGame(id);
		                  return ResponseEntity.ok(jr);
			    }
			return ResponseEntity.ok(new JwtResponse<String>(true,"","Ou pa gen dwa sa. "+utt.getRole().getName()));
		        
	    }
	    
	    
	    @RequestMapping(value = "/api/getModeGameMaster", method = RequestMethod.GET)
	    public ResponseEntity<?> getModeGameMaster (Authentication auth) throws Exception {
		        UserEntity utt = getUser(auth);
		        if(utt.getRole().getName().equals(RoleName.ADMIN) || utt.getRole().getName().equals(RoleName.MASTER) ) {
		        	      JwtResponse jr = apps.getModeGameMaster();
		                  return ResponseEntity.ok(jr);
			    }
			return ResponseEntity.ok(new JwtResponse<String>(true,"","Ou pa gen dwa sa. "+utt.getRole().getName()));
		        
	    }
	    
	    @RequestMapping(value = "/api/addModeGameToGame/{id}/{no_mgm}", method = RequestMethod.GET)
	    public ResponseEntity<?> addModeGameToGame (@PathVariable("no_mgm") String no_mgm, @PathVariable("id") Long id, Authentication auth) throws Exception {
		        UserEntity utt = getUser(auth);
		        if(utt.getRole().getName().equals(RoleName.ADMIN) || utt.getRole().getName().equals(RoleName.MASTER) ) {
		        	      JwtResponse jr =banks.addModeGameToGame(id, no_mgm);
		                  return ResponseEntity.ok(jr);
			    }
			return ResponseEntity.ok(new JwtResponse<String>(true,"","Ou pa gen dwa sa. "+utt.getRole().getName()));
		        
	    }
	    
	    
	    @RequestMapping(value = "/api/getUsecoupon/{debut}/{fin}/{mg}", method = RequestMethod.GET)
	    public ResponseEntity<?> initNewGame (@PathVariable("mg") int mg,@PathVariable("debut") String debut, @PathVariable("fin") String fin, Authentication auth) throws Exception {
		        UserEntity utt = getUser(auth);
		        if(utt.getRole().getName().equals(RoleName.ADMIN) || utt.getRole().getName().equals(RoleName.MASTER) ) {
		        	      JwtResponse jr = apps.getUseCoupon(debut,fin, mg);
		                  return ResponseEntity.ok(jr);
			    }
			return ResponseEntity.ok(new JwtResponse<String>(true,"","Ou pa gen dwa sa. "+utt.getRole().getName()));
		        
	    }
	
}
