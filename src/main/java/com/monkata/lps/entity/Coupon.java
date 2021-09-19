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
		
		
}


