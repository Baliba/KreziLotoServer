package com.monkata.lps.Helper;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DError {
  public boolean error;
  public String msg;
  public int amount;
  public int code_error;
public DError(boolean error, String msg) {
	super();
	this.error = error;
	this.msg = msg;
	this.code_error = 200;
}

  
}
