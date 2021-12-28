package dto;

import lombok.Data;

@Data
public class BankSold {
	
	double mgWin=0, mgLost=0, gWin=0, gLost= 0;
	
	public  double bank_sold = 0;
	double mgwl, wl;
	public String msg;
	public BankSold() {
		
	}
	
	public void init(){
		   mgwl = mgWin - mgLost;
		   wl = gWin - gLost;
		   if(mgwl==0 && wl==0) {
			  this.bank_sold = 1000;  
			  msg="GAME NULL";
			  return;
		   }
		  
		   if(wl<0) {
			  this.bank_sold = 500;  
			  msg="GAME LOST";
			  return; 
		   }
		 
		   if(wl>0 && mgwl>wl) {
			  this.bank_sold = 500;  
			  msg="GAME WIN BUT LESS THAN WIN USER";
			  return; 
		   }
		   
		   if(mgwl<0 && wl>0) {
			   this.bank_sold =   wl*0.3;
			   
		      } else {
			   this.bank_sold = (wl*0.1>0)? wl*0.1 : 500 ;
		   }
	}

}
