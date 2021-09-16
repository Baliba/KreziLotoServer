package dto;

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
public class Ticket {

 
  private Long   id;
  private Long   code_agent;
  private Date   date_ticket;
  private String sdatet;
  private String sheure;
  private  double total_price;
  private Long  id_game;
  private Long  id_gamemaster;
  private String  game_name;
  private String  place;
  private Long    id_pg;
  private String  client;
  private double  max_win;
  private double  win_pay;
  private Date    date_pay;
  private Long    who_pay;
  private boolean pay;
  public  boolean over;
  public Ticket() {}
  
}
