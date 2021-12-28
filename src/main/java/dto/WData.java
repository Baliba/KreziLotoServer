package dto;

import lombok.Data;

@Data
public class WData {
  int index; 
  boolean win;
  
  public WData(){}

public WData(boolean b, int iWinIndex) {
  win = b;
  index = iWinIndex;
}
}
