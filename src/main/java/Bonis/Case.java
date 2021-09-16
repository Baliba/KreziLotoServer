package Bonis;

import lombok.Data;
// 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
@Data
public class Case {
  int id;	
  int type_win;
  float sold;
public Case() {
	super();
	// TODO Auto-generated constructor stub
}
public Case(int id,int type_win, float sold) {
	this.id = id;
	this.type_win = type_win;
	this.sold = sold;
}



  
}
