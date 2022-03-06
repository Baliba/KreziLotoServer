package dto;

import lombok.Data;

@Data
public class BankSold {
	
	double mgWin=0, mgLost=0, gWin=0, gLost= 0;
	
	public  double bank_sold = 0;
	double mgwl, wl;
	public String msg="NOT MENTION";
	public BankSold() {
	}
	
	public void init(double bs){
		
		   mgwl = mgWin - mgLost;
		   wl = gWin - gLost;
		   if(mgwl==0 && wl==0) {
			  this.bank_sold = 1000;  
			  msg="GAME NULL  "+wl+"G USER "+mgwl+"G";
			  return;
		   }
		  
		   if(wl<0) {
			  this.bank_sold = 500;  
			  msg="GAME LOST "+wl+"G USER "+mgwl+"G";;
			  return; 
		   }
		 
		   if(wl>0 && mgwl>wl) {
			  this.bank_sold = 500;  
			  msg="GAME WIN "+wl+"G BUT LESS THAN WIN USER "+mgwl+"G ";
			  return; 
		   }
		   
		   if(mgwl<0 && wl>0) {
			   this.bank_sold =   wl*0.20;
			   msg="GAME WIN "+wl+"G USER LOST "+mgwl+"G";
		      } else {
			   this.bank_sold = (wl*0.1>0)? wl*0.1 : 500 ;
			   msg="GAME  "+wl+"G | USER  "+mgwl+"G";
		   }
		 this.bank_sold += (bs*0.8);
	}



}
