package com.monkata.lps.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.monkata.lps.Request.BJReq;
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
import com.monkata.lps.entity.blackjack.BjConfig;
import com.monkata.lps.response.JwtResponse;
import com.monkata.lps.service.AppService;
import com.monkata.lps.service.BankService;
import com.monkata.lps.service.GameService;
import com.monkata.lps.service.GameService.GameResp;
import com.monkata.lps.service.JwtUserDetailsService;
import com.monkata.lps.service.NotService;
import com.monkata.lps.service.TicketService;

import dto.CouponDto;
import dto.SearchTicketRes;

@CrossOrigin("*")
@RestController
@RequestMapping({ "/api/game" })
public class GameCtrl extends BaseCtrl {
	
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
	
	@Autowired
	GameService games;

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
	
	    @RequestMapping(value = "/blackjack/init", method = RequestMethod.GET)
	    public ResponseEntity<?> initBj (Authentication auth) throws Exception {
		        UserEntity utt = getUser(auth);
		        if(utt.getRole().getName().equals(RoleName.ADMIN) || utt.getRole().getName().equals(RoleName.MASTER) ) {
		        	JwtResponse jr = games.iniBlackJackNow();
			    	return ResponseEntity.ok(jr);
			    }
				return ResponseEntity.ok(new JwtResponse<String>(true,"","Ou pa gen dwa sa. "));
		        
	    }
	    
	    @RequestMapping(value = "/blackjack/config", method = RequestMethod.GET)
	    public ResponseEntity<?> getBjConfig ( Authentication auth) throws Exception {
		        UserEntity utt = getUser(auth);
		        if(utt.getRole().getName().equals(RoleName.ADMIN) || utt.getRole().getName().equals(RoleName.MASTER) ) {
		        	JwtResponse jr = games.getBjConfig();
			    	return ResponseEntity.ok(jr);
			    }
				return ResponseEntity.ok(new JwtResponse<String>(true,"","Ou pa gen dwa sa. "));
	    }
	    
	    
	    @RequestMapping(value = "/blackjack/save", method = RequestMethod.POST)
	    public ResponseEntity<?> editBJConfig (@RequestBody BjConfig bjc, Authentication auth) throws Exception {
		        UserEntity utt = getUser(auth);
		        if(utt.getRole().getName().equals(RoleName.ADMIN) || utt.getRole().getName().equals(RoleName.MASTER) ) {
		        	JwtResponse jr = games.editBjc(bjc);
			    	return ResponseEntity.ok(jr);
			    }
				return ResponseEntity.ok(new JwtResponse<String>(true,"","Ou pa gen dwa sa. "));
	    }
	    
	    @RequestMapping(value = "/blackjack/deck", method = RequestMethod.GET)
	    public ResponseEntity<?> getSimpleDeck () throws Exception {
		       	JwtResponse jr = games.getSimpleDeck();
			    return ResponseEntity.ok(jr);
	    }
	    
	    @RequestMapping(value = "/blackjack/configUser", method = RequestMethod.GET)
	    public ResponseEntity<?> configUser () throws Exception {
		       	JwtResponse jr = games.configUser();
			    return ResponseEntity.ok(jr);
	    }
	    
	    @RequestMapping(value = "/blackjack/initGameBJ", method = RequestMethod.POST)
	    public ResponseEntity<?> initGameBJ (@RequestBody BJReq bjc, Authentication auth) throws Exception {
	    	    UserEntity utt = getUser(auth);
	    	    JwtResponse jr = games.initGameBJ(bjc,utt);
			    return ResponseEntity.ok(jr);
	    }
	    
	    @RequestMapping(value = "/blackjack/hit/{id}", method = RequestMethod.GET)
	    public ResponseEntity<?> hit (@PathVariable("id") Long id,Authentication auth) throws Exception {
	    	     UserEntity utt = getUser(auth);
	    	     GameResp jr = games.blackJackHit(id,utt);
			     return ResponseEntity.ok(jr);
	    }
	    
	 
}
