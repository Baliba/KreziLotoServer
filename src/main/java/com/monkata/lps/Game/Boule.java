package com.monkata.lps.Game;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.monkata.lps.Request.RBoule;
import com.monkata.lps.entity.cObj;

import lombok.Data;

 @Data
 @Entity
 public class Boule {
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(strategy = GenerationType.AUTO)	
 private Long   id;
 private Long   id_mg;
 private String code_mg;
 private Long   id_game;
 private Long   id_gamemaster;
 private String lot;
 private double montant;
 private double pwin;
 @ManyToOne
 @JsonIgnoreProperties("lots")	
 private Ticket ticket;
 public Boule() {
	 
 }
 public Boule(RBoule rb, Game g, ModeGame mg) {
    id_game = g.getId();
    id_gamemaster = g.getGamemaster().getId();
    id_mg=rb.getId_mg();
    code_mg = rb.getCode_mg();
    lot = rb.getLot();
    montant = rb.getMontant();
    pwin = 0;
    if(mg.getPart()==1) {
      String s = mg.getWin().split("=")[1];
         pwin = (montant/mg.getPoint_per_price())*Double.parseDouble(s);
        } else {
    	String[] wins = mg.getWin().split("/");
    	if(wins.length>0) {
    	   String s = wins[0].split("=")[1];
    	   pwin = (montant/mg.getPoint_per_price())*Double.parseDouble(s);
    	}
    }
 }
 
 }
