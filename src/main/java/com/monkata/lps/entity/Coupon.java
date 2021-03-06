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

import dto.CouponDto;
import lombok.Data;

@Data
@Entity
public class  Coupon  extends cObj implements Serializable {
	
	    private static final long serialVersionUID = 1L;
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    
	    @Column(unique=true)
	    private String code;
	    
	    @Column(nullable=true, columnDefinition = "int default 50")
	    int min;
	    
	    private int price;
	    public  boolean type_coupon;
	    public  boolean active;
	    
	    @JsonFormat(pattern = "dd-MM-yyyy")
	    private LocalDate date_exp;
	    
	    @Column(nullable=true)
	    Long id_user;
	    
	    @Column(nullable=true, columnDefinition = "int default 0")
	    int mode_pay;
	    
	    @Column(nullable=true, columnDefinition = "int default 0")
	    int type_game;
	    
	    @Column(nullable=true, columnDefinition = "int default 0")
	    double win_agent;
	    
		public Coupon(String code, int price, boolean type_coupon, boolean active, LocalDate date_exp) {
			super();
			this.code = code;
			this.price = price;
			this.type_coupon = type_coupon;
			this.active = active;
			this.date_exp = date_exp;
		}

		public Coupon() {
			super();
		}

		public void setData(CouponDto cp) {
			min = cp.getMin();
		    id_user = cp.getId_user();
		    win_agent = cp.getWin_agent();
		    type_game = cp.getType_game();
		    mode_pay  = cp.getMode_pay();
		}
		
		
}


