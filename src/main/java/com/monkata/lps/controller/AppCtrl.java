package com.monkata.lps.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import com.monkata.lps.Console;
import com.monkata.lps.Game.Boule;
import com.monkata.lps.Game.BouleClient;
import com.monkata.lps.Game.DateTF;
import com.monkata.lps.Game.Game;
import com.monkata.lps.Game.GameBrain;
import com.monkata.lps.Game.GameMaster;
import com.monkata.lps.Game.ModeGame;
import com.monkata.lps.Game.ParamsGame;
import com.monkata.lps.Game.Ticket;
import com.monkata.lps.Game.TicketClient;
import com.monkata.lps.Game.UtilGame;
import com.monkata.lps.Helper.Lang;
import com.monkata.lps.Helper.Log;
import com.monkata.lps.Helper.MCC;
import com.monkata.lps.Request.DepoReq;
import com.monkata.lps.Request.PassRep;
import com.monkata.lps.Request.RBoule;
import com.monkata.lps.Request.TicketRequest;
import com.monkata.lps.Request.TicketRequestClient;
import com.monkata.lps.Request.UserReq;
import com.monkata.lps.Tiraj.Tiraj;
import com.monkata.lps.components.RoleName;
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
import com.monkata.lps.entity.Depot;
import com.monkata.lps.entity.Notification;
import com.monkata.lps.entity.PVBank;
import com.monkata.lps.entity.Payout;
import com.monkata.lps.entity.UseCoupon;
import com.monkata.lps.entity.UserEntity;
import com.monkata.lps.entity.UserEntityReq;
import com.monkata.lps.response.AppResponse;
import com.monkata.lps.response.JwtResponse;
import com.monkata.lps.response.RSTicket;
import com.monkata.lps.response.RSTicketClient;
import com.monkata.lps.response.TicketToPay;
import com.monkata.lps.service.AppService;
import com.monkata.lps.service.BankService;
import com.monkata.lps.service.JwtUserDetailsService;
import com.monkata.lps.service.NotService;
import com.monkata.lps.service.PayoutService;
import com.monkata.lps.service.TicketService;
import com.monkata.lps.service.TicketService.MaxSellError;
import com.monkata.lps.service.TicketService.VResp;

import dto.BankAndLang;
import dto.ChekBoul;
import dto.NParamsGame;
import dto.NRole;
import lombok.Data;

@RestController
@CrossOrigin("*")
@RequestMapping({ "/api" })
public class AppCtrl extends BaseCtrl {
	
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

	@GetMapping("/home")
	public ResponseEntity<?> getApp() {
		Bank b = null;
		if (bank.findAll().size() > 0) {
			b = bank.findAll().get(0);
		}
		return ResponseEntity.ok(new JwtResponse<Bank>(false, b, "Bank"));
	}
	
	
	@GetMapping("/getMytickets")
	public ResponseEntity<?> getMytickets(Authentication auth) {
		UserEntity utt = getUser(auth);
	    List<TicketClient> tcs =	ticketc.getMytickets(utt.getId());
		return ResponseEntity.ok(new AppResponse<List<TicketClient>>(false,"lis tike ankou yo",tcs));
	}
	
	
	@GetMapping("/getTicketById/{id}")
	public ResponseEntity<?> getTicketById(@PathVariable("id") Long id, Authentication auth) {
		UserEntity utt = getUser(auth);
	    TicketClient tcs = sticket.getTicketClientById(utt.getId(), id);
		return ResponseEntity.ok(new AppResponse<TicketClient>(false,"Tike",tcs));
	}
	
	
	@GetMapping("/verify/{id}")
	public ResponseEntity<?> verify(@PathVariable("id") Long id, Authentication auth) {
		UserEntity utt = getUser(auth);
		Bank bank = this.getBankConfig();
		if(!bank.isBlock_verify()) {
			Optional<ParamsGame> opg = pgame.findById(utt.getParamgame());
			VResp tcs = sticket.verify(utt.getId(), id, opg.get());
			return ResponseEntity.ok(new AppResponse<VResp>(false,"Tike",tcs));
		   } else {
			VResp vr = sticket.getVResp("Verifye tikè bloke nan moman an");
			return ResponseEntity.ok(new AppResponse<VResp>(true,"",vr));
		}
		
	}
	
