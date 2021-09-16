package com.monkata.lps.Request;

import java.time.LocalDate;

import lombok.Data;

@Data
public class PayoutReq {
 float sold;	
 String  pass;
 int type_pay;
 int pin;
 LocalDate dateCheck;
 
String  moncash , swift,
 nom_bank,
 nom_compte,
 numero,
 nom_complet;

public PayoutReq() {
	super();
}


    
}
