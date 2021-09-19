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
import com.monkata.lps.Game.BouleClient;
import com.monkata.lps.Request.TicketRequest;

import lombok.Data;

@Data
public class NumberTracking {
 String mg;	
 String boule;
 Long play_time;
 double win ;
 double lost; 
 public NumberTracking() {
 }
 public NumberTracking(String mg , String bc, Long pt, double win, double win_price) {
	 this.mg = mg;
	 this.boule = bc;
	 this.play_time = pt;
	 this.win = win;
	 this.lost = win_price;
 }
 

}
