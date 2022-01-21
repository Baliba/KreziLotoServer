package Specification;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SearchCriteria {
	private String key;
    private String operation;
    private Object value;
    private Object value2;	
    
    private LocalDateTime debut;
    private LocalDateTime fin;	
    
       
  public SearchCriteria(String key, String o,LocalDateTime deb, LocalDateTime fn) {
	  
	  this.key = key;
      this.operation = o;
      this.debut = deb;
      this.fin = fn;
	}

public SearchCriteria(String key, String o, String deb, String fn) {
	 this.key = key;
     this.operation = o;
     this.value = deb;
     this.value2 = fn;
}

public SearchCriteria(String k, String o, Object obj) {
	 key = k;
	 operation = o;
	 value = obj;
}

		
}
