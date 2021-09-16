package dto;

import java.util.HashMap;

import com.monkata.lps.entity.Bank;

import lombok.Data;
@Data
public class BankAndLang {
	HashMap<String,String>  lg;
	Bank bank;
	public BankAndLang() {
		super();
	}
	
}
