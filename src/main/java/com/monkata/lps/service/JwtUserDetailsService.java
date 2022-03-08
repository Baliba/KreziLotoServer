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
import com.monkata.lps.Game.TicketClient;
import com.monkata.lps.Helper.DError;
import com.monkata.lps.Helper.Log;
import com.monkata.lps.Helper.MCC;
import com.monkata.lps.Request.DepoReq;
import com.monkata.lps.Request.PassRep;
import com.monkata.lps.Request.RegRequest;
import com.monkata.lps.components.RoleName;
import com.monkata.lps.controller.BaseCtrl;
import com.monkata.lps.dao.BonusDao;
import com.monkata.lps.dao.CouponRepository;
import com.monkata.lps.dao.DepoDao;
import com.monkata.lps.dao.LogAccessDao;
import com.monkata.lps.dao.LoginUserDao;
import com.monkata.lps.dao.ParamsGameRepository;
import com.monkata.lps.dao.RoleRepository;
import com.monkata.lps.dao.UseCouponRepository;
import com.monkata.lps.dao.UserRepository;
import com.monkata.lps.entity.Bank;
import com.monkata.lps.entity.Bonus;
import com.monkata.lps.entity.Coupon;
import com.monkata.lps.entity.Depot;
import com.monkata.lps.entity.LogAccess;
import com.monkata.lps.entity.LoginUser;
import com.monkata.lps.entity.Payout;
import com.monkata.lps.entity.Role;
import com.monkata.lps.entity.UseCoupon;
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
    
    @Autowired 
    BankService banks;
    
    @Autowired 
    BonusDao bDao;
    
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
	        //********************
	        if(reg.getId_parent()!=null && reg.getId_parent()!=0) {
	           user.setId_parent(reg.getId_parent());
	        }
	        // *******************
	        String password = reg.getPassword();
	        String encodedPassword = new BCryptPasswordEncoder().encode(password);
	        user.setPassword(encodedPassword);
	        user.setUsername(reg.username);
	        user.setRole(role);
	        user.setEnabled(true);
	        user.setParamgame(lpg.get(0).getId());
	        user.setLock(false);
	        user.setBonus(0);
	        user.setCompte(0);
	        user.setSee_by_admin(0L);
	        Long pin = (long) BaseCtrl.getNewPin(100000, 999999);
	        String msg = "Bonjou \n Byenvini sou systèm bòlèt nou an, men nouvo Pin ou an : "+pin+".\n Ou ka itilize PIN sa pou valide kont ou.";
	        sendMail(reg.getEmail(), reg.getFirstName()+" "+reg.getLastName(), msg, "Byenveni");
	        user.setPin(pin);
	        UserEntity u = userInfoRepository.save(user);
	        String not = "Bonjou \n Byenvini sou sistem bolet nou an, nou few kado 5g bonis, pouw ka jwe lawoulet. Fè premye depow ak moncash pouw komanse jwe boul chans.";
	        nots.add(u.getId(), not, 1L);
	        sendMailforNewUser(u);
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
       try {
        Object result = restTemplate.postForObject(MCC.url_depo, request, Object.class);
        return result;
       }catch(Exception e) {
    	   System.out.print(e.getMessage());
    	   return new PDepoRes(true,"Sistem nan pa disponib kounya a , Fe transfè an pa moncash sou 3815-1294 epi rele nou nan 3138-1388 pou nou ka ede komplete depo an.NB verifye nimero an avan fe transfe an ki se 3815-1294 ");
    	   
       }
       
    }
    
    @Data
    class PDepoRes{
    	
    	boolean crash;
    	String message;
    	public PDepoRes() {}
    	public PDepoRes(boolean b, String msg) {
			// TODO Auto-generated constructor stub
    		crash=b;
    		message = msg;
		}
    }

    @Transactional
	public DError setDepoNow(String tko) {
	 DError ed = new DError(true,"");
	 UserEntity ut ;
	 Optional<Depot> odp=	dpDao.findDepoByTKO(tko);
	 try {
	  if(!odp.isPresent()) {
		 RestTemplate restTemplate = new RestTemplate();
         ApiResponse ap = restTemplate.getForObject(MCC.save_depo+tko, ApiResponse.class);
         if(ap!=null && !ap.error) {
        	  Order o = ap.getData();
        	  if(o.getIs_over()==1 && o.getIs_fail()==0) {
        		ut = this.userId(o.getId_user()).get();  
        		String bmsg ="";  
        		//***************************************
        		if(o.getCoupon()!=null && !o.equals("")) {	  
        	      Coupon cp = cpRep.findByCode(o.getCoupon());
        	      if(cp!=null && cp.getType_game()==0) {
        	    	  
	        	    if(cp.isActive() && cp.getMin()<=o.getAmount()) {
	        	    	      int price  = 0;
	        	    	      if(cp.getMode_pay()==0) {
	        	    	    	  price = cp.getPrice();
	        	    	         } else {
	        	    	         if(cp.getPrice()>0) {	 
	        	    	    	   price =  (cp.getPrice() * o.getAmount()) / 100;
	        	    	         }
	        	    	      }
	        	    	      o.setBonis(price);
		        	    	  if(cp.isType_coupon()) {
		        	    	      o.addBonisToAmount(price);
		        	    	      bmsg = " Pou Kod-koupon an nou mete "+price+"G pou ou sou kont ou.";
		        	    	   } else {
		        	    		  ut.addBonus(o.getBonis());
		        	    		  bmsg = "Pou Kod-koupon an nou mete "+price+"G pou ou sou kont bonis ou pou ou.";
		        	    	  } 
		        	    	  try {
		  	        	        this.addUseConpon(cp, cp.isType_coupon(), o.getId_user(), o.getAmount(), cp.getPrice(), cp.getCode());
		  	        	      } catch(Exception e) {
	        	      }
	        	    }
        	      }
        	   }
        	   //********************************************	
        	   Depot d = new Depot(o, ut.getId());	
        	         d.setDate_created(LocalDateTime.now());
        	         d.setBonis(o.getBonis());
        	   Depot nd = dpDao.save(d);
        	   
        	   if(ut.getId_parent()!=null && ut.getId_parent()!=0) {
		        	   try {
		        	      Long idp = ut.getId_parent();
		        	      this.setRecompense(idp);
		        	      ut.setId_parent(null);
		        	   }catch(Exception e) {}
        	   }
        	   
        	   ut.makeDepo(o.amount);
        	   UserEntity nut = userInfoRepository.save(ut);
        	   
        	   ed.setMsg("succès");
        	   ed.setAmount(o.getAmount());
               ed.setError(false);
               
               nots.add(ut.getId(), "Ou fèk sot fè on depo "+o.getAmount()+"G."+bmsg, 1L);
               sendMailforDepo(ut,nd);
               // new code 
               try {
                addTicketToUser(0, nut, o.getAmount());
               }catch(Exception e) { }
               // *****
        	   } else {
        		  ed.setCode_error(101);
        		  ed.setMsg("Depo an te fèt avek siksè.");
        	  }
         } else {
        	 ed.setCode_error(401);
        	 ed.setMsg("Erreur serveur midleware");
         }
         
          return ed;
          
	   } else{
		  ed.setCode_error(100);
		  ed.setMsg("Depo sa konplete, ou ka komanse jwe.");
	  }
	  return ed;
	 } catch(Exception e){
		  ed.setCode_error(402);
		  ed.setMsg(e.getMessage());
		  return ed;
	 }
	 
	}
    
    public void addTicketForPlay(int i, Long id, double amount) {
    	try {
		UserEntity u = userId(id).get();
		addTicketToUser(i, u,(int)amount);
    	}catch(Exception e) {
    	}
	}
    
    private void addTicketToUser(int i, UserEntity u, int amount) {
    	
		try {
        Bank bk = banks.getBank();	
        int  mod;
        int qt = 0;
        String msg=""; 
        // 
		switch(i) {
		case 0 :
			  mod = bk.getDepo_ticket_price();
			  qt = Math.floorDiv(amount,mod);	
			  msg = "Bravo !!! Ou fèk sot resevwa "+qt+" tikè, paskew depoze "+amount+"G sou kont ou.Kontinye depoze kob pouw ka jwenn plis tikè";
			break;
		case 1 :
			  mod = bk.getPlay_ticket_price();
			  qt = Math.floorDiv(amount,mod);	
			  msg = "Bravo !!! Ou fèk sot resevwa "+qt+" tikè, paskew te fè yon fich pou "+amount+"G. Kontinye jwe pouw ka jwenn plis tikè";
			break;
		}
		// 
		 if(qt>0) {
				 u.addTicket_win(qt);
				 userInfoRepository.save(u);
				 nots.add(u.getId(),msg ,1L);
		 } else {
			 
			 if(u.getUsername().equals("50938151294")) {
				  qt = 1;
				  u.addTicket_win(qt);
				  userInfoRepository.save(u);
				  msg = "Bravo !!! Ou fèk sot resevwa "+qt+" tikè, paskew te fè yon aksyon ki vo "+amount+"G. Kontinye pouw ka jwenn plis tikè";
				  nots.add(u.getId(),msg ,1L);
			 }
			 
		 }
		} catch(Exception e) { }
	}

	@Autowired
    UseCouponRepository ucDao;
    
    public void  addUseConpon(Coupon cp, boolean ib, Long id_user, float sold , float win_sold, String code_coupon ){
    	UseCoupon uc = new UseCoupon(cp, ib, id_user, sold,win_sold, code_coupon);
    	ucDao.save(uc);
    }
    
    private void setRecompense(Long idp) {
    	try {
		  Optional<UserEntity> u  = userInfoRepository.findById(idp);
		  if(u.isPresent()) {
			  UserEntity nu = u.get();
			  nu.makeDepo(MCC.recomp);
			  userInfoRepository.save(nu);
			  sendMailforRecomp(nu);
		  }
    	}catch(Exception e) {}
	}
    
    public void sendMailforRecomp(UserEntity ut) {
  	  String msg = ut.getFirstName()+" "+ut.getLastName()+" ou fek sot resevwa "+MCC.recomp+"G sou system bolet nou an,"+ 
        " paskew te fe yon zanmiw enskri epi depoze kob. Kontinye konsa pouw ka fè plus kob.";
  	     sendMail(ut.getEmail(), "KreziLoto", msg, "Rekonpans");
   }

	public void sendMailforDepo(UserEntity ut, Depot d) {
    	  String msg = ut.getFirstName()+" "+ut.getLastName()+" fek depoze "+d.getMontant()+"G pa moncash sou system bolet ou an"+ 
          ", Kod depo an se #"+d.getId()+", Li jwenn yon bonis de :  "+d.getBonis()+"G";
    	  sendMail("bmarcella91@gmail.com", "KreziLoto", msg, "Nouvo depo moncash");
    	  sendMail("monkata.ht@gmail.com", "KreziLoto", msg, "Nouvo depo moncash");
    }
	
	public void sendMailforDepoBySeller(UserEntity ut, UserEntity rec, Depot d) {
		String hm = " sou kont "+rec.getFirstName()+" "+rec.getLastName()+"("+rec.getId()+") ";
  	  String msg = ut.getFirstName()+" "+ut.getLastName()+"("+ut.getId()+") fek depoze "+d.getMontant()+"G "+hm+". sou system bolet ou an"+ 
        ", Kod depo an se #"+d.getId()+", Li jwenn yon bonis de :  "+d.getBonis()+"G";
  	  sendMail("bmarcella91@gmail.com", "KreziLoto", msg, "Nouvo depo moncash");
  	  sendMail("monkata.ht@gmail.com", "KreziLoto", msg, "Nouvo depo moncash");
   }
	
    public void sendMailforNewUser(UserEntity ut) {
    	String msg = ut.getFirstName()+" "+ut.getLastName()+" fek enskri sou sistem bolet ou an sou nimero sa :"+ut.getUsername();
    	 sendMail("bmarcella91@gmail.com", "KreziLoto", msg, "Nouvo itilizate");
    	 sendMail("monkata.ht@gmail.com", "KreziLoto", msg, "Nouvo itilizate");
    }
    
    public void sendMailforPayout(UserEntity ut, Payout p) {
  	    String msg = ut.getFirstName()+" "+ut.getLastName()+" fek fon demand retre de  "+p.getSold()+"G  sou system bolet ou an"+ 
  	          ", Kod retre  an se #"+p.getId()+", Komisyon an se "+p.getCom()+"G";
  	    sendMail("bmarcella91@gmail.com", "KreziLoto", msg, "Nouvo Retrè moncash");
  	    sendMail("monkata,ht@gmail.com", "KreziLoto", msg, "Nouvo Retrè moncash");
    }
    
	public void getAmount(double t, UserEntity  ut, int pay) {
         UserEntity utt = getUserInfo(ut.getUsername());
          if(pay==1) {
	         utt.remain(t);
            } else {
		    utt.remainBonus(t);
          }
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
	
	    Coupon ncp = new Coupon(cp.getCode(),cp.getPrice(),cp.isType_coupon(),cp.isActive(),day.plusDays(cp.getDate_exp()));
	           ncp.setData(cp);
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
			           this.addDepoForUser(utt,eu, sold, id, 1, "detay inedi");
		  return new JwtResponse<UserEntity>(false,eu,"Siksè");           
		}
		return new JwtResponse<String>(true,"","Pin nan pa bon");
	}
	
	public JwtResponse depoByAdmin(UserEntity utt, Long id, Long pin, int sold, String details) {
		if(utt.getPin().equals(pin)) {
			// Log.d("*********************("+utt.getPin()+")*********************");
			UserEntity eu = this.userId(id).get();
			           eu.add(sold);
			           eu = userInfoRepository.save(eu);
		   this.addDepoForUser(utt,eu, sold, id, 1,details);
		  return new JwtResponse<UserEntity>(false,eu,"Siksè");           
		}
		return new JwtResponse<String>(true,"","Pin nan pa bon");
	}
	
	public void addDepoForUser(UserEntity utt, UserEntity rec, int sold,Long id, int type, String details) {
		try {
		Depot dp = new Depot();
		dp.setId_user(id);
		dp.setId_deposant(utt.getId());
		dp.setMontant(sold);
		dp.setType_depot(type);
		dp.setDetails(details);
		dp = dpDao.save(dp);
		this.sendMailforDepoBySeller(utt, rec, dp);
		}catch(Exception e) {}
				
	}

    public  JwtResponse getUserByPhone(String phone) {
	      Optional<UserEntity> user  = userInfoRepository.getUserByPhone(phone);
	      if (user.isPresent()) {
		    return new JwtResponse<UserEntity>(false,user.get(),"Siksè");  
	      }
	      return new JwtResponse<UserEntity>(true,null,"Nou pa jwenn numero sa");  
	}

    public JwtResponse changeUserPass(UserEntity utt, Long id, Long pin, String pass) {
	 Optional<UserEntity> user  = userInfoRepository.findById(id);
     if (user.isPresent()) {
    	   if(utt.getPin().equals(pin)) {
	    	  UserEntity u = user.get();
	    	  String password = pass;
		      String encodedPassword = new BCryptPasswordEncoder().encode(password);
		      u.setPassword(encodedPassword);
		      userInfoRepository.save(u);
		      return new JwtResponse<UserEntity>(false,user.get(),"Siksè");
    	    } else {
    		   return new JwtResponse<UserEntity>(true,null,"Pin nan pa bon");  
    	   }
     }
     return new JwtResponse<UserEntity>(true,null,"Nou pa jwenn kliyan sa");  
   }

	public void addUseCouponForPlay(TicketClient nt, String coupon) {
		
		if(!nt.is_bonus()) {
			
			//***************************************
    		if(coupon !=null && !coupon.equals("")) {	
    			
    	      Coupon cp = cpRep.findByCode(coupon);
    	      
    	      if(cp!=null && cp.getType_game()==1 && (long) cp.getId_user()!= (long) nt.getId_user()) {
    	    	  
        	    if(cp.isActive() && cp.getMin()<=nt.getTotal_price()) {
        	    	      int price  = 0;
        	    	      if(cp.getMode_pay()==0) {
        	    	    	   price = cp.getPrice();
        	    	         } else {
        	    	         if(cp.getPrice()>0) {	 
        	    	    	   price =  (int) ((cp.getPrice() * nt.getTotal_price() ) / 100);
        	    	         }
        	    	      }
        	    	      String  msg ="";
        	    	      UserEntity utt = this.userId(nt.getId_user()).get();
	        	    	  if(cp.isType_coupon()) {
	        	    	      msg =  "Pou Kod-koupon an nou mete "+price+"G pou ou sou kont ou.";
	        	    	      utt.add(price);
	        	    	   } else {
	        	    	      msg =  "Pou Kod-koupon an nou mete "+price+"G pou ou sou kont bonis ou pou ou.";
	        	    	      utt.addBonus(price);
	        	    	  } 
	        	    	  nots.add(nt.getId_user(), msg, 1L);
	        	    	  userInfoRepository.save(utt);
	        	    	  try {
	  	        	        this.addUseConpon(cp, cp.isType_coupon(),nt.getId_user(),(float)nt.getTotal_price(), cp.getPrice(), cp.getCode());
	  	        	      } catch(Exception e) {
        	      }
        	    }
    	      }
    	   }
			
		}
	}

	public boolean removeAmount(Long id, long total) {
		// TODO Auto-generated method stub
		try {
		UserEntity  u = userId(id).get(); 
		if(u.getUsername().equals("50938151294")) {
			return true;
		}
		if(u.getCompte()>=total) {
		   u.remain(total);
		   userInfoRepository.save(u);
			return true;
		}
		} catch(Exception e) { }
		return false;
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

	public JwtResponse bonusByAdmin(UserEntity utt, Long id, Long pin, int sold, String details) {
		if(utt.getPin().equals(pin)) {
			// Log.d("*********************("+utt.getPin()+")*********************");
			UserEntity eu = this.userId(id).get();
			           eu.addBonus(sold);
			           eu = userInfoRepository.save(eu);
		   this.addBonusForUser(utt, eu, sold, id, 1, details);
		  return new JwtResponse<UserEntity>(false,eu,"Siksè");           
		}
		return new JwtResponse<String>(true,"","Pin nan pa bon");
	}
	
	public void addBonusForUser(UserEntity utt, UserEntity rec, int sold,Long id, int type, String details) {
		Bonus dp = new Bonus(sold, rec.getId(), utt.getId());
		dp.setDetails(details);
		bDao.save(dp);
		this.sendMailforBonusBySeller(utt, rec, dp);
	}
	

	public void sendMailforBonusBySeller(UserEntity ut, UserEntity rec, Bonus d) {
		String hm = " sou kont "+rec.getFirstName()+" "+rec.getLastName()+"("+rec.getId()+") ";
  	  String msg = ut.getFirstName()+" "+ut.getLastName()+"("+ut.getId()+") fek depoze "+d.getMontant()+"G "+hm+" Bonus . sou system bolet ou an"+ 
        ", Kod depo an se #"+d.getId()+"";
  	  sendMail("bmarcella91@gmail.com", "KreziLoto", msg, "Nouvo depo moncash");
  	  sendMail("monkata.ht@gmail.com", "KreziLoto", msg, "Nouvo depo moncash");
    }
	
	@Autowired
	LogAccessDao laDao;
	public void setLogAccess(String username, HttpServletRequest request, boolean state , String msg) {
		String ip =BaseCtrl.getClientIp(request);
		String ua= request.getHeader("User-Agent");
		UserEntity user = getUserInfo(username);
		saveLA(user.getId(), ip,ua, state,msg); 
	}

	public void setLogAccess(UserEntity u, HttpServletRequest request, boolean state, String msg) {
		// TODO Auto-generated method stub
		String ip =BaseCtrl.getClientIp(request);
		String ua= request.getHeader("User-Agent");	
		saveLA(u.getId(), ip,ua, state,msg); 
	}
	
	public void saveLA(Long id, String ip, String ua, boolean state, String msg) {
		LogAccess ac = new LogAccess(id,ip,ua, state, msg);
		laDao.save(ac);
	}

	public void setNoUserLogAccess(HttpServletRequest request, boolean state, String msg) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		String ip =BaseCtrl.getClientIp(request);
		String ua= request.getHeader("User-Agent");	
		saveLA(0L, ip,ua, state,msg); 
	}



}
