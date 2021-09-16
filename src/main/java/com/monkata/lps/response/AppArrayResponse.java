/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.monkata.lps.response;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 *
 * @author User
 */
@Data
public class AppArrayResponse<T> implements Serializable  {
    public final  boolean crash;
    public final  String  message;
    public final  List<T> data;
    public AppArrayResponse(boolean c,String msg,List<T> d){
        crash=c;
        message = msg;
        data = d;
    }
}
