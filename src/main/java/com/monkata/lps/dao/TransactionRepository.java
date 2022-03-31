package com.monkata.lps.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.monkata.lps.entity.Bank;
import com.monkata.lps.entity.TransactionUser;



@CrossOrigin("*")
@Repository
public interface TransactionRepository extends JpaRepository<TransactionUser, Long> {
	

	
	@Query("SELECT tu FROM TransactionUser tu WHERE date_trans BETWEEN :debut AND :fin  ")
	List<TransactionUser> getTransactionByDate(LocalDateTime debut, LocalDateTime fin);

	@Query("SELECT tu FROM TransactionUser tu WHERE date_trans BETWEEN :debut AND :fin AND type_transaction=:mg ")
	List<TransactionUser> getTransactionByDateAndMg(LocalDateTime debut, LocalDateTime fin, int mg);

	@Query("SELECT tu FROM TransactionUser tu WHERE date_trans BETWEEN :debut AND :fin AND id_user =:id ")
	List<TransactionUser> getTransactionByDateAndUser(LocalDateTime debut, LocalDateTime fin, Long id);
    
	@Query("SELECT tu FROM TransactionUser tu WHERE date_trans BETWEEN :debut AND :fin AND id_user =:id AND type_transaction=:mg ")
	List<TransactionUser> getTransactionByDateAndUserAndMg(LocalDateTime debut, LocalDateTime fin, Long id, int mg);
	
	
}
