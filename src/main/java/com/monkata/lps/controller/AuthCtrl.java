package com.monkata.lps.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.monkata.lps.Helper.Log;
import com.monkata.lps.Request.JwtRequest;
import com.monkata.lps.Request.RegRequest;
import com.monkata.lps.components.RoleName;
import com.monkata.lps.dao.UserRepository;
import com.monkata.lps.entity.Bank;
import com.monkata.lps.entity.UserEntity;
import com.monkata.lps.response.AppResponse;
import com.monkata.lps.response.JwtResponse;
import com.monkata.lps.security.JwtToken;
import com.monkata.lps.service.BankService;
import com.monkata.lps.service.JwtUserDetailsService;

import dto.NewPassword;


@RestController
@CrossOrigin("*")
public class AuthCtrl  extends BaseCtrl{

	    @Autowired
	    private JwtToken jwtToken;

	    @Autowired
	    private JwtUserDetailsService jwtUserDetailsService; 
	    
	    @Autowired
	    private UserRepository userInfoRepository;
	    
	    @Autowired
	    BankService sb;
	    
	    @Autowired
		private JwtUserDetailsService UserDetails;
	    
	    
	    public UserEntity getUser(Authentication authentication) {
			UserDetails me = (UserDetails) authentication.getPrincipal();
			return this.UserDetails.getUserInfo(me.getUsername());
		}
	    
