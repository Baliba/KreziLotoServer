package com.monkata.lps.Game;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.swing.text.DateFormatter;

public class DateTF {
	public String f ;
	LocalDateTime localDateTime;
	Date date;
	public DateTF(String f) {
		this.f = f;
		
	}
    public  LocalDateTime  getDateTimeNow(){
      localDateTime = LocalDateTime.now();
   	  DateTimeFormatter formatter = DateTimeFormatter.ofPattern(f);
   	  String text = localDateTime.format(formatter);
   	  return LocalDateTime.parse(text, formatter);	
    }
    public String now() {
		Date date = new Date();
		String strDateFormat = "hh:mm:ss";
		DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
		String formattedDate = dateFormat.format(date);
		return formattedDate.trim();
	}
}
