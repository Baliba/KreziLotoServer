package com.monkata.lps.Request;

import lombok.Data;

@Data
public class DepoReq {
    Long sold;
    String cp;

	public DepoReq(Long sold) {
		super();
		this.sold = sold;
	}
	
	public DepoReq(Long sold, String cp) {
		super();
		this.sold = sold;
		this.cp = cp;
	}

	public DepoReq() {
		super();
		// TODO Auto-generated constructor stub
	}
    
}
