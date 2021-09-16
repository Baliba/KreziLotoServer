package com.monkata.lps.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.monkata.lps.entity.Bank;
import com.monkata.lps.entity.Commission;
import com.monkata.lps.entity.PVBank;
import com.monkata.lps.entity.Payout;



@CrossOrigin("*")
@RepositoryRestResource
public interface PayoutRepository extends JpaRepository<Payout, Long> {
	
	  @Query("SELECT p FROM Payout p WHERE id_user= :id AND (pay IS NULL OR pay = 0) ")
	  List<Payout> getCurrentPayout(@Param("id") Long id_user );
	  
	  
	  @Query("SELECT p FROM Payout p WHERE id_user= :id AND pay = 1 AND  date_created > CURRENT_DATE - :d ")
	  List<Payout> getLastPayout(@Param("id") Long id_user, @Param("d") int d);
	  
	  

	  @Query("SELECT p FROM Payout p WHERE (pay IS NULL OR pay = 0) AND type_pay = 0 ")
	  List<Payout> CurrentPayOutMC();
	  @Query("SELECT p FROM Payout p WHERE (pay IS NULL OR pay = 0) AND (type_pay = 1 OR type_pay = 2) ")
	  List<Payout> CurrentPayOutBK();
	  @Query("SELECT p FROM Payout p WHERE pay = 1 AND type_pay = 0 ")
	  List<Payout> payOutMC();
	  
	  @Query("SELECT p FROM Payout p WHERE pay = 1  AND (type_pay = 1 OR type_pay = 2) ")
	  List<Payout> payOutBK();

	  @Query("SELECT p FROM Payout p WHERE (pay IS NULL OR pay = 0) AND type_pay = 3 ")
	  List<Payout> CurrentPayOutCK();

	  @Query("SELECT p FROM Payout p WHERE pay = 1 AND type_pay = 3 ")
	  List<Payout> payOutCK();



}
