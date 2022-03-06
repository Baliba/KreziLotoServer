package com.monkata.lps.controller.Mobil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.monkata.lps.Game.ParamsGame;
import com.monkata.lps.controller.BaseCtrl;
import com.monkata.lps.dao.BankRepository;
import com.monkata.lps.dao.BouleClientRepository;
import com.monkata.lps.dao.BouleRepository;
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
import com.monkata.lps.entity.UserEntity;
import com.monkata.lps.response.JwtResponse;
import com.monkata.lps.service.AppService;
import com.monkata.lps.service.JwtUserDetailsService;
import com.monkata.lps.service.NotService;
import com.monkata.lps.service.TicketService;


@RestController
@CrossOrigin("*")
@RequestMapping({ "/Mobile" })
public class MobCtrl extends BaseCtrl{
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

	@GetMapping("/getGameSetting")
	public ResponseEntity<?> getGameSetting(Authentication auth) {
		
		try {
			UserEntity utt = getUser(auth);
			if(utt.getParamgame()!=null &&  utt.getParamgame()!=0) {
				Optional<ParamsGame> opg = pgame.findById(utt.getParamgame());
				if(opg.isPresent()) {
					ParamsGame pg = opg.get();
					ParamsGame npg = getAllValidGame(pg);
					return ResponseEntity.ok(npg);
				} else {
					ParamsGame pg =pgame.findAll().get(0);
					ParamsGame npg = getAllValidGame(pg);
					return ResponseEntity.ok(npg);
				}
			} else {
				ParamsGame pg =pgame.findAll().get(0);
				ParamsGame npg = getAllValidGame(pg);
				return ResponseEntity.ok(npg);
			}
			} catch(Exception e) {
				ParamsGame pg =pgame.findAll().get(0);
				ParamsGame npg = getAllValidGame(pg);
				return ResponseEntity.ok(npg);
			}
	}
}
