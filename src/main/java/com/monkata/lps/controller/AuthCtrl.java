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

import com.monkata.lps.Request.JwtRequest;
import com.monkata.lps.Request.RegRequest;
import com.monkata.lps.dao.UserRepository;
import com.monkata.lps.entity.UserEntity;
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
		            return ResponseEntity.ok(new JwtResponse<UserEntity>(true, null,sb.word("MSG_BAD_USER")));
		        }
	            UserDetails user = jwtUserDetailsService.loadUserByUsername(username);

	            if(!user.isEnabled()) {
	        	  return ResponseEntity.ok(new JwtResponse<UserEntity>(true, null,sb.word("MSG_BLOCK_ACCOUNT")));
	            }
	            UserEntity u = null;
	             boolean rep = jwtUserDetailsService.autoLogin(user, username, password);
	             if(!rep) {
		        	 return ResponseEntity.ok(new JwtResponse<UserEntity>(true, null,sb.word("MSG_BAD_PASS")));
		         }
	             
		         u = jwtUserDetailsService.getUserInfo(username);
		         u.setToken(jwtToken.generateToken(user));
		         jwtUserDetailsService.setLoginInfo(u.getId(), request);
		         return ResponseEntity.ok(new JwtResponse<UserEntity>(false, u,sb.word("MSG_SUCCESS")));
		       
	     
	    }
	    
	    
	    @RequestMapping(value = "/api/register", method = RequestMethod.POST)
	    public ResponseEntity<?> register(@RequestBody RegRequest reg) throws Exception {
	            String username = reg.getUsername();
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
		        if(utt.getPin() == pin) {
		           utt.setValider(1);
		           utt = userInfoRepository.save(utt);
		           return ResponseEntity.ok(new JwtResponse<UserEntity>(false, utt ,"Siksè"));
		        }
		        return ResponseEntity.ok(new JwtResponse<UserEntity>(true, utt ,"Pin nan pa bon."));
		        
	    }
	    
	    
	    @RequestMapping(value = "/api/cpass/{email}", method = RequestMethod.GET)
	    public ResponseEntity<?> validerCompte (@PathVariable("email") String email, Authentication auth) throws Exception {
	    	   Optional<UserEntity> outt = userInfoRepository.findByEmail(email);
	    	   if (outt.isPresent()){
	    		    UserEntity utt = outt.get();
	    		   //Long pin = 123456L;
	    		    Long pin = (long) getNewPin(100000, 999999);
			        String msg = "Bonjou \n Ou fek fe on nouvo demand pou chanje kod sekre ou, men nouvo PIN ou an : "+pin+" pouw modifye kod sekrè a.";
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
	    		    if(np.getPin() == utt.getPin()) {
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
