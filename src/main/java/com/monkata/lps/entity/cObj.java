/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.monkata.lps.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
        value = {"createdAt", "updatedAt"},
        allowGetters = true
)
public class cObj {
	
        @Temporal(TemporalType.TIMESTAMP)
        @Column(nullable = false, updatable = false)
        @CreationTimestamp    
	    private Date  created_at;
        
	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name = "updated_at", nullable = true)
	    @LastModifiedDate
	    private Date updated_at;
	    
	    private Long updated_by;
	    private Long created_by;
	    
	  
}
