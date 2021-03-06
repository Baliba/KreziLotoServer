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
import com.monkata.lps.Helper.Log;
import com.monkata.lps.Request.TicketRequest;

import lombok.Data;

@Data
public class Sold {
private double  sold;
private long var;


public Sold(double sold ) {
	
	if(sold>0) {
	   this.sold = sold;
	} else {
		this.sold = 0;
	}
}

public Sold(double sold, String w ) {
	Log.d("<<<>>>>"+w+" ---> "+sold);
	if(sold>0) {
	   this.sold = sold;
	} else {
		this.sold = 0;
	}
}

public Sold(double sold, long var ) {
	if(sold>0) {
	   this.sold = sold;
	} else {
		this.sold = 0;
	}
	var = var;
}

 public Sold() {
	
 } 
  
 
}
