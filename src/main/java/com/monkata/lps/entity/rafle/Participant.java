package com.monkata.lps.entity.rafle;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Participant  implements Serializable  {
	
	    private static final long serialVersionUID = 1L;
	    @Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private Long id;
	    
	    @Column(nullable=true)
	    LocalDateTime date_inscrit;
	    
	    @Column(nullable=true)
	    Long id_use_part;
	    
	    @Column(nullable=true)
	    Long id_rafle;

}
