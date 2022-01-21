package Specification;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.monkata.lps.Game.TicketClient;

public class TicketClientSpecification implements Specification<TicketClient> {
	     private SearchCriteria criteria;
	
	     public TicketClientSpecification(SearchCriteria sc) {
			this.criteria = sc;
		}

		@Override
	    public Predicate toPredicate (Root<TicketClient> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
	 
	        if (criteria.getOperation().equalsIgnoreCase(">")) {
	            return builder.greaterThan(
	              root.<String> get(criteria.getKey()), criteria.getValue().toString());
	        } 
	        else if (criteria.getOperation().equalsIgnoreCase("<")) {
	            return builder.lessThan(
	              root.<String> get(criteria.getKey()), criteria.getValue().toString());
	        } 
	        else if (criteria.getOperation().equalsIgnoreCase(">=")) {
	            return builder.greaterThanOrEqualTo(
	              root.<String> get(criteria.getKey()), criteria.getValue().toString());
	        } 
	        else if (criteria.getOperation().equalsIgnoreCase("<=")) {
	            return builder.lessThanOrEqualTo(
	              root.<String> get(criteria.getKey()), criteria.getValue().toString());
	        } 
	        else if (criteria.getOperation().equalsIgnoreCase("!")) {
	        	     
	        	// return builder.between(root.<LocalDateTime>get(criteria.getKey()) ,getLDT(criteria.getValue()),getLDT(criteria.getValue2()));
	        	    LocalDateTime debut = getLDT(criteria.getValue());

	        		LocalDateTime fin =  getLDT(criteria.getValue2());
	        
					return builder.between(root.get(criteria.getKey()) , debut,fin);
				
	        } 
	        else if (criteria.getOperation().equalsIgnoreCase(":")) {
	            if (root.get(criteria.getKey()).getJavaType() == String.class) {
	                return builder.like(
	                  root.<String>get(criteria.getKey()), "%" + criteria.getValue() + "%");
	            } else {
	                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
	            }
	        }
	        else if (criteria.getOperation().equalsIgnoreCase("=")) {
	                return builder.equal(root.get(criteria.getKey()),criteria.getValue());
	        }
	        return null;
	    }
		
		
		public static String getLDT3(String str) {
			 return str;
//			 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//			 return  LocalDateTime.parse(str, formatter);
		 }
		
		public static LocalDateTime getLDT(Object str) {
			 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			 return  LocalDateTime.parse(str.toString(), formatter);
		 }
		public static Date getLDT2(String str) throws ParseException {
		  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  return formatter.parse(str);
		}
         
}