	@GetMapping("/verifyForUser/{id}/{idu}")
	public ResponseEntity<?> verifyForUser(@PathVariable("id") Long id, @PathVariable("idu") Long idu, Authentication auth) {
		UserEntity utt = getUser(auth);
		
		if(utt.getRole().getName().equals(RoleName.ADMIN) || utt.getRole().getName().equals(RoleName.MASTER) ) {
		Bank bank = this.getBankConfig();
		if(!bank.isBlock_verify()) {
		       //
			   UserEntity user = userRepository.getOne(idu);
			   // 
			   Optional<ParamsGame> opg = pgame.findById(user.getParamgame());
			   //
			   VResp tcs = sticket.verify(idu, id, opg.get());
			   return ResponseEntity.ok(new AppResponse<VResp>(false,"Tike",tcs));
			   
		      } else {
			  VResp vr = sticket.getVResp("Verifye tikè bloke nan moman an");
			  return ResponseEntity.ok(new AppResponse<VResp>(true,"",vr));
		   }
		  } else {
			VResp vr = sticket.getVResp("Ou pa gen dwa pou verifye fich pou moun");
			return ResponseEntity.ok(new AppResponse<VResp>(true,"",vr));
		}
	}
	
	@GetMapping("/verifyTicket/{id}")
	public ResponseEntity<?> verifyTicket(@PathVariable("id") Long id, Authentication auth) {
		UserEntity utt = getUser(auth);
		
		if(utt.getRole().getName().equals(RoleName.ADMIN) || utt.getRole().getName().equals(RoleName.MASTER) ) {
		Bank bank = this.getBankConfig();
		if(!bank.isBlock_verify()) {
		       //
			   TicketClient tk =  ticketc.findById(id).get();
			   
			   UserEntity user = userRepository.getOne(tk.getId_user());
			   // 
			   Optional<ParamsGame> opg = pgame.findById(user.getParamgame());
			   //
			   VResp tcs = sticket.verify(tk.getId_user(), id, opg.get());
			   return ResponseEntity.ok(new AppResponse<VResp>(false,"Tike",tcs));
			   
		      } else {
			  VResp vr = sticket.getVResp("Verifye tikè bloke nan moman an");
			  return ResponseEntity.ok(new AppResponse<VResp>(true,"",vr));
		   }
		  } else {
			VResp vr = sticket.getVResp("Ou pa gen dwa pou verifye fich pou moun");
			return ResponseEntity.ok(new AppResponse<VResp>(true,"",vr));
		}
		
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
	
	@GetMapping("/countNotification")
	public ResponseEntity<?> countNotification(Authentication auth) {
		UserEntity utt = getUser(auth);
		Long n =  notDao.countNot(utt.getId()); 
		return ResponseEntity.ok(n);
	}
	
	@GetMapping("/getNotification")
	public ResponseEntity<?> getNotification(Authentication auth) {
		UserEntity utt = getUser(auth);
		List<Notification> n =  notDao.getNotByUser(utt.getId()); 
		return ResponseEntity.ok(n);
	} 

	
	@Transactional
	@RequestMapping(value = "/addTicketClient/{pay}", method = RequestMethod.POST)
	public ResponseEntity<?> addTicketClient(@PathVariable("pay") int pay, @RequestBody TicketRequestClient tk,
	    Authentication auth) throws Exception {
		// 
		UserEntity utt = getUser(auth);
		// 
		Optional<ParamsGame> opg = pgame.findById(utt.getParamgame());
		ParamsGame pg = opg.get();
		Game CGAME = UtilGame.getGame(pg, tk.getId_game());
		//
		
//		// verify boul 
//		ChekBoul ch =  this.CheckBouleNow(tk, CGAME);
//		
//		if(ch.getRbs().size()>0) {
//			RSTicketClient nrst = new RSTicketClient<List<RBoule>>();
//			nrst.setMessage("Fich ou an gen boul ki fermen ladan li.");
//			nrst.setData(ch.getRbs());
//			nrst.setCode(999);
//			return ResponseEntity.ok(nrst);	
//		}
		
		RSTicketClient rst = new RSTicketClient<MaxSellError>();
		Bank bank = this.getBankConfig();
		
		if(bank!=null && bank.isBlock_make_ticket()) {
			rst.setMessage("Systèm nan pa disponib pou kounya");
			return ResponseEntity.ok(rst);	
		}
		
		rst.setId_user(utt.getId());
		if (!utt.getRole().getName().equals(RoleName.CLIENT)) {
			rst.setMessage("Ou pa ka jwe ak kont sa ");
			return ResponseEntity.ok(rst);
		}
		
		
		LocalTime now = LocalTime.now();
		 
		try {
		int dnow = LocalDate.now().getDayOfWeek().getValue();
		// Log.d("#########################----------->"+dnow);
		if(dnow == CGAME.getGamemaster().getDay_block()) {
			rst.setMessage("Jwèt sa bloke pou jodia");
			return ResponseEntity.ok(rst);	
		}
		}catch(Exception e) { }
		
		try {
			if(!CGAME.getGamemaster().isEnabled()) {
				rst.setMessage("Jwèt sa pa disponib");
				return ResponseEntity.ok(rst);	
			}
		}catch(Exception e) {}
		
		if (utt.isEnabled()) {
		
			MaxSellError mse = sticket.checkLotsOfTickets(tk.getLots(), CGAME);
			if (mse.isError()) {
				rst.setData(mse);
				rst.setCode(201);
				rst.setMessage("Fich ou an gen boul ki fèmen ladan li.");
				return ResponseEntity.ok(rst);
			}

			LocalTime tt = LocalTime.parse(CGAME.getGamemaster().getHour_to_block());
			LocalTime ss = LocalTime.parse(CGAME.getGamemaster().getHour_to_start_sell());
			if (now.isAfter(tt) || now.isBefore(ss)) {
				rst.setCrash(true);
				rst.setMessage(" Jwèt " + CGAME.getGamemaster().getCode() + " an pa disponib pou kounya ");
				return ResponseEntity.ok(rst);
			}
			
			if (CGAME != null) {
				// Dont forget to add delai from game
				Bank bk = this.getBankConfig();
				TicketClient nt = new TicketClient(pay, tk, utt.getId(), CGAME,bk.getDateTimeFormat(),CGAME.getGamemaster().getDelai());
				nt.setId_pg(pg.getId());
				nt.setId_user(utt.getId());
				List<BouleClient> lots = new ArrayList<>();
				for (RBoule nb : tk.getLots()) {
					ModeGame mg = UtilGame.getModeGame(CGAME, nb.getId_mg());
					if (mg != null) {
						BouleClient nbn = new BouleClient(pay, nb, CGAME, mg);
						lots.add(nbn);
					} else {
						rst.setMessage(nb.getId_mg() + " mod jwet sa pa bon pou boul sa :"+nb.getLot()+" nan "+CGAME.getName());
						return ResponseEntity.ok(rst);
					}
				}
				
				boolean isSold = sticket.checkTotalSoldTicktets(tk.getLots(), utt.getUsername(), pay);

				 if (!isSold) {
					rst.setMessage("Ou pa gen ase Kob pou jwe");
					return ResponseEntity.ok(rst);
				 }
				 
				 UserDetails.getAmount(sticket.getTotalSoldTicktets(tk.getLots()), utt, pay);
				 nt.setId_user(utt.getId());
				 nt.setTotalPrice(sticket.getTotalSoldTicktets(tk.getLots()));
				 nt = ticketc.save(nt);
				 
				 List<BouleClient> nlots = new ArrayList<>();
				 
				 for(BouleClient nb : lots) {
				    nb.setTicketclient(nt);
				  	nlots.add(boulec.save(nb));
				}
				 
				nt.setLots(nlots);
				nt.setMax_win(sticket.getMaxwin(nlots));
				// nt.setMGain();
				nt = ticketc.save(nt);
				rst.setCode(200);
				rst.setId_ticket(nt.getId());
				rst.setSdate(nt.getSdatet());
				rst.setSheure(nt.getSheure());
				rst.setCrash(false);
				rst.setMessage("Fich la kreye avec siksè...");
				nots.add(utt.getId(),"Ou fèk sot fè yon fich pou "+nt.getTotal_price()+" G.",1L);
				
				try {
				UserDetails.addTicketForPlay(1, utt.getId(), nt.getTotal_price());
				}catch(Exception e) {
					System.out.print("-----------|  "+e.getMessage()+" |++++++++++");
				}
				
				
				if(!nt.is_bonus()) {
					
					try {
						UserDetails.addUseCouponForPlay(nt, tk.getCoupon());
				    }catch(Exception e) {
				    	System.out.print("-----------|  "+e.getMessage()+" |++++++++++");
				    }
					
				  try {
	        	     apps.setDebitTransaction(4,"Jwe Bolèt",nt.getId(),nt.getTotal_price(),utt.getId());
	        	   }catch(Exception e) { }
				  
				 }
				
				return ResponseEntity.ok(rst);
			   } else {
				 rst.setMessage("Jwet sa  pa disponible");
				 return ResponseEntity.ok(rst);
			}
		} else {
			rst.setMessage("Kont ou bloke ou paka fe fich");
			return ResponseEntity.ok(rst);
		}
	}


	// is seller
	@Transactional
	@RequestMapping(value = "/addTicket", method = RequestMethod.POST)
	public ResponseEntity<?> addTicket(@RequestBody TicketRequest tk, Authentication auth) throws Exception {
		UserEntity utt = getUser(auth);

		RSTicket rst = new RSTicket();
		
		Bank bank = this.getBankConfig();
		if(bank!=null && bank.isBlock_make_ticket()) {
			rst.setMessage("Systèm nan pa disponib pou kounya");
			return ResponseEntity.ok(rst);	
		}
		
		rst.setId_user(utt.getId());
		if (!utt.getRole().getName().equals(RoleName.SELLER)) {
			rst.setMessage("Ou pa gen dwa vann fich");
			return ResponseEntity.ok(rst);
		}
		
		LocalTime now = LocalTime.now();
		if (utt.isEnabled()) {
			Optional<ParamsGame> opg = pgame.findById(utt.getParamgame());
			ParamsGame pg = opg.get();
			Game CGAME = UtilGame.getGame(pg, tk.getId_game());

			LocalTime tt = LocalTime.parse(CGAME.getGamemaster().getHour_to_block());
			LocalTime ss = LocalTime.parse(CGAME.getGamemaster().getHour_to_start_sell());
			if (now.isAfter(tt) || now.isBefore(ss)) {
				rst.setCrash(true);
				rst.setMessage(" Jwèt " + CGAME.getGamemaster().getCode() + " an pa disponib pou kounya ");
				return ResponseEntity.ok(rst);
			}
			if (CGAME != null) {
				// Dont forget to add delai from game
				Ticket nt = new Ticket(tk, utt.getId(), CGAME, "dd-MM-yyyy HH:mm:ss", CGAME.getGamemaster().getDelai());
				nt.setId_pg(pg.getId());
				List<Boule> lots = new ArrayList<>();
				for (RBoule nb : tk.getLots()) {
					ModeGame mg = UtilGame.getModeGame(CGAME, nb.getId_mg());
					if (mg != null) {
						Boule nbn = new Boule(nb, CGAME, mg);
						lots.add(nbn);
					} else {
						rst.setMessage(nb.getLot() + " mod jwet sa pa bon pa bon");
						return ResponseEntity.ok(rst);
					}
				}
				nt.setClient(tk.getClient());
				nt = ticket.save(nt);
				rst.setId_ticket(nt.getId());
				for (Boule nb : lots) {
					nb.setTicket(nt);
					boule.save(nb);
				}
				nt.setLots(lots);
				nt.setMGain();
				nt.setTotalPrice();
				nt = ticket.save(nt);
				rst.setSdate(nt.getSdatet());
				rst.setSheure(nt.getSheure());
				rst.setCrash(false);
				rst.setMessage("Fich la kreye avec siksè...");
				return ResponseEntity.ok(rst);
			} else {
				rst.setMessage("Jwet sa " + tk.getGame_name() + " pa disponible");
				return ResponseEntity.ok(rst);
			}
		} else {
			rst.setMessage("Kont ou bloke ou paka fe fich");
			return ResponseEntity.ok(rst);
		}
	}

	@GetMapping("/now")
	public ResponseEntity<?> now(Authentication auth) {
		String sdatet = toDay();
		return ResponseEntity.ok(new JwtResponse<String>(false, sdatet, "Success"));
	}

	@GetMapping("/time")
	public ResponseEntity<?> time(Authentication auth) {
		return ResponseEntity.ok(new JwtResponse<String>(false, toDay(), "Success"));
	}

	@GetMapping("/getGames")
	public ResponseEntity<?> getGames(Authentication auth) {
		return ResponseEntity.ok(mstgame.getGames());
	}

	@GetMapping("/test")
	public ResponseEntity<?> test(Authentication auth) {
		// boule.deleteAll();
		// tiraj.deleteAll();
		Ticket tk = ticket.findAll().get(0);
		tk.setSdatet("16-01-2021");
		return ResponseEntity.ok(ticket.findAll());
	}

	@GetMapping("/getTicketsByDraw/{id}/{page}/{size}")
	public ResponseEntity<?> getTicketsByDraw(@PathVariable("id") Long id, @PathVariable("page") int page,
			@PathVariable("size") int size) {
		Tiraj t = tiraj.findById(id).get();
		Long idg = t.getId_game();
		Page<Ticket> pt = sticket.getTicketByPage(size, page, idg, t.getDate_tiraj());
		return ResponseEntity.ok(pt);
	}

	@GetMapping("/searchTicketById/{id}")
	public ResponseEntity<?> getTicketsByDraw(@PathVariable("id") Long id) {
		Optional<Ticket> t = ticket.findById(id);
		TicketToPay ttp = new TicketToPay();
		if (t.isPresent()) {
			Ticket tk = t.get();
			Tiraj tr = tiraj.isGameDrawToday(tk.getSdatet(), tk.getId_game());
			if (tr != null) {
				GameBrain bg = new GameBrain(tk, tr);
				ttp.setCrash(false);
				ttp.setMessage("siksè");
				ttp.setTicket(tk);
			} else {
				ttp.setMessage("Tiraj la poko fe " + tk.getSdatet());
			}
		} else {
			ttp.setMessage("Nou pa jwen ticket sa ");
		}
		return ResponseEntity.ok(ttp);
	}

	@GetMapping("/lang")
	public ResponseEntity<?> lang(Authentication auth) {
		HashMap<String, String> lg = new HashMap<String, String>();
		int l = sbank.getBank().getLang();
		if (l == 1) {
			lg = Lang.en();
		} else {
			lg = Lang.kr();
		}
		return ResponseEntity.ok(lg);
	}

	@GetMapping("/setLang/{lang}")
	public ResponseEntity<?> setLang(Authentication auth, @PathVariable("lang") int l) {
		HashMap<String, String> lg = new HashMap<String, String>();
		Bank b = sbank.getBank();
		b.setLang(l);
		bank.save(b);
		if (l == 1) {
			lg = Lang.en();
		} else {
			lg = Lang.kr();
		}
		return ResponseEntity.ok(lg);
	}

	@GetMapping("/bank")
	public ResponseEntity<?> bank(Authentication auth) {
		HashMap<String, String> lg = new HashMap<String, String>();
		Bank b = sbank.getBank();
		return ResponseEntity.ok(b);
	}

	@Autowired
	BankService sbank;

	@GetMapping("/bankAndLang")
	public ResponseEntity<?> bankAndLang(Authentication auth) {
		BankAndLang bl = sbank.getBankLang();
		return ResponseEntity.ok(bl);
	}

	@GetMapping("/setUser")
	public ResponseEntity<?> setUser(Authentication auth) {
		SetUser su = new SetUser();
		List<UserEntity> tech = user.getUserRole(RoleName.TECH);
		List<UserEntity> sup = user.getUserRole(RoleName.SUPERVISOR);
		List<NRole> r = role.getRoles();
		List<PVBank> pbs = pvBank.findAll();
		List<NParamsGame> pgs = pgame.getPG();
		su.setTech(tech);
		su.setSup(sup);
		su.setRoles(r);
		su.setBanks(pbs);
		su.setPgs(pgs);

		return ResponseEntity.ok(su);
	}
	
	@GetMapping("/setClient")
	public ResponseEntity<?> setClient(Authentication auth) {
		SetUser su = new SetUser();
		List<UserEntity> c = user.getUserRole(RoleName.CLIENT);
		List<NRole> r = role.getRoles();
		List<PVBank> pbs = pvBank.findAll();
		List<NParamsGame> pgs = pgame.getPG();
		su.setClient(c);
		su.setRoles(r);
		su.setBanks(pbs);
		su.setPgs(pgs);
		return ResponseEntity.ok(su);
	}
	
	@RequestMapping(value = "/findOneClientById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> findOneClientById (@PathVariable("id") Long id) throws Exception {
        UserEntity u =  user.findById(id).get();
		return ResponseEntity.ok(new AppResponse<UserEntity>(false,"Detay itilizate ",u));
	}

	@RequestMapping(value = "/editUser", method = RequestMethod.POST)
	public ResponseEntity<?> login(@RequestBody UserEntityReq user, Authentication auth) throws Exception {

		UserEntity utt = getUser(auth);
	    if(utt.getRole().getName().equals(RoleName.ADMIN) || utt.getRole().getName().equals(RoleName.MASTER) ) {
	    	JwtResponse jr = UserDetails.updateUserNow(user);
	    	return ResponseEntity.ok(jr);
	    }
		return ResponseEntity.ok(new JwtResponse<String>(true,"","Ou pa gen dwa sa."));
	}

	@Data
	public class SetUser {
	
		List<UserEntity> tech, sup , client;
		List<PVBank> banks;
		List<NRole> roles;
		List<NParamsGame> pgs;
	}

	@RequestMapping(value = "/updateProfil", method = RequestMethod.POST)
	public ResponseEntity<?> updateProfil(@RequestBody UserReq u, Authentication auth) throws Exception {
		UserEntity utt = getUser(auth);
		utt.setLastName(u.getLastName());
		utt.setFirstName(u.getFirstName());
		utt.setSex(u.getSex());
		utt.setDob(u.getDob());
		utt.setPhone(u.getPhone());
		utt.setPhone_b(u.getPhone_b());
		utt.setAdress(u.getAdress());
		utt= user.save(utt);
		return ResponseEntity.ok(new AppResponse<UserEntity>(false,"Siksè",utt));
	}
	
	@RequestMapping(value = "/depotMoncash", method = RequestMethod.POST)
	public ResponseEntity<?> depot(@RequestBody DepoReq u, Authentication auth) throws Exception {
		UserEntity utt = getUser(auth);
		Bank bank = this.getBankConfig();
		if(bank!=null && bank.isBlock_depo()) {
			return ResponseEntity.ok(new AppResponse<String>(true,"Depo an bloke pou kounya an",""));
		}
		
		return ResponseEntity.ok(UserDetails.prepareDepo(u, utt.getId()));
	}
	
	@RequestMapping(value = "/changePass", method = RequestMethod.POST)
	public ResponseEntity<?> depot(@RequestBody PassRep u, Authentication auth) throws Exception {
		UserEntity utt = getUser(auth);
		return ResponseEntity.ok(UserDetails.changePass(u,utt));
	}
	
	@RequestMapping(value = "/getGameMaster", method = RequestMethod.GET)
	public ResponseEntity<?> getGameMaster(Authentication auth) throws Exception {
		UserEntity utt = getUser(auth);
	    if(utt.getRole().getName().equals(RoleName.ADMIN) || utt.getRole().getName().equals(RoleName.MASTER) ) {
	     List<GameMaster> gm = mstgame.findAll();
	     return ResponseEntity.ok(new JwtResponse<List<GameMaster>>(false,gm,"Sikse"));
	    }
		return ResponseEntity.ok(new JwtResponse<String>(true,"","Ou pa gen dwa sa."+ utt.getRole().getName()));
	}
	
	@RequestMapping(value = "/retre/{index}", method = RequestMethod.GET)
	public ResponseEntity<?> retre(@PathVariable("index") int index, Authentication auth) throws Exception {
		UserEntity utt = getUser(auth);
	    if(utt.getRole().getName().equals(RoleName.ADMIN) || utt.getRole().getName().equals(RoleName.MASTER) ) {
	     List<Payout> po = new ArrayList<>();
	     switch(index) {
	      case  1 :
	    	  po = payDao.CurrentPayOutMC();
	    	  break;
	      case  2 :
	    	  po = payDao.CurrentPayOutBK();
	    	  break;
	      case  3 :
	    	  po = payDao.CurrentPayOutCK();
	    	  break;
	      case  4 :
	    	  po = payDao.payOutMC();
	    	  break;
	      case  5 :
	    	  po = payDao.payOutBK();
	    	  break;
	      case  6 :
	    	  po = payDao.payOutCK();
	    	  break;
	     }
	     return ResponseEntity.ok(new JwtResponse<List<Payout>>(false,po,"List Retrè"));
	    }
		return ResponseEntity.ok(new JwtResponse<String>(true,"","Ou pa gen dwa sa."));
	}
	@Autowired
	PayoutService pays;
	@RequestMapping(value = "/setPaymentByAdmin/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> setPaymentByAdmin(@PathVariable("id") Long id, Authentication auth) throws Exception {
		UserEntity utt = getUser(auth);
	    if(utt.getRole().getName().equals(RoleName.ADMIN) || utt.getRole().getName().equals(RoleName.MASTER) ) {
	    	JwtResponse jr = pays.setPaymentByAdmin(id,utt);
	    	return ResponseEntity.ok(new JwtResponse<String>(false,"","siksè."));
	    }
		return ResponseEntity.ok(new JwtResponse<String>(true,"","Ou pa gen dwa sa."));
		
	}
	
	@RequestMapping(value = "/block/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> blockUser(@PathVariable("id") Long id, Authentication auth) throws Exception {
		UserEntity utt = getUser(auth);
	    if(utt.getRole().getName().equals(RoleName.ADMIN) || utt.getRole().getName().equals(RoleName.MASTER) ) {
	    	JwtResponse jr = UserDetails.blockUser(id, false);
	    	return ResponseEntity.ok(jr);
	    }
		return ResponseEntity.ok(new JwtResponse<String>(true,"","Ou pa gen dwa sa."+utt.getRole().getName()));
		
	}
	@RequestMapping(value = "/unblock/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> unblockUser(@PathVariable("id") Long id, Authentication auth) throws Exception {
		UserEntity utt = getUser(auth);
	    if(utt.getRole().getName().equals(RoleName.ADMIN) || utt.getRole().getName().equals(RoleName.MASTER) ) {
	    	JwtResponse jr = UserDetails.blockUser(id, true);
	    	return ResponseEntity.ok(jr);
	    }
		return ResponseEntity.ok(new JwtResponse<String>(true,"","Ou pa gen dwa sa."));
		
	}
	
	@RequestMapping(value = "/getNewUser", method = RequestMethod.GET)
	public ResponseEntity<?> getNewUser(Authentication auth) throws Exception {
		UserEntity utt = getUser(auth);
	    if(utt.getRole().getName().equals(RoleName.ADMIN) || utt.getRole().getName().equals(RoleName.MASTER) ) {
	    	JwtResponse jr = UserDetails.getNewUser();
	    	return ResponseEntity.ok(jr);
	    }
		return ResponseEntity.ok(new JwtResponse<String>(true,"","Ou pa gen dwa sa."));
		
	}
	
	@RequestMapping(value = "/validerUser/{pin}", method = RequestMethod.GET)
	public ResponseEntity<?> validerUser(Authentication auth, @PathVariable("pin") Long pin) throws Exception {
		UserEntity utt = getUser(auth);
	    if(utt.getRole().getName().equals(RoleName.ADMIN) || utt.getRole().getName().equals(RoleName.MASTER) ) {
	    	JwtResponse jr = UserDetails.validerUser(utt.getId(), pin );
	    	return ResponseEntity.ok(jr);
	    }
		return ResponseEntity.ok(new JwtResponse<String>(true,"","Ou pa gen dwa sa."));
		
	}
	
	@RequestMapping(value = "/resendEmail", method = RequestMethod.GET)
	public ResponseEntity<?> resendEmail(Authentication auth) throws Exception {
		UserEntity utt = getUser(auth);
	        Object jr = UserDetails.resendPin(utt);
	    	return ResponseEntity.ok(jr);
	}
	
	@RequestMapping(value = "/getUserInfoNow", method = RequestMethod.GET)
	public ResponseEntity<?> getCompte(Authentication auth) throws Exception {
		try {
		    UserEntity utt = getUser(auth);
	        return ResponseEntity.ok(utt);
		 }catch(Exception e) {
			 return ResponseEntity.ok(null);
		}
	}
	
	
	@GetMapping("/sponsors")
	public ResponseEntity<?> sponsor(Authentication auth) {
		UserEntity utt = getUser(auth);
		List<UseCoupon> ucs  = apps.getUseCouponByUser(utt.getId()); 
		return ResponseEntity.ok(new JwtResponse<List<UseCoupon>>(false,ucs,"Siksè."));
	}
	
	@GetMapping("/sponsors/{debut}/{fin}")
	public ResponseEntity<?> sponsor(Authentication auth, @PathVariable("debut") String debut, @PathVariable("fin") String fin) {
		UserEntity utt = getUser(auth);
		List<UseCoupon> ucs  = apps.getUseCouponByUser(utt.getId(), debut, fin); 
		return ResponseEntity.ok(new JwtResponse<List<UseCoupon>>(false,ucs,"Siksè."));
	}
	
	@Transactional
	@GetMapping("/payAgentNow/{id}")
	public ResponseEntity<?> payAgentNow(Authentication auth, @PathVariable("id") Long idc) {
		UserEntity utt = getUser(auth);
		return ResponseEntity.ok(apps.payAgentNow(utt.getId(),idc));
	}
	
	
	

}
