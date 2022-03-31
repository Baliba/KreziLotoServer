package com.monkata.lps.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class TransactionUser  extends cObj {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@Column(nullable=true, columnDefinition = "int default 0")
    private Long id_action;
    
    private Long id_user;
    
    private int type_transaction;
    
    private String transaction;
    
    @Column(nullable=true, columnDefinition = "int default 0")
    private double credit;
    
    @Column(nullable=true, columnDefinition = "int default 0")
    private double debit;
    
    public double balance;
    
    public LocalDateTime date_trans;
    public TransactionUser() {}
   
}
