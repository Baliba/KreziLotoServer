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
	    
	    public LocalDateTime date_order;
	    
		public UseCoupon() {
			super();
		}

		public UseCoupon(boolean ib, Long id_user, float sold, float win_sold, String code_coupon) {
			is_bonus = ib;
			this.id_user = id_user;
			amount = sold;
			win_amount = win_sold;
			this.code_coupon = code_coupon;
			this.date_order = LocalDateTime.now().minusSeconds(1);
		}
		
		
}