	    @RequestMapping(value = "/api/login", method = RequestMethod.POST)
	    public ResponseEntity<?> login(@RequestBody JwtRequest authenticationRequest, HttpServletRequest request) throws Exception {
	        String username = authenticationRequest.getUsername();
	        String password = authenticationRequest.getPassword();
		        if (!userInfoRepository.existsByUsername(username)){
		        	jwtUserDetailsService.setNoUserLogAccess(request,false,sb.word("MSG_BAD_USER"));
		            return ResponseEntity.ok(new JwtResponse<UserEntity>(true, null,sb.word("MSG_BAD_USER")));
		        }
		        
	             UserDetails user = jwtUserDetailsService.loadUserByUsername(username);
	            
	             UserEntity u = null;
	             boolean rep = jwtUserDetailsService.autoLogin(user, username, password);
	             
	             if(!rep) {
	            	 jwtUserDetailsService.setLogAccess(username, request,false,sb.word("MSG_BAD_PASS"));
		        	 return ResponseEntity.ok(new JwtResponse<UserEntity>(true, null,sb.word("MSG_BAD_PASS")));
		         }
	      
		          u = jwtUserDetailsService.getUserInfo(username);
		          
		          if(!u.isEnabled() || u.isLock()) {
		        	  jwtUserDetailsService.setLogAccess(u, request,false,sb.word("MSG_BLOCK_ACCOUNT"));
		        	  return ResponseEntity.ok(new JwtResponse<UserEntity>(true, null,sb.word("MSG_BLOCK_ACCOUNT")));
		          }
		         
		          Bank bank = this.getBankConfig();
		    	  if(bank!=null && bank.isBlock_login()) {
		    		 if(!u.getRole().getName().equals(RoleName.ADMIN) &&  !u.getRole().getName().equals(RoleName.MASTER) ) {
		    		         return ResponseEntity.ok(new JwtResponse<String>(true, null,"Pou kounya system nan pa bay dwa pou moun konekte retounen pita."));
		    		  }
		    	  }
		         u.setToken(jwtToken.generateToken(user));
		         jwtUserDetailsService.setLoginInfo(u.getId(), request);
		         jwtUserDetailsService.setLogAccess(u, request,true,sb.word("MSG_SUCCESS"));
		         return ResponseEntity.ok(new JwtResponse<UserEntity>(false, u,sb.word("MSG_SUCCESS")));
		      
	    }
	    
	    
	    @RequestMapping(value = "/api/register", method = RequestMethod.POST)
	    public ResponseEntity<?> register(@RequestBody RegRequest reg) throws Exception {
	            String username = reg.getUsername();
	            
	            Bank bank = this.getBankConfig();
	    		if(bank!=null && bank.isBlock_register()) {
	    		    return ResponseEntity.ok(new JwtResponse<String>(true, null,"Pou kounya system nan pa bay dwa pou moun enskri retounen pita."));
	    		}
	    		
		        if (userInfoRepository.existsByUsername(username)){
		            return ResponseEntity.ok(new JwtResponse<UserEntity>(true, null,sb.word("MSG_PHONE_EXIST")));
		         }
		        
		         if (userInfoRepository.findByEmail(reg.getEmail()).isPresent()){
		            return ResponseEntity.ok(new JwtResponse<UserEntity>(true, null,sb.word("MSG_USER_EXIST")));
		         }
		        
	             Optional<UserEntity> ou = jwtUserDetailsService.create(reg);
	             if(ou.isPresent()) {
		            UserDetails user = jwtUserDetailsService.loadUserByUsername(username);
		            UserEntity u = null;
		             boolean rep = jwtUserDetailsService.autoLogin(user, username,reg.getPassword());
			         u = jwtUserDetailsService.getUserInfo(username);
			         u.setToken(jwtToken.generateToken(user));
			         return ResponseEntity.ok(new JwtResponse<UserEntity>(false, u,sb.word("MSG_SUCCESS")));
	             } else {
	            	    return ResponseEntity.ok(new JwtResponse<UserEntity>(true, null,sb.word("NO_GAME")));
	             }
	    }
	    
	    
	    @RequestMapping(value = "/api/isUserNameExist/{username}", method = RequestMethod.GET)
	    public ResponseEntity<?> isUserNameExist (@PathVariable("username") String username) throws Exception {
		        if (userInfoRepository.existsByUsername(username)){
		            return ResponseEntity.ok(new JwtResponse<UserEntity>(true, null,sb.word("MSG_PHONE_EXIST")));
		        }
		        return ResponseEntity.ok(new JwtResponse<UserEntity>(false, null,"Siksè"));
		        
	    }
	    
	    
	    @RequestMapping(value = "/api/changeEmail/{email}", method = RequestMethod.GET)
	    public ResponseEntity<?> changeEmail (@PathVariable("email") String email, Authentication auth) throws Exception {
		        if (userInfoRepository.findByEmail(email).isPresent()){
		            return ResponseEntity.ok(new JwtResponse<UserEntity>(true, null,sb.word("MSG_USER_EXIST")+"<"+email+">"));
		        }
		        UserEntity utt = getUser(auth);
		        utt.setEmail(email);
		        Long pin =(long) getNewPin(100000, 999999);
		        String msg = "Bonjou \n Ou fek Change imel ou, men nouvo Pin ou an : "+pin;
		        jwtUserDetailsService.sendMail(email, utt.getFirstName()+" "+utt.getLastName(), msg, "Nouvo PIN");
		        utt.setPin(pin);
		        utt = userInfoRepository.save(utt);
		        return ResponseEntity.ok(new JwtResponse<String>(false, utt.getEmail() ,"Siksè"));
		        
	    }
	    
	    
	    @RequestMapping(value = "/api/valider/{pin}", method = RequestMethod.GET)
	    public ResponseEntity<?> validerCompte (@PathVariable("pin") Long pin, Authentication auth) throws Exception {
		        UserEntity utt = getUser(auth);
		        Log.d(utt.getPin() +"=="+ pin);
		        if((long)utt.getPin() == (long) pin) {
		           utt.setValider(1);
		           utt = userInfoRepository.save(utt);
		           return ResponseEntity.ok(new JwtResponse<UserEntity>(false, utt ,"Siksè"));
		        }else {
		          return ResponseEntity.ok(new JwtResponse<UserEntity>(true, utt ,"Pin nan pa bon."));
		        }
		        
	    }
	    
	    
	    @RequestMapping(value = "/api/cpass/{email}", method = RequestMethod.GET)
	    public ResponseEntity<?> validerCompte (@PathVariable("email") String email, Authentication auth) throws Exception {
	    	   Optional<UserEntity> outt = userInfoRepository.findByEmail(email);
	    	   if (outt.isPresent()){
	    		    UserEntity utt = outt.get();
	    		   //Long pin = 123456L;
	    		    Long pin = (long) getNewPin(100000, 999999);
			        String msg = "Bonjou \n Ou fek fe on nouvo demand pou chanje kod sekre ou, men nouvo PIN ou an : "+pin+" pouw modifye kod sekre a.";
			        jwtUserDetailsService.sendMail(email, utt.getFirstName()+" "+utt.getLastName(), msg, "Rekipere kont ou.");
			        utt.setPin(pin);
	    		    userInfoRepository.save(utt);
	    		    msg = "Ou resevwa on imel avek nouvo kòd sekrè an.";
	    		    return ResponseEntity.ok(new JwtResponse<String>(false, "" ,msg));
		       }
	    	   return ResponseEntity.ok(new JwtResponse<UserEntity>(true, null,"Imel sa pa valid"));
		        
	    }
	    
	    
	    @RequestMapping(value = "/api/newPassword", method = RequestMethod.POST)
	    public ResponseEntity<?> newPassword (@RequestBody NewPassword np, Authentication auth) throws Exception {
	    	   Optional<UserEntity> outt = userInfoRepository.findByEmail(np.getEmail());
	    	   if (outt.isPresent()){
	    		    UserEntity utt = outt.get();
	    		    if(utt.getPin().equals(np.getPin())) {
	    		      Long pin =(long) getNewPin(100000, 999999);
			          String msg = "Bonjou \n Ou fek chanje kod sekre ou.";
			          jwtUserDetailsService.sendMail(np.getEmail(), utt.getFirstName()+" "+utt.getLastName(), msg, "Rekipere kont ou.");
			          utt.setPin(pin);
			          String password = np.getPass();
				      String encodedPassword = new BCryptPasswordEncoder().encode(password);
				      utt.setPassword(encodedPassword);
	    		      userInfoRepository.save(utt);
	    		      msg = "Ou Fek chanje kod sekre ou an.";
	    		      return ResponseEntity.ok(new JwtResponse<String>(false, "" ,msg));
	    		    }
	    		    return ResponseEntity.ok(new JwtResponse<UserEntity>(true, null,"PIN nan pa bon"));
		       }
	    	   return ResponseEntity.ok(new JwtResponse<UserEntity>(true, null,"Imel sa pa valid"));
		        
	    }
	    
	    
	    

}
