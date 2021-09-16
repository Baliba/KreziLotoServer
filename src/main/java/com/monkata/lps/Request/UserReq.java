package com.monkata.lps.Request;

import java.time.LocalDate;

import javax.persistence.Column;

import lombok.Data;

@Data
public class UserReq{
	 String  lastName,firstName, sex, token;
	 LocalDate dob;
	 String phone, phone_b, adress;
	public UserReq() {
		super();
		// TODO Auto-generated constructor stub
	}
	 
}
