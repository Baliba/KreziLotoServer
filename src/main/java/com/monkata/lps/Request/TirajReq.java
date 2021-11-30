package com.monkata.lps.Request;

import com.monkata.lps.Game.GameMaster;

import lombok.Data;

@Data
public class TirajReq {
 GameMaster  game;
 String dateg, timeg;
 String num3, win4;
 public int win_price;
}
