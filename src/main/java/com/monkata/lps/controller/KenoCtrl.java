package com.monkata.lps.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.monkata.lps.Helper.KenoBrain;
import com.monkata.lps.Helper.Log;
import com.monkata.lps.components.RoleName;
import com.monkata.lps.dao.BankRepository;
import com.monkata.lps.dao.BouleClientRepository;
import com.monkata.lps.dao.BouleRepository;
import com.monkata.lps.dao.DepoDao;
import com.monkata.lps.dao.GameMasterRepository;
import com.monkata.lps.dao.GameRepository;
import com.monkata.lps.dao.KenoConfigRepository;
import com.monkata.lps.dao.KenoRepository;
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
import com.monkata.lps.entity.Keno;
import com.monkata.lps.entity.KenoConfig;
import com.monkata.lps.entity.UserEntity;
import com.monkata.lps.response.JwtResponse;
import com.monkata.lps.service.JwtUserDetailsService;
import com.monkata.lps.service.KenoService;
import com.monkata.lps.service.NotService;
import com.monkata.lps.service.TicketService;

import dto.BankSold;
import dto.CouponDto;
import dto.InitGameReq;
import dto.KenoReq;
import dto.KenoRes;
import dto.WData;

@CrossOrigin("*")
@RestController
@RequestMapping({ "/api/keno" })
public class KenoCtrl extends BaseCtrl {
	
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
    KenoRepository keno ;
	
	@Autowired
	KenoConfigRepository ckeno;
	
	@Autowired
	KenoService kenos;

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
	
	@RequestMapping(value = "/getPlayerMoney", method = RequestMethod.GET)
	public ResponseEntity<?> getPlayerMoney(Authentication auth) throws Exception {
		try {
		    UserEntity utt = getUser(auth);
	        return ResponseEntity.ok(utt.getCompte());
		 }catch(Exception e) {
			 return ResponseEntity.ok(null);
		}
	}
	
	@RequestMapping(value = "/initKenoPart", method = RequestMethod.POST)
	public ResponseEntity<?> initKenoPart(Authentication auth, @RequestBody KenoReq kr) throws Exception {

		    UserEntity utt = getUser(auth);
		    
		    Bank bank = this.getBankConfig();
		    
	  		if(bank!=null && bank.isBlock_keno()) {
	  			return ResponseEntity.ok(new JwtResponse<Double>(true,utt.getCompte(), "Keno pa disponib pou kounya."));   
	  		}
	  		
		    if(utt.getCompte()>=kr.getBet()) {
		    	
		      if(kr.getTotal_num()>10 || kr.getTotal_num()!=kr.getLots().size()) {
		    	  return ResponseEntity.ok(new JwtResponse<Double>(true,utt.getCompte(), "Numerow chwazi yo pa bon."));
		      } 	
		      
		      if(kr.getBet()<10 || kr.getBet()>100) {
		    	  return ResponseEntity.ok(new JwtResponse<Double>(true,utt.getCompte(), "Ou ka parye ant 10 a 100g."));
		      }
		      utt.remain(kr.getBet());
		      utt = user.save(utt);
		      Keno k = new Keno(kr, utt.getId());
		      k.setOver(false);
		      k.setDate();
		      k = keno.save(k);
		      
		      KenoConfig kc = kenos.getKC();
		      // 
		      BankSold bs=  this.setBank(utt.getId());
		      bs.init(kc.getBank_sold());
		      KenoRes krs= new KenoRes(k, utt);
		      krs.setBank_sold(bs.getBank_sold());
		      Log.d("|===========BANK SOLD ==============> "+bs.getBank_sold() );
		      krs.setBs(bs);
		  
		      WData index = KenoBrain.getWinIndex(kc.getPayouts(), kr.getTotal_num(), kr.getBet(), bs.getBank_sold(), kc.getWin_occurrence(), kc.getGlobal_occurrence());
		      double sold = krs.setIWinIndexNow(index, kc.getPayouts(), kr.getTotal_num(), kr.getLots(), kr.get_aNumSelected());
		      // New Keno ==
		      Keno ko = k;  
		      ko.setDraw(krs.getAcombination());
		      if(!index.isWin()) {
		    	  ko.setOver(true);
		    	  ko.set_win(false);
		    	  ko.setWin_sold(0);
		      } else {
		    	  ko.setOver(true);
		    	  ko.setWin_sold(sold);
		    	  ko.set_win(true);
		    	  ko.setNext_win_sold(sold);
		    	  utt.add(sold);
		    	  user.save(utt);
		      }
		      
		      keno.save(ko);
		      krs.retartKeno();
		  	  return ResponseEntity.ok(new JwtResponse<KenoRes>(false,krs,"Siksè."));
		    } else {
		    	return ResponseEntity.ok(new JwtResponse<Double>(true,utt.getCompte(), "Ou pa gen ase lajan."));
		    }
	
	}
	
