package com.monkata.lps.Helper;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.monkata.lps.entity.UserEntity;

import lombok.Data;

@Data
public class DError {
  public boolean error;
  public String msg;
  public int amount;
  public int code_error;
 
  double compte;
  double hcompte;
  int bonus;
  int hbonus;
  
  public void setCompte(UserEntity utt){
	  this.compte  =  utt.getCompte();
	  this.hcompte =  utt.getHcompte();
	  this.bonus   =  utt.getBonus();
	  this.hbonus  =  utt.getHbonus();
  }
 
public DError(boolean error, String msg) {
	super();
	this.error = error;
	this.msg = msg;
	this.code_error = 200;
	
}

  
}
