/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.monkata.lps.Request;

import java.io.Serializable;
import lombok.Data;

@Data
public final class JwtRequest implements Serializable  {
    
    private static final long serialVersionUID = 5926468583005150707L;
	public  String username;
    public  String password;
    public JwtRequest(){
    }
    public JwtRequest(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
    }
    
    public String getUsername() {
 		return username;
 	}
 	public void setUsername(String username) {
 		this.username = username;
 	}
 	public String getPassword() {
 		return password;
 	}
 	public void setPassword(String password) {
 		this.password = password;
 	}

   
    
}
