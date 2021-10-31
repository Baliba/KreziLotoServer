package com.monkata.lps.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

import com.monkata.lps.Console;
import com.monkata.lps.Game.GameMaster;
import com.monkata.lps.Request.TirajReq;
import com.monkata.lps.Tiraj.Tiraj;
import com.monkata.lps.dao.BankRepository;
import com.monkata.lps.dao.BouleRepository;
import com.monkata.lps.dao.GameMasterRepository;
import com.monkata.lps.dao.GameRepository;
import com.monkata.lps.dao.ModeGameRepository;
import com.monkata.lps.dao.ParamsGameRepository;
import com.monkata.lps.dao.RoleRepository;
import com.monkata.lps.dao.TicketRepository;
import com.monkata.lps.dao.TirajRepository;
import com.monkata.lps.dao.UserRepository;
import com.monkata.lps.entity.Bank;
import com.monkata.lps.entity.UserEntity;
import com.monkata.lps.response.AppResponse;
import com.monkata.lps.response.JwtResponse;
import com.monkata.lps.service.JwtUserDetailsService;
import com.monkata.lps.service.KenoService;

@CrossOrigin("*")
@RestController
@RequestMapping({ "/api/tiraj" })
public class TirajCtrl extends BaseCtrl {
	
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
	    private BouleRepository boule;
	    
	    @Autowired
	    private TirajRepository tiraj;
	    
	    @Autowired
		private JwtUserDetailsService UserDetails;
	    
	    @RequestMapping(value = "/add", method = RequestMethod.POST)
	    public ResponseEntity<?> add(@RequestBody TirajReq tq) throws Exception {
	    	      if(tq.getWin4().equals("") || tq.getWin4()==null || tq.getNum3().equals("") || tq.getNum3()==null ) {
	  		        return ResponseEntity.ok(new JwtResponse<String>(true,null,"Boul yo pa bon"));
	  		      }
	    	     Long idt = (long) tq.getGame().getId();
	    	     Tiraj itj = tiraj.isGameDrawToday(tq.getDateg(), idt);
	    	     LocalDateTime now = LocalDateTime.now();
	    	     if(itj==null) {
		             GameMaster g = mstgame.findById(idt).get();
		             LocalDateTime tt = formatDate(tq.getDateg(),tq.getTimeg());
		             
			  		 if(now.isBefore(tt)) {
			  		       return ResponseEntity.ok(new JwtResponse<Tiraj>(true,null,"Li poko lè pou tiraj la, fow tann li : <b>"+g.getHour_draw()+"</b>"));
			  		 }
		             Tiraj tj = new Tiraj(g.getDelai());
		             tj.setCode_game(g.getCode());
		             tj.setDate_tiraj( tq.getDateg());
		             tj.setHeure_tiraj(tq.getTimeg());
		             tj.setNo_game(g.getNo());
		             tj.setId_game(g.getId());
		             // SET WIN 3 
		             tq.setNum3(setWin3(tq.getNum3()));
		             tq.setWin4(setWin4(tq.getWin4()));
		             // lot 0 a 2
		             tj.setLot_0(String.valueOf(tq.getNum3().charAt(0)));
		             tj.setLot_1(String.valueOf(tq.getNum3().charAt(1)));
		             tj.setLot_2(String.valueOf(tq.getNum3().charAt(2)));
		             //  Lot 3 a 6
		             tj.setLot_3(String.valueOf(tq.getWin4().charAt(0)));
		             tj.setLot_4(String.valueOf(tq.getWin4().charAt(1)));
		             tj.setLot_5(String.valueOf(tq.getWin4().charAt(2)));
		             tj.setLot_6(String.valueOf(tq.getWin4().charAt(3)));
		             tiraj.save(tj);
			         return ResponseEntity.ok(new JwtResponse<Tiraj>(false,tj,"Success"));
	    	     }
	    	     return ResponseEntity.ok(new JwtResponse<Tiraj>(true,itj,"<h3> Ou tire bolet <b>"+itj.getCode_game()+"</b> pou dat sa : <b>"+itj.getDate_tiraj()+"</b>  deja</h3> "));
	    }
	    
	    public String setWin3(String win) {
	    	if(win.length()==1) {
	    		return "00"+win;
	    	}else if(win.length()==2) {
	    		return "0"+win;
	    	}else {
	    		return win;
	    	}
	    }
	    
	    public String setWin4(String win) {
	    	if(win.length()==1) {
	    		return "000"+win;
	    	}else if(win.length()==2) {
	    		return "00"+win;
	    	}else if(win.length()==3) {
	    		return "0"+win;
	    	 } else {
	    		return win;
	    	}
	    }
        
	    @GetMapping("/getDrawForOneGame")
	    public ResponseEntity<?> getDrawForOneGame (Authentication auth){
	    	 String date = toDay();
	    	 return ResponseEntity.ok(tiraj.findAll()); 
	     }
	    
	    @GetMapping("/getDrawsOfTheDay")
	    public ResponseEntity<?> getDrawOfTheDay (Authentication auth){
	    	 String date = toDay();
	    	 List<Tiraj> t =tiraj.getDrawsByDate(date);
	    	 return ResponseEntity.ok(t);
	    }
	    
	    @GetMapping("/getDrawYDay")
	    public ResponseEntity<?> getDrawYDay (Authentication auth){
	    	 String date = yDay();
	    	 List<Tiraj> t =tiraj.getDrawsByDate(date);
	    	 return ResponseEntity.ok(t);
	    }
	    
	    @GetMapping("/getDrawById/{id}")
	    public ResponseEntity<?> getDrawById (@PathVariable("id") Long id, Authentication auth){
	    	 Tiraj t =tiraj.findById(id).get(); 
	    	 return ResponseEntity.ok(t);
	    }
	    
	    @Autowired
	    KenoService keno;
	    
	    @RequestMapping(value = "/startGameNow", method = RequestMethod.GET)
	    public ResponseEntity<?> startGameNow(Authentication auth) throws Exception {
	    	   UserEntity user = this.getUser(auth);
	    	   Bank bank = this.getBankConfig();
	   		   if(bank!=null && bank.isBlock_roulette()) {
	   			return ResponseEntity.ok(new AppResponse<String>(true,"Jwèt roulèt la bloke pou kounya.",""));
	   		   }
	   		   
	    	   return ResponseEntity.ok(keno.play(user)); 
	    
	    }
	    

	   
	    
	   
	  
	   
	       
}
