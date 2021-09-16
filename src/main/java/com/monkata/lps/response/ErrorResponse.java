/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.monkata.lps.response;

import java.io.Serializable;

public class ErrorResponse  extends AppResponse implements Serializable {
    
    private static final long serialVersionUID = -8091879091924046844L;

    private final String message;
    public ErrorResponse(String msg,boolean state) {
        super(state, msg, null);
        this.message = msg;
    }

}
