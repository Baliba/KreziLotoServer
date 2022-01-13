package com.monkata.lps.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@Entity
public class  UseCoupon  extends cObj implements Serializable {
	
	    private static final long serialVersionUID = 1L;
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    
	    private String code_coupon;
	    
	    private Long id_user;
	    
	    @Column(nullable=true)
	    float amount;
	    
	    @Column(nullable=true)
	    float win_amount;
	    
	    @Column(nullable=true)
	    boolean is_bonus;
	    
	    @Column(nullable=true)
	    Long id_agent;
	    
	    @Column(nullable=true, columnDefinition = "int default 0")
	    int mode_pay;
	    
	    @Column(nullable=true, columnDefinition = "int default 0")
	    int type_game;
	    
	    @Column(nullable=true, columnDefinition = "int default 0")
	    int is_agent_pay;
	    
	    @Column(nullable=true)
	    public LocalDateTime date_agent_pay;
	    
	    @Column(nullable=true)
	    double win_agent;
	    
	    public LocalDateTime date_order;
	    
	    @Column(nullable=true,  columnDefinition = "int default 0")
	    double agent_amount;
	    
	    @Column(nullable=true,  columnDefinition = "int default 0")
	    double per_client;
		public UseCoupon() {
			super();
		}

		public UseCoupon(Coupon cp, boolean ib, Long id_user2, float sold, float win_sold, String cc) {
			// TODO Auto-generated constructor stub
			is_bonus = ib;
			this.id_user = id_user;
			amount = sold;
			if(cp.getMode_pay()==0) {
			  win_amount = cp.getPrice();
   	         } else {
   	         if(cp.getPrice()>0) {	 
   	        	win_amount =  (cp.getPrice() * amount) / 100;
   	        	per_client = cp.getPrice();
   	         }
   	        }
	
			this.code_coupon = cc;
			
			this.date_order = LocalDateTime.now().minusSeconds(1);
			
			id_agent  = cp.getId_user();
			
			mode_pay  = cp.getMode_pay();
			
		    type_game = cp.getType_game();
		    
		    // poursantaj 
			win_agent = cp.getWin_agent();
            // 
			if(win_agent>0) {
			  double per = (amount * win_agent) / 100;
			   agent_amount = Math.ceil(per);
			}
		}
		
		
}


