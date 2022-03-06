package com.monkata.lps.entity.blackjack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lombok.Data;

@Data
public class CombMaker {
	
	
   public static List<Card> getwinsCardFor21_3(boolean win , int bj_win,String bg) {
		int z;
		int c = random(1, 4);
		int nc = c;
		int nz = 2;
	    List<Card> cards = new ArrayList<Card>();
            int s=2,e=2;
			if(bj_win==0) {
				s = 2;
				e = 7;
			} else if(bj_win==1) {
				 s = 5;
				 e = 10;
			}else if(bj_win==2) {
			     s = 5;
				 e = 13;
			}
	         else if(bj_win==3) {
	             s = 1;
				 e = 13;
			} else {
				  s = 2;
			      e = 13;
			}
			z = random(s, e);
		    cards.add(new Card(z,c,bg));
		    if(win) {
		      cards.add(new Card(z,c,bg));
		    } else {
		     nz = z;
		     while(nc==c) {
			    	  nc = random(1, 4);
			  } 
		     while(nz==z) {
			    	  nz = random(s, e);
			 } 
		     cards.add(new Card(nz,nc,bg));
		    }
	        return cards;
	}
	
   public static List<Card> getwinsCardForMix(boolean win , int bj_win, String bg) {
	   
	    int z;
		int c = random(1, 4);
		int nc = c;
		int nz = 2;
	    List<Card> cards = new ArrayList<Card>();
	    int s=2,e=2;
		if(bj_win==0) {
			s = 2;
			e = 7;
		} else if(bj_win==1) {
			 s = 5;
			 e = 10;
		}else if(bj_win==2) {
		     s = 5;
			 e = 14;
		}
         else if(bj_win==3) {
             s = 1;
			 e = 13;
		} else {
			  s = 2;
		      e = 13;
		}
		z = random(s, e);
		int c2 = random(1, 4);
		cards.add(new Card(z,c,bg));
		if(win) {
		  cards.add(new Card(z,c2,bg));
		} else {
			 nz = z;
			 nc = random(1, 4);
		     while(nz==z) {
			    	  nz = random(s, e);
			 } 
		     cards.add(new Card(nz,nc,bg));
		}
		return cards;
	}
   
   public static List<Card> getwinsCardForColour(boolean win , int bj_win, String bg) {
	   
	    int z;
		int c = random(1, 4);
		int nc = c;
		int nz = 2;
	    List<Card> cards = new ArrayList<Card>();
	    int s=2,e=2;
		if(bj_win==0) {
			s = 2;
			e = 7;
		} else if(bj_win==1) {
			 s = 5;
			 e = 10;
		}else if(bj_win==2) {
		     s = 5;
			 e = 14;
		}
         else if(bj_win==3) {
             s = 1;
			 e = 13;
		} else {
			  s = 2;
		      e = 13;
		}
		z = random(s, e);
		int z2 = random(1, 13);
		cards.add(new Card(z,c,bg));
		if(win) {
		  cards.add(new Card(z2,c,bg));
		} else {
			 while(nc==c) {
				 nc = random(1, 4);
			 } 
		   cards.add(new Card(z2,nc,bg));
		}
		return cards;
		
  	}

   public static List<Card> getwinsCardForPerfect(boolean win , int bj_win,String bg) {
	    int z;
		int c = random(1, 4);
		int nc = c;
		int nz = 2;
	    List<Card> cards = new ArrayList<Card>();
	    int s=2,e=2;
		if(bj_win==0) {
			s = 2;
			e = 7;
		} else if(bj_win==1) {
			 s = 5;
			 e = 10;
		}else if(bj_win==2) {
		     s = 5;
			 e = 14;
		}
         else if(bj_win==3) {
             s = 1;
			 e = 13;
		} else {
			  s = 2;
		      e = 13;
		}
		z = random(s, e);
	    cards.add(new Card(z,c,bg));
	    if(win) {
	      cards.add(new Card(z,c,bg));
	    } else {
	    	int ch = random(1, 10);
	    	nz = z;
	    	if(ch<=3) {
	    		while(nc==c) {
	 			   nc = random(1, 4);
	 			} 
	    		cards.add(new Card(nz,nc,bg));
	    	} if(ch<=6) {
	    	   while(nz==z) {
	   				  nz = random(s, e);
	   		    } 
		    		cards.add(new Card(nz,nc,bg));
		    	}
	    	else {
	    		while(nc==c) {
	   			   nc = random(1, 4);
	   			} 
	   		    while(nz==z) {
	   				  nz = random(s, e);
	   		    } 
	    		cards.add(new Card(nz,nc,bg));
	    	}
		  
		   
	    }
	    return cards;
   }
   public static int random(int min, int max) {
		  Random rand = new Random(); 
		 return  rand.nextInt((max - min) + 1) + min;
	}

   public static List<Card> getCardforUser(int bj_win, String bg){
	    int z;
		int c = random(1, 4);
		int nc = c;
		int nz = 2;
	    List<Card> cards = new ArrayList<Card>();
	    int s=2,e=2;
	    if(bj_win ==0) {
			s = 2;
			e = 7;
		} else if(bj_win==1) {
			 s = 5;
			 e = 10;
		} else if(bj_win==2) {
		     s = 5;
			 e = 14;
		}
         else if(bj_win==3) {
             s = 1;
			 e = 13;
		} else {
			  s = 2;
		      e = 13;
		}
		z = random(s, e);
		cards.add(new Card(z,c,bg));
	    if(bj_win ==0) {
				s = 2;
				e = 7;
			} else if(bj_win==1) {
				 s = 5;
				 e = 10;
			} else if(bj_win==2) {
			     s = 5;
				 e = 14;
			}
	         else if(bj_win==3) {
	             s = 1;
				 e = 13;
			} else {
				  s = 2;
			      e = 13;
		}
		z = random(s, e);
	    nc = random(1, 4);
	    cards.add(new Card(nz,nc,bg));
	    return cards;
   }

   public static Card getFirstDealCard(int bj_win, String bg) {
	   
	    int z;
		int c = random(1, 4);
		int nc = c;
		int nz = 2;
	    int s=2,e=2;
		if(bj_win==3) {
			s = 2;
			e = 7;
		} else if(bj_win==2) {
			 s = 5;
			 e = 10;
		}else if(bj_win==1) {
		     s = 5;
			 e = 14;
		}
        else if(bj_win==0) {
             s = 8;
			 e = 13;
		} else {
			  s = 2;
		      e = 13;
		}
		z = random(s, e);
	  return  new Card(z,c,bg);
   }

   public static Card getCardsForLose(List<Integer> tt) {
		// TODO Auto-generated method stub
	    int z;
		int c = random(1, 4);
	     if(tt.size()==1) {
	         int bj = tt.get(0);
	         int s=2,e=2;
				if( bj>=2 && bj<=8) {
					s = 2;
					e = 8;
				} else if(bj>=9 && bj<=15) {
					 s =2;
					 e = 5;
				} else if(bj>=16 && bj<=20) {
				     s = 6;
					 e = 13;
				}	
			z = random(s, e);  
			Card ca = new Card(z,c);
	     }
		return null;
  }

}
