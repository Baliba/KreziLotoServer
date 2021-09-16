package com.monkata.lps.Game;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@Entity
public class ParamsGame {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)		
  private Long id;	
  
  public  String name;
 
  public int is_for_client = 1; 
  @OneToMany(mappedBy = "paramsgame") 
  @JsonIgnoreProperties({"paramsgame"})
  private List<Game> games;
  
}
