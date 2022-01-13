package com.monkata.lps.Game;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class WinName {
  @Id
  public String name;
  
  public String syntax;
  public String code_mg;
  
  public WinName(String name, String syntax, String code_mg) {
	super();
	this.name = name;
	this.syntax = syntax;
	this.code_mg = code_mg;
  }

   public WinName() {
		super();
   }
  
 
}
