package com.monkata.lps.entity.rafle;

import java.io.Serializable;

import lombok.Data;

@Data
public class Winner implements Serializable {
    Long id_winner;
    int place;
    
   public Winner(){}
    
}
