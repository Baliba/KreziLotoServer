package com.monkata.lps.Game;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.monkata.lps.entity.cObj;

import lombok.Data;

@Data
@Entity
public class GameMaster  extends cObj  {
	    private static final long serialVersionUID = 1L;
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    @Column(unique=true)
	    private  String  no;
	    @Column(unique=true)
	    private  String  code;
	    private  String  hour_to_start_sell;
	    private  String  hour_to_block;
	    private  String  hour_draw;
	    //------------------------------------
	    @Column(columnDefinition = "int default 100000")
	    private int      max_ticket;
	    
	    @Column(columnDefinition = "int default 50000")
	    private double   max_price_sell;
	    // ----------------------
	    private int      index;
	    @Column(columnDefinition = "int default 90")
	    private int  delai;
		@OneToMany(mappedBy = "gamemaster") 
		@JsonIgnoreProperties({"gamemaster","paramsgame","modegames"})
		private List<Game> games = new ArrayList<>();
		
		@Column(name = "enabled", columnDefinition = "boolean default true")
		public  boolean enabled;
		
		public GameMaster() {
			super();
			this.enabled = true;
		}

		public GameMaster(Long id,String no, String code, String hour_to_start_sell, String hour_to_block, String hour_draw, int index) {
			super();
			this.id = id;
			this.no = no;
			this.code = code;
			this.hour_to_start_sell = hour_to_start_sell;
			this.hour_to_block = hour_to_block;
			this.hour_draw = hour_draw;
			this.index = index;
			this.enabled = true;
		}
		
		
	   
	  
}
