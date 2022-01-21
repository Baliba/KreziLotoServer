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
public class SearchTicketRes {
	Long id_game;
	String date_debut;
	String date_fin;
	int payment;
	int state;
	int verify;
	public SearchTicketRes() {}
}