	@RequestMapping(value = "/getUserInfoForKeno", method = RequestMethod.GET)
	public ResponseEntity<?> getCompte(Authentication auth) throws Exception {
		 try {
		    UserEntity utt = getUser(auth);
		    InitGameReq igr = new InitGameReq ();
		    igr.setUser(utt);
	        return ResponseEntity.ok(igr);
		   } catch(Exception e) {
			 return ResponseEntity.ok(null);
		}
	}
	
	@JsonIgnore
	double mgWin=0, mgLost=0, gWin=0, gLost= 0;
	
	private BankSold setBank(Long id) {
		
		BankSold bs = new BankSold();
		mgLost = 0;
		mgWin = 0;
		gLost = 0;
		gWin = 0 ;
		Optional<dto.Sold> sold =   keno.getMyGlobalLost(id);
		if(sold.isPresent() &&  sold.get().getSold()>0 ) {
			mgLost = sold.get().getSold();
			bs.setMgLost(mgLost);
		}
		sold =   keno.getMyGlobalWin(id);
		if(sold.isPresent() &&  sold.get().getSold()>0) {
			mgWin = sold.get().getSold();
			bs.setMgWin(mgWin);
		}
		try {
			sold =   keno.getGlobalLost();
			if(sold.isPresent() &&  sold.get().getSold()>0 ) {
				gLost = sold.get().getSold();
				bs.setGLost(gLost);
			}
		} catch(Exception e) {
			gLost = 0;
			bs.setGLost(gLost);
		}
		
		
		try {
			sold =   keno.getGlobalWin();
			if(sold.isPresent() &&  sold.get().getSold()>0 ) {
				gWin = sold.get().getSold();
				bs.setGWin(gWin);
			}
		 } catch(Exception e) {
			gWin = 0;
			bs.setGWin(gWin);
		}
		
		
		return bs;
	}
	
	
	@RequestMapping(value = "/config", method = RequestMethod.GET)
	public ResponseEntity<?> config() throws Exception {
	    	return ResponseEntity.ok(kenos.getKC());
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseEntity<?> save(Authentication auth,  @RequestBody KenoConfig kn) throws Exception {
    	UserEntity utt = getUser(auth);
	    if(utt.getRole().getName().equals(RoleName.ADMIN) || utt.getRole().getName().equals(RoleName.MASTER) ) {
	    	ckeno.save(kn);
	    	return ResponseEntity.ok(new JwtResponse<String>(false,"",""));
	    }
		return ResponseEntity.ok(new JwtResponse<String>(true,"","Ou pa gen dwa sa. "+utt.getRole().getName()));
		
	}
	
	@RequestMapping(value = "/getKenoStat/{day}/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getKenoStat(Authentication auth,@PathVariable("day") int day, @PathVariable("id") Long id) throws Exception {
    	UserEntity utt = getUser(auth);
	    if(utt.getRole().getName().equals(RoleName.ADMIN) || utt.getRole().getName().equals(RoleName.MASTER) ) {
	    	List<Keno> ks;
	    	if(id>0) {
	    	    ks = kenos.getListGame(day, id);
	    	} else {
	    		ks = kenos.getListGame(day);
	    	}
	    	return ResponseEntity.ok(new JwtResponse<List<Keno>>(true,ks,"Siksè. "+utt.getRole().getName()));
	    }
		return ResponseEntity.ok(new JwtResponse<String>(true,"","Ou pa gen dwa sa."));
	}
	
	
	@RequestMapping(value = "/getGlobalStatKeno", method = RequestMethod.GET)
	public ResponseEntity<?> getGlobalStatKeno(Authentication auth) throws Exception {
    	UserEntity utt = getUser(auth);
	    if(utt.getRole().getName().equals(RoleName.ADMIN) || utt.getRole().getName().equals(RoleName.MASTER) ) {
	    	BankSold bs = new BankSold();
			Optional<dto.Sold>	sold =   keno.getGlobalLost();
			if(sold.isPresent()) {
				gLost = sold.get().getSold();
				bs.setGLost(gLost);
			}
			sold =   keno.getGlobalWin();
			if(sold.isPresent()) {
				gWin = sold.get().getSold();
				bs.setGWin(gWin);
			}
	    	return ResponseEntity.ok(new JwtResponse<BankSold>(true,bs,"Siksè."));
	    }
		return ResponseEntity.ok(new JwtResponse<String>(true,"","Ou pa gen dwa sa. "+utt.getRole().getName()));
	}
	
	
}
