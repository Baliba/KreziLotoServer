package com.monkata.lps.Tiraj;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.monkata.lps.Game.Game;
import com.monkata.lps.Game.GameMaster;
import com.monkata.lps.entity.cObj;

import lombok.Data;

@Data
@Entity
public class Tiraj  implements  Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)	
  private Long   id;
  private Long   id_game;
  private String code_game, no_game;	
  private String date_tiraj;
  private String heure_tiraj;
  private LocalDate date_limit_pay;
  String lot_0;
  String lot_1;
  String lot_2;
  String lot_3;
  String lot_4;
  String lot_5;
  String lot_6;
  public Tiraj(int delai) {
	  LocalDate localDate = LocalDate.now();
	  date_limit_pay =  localDate.plusDays(delai);
  }
  public Tiraj() {
	  
  }
  
}
