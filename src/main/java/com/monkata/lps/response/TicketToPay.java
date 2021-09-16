package com.monkata.lps.response;

import com.monkata.lps.Game.Ticket;

import lombok.Data;

@Data
public class TicketToPay {
  public Ticket ticket;
  String message;
  boolean crash;
public TicketToPay() {
	super();
	crash = true;
}
  
}
