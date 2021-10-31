/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.monkata.lps.service;


import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.monkata.lps.Helper.DError;
import com.monkata.lps.Helper.Log;
import com.monkata.lps.Helper.MCC;
import com.monkata.lps.Request.DepoReq;
import com.monkata.lps.Request.PassRep;
import com.monkata.lps.Request.RegRequest;
import com.monkata.lps.components.RoleName;
import com.monkata.lps.controller.BaseCtrl;
import com.monkata.lps.dao.CouponRepository;
import com.monkata.lps.dao.DepoDao;
import com.monkata.lps.dao.LoginUserDao;
import com.monkata.lps.dao.ParamsGameRepository;
import com.monkata.lps.dao.RoleRepository;
import com.monkata.lps.dao.UserRepository;
import com.monkata.lps.entity.Coupon;
import com.monkata.lps.entity.Depot;
import com.monkata.lps.entity.LoginUser;
import com.monkata.lps.entity.Role;
import com.monkata.lps.entity.UserEntity;
import com.monkata.lps.entity.UserEntityReq;
import com.monkata.lps.response.ApiResponse;
import com.monkata.lps.response.AppResponse;
import com.monkata.lps.response.JwtResponse;
import com.monkata.lps.response.Order;

import dto.CouponDto;
import dto.DepoStat;
import dto.NParamsGame;
import dto.Sold;
import lombok.Data;
import net.minidev.json.JSONObject;

