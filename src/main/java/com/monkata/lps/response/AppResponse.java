/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.monkata.lps.response;

import java.io.Serializable;

import lombok.Data;

/**
 *
 * @author User
 */
@Data
public class AppResponse<T> implements Serializable  {
	private static final long serialVersionUID = 1L;
	public final  boolean crash;
    public final  String  message;
    public final  T data;
    public AppResponse(boolean c,String msg,T d){
        crash=c;
        message = msg;
        data = d;
    }
}
