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
public class ModeGameMaster extends cObj {

        
        @Id
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
	    @Column(name = "enabled", columnDefinition = "boolean default true")
	    private boolean enabled;
	    
	    
	    public ModeGameMaster(ModeGame mg) {
	        this.no =   mg.getNo();
	        this.name = mg.getName();
	        this.code = mg.getCode();
	        this.max_sell = mg.getMax_sell();
	        this.min_sell = mg.getMin_sell();
	        this.mask = mg.getMask();
	        this.max_num = mg.getMax_num();
	        seg = mg.isSeg();
	        this.win = mg.getWin();
	        this.part = mg.getPart();
	        this.index = mg.getIndex();
	        enabled = true;
	    }
	    
	    public ModeGameMaster(String id, String name, String code, double max_sell, double min_sell, String mask, int max_num, String win, int part,int i ) {
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
	        enabled = true;
	    }

	    public ModeGameMaster(String id, String name, String code, double max_sell, double min_sell, String mask, int max_num, int pas, boolean isSeg, String win, int part, int i) {
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
	        enabled = true;
	    }

		public ModeGameMaster() {
	
		}
		

}
