package com.monkata.lps.response;

import java.util.List;

import com.monkata.lps.Game.Game;
import com.monkata.lps.Game.ModeGame;
import com.monkata.lps.Game.ParamsGame;

import lombok.Data;

@Data
public class GameSetting {
	ParamsGame paramsgame;
	public GameSetting(ParamsGame paramsgame) {
		super();
		this.paramsgame = paramsgame;
	}

}
