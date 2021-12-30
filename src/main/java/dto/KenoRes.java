package dto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.monkata.lps.Helper.KenoBrain;
import com.monkata.lps.Helper.Log;
import com.monkata.lps.dao.KenoRepository;
import com.monkata.lps.entity.Keno;
import com.monkata.lps.entity.KenoPayouts;
import com.monkata.lps.entity.UserEntity;

import lombok.Data;

@Data
public class KenoRes {
Keno keno;
double balance;
@JsonIgnore
double mgWin=0, mgLost=0, gWin=0, gLost= 0;

public  double bank_sold;

public int iwinindex;

public boolean bWin;

int irandwinindex;

List<Integer> awintemplist;

List<Integer> alosetemplist;

List <Integer>  awinoccurrencelist;

List <Integer> acombination;

double win_sold;

int ihitsnumber;

BankSold bs;

WData game;

public KenoRes() {
	this.bWin = false;
}

public KenoRes(Keno k, double compte) {
	this.bWin = false;
}

public KenoRes(Keno k, UserEntity u) {
	keno = k;
	keno.setOver(false);
	balance = u.getCompte();
}

public double setIWinIndexNow(WData index, List<KenoPayouts> PAYOUTS, int _iTotalNum, List<Integer> _aListSelected, List<Boolean> _aNumSelected) {
	this.game = index;
	if(index.isWin()) {
		iwinindex = index.getIndex();
		Log.d("|=========== iwinindex ==============> "+ iwinindex );
		this.bWin = true;
		awinoccurrencelist = KenoBrain.win(PAYOUTS, _iTotalNum, iwinindex);
		Log.d("|=========== awinoccurrencelist size ==============> "+ awinoccurrencelist.size() );
		irandwinindex = KenoBrain.getRandomOcc(awinoccurrencelist);
	    awintemplist = KenoBrain.getAWinTempList(_aListSelected);
	    Collections.shuffle(awintemplist);
	    alosetemplist = KenoBrain.getALoseTempList(_aNumSelected);
	    Collections.shuffle(alosetemplist);
	    Log.d("|=========== RANDOM ==============> "+irandwinindex );
	    ihitsnumber =  awinoccurrencelist.get(irandwinindex);
	    acombination = KenoBrain.getCombination(awinoccurrencelist, irandwinindex, awintemplist, alosetemplist);
	    Collections.shuffle(acombination);
	    win_sold = KenoBrain.getWinSold(ihitsnumber,PAYOUTS, keno.getBet(),_iTotalNum );
	 } else {
		win_sold = 0;
	 }
	return win_sold ;
}

public void  retartKeno(){
	this.keno.setOver(false);
}



}
