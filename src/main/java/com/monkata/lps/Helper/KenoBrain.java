package com.monkata.lps.Helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.monkata.lps.entity.KenoPayouts;

import dto.WData;

public class KenoBrain {

	
	
	public static WData getWinIndex(List<KenoPayouts> PAYOUTS ,int _iTotalNum, double _iCurBet, double _iBank, List<Integer> WIN_OCCURRENCE, int go ){
		
		    int iWinIndex = -1;
		   
	        for (int  i = 0; i < PAYOUTS.get(_iTotalNum-1).getPays().size(); i++) {
	        	int pay = PAYOUTS.get(_iTotalNum-1).getPays().get(i);
	            double iTotalWin = pay * _iCurBet;
	           //  Log.d("|===========NORMAL==============>"+ iTotalWin+" = "+pay+"*"+_iCurBet+"\n" );
	                iWinIndex = i;
	                if (iTotalWin <= _iBank ) {
	                break;
	            } 
	        }
	        
	        
	        
	        if(iWinIndex == -1) {
	           int  chans_2 = -1;
	           for (int  i = 0; i < PAYOUTS.get(_iTotalNum-1).getPays().size(); i++) {
	        	    chans_2 = random(0, 100);
		        	int pay = PAYOUTS.get(_iTotalNum-1).getPays().get(i);
		            double iTotalWin = pay * _iCurBet;
		          //  Log.d("|============ CHANCE 2 =============>"+ iTotalWin+" = "+pay+"*"+_iCurBet+"\n" );
		            if (chans_2 < go &&  iTotalWin <= _iBank * 1.5) {
		                iWinIndex = i;
		                break;
		            } 
		        }
	        }
	        
	       // Log.d("|==========================> BANK_"+ _iBank +"G | INDEX-> "+iWinIndex+"\n");

	        int  iRandWin = random(0, 100);
	        //  Log.d("|==========================> BANK_"+iRandWin);
	        if (iRandWin < WIN_OCCURRENCE.get(_iTotalNum-1) && iWinIndex>-1) {
	             return new WData(true, iWinIndex);
	           } else {
	             return new WData(false, -1);
	        }
	}
	
	  public static int random(int min, int max) {
		  Random rand = new Random(); 
		 return  rand.nextInt((max - min) + 1) + min;
	  }
	  
	  
	  public static List<Integer>  win(List<KenoPayouts> PAYOUTS, int _iTotalNum, int iMaxWinIndex ) {
		  
		  // 
		  List<Integer> aWinOccurrenceList = new ArrayList<Integer>();
		  
		   for (int  i = PAYOUTS.get(_iTotalNum-1).getPays().size() - 1; i >= iMaxWinIndex; i--) {
			   
	            for (int j = 0; j < PAYOUTS.get(_iTotalNum - 1).getOccurrence().get(i); j++) {
	            	
	                aWinOccurrenceList.add(PAYOUTS.get(_iTotalNum - 1).getHits().get(i));
	                
	            }
	            
	        }
		  
	      return aWinOccurrenceList ;
	  }
	  
	  public static int  getRandomOcc(List<Integer> aWinOccurrenceList ) {
		  return (int) Math.floor(Math.random() * aWinOccurrenceList.size()); 
	  }
	  
	  public static  List<Integer>  getAWinTempList(List<Integer> _aListSelected ) {
	        //Copy win numbers
	        List<Integer> aWinTempList = new ArrayList<Integer>();
	        for (int i = 0; i < _aListSelected.size(); i++) {
	            aWinTempList.add(i, _aListSelected.get(i) + 1);
	        }	
	        
	        return aWinTempList; 
	  }
	  
	  public static  List<Integer>  getALoseTempList  (List<Boolean> _aNumSelected ) {
		  List<Integer> aLoseTempList = new ArrayList<Integer>();
	        for (int i = 0; i < _aNumSelected.size(); i++) {
	            if (!_aNumSelected.get(i)) {
	                aLoseTempList.add(i+1);
	            }
	        }
	       return  aLoseTempList;
	  }
	  
	  
	  public static  List<Integer>  getCombination(List<Integer> aWinOccurrenceList, int iRandWinIndex, List<Integer> aWinTempList, List<Integer> aLoseTempList ) {
		  List<Integer> aCombination = new ArrayList<Integer>();
		  for (int i = 0; i < 20; i++) {
	            if (i < aWinOccurrenceList.get(iRandWinIndex)) {
	                aCombination.add(aWinTempList.get(i));
	            } else {
	                aCombination.add(aLoseTempList.get(i));
	            }
	        }
		  
		  return aCombination;
	  }

	public static double getWinSold(int ihitsnumber, List<KenoPayouts> PAYOUTS, double _iCurBet, int _iTotalNum) {
		   //Update Money
        for (int  i = 0; i < PAYOUTS.get(_iTotalNum - 1).hits.size(); i++) {
            if (PAYOUTS.get(_iTotalNum - 1).hits.get(i) == ihitsnumber) {
                double iTotalWin = (_iCurBet * PAYOUTS.get(_iTotalNum - 1).pays.get(i));
                return   iTotalWin;
            }
        }
        return 0;
	}

	
}