@Component
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userInfoRepository;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private ParamsGameRepository pgame;
    
    @Autowired
    NotService nots;
    
    @Autowired
    CouponRepository cpRep;
    
    @Autowired 
    DepoDao dpDao;
    public  Optional<UserEntity>  userId(Long id)  {
            Optional<UserEntity> user = userInfoRepository.findById(id);
            return user;
   
    }
    
    public void  changePass(String hpass2, String hpass,String pass){
    	
    }
    
   public UserEntity save(UserEntity u){
    	return userInfoRepository.save(u);
    }
 
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userInfoRepository.findByUsername(username);
        
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),user.getAuthorities());
    }
    
    public UserEntity getUserInfo(String username) {
    	return userInfoRepository.findByUsername(username);
    }
    public boolean  autoLogin (UserDetails user, String username, String password) {
    	try {
    	Collection<? extends GrantedAuthority> auth =  user.getAuthorities();
        UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
        if(upat!=null) {
	        authenticationManager.authenticate(upat);
	        if (upat.isAuthenticated()) {
	            SecurityContextHolder.getContext().setAuthentication(upat);
	            return true;
	        }
        }
         return false;
    	} catch(Exception e) { 
    		return false;
    	}
    }
    
    public  Optional<UserEntity> create(RegRequest reg){
    	List<NParamsGame> lpg =  pgame.getPG();
    	if(!lpg.isEmpty()) {
	     	Role role = roleRepository.findByName(RoleName.CLIENT);
	        UserEntity user = new UserEntity();
	        user.setFirstName(reg.getFirstName());
	        user.setLastName(reg.getLastName());
	        user.setEmail(reg.getEmail());
	        String password = reg.getPassword();
	        String encodedPassword = new BCryptPasswordEncoder().encode(password);
	        user.setPassword(encodedPassword);
	        user.setUsername(reg.username);
	        user.setRole(role);
	        user.setEnabled(true);
	        user.setParamgame(lpg.get(0).getId());
	        user.setLock(false);
	        user.setBonus(5);
	        user.setCompte(0);
	        Long pin = (long) BaseCtrl.getNewPin(100000, 999999);
	        String msg = "Bonjou \n Byenvini sou systèm bòlèt nou an, men nouvo Pin ou an : "+pin+".\n Ou ka itilize PIN sa pou valide kont ou.";
	        sendMail(reg.getEmail(), reg.getFirstName()+" "+reg.getLastName(), msg, "Byenveni");
	        user.setPin(pin);
	        UserEntity u = userInfoRepository.save(user);
	        String not = "Bonjou \n Byenvini sou sistem bolet nou an, nou few kado 5g bonis, pouw ka jwe lawoulet. Fè premye depow ak moncash pouw komanse jwe boul chans.";
	        nots.add(u.getId(), not, 1L);
	        return  Optional.of(u);
    	}
    	return Optional.ofNullable(null);
    }
    @Transactional 
    public   Object prepareDepo(DepoReq u, Long idu) {
        Long sold  = u.getSold();
        String cp = u.getCp();
    	DepoReqToMK d = new DepoReqToMK();
    	// ResponseEntity<?> result ;
    	d.setId_user(idu);
        d.setAmount(sold);
        d.setMethod_payment(0);
        d.setType_order("DEPOT");
        d.setCoupon(u.getCp());
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Apikey",MCC.apikey);    
        headers.set("Appkey",MCC.appkey);  
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setContentType(MediaType.APPLICATION_JSON);
        MultiValueMap<String, DepoReqToMK > map= new LinkedMultiValueMap<String,DepoReqToMK>();
        map.add("data",d);
        HttpEntity<MultiValueMap<String, DepoReqToMK>> request = new HttpEntity<MultiValueMap<String,DepoReqToMK >>(map, headers);
        Object result = restTemplate.postForObject(MCC.url_depo, request, Object.class);
        return result;
    }

    @Transactional
	public DError setDepoNow(String tko, UserEntity ut) {
	 DError ed = new DError(true,"");
	 Optional<Depot> odp=	dpDao.findDepoByTKO(tko);
	 try {
	  if(!odp.isPresent()) {
		 RestTemplate restTemplate = new RestTemplate();
         ApiResponse ap = restTemplate.getForObject(MCC.save_depo+tko, ApiResponse.class);
         if(ap!=null && !ap.error) {
        	  Order o = ap.getData();
        	  
        	  if(o.getIs_over()==1 && o.getIs_fail()==0) {
        		UserEntity u = getUserInfo(ut.getUsername());		  
        		String bmsg ="";  
        		
        	   if(o.getCoupon()!=null && !o.equals("")) {	  
        	      Coupon cp = cpRep.findByCode(o.getCoupon());
        	      if(cp!=null) {
	        	    if(cp.isActive() && cp.getMin()<=o.getAmount()) {
	        	    	o.setBonis(cp.getPrice());
		        	    	  if(cp.type_coupon) {
		        	    	     o.addBonisToAmount(cp.getPrice());
		        	    	     bmsg = " Pou Kod-koupon an nou mete "+cp.getPrice()+"G pou ou sou kont ou.";
		        	    	  } else {
		        	    		  u.addBonus(o.getBonis());
		        	    		  bmsg = "Pou Kod-koupon an nou mete "+cp.getPrice()+"G pou ou sou kont bonis ou pou ou.";
		        	    	  } 
	        	      }
        	      }
        	   }
        	   Depot d = new Depot(o, u.getId());	
        	   d.setDate_created(LocalDateTime.now());
        	   Depot nd = dpDao.save(d);
        	   u.makeDepo(o.amount);
        	   userInfoRepository.save(u);
        	   ed.setMsg("succes");
        	   ed.setAmount(o.getAmount());
               ed.setError(false);
               nots.add(ut.getId(), "Ou fèk sot fè on depo "+o.getAmount()+"G."+bmsg, 1L);
        	  } else {
        		  ed.setCode_error(101);
        		  ed.setMsg("Depo sa echwe ou byen li fet deja");
        	  }
         } else {
        	 ed.setCode_error(401);
        	 ed.setMsg("Erreur serveur midleware");
         }
          return ed;
	   }else{
		  ed.setCode_error(100);
		  ed.setMsg("Depo sa an fet deja ");
	  }
	  return ed;
	 } catch(Exception e){
		  ed.setCode_error(402);
		  ed.setMsg(e.getMessage());
		  return ed;
	 }
	}

	public void getAmount(double t, UserEntity  ut ) {
      UserEntity utt = getUserInfo(ut.getUsername());
	  utt.remain(t);
	  userInfoRepository.save(utt);
	}
	
	public void setAmount(double t, UserEntity  ut) {
		  UserEntity utt = getUserInfo(ut.getUsername());
		  utt.add(t);
		  userInfoRepository.save(utt);
	}
	
	public void addAmount(double t, Long id) {
		  UserEntity utt = userId(id).get();
		  utt.add(t);
		  userInfoRepository.save(utt);
		
	}
	
	public UserEntity addAmountV2(double t, Long id) {
		  UserEntity utt = userId(id).get();
		  utt.add(t);
		  return  userInfoRepository.save(utt);
		
	}
	
	public UserEntity  addBonus(double t, Long id) {
		  UserEntity utt = userId(id).get();
		  utt.addBonus(t);
		  return  userInfoRepository.save(utt);
		
	}
	
	public UserEntity removeBonus(double t, Long id) {
		  UserEntity utt = userId(id).get();
		  utt.removeBonus(t);
	     return  userInfoRepository.save(utt);
		
	}
	
	
	public PayRes pay(float sold) {
		 RestTemplate restTemplate = new RestTemplate();
		 PayRes ap = restTemplate.getForObject(MCC.url_depo, PayRes.class);
		 return null;
	}
	
	@Data
	public class PayRes{
		public Order order;
		public String  pay;
	}
	
	@Data 
	@JsonIgnoreProperties(ignoreUnknown = true)
	public class MKRes implements Serializable {
		public boolean crash;
		public String MESSAGE;
		public int code;
		public DepoRes DATA;
		public MKRes() {}
	}
	
	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	 class DepoRes implements Serializable {
		public Order order;
		public String  pay;
		public DepoRes() {}
	}
	
	 @Data
	 class DepoReqToMK  {
		public int method_payment;
		public Long amount, id_user;
		public String  type_order, coupon;
		public DepoReqToMK() {}

	}

	public AppResponse<String> changePass(PassRep u, UserEntity utt) {
		 String password = u.getHpass();
	     String encodedPassword = new BCryptPasswordEncoder().encode(password);
	     if(encodedPassword.equals(utt.getPassword())) {
		  password = u.getNpass();
		  encodedPassword = new BCryptPasswordEncoder().encode(password);
		  utt.setPassword(encodedPassword);
		  userInfoRepository.save(utt);
		  return new AppResponse<String>(false,"kod sekre ou an modifye avek Siksè ","");
	     }
	 	return new AppResponse<String>(true,"Kod sekre yo pa menm",""  );
	}
    @Autowired
    LoginUserDao luDao;
    
   

	public void setLoginInfo(Long id, HttpServletRequest request) {
		// TODO Auto-generated method stub
		Optional<LoginUser> oe = luDao.findById(id);
		 String   ipAddress = BaseCtrl.getClientIp(request);
	     LoginUser lu;
	     if(oe.isPresent()) {
		      lu = oe.get();
	     }else {
	    	  lu = new LoginUser();
	     }
	    lu.setIp(ipAddress);
		lu.setDate_log(BaseCtrl.toDayFixe());
	    luDao.save(lu);
	}

	public JwtResponse blockUser(Long id , boolean d) {
		UserEntity ue = userInfoRepository.findById(id).get();
                   ue.setEnabled(d);
                   ue.setLock(!d);
                   ue =  userInfoRepository.save(ue) ;   
		return new JwtResponse<UserEntity>(false,ue,"Siksè");
	}

	public JwtResponse updateUserNow(UserEntityReq u) {
	    UserEntity utt = userInfoRepository.findById(u.getId()).get();
		utt.setLastName(u.getLastName());
		utt.setFirstName(u.getFirstName());
		utt.setSex(u.getSex());
		utt.setDob(u.getDob());
		utt.setPhone(u.getPhone());
		utt.setPhone_b(u.getPhone_b());
		utt.setAdress(u.getAdress());
		//
		utt.setEmail(u.getEmail());
		// 
		utt.setPin((long)BaseCtrl.getNewPin(100000,999999));
		// 
		utt.setMoncashnumber(u.getMoncashnumber());
		// 
		utt.setSwift(u.getSwift());	
    	utt.setNombank(u.getNombank());
    	utt.setNocompte(u.getNocompte());
    	utt.setNomcompte(u.getNomcompte());
	    utt =  userInfoRepository.save(utt);  
	    return new JwtResponse<UserEntity>(false,utt,"Siksè");
	}

	public JwtResponse getNewUser() {
		// TODO Auto-generated method stub
	    return new JwtResponse<List<UserEntity>>(false,userInfoRepository.getNewUser(),"Siksè");
	}

	public JwtResponse validerUser(Long id, Long id2) {
		// TODO Auto-generated method stub
		UserEntity utt = userInfoRepository.findById(id2).get();
		utt.setSee_by_admin(id);
		utt =  userInfoRepository.save(utt);  
		return new JwtResponse<UserEntity>(false,null,"Siksè");
	}
	
	
	public Object sendMail(String email, String nom, String msg, String sujet) {
		    try {
		    RestTemplate restTemplate = new RestTemplate();
	        HttpHeaders headers = new HttpHeaders();
	        headers.set("apikey",MCC.apikey);    
	        headers.set("appkey",MCC.appkey);   
	        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        MultiValueMap<String, Mail> map= new LinkedMultiValueMap<String, Mail>();
	        Mail mail = new Mail();
	        mail.setEmail(email);
	        mail.setName(nom);
	        mail.setMessage(msg);
	        mail.setSujet(sujet);
	        map.add("data",mail);
	        HttpEntity<MultiValueMap<String, Mail>> request = new HttpEntity<MultiValueMap<String, Mail>>(map, headers);
	        Object result = restTemplate.postForObject(MCC.url_mail, request, Object.class);
	       
	        return result;
	       
		    } catch(Exception e) {
		    	return e;
		    }
	}
    
	public JwtResponse addCouponNow(CouponDto cp, LocalDate day) {
		Log.d(cp.getCode()+"/"+cp.getPrice()+"/"+cp.isActive()+"/"+cp.isType_coupon());
	    Coupon ncp = new Coupon(cp.getCode(),cp.getPrice(),cp.isType_coupon(),cp.isActive(),day.plusDays(cp.getDate_exp()));
	    Coupon cpo =  cpRep.save(ncp);
		return new JwtResponse<Coupon>(false,cpo,"Siksè");
	}
	
	@Data
	class Mail{
		String email, name, message, sujet;
		public Mail() {
			
		}
	}

	public Object resendPin(UserEntity utt) {
		Long pin = (long)BaseCtrl.getNewPin(100000,999999);
		utt.setPin(pin);
		userInfoRepository.save(utt);  
		String msg = "Bonjou \n Men nouvo Pin ou an : "+pin+".\n Ou ka itilize PIN sa pou valide kont ou.";
	    return  sendMail(utt.getEmail(), utt.getFirstName()+" "+utt.getLastName(), msg, "Nouvo pin");
	}


	public JwtResponse getPastDepotByAdmin(int day,  int index) {
		List<Depot> dps = dpDao.getPastDepotByAdmin(day, index);
		return new JwtResponse<List<Depot>>(false,dps,"Siksè");
	}

	public JwtResponse getDepoStat() {
		// TODO Auto-generated method stub
		DepoStat ds = new DepoStat();
		try {
			Optional<Sold> s1 =  dpDao.getTotalDepo();
			if(s1.isPresent()) {
				ds.setDepo_tt(s1.get().getSold());
			}
		}
		catch(Exception e) {
			
		}
		// *************** ||**************//
		try {
			Optional<Sold> s2 =  dpDao.getTotalDepoOther(0);
			if(s2.isPresent()) {
				ds.setDepo_mc(s2.get().getSold());
			} 
		 } catch(Exception e) {
			
		}
		
		// *************** ||**************//
	    try {
	    Optional<Sold> s2 =  dpDao.getTotalDepoOther(1);
		if(s2.isPresent()) {
		   ds.setDepo_slr(s2.get().getSold());
		}
	     }
	    catch(Exception e) {
				
		}
		// *************** ||**************//
	    try {
		    Optional<Sold>  s2 =  dpDao.getTotalDepoOther(2);
			if(s2.isPresent()) {
				ds.setDepo_cc(s2.get().getSold());
			}
	    } catch(Exception e) {
			
	    }
		return new JwtResponse<DepoStat>(false,ds,"Siksè");
	}

	public JwtResponse depoByAdmin(UserEntity utt, Long id, Long pin, int sold) {
	
		if(utt.getPin().equals(pin)) {
			// Log.d("*********************("+utt.getPin()+")*********************");
			UserEntity eu = this.userId(id).get();
			           eu.add(sold);
			           eu =userInfoRepository.save(eu);
		  return new JwtResponse<UserEntity>(false,eu,"Siksè");           
		}
		return new JwtResponse<String>(true,"","Pin nan pa bon");
	}

   public  JwtResponse getUserByPhone(String phone) {
	      Optional<UserEntity> user  = userInfoRepository.getUserByPhone(phone);
	      if (user.isPresent()) {
		    return new JwtResponse<UserEntity>(false,user.get(),"Siksè");  
	      }
	      return new JwtResponse<UserEntity>(true,null,"Nou pa jwenn numero sa");  
	}


}
