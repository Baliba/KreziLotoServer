package dto;

import lombok.Data;

@Data
public class UserRapport {
  Long id;
  String username;
  String fullName;
  public double depot;
  public double retrait;
  public double play;
  public double win;
  public UserRapport(){
	  this.depot = 0;
	  this.retrait = 0;
	  this.play = 0;
	  this.win = 0;
  }
}
