package dto;

import com.monkata.lps.dao.ParamsGameRepository;

import lombok.Data;

@Data
public class NParamsGame {
	
  private Long id;	
  
  private String name;
  
  public NParamsGame(Long id , String name) {
  	  this.id = id;
      this.name = name;
  }
 
}
