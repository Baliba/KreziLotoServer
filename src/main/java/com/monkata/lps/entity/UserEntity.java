package com.monkata.lps.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import lombok.Data;

@Entity
@Data
public class UserEntity extends cObj implements Serializable, UserDetails{
	    private static final long serialVersionUID = 1L;
	    @Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private Long id;
	    
	    @Column(nullable=true)
	    Long id_parent;
	    
	    @Column(nullable=true)
	    String username, password, lastName,firstName, sex, token;
	    
	    @Column(nullable=true)
	    LocalDate dob;
	    
	    @Column(unique=true, nullable=true)
	    String email;
	    
	    String phone, phone_b, adress;
	    
	    Long supervisor , tech, paramgame;
	    
	    @Column(nullable=true,  columnDefinition = "int default 123456")
	    Long pin;
	    
	    @Column(nullable=true)
	    Long pvbank;
	    
	    @ManyToOne
	    @JsonIgnoreProperties("users")
	    public Role role;
	    
	    @Column(nullable=true)
	    boolean enabled;
	    
	    @Column(nullable=true)
	    boolean lock;
	    
	    // Bank transfert money 
	    
	    @Column(nullable=true)
	    String  swift;
	    
	    @Column(nullable=true)
	    String  nombank;
	    
	    @Column(nullable=true)
	    String  nocompte;
	    
	    @Column(nullable=true)
	    String  nomcompte;
	    
	    // 
	    
	    @Column(nullable=true)
	    String  moncashnumber;
	  
	    // 
	    
	    @Column(nullable=true)
	    Long bank;
	    
	    @Column(nullable=true)
	    double compte;
	    
	    @Column(nullable=true)
	    double hcompte;
	   
	    
	    
	    @Column(nullable=true, columnDefinition = "int default 0")
	    int bonus;
	    
	    @Column(nullable=true,  columnDefinition = "int default 0")
	    int hbonus;
	    
	    @Column(nullable=true,  columnDefinition = "int default 0")
	    int is_pay;
	    
	    @Column(nullable=true,  columnDefinition = "int default 0")
	    Long see_by_admin;
	     
	    @Column(nullable=true,  columnDefinition = "int default 0")
	    int valider; 
	    
	    @Override
	    public Collection<? extends GrantedAuthority> getAuthorities() {
	            List<GrantedAuthority> auth =  new ArrayList<>();
	            String name = role.getName();
	            auth.add(new SimpleGrantedAuthority(name));
	            return auth; 
	    }
		@Override
		public boolean isAccountNonExpired() {
			// TODO Auto-generated method stub
			return false;
		}
		@Override
		public boolean isAccountNonLocked() {
			return lock;
		}
		@Override
		public boolean isCredentialsNonExpired() {
			return false;
		}
		@Override
		public boolean isEnabled() {
			return enabled;
		}
		public void makeDepo(int amount) {
			this.setHCompteNow();
			this.compte+= amount;
		}
		
		public void add(double amount) {
			this.setHCompteNow();
			this.compte+= amount;
		} 
		public void remain(double t) {
			this.setHCompteNow();
			this.compte -=t;  
		}
		
		public void setHCompteNow(){
			 if(this.compte>0) { this.hcompte = this.compte; }
		}
		public void setHBonusNow(){
			 if(this.bonus>0) { this.hbonus = this.bonus; }
		}
		public void removeBonus(double t) {
			// TODO Auto-generated method stub
			this.setHBonusNow();
			this.bonus-=t;
		}
		
		public void addBonus(double t) {
			// TODO Auto-generated method stub
			this.setHBonusNow();
			this.bonus +=t;
		}
		public void remainBonus(double t) {
			// TODO Auto-generated method stub
			this.setHBonusNow();
			this.bonus-=t;
		}
}
