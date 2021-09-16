package dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.monkata.lps.entity.Role;

import lombok.Data;

@Data
public class RequestUser {
	
	    private Long id;
	    
	    String username, password, lastName,firstName, sex, token;
	    String email;
	    
	    String phone, phone_b, adress;
	    
	    Long supervisor , tech, paramgame, pin;
	    
	    Long pvbank;
	    public Long role;
	    
	    boolean enabled;
	    boolean lock;
	    Long bank;
	    double compte;
	    double hcompte;
	   

}
