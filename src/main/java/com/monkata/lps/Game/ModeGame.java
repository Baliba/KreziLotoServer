package com.monkata.lps.Game;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.monkata.lps.entity.cObj;

import lombok.Data;

@Data
@Entity
public class ModeGame extends cObj {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
	    Long id;
        
        String no;
        
	    String name;
        
	    String code;
	    
	    double max_sell;
	    double min_sell;
	    String mask;
	    int max_num;
	    int pas;
	    boolean seg;
	    private int index;
	    private String win;
	    private int part;
	    @Column(nullable=true)
	    private double point_per_price;
	    
	    @Column(nullable=true)
	    private double online_limit;
	    
	    @Column(nullable=true)
	    private double sales_commission;
	    @Column(nullable=true)
	    private double savings;
	    
	    @ManyToOne
		@JsonIgnoreProperties({"modegames"})	
		private Game game;
	    
	    public ModeGame(String id, String name, String code, double max_sell, double min_sell, String mask, int max_num, String win, int part,int i ) {
	        this.no = id;
	        this.name = name;
	        this.code = code;
	        this.max_sell = max_sell;
	        this.min_sell = min_sell;
	        this.mask = mask;
	        this.max_num = max_num;
	        seg = false;
	        this.win = win;
	        this.part = part;
	        this.index = i;
	    }

	    public ModeGame(String id, String name, String code, double max_sell, double min_sell, String mask, int max_num, int pas, boolean isSeg, String win, int part, int i) {
	        this.no = id;
	        this.name = name;
	        this.code = code;
	        this.max_sell = max_sell;
	        this.min_sell = min_sell;
	        this.mask = mask;
	        this.max_num = max_num;
	        this.pas = pas;
	        this.seg = isSeg;
	        this.win = win;
	        this.part = part;
	        this.index = i;
	    }

		public ModeGame() {
	
		}
		

}
