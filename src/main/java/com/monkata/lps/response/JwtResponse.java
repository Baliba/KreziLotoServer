/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.monkata.lps.response;

import java.io.Serializable;

import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;
@Data
public class JwtResponse<T> extends AppResponse implements Serializable {
    
    private static final long serialVersionUID = -8091879091924046844L;
  
    public JwtResponse (boolean c,T user,String msg) {
    	super(c, msg, user);
    } 
}
