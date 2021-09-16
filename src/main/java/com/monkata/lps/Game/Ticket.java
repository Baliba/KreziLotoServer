package com.monkata.lps.Game;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.monkata.lps.Request.TicketRequest;

import lombok.Data;

@Data
@Entity
public class Ticket {

 
private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)	
  private Long   id;
  private Long   code_agent;
  private LocalDateTime    date_ticket;
  private String sdatet;
  private String sheure;
  private  double total_price;
  private Long  id_game;
  private Long  id_gamemaster;
  private String  game_name;
  private String  place;
  private Long id_pg;
  private String client;
  private double max_win;
  private LocalDateTime date_exp;
  //  boule after win 
  @Column(nullable=true)
  private double win_pay;
  
  @Column(nullable=true)
  private Date   date_pay;
  
  @Column(nullable=true)
  private Long   who_pay;
  
  @Column(nullable=true)
  private boolean pay, over;
  
  @Column(nullable=true)
  private  String  hour_draw;
  
  // ++++++++++++
  @OneToMany(mappedBy = "ticket")
  @JsonIgnoreProperties({"ticket"})
  private List<Boule> lots;
  
  
  
  public Ticket() {}
  public Ticket(TicketRequest tk, Long id, Game g,String f,int delai) {
	    code_agent = id;
	    place = tk.getPlace();
        id_gamemaster = g.getGamemaster().getId();
        id_game =  g.getId();
	    game_name= g.getGamemaster().getCode();
	    this.hour_draw = g.getGamemaster().getHour_draw();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	    String text = LocalDateTime.now().format(formatter);
	    date_ticket = LocalDateTime.now();
	    pay=false;
	    over = false;
	    setLDate(f);
	    this.date_exp   = LocalDateTime.parse(text,formatter).plusDays(delai);
  }
  
  public void  setLDate(String f){
	  LocalDateTime  localDateTime = LocalDateTime.now();
	  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	  String   sd = localDateTime.format(formatter);
	  sheure   =    sd.split(" ")[1];
	  sdatet =      sd.split(" ")[0];
  }
  
  public void setTotalPrice() {
      total_price = 0;
      for(Boule l : lots){
           total_price+= l.getMontant();
      }
  }

public void setMGain() {
	for (Boule b : lots) {
		max_win += b.getPwin();
	}
}
 public void  setClient(String c){
	 LocalDateTime  localDateTime = LocalDateTime.now();
	 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	 String   sd =     localDateTime.format(formatter);
	 client = c+"-"+sd;
}
 
}
