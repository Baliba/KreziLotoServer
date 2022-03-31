package com.monkata.lps.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
import com.monkata.lps.controller.AppCtrl.SetUser;
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
import com.monkata.lps.entity.Bank;
import com.monkata.lps.entity.Coupon;
import com.monkata.lps.entity.PVBank;
import com.monkata.lps.entity.UserEntity;
import com.monkata.lps.response.JwtResponse;
import com.monkata.lps.service.AppService;
import com.monkata.lps.service.BankService;
import com.monkata.lps.service.JwtUserDetailsService;
import com.monkata.lps.service.NotService;
import com.monkata.lps.service.TicketService;

import dto.CouponDto;
import dto.NParamsGame;
import dto.NRole;
import dto.SearchTicketRes;

@RestController
public class SystemCtrl extends BaseCtrl {
	
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
	
	    @Transactional
	    @GetMapping("/api/getUserStat/{id}/{page}/{size}")
		public ResponseEntity<?> getBlockUser(@PathVariable("size") int size, @PathVariable("page") int page,  @PathVariable("id") Long id, Authentication auth) {
	    	    UserEntity utt = getUser(auth);
		        if(utt.getRole().getName().equals(RoleName.ADMIN) || utt.getRole().getName().equals(RoleName.MASTER) ) {
		        	    JwtResponse jr = apps.getUserStat(id, page, size);
						return ResponseEntity.ok(jr);
				 }
		    	return ResponseEntity.ok(new JwtResponse<String>(true,"","Ou pa gen dwa sa. "+utt.getRole().getName()));
				  
		}
	    
	    @Transactional
	    @GetMapping("/api/monthlyRepport/{year}")
		public ResponseEntity<?> monthlyRepport(@PathVariable("year") int year,Authentication auth) {
	    	    UserEntity utt = getUser(auth);
		        if(utt.getRole().getName().equals(RoleName.ADMIN) || utt.getRole().getName().equals(RoleName.MASTER) ) {
		        	    JwtResponse jr = apps.monthlyRepport(year);
						return ResponseEntity.ok(jr);
				 }
		    	return ResponseEntity.ok(new JwtResponse<String>(true,"","Ou pa gen dwa sa. "+utt.getRole().getName()));
				  
	    }
	    
	    @Transactional
	    @GetMapping("/api/changeTicket/{sold}")
		public ResponseEntity<?> changeTicket(@PathVariable("sold") double sold, Authentication auth) {
	    	    UserEntity utt = getUser(auth);
		        if(utt.getRole().getName().equals(RoleName.CLIENT) || utt.getRole().getName().equals(RoleName.MASTER) || utt.getRole().getName().equals(RoleName.ADMIN)) {
		        	    Bank b =this.getBankConfig();
		        	    JwtResponse jr = apps.changeTicketToMoney(sold,utt,b);
						return ResponseEntity.ok(jr);
				}
		    	return ResponseEntity.ok(new JwtResponse<String>(true,"","Ou pa gen dwa sa. "+utt.getRole().getName()));
				  
		}
	    
	    @Transactional
	    @GetMapping("/api/getTransaction/{debut}/{fin}/{mg}/{id}")
		public ResponseEntity<?> getTransaction(@PathVariable("id") Long id, @PathVariable("debut") String debut, @PathVariable("fin") String fin, @PathVariable("mg") int mg, Authentication auth) {
	    	    UserEntity utt = getUser(auth);
		        if(utt.getRole().getName().equals(RoleName.ADMIN) || utt.getRole().getName().equals(RoleName.MASTER)) {
		        	    Bank b =this.getBankConfig();
		        	    JwtResponse jr = apps.getTransaction(id,debut, fin, mg);
						return ResponseEntity.ok(jr);
				}
		    	return ResponseEntity.ok(new JwtResponse<String>(true,"","Ou pa gen dwa sa. "+utt.getRole().getName()));
				  
		}
	
}
