package dto;

import com.monkata.lps.Game.TicketClient;
import com.monkata.lps.entity.UserEntity;

import lombok.Data;

@Data
public class TcDto {

     public UserEntity user;
	 
	 public  TicketClient tk;
	 
	 public  TcDto( UserEntity u, TicketClient tk){
		  this.user = u;
		  this.tk = tk;
	 }
	 
	 public  TcDto() {}
}
