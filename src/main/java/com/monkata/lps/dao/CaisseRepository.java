package com.monkata.lps.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.monkata.lps.entity.Bank;
import com.monkata.lps.entity.Caisse;

import dto.Sold;



@CrossOrigin("*")
@RepositoryRestResource
public interface CaisseRepository extends JpaRepository<Caisse, Long> {
    
//	@Query("SELECT new dto.Sold(SUM(t.sold)) from Caisse t WHERE type_trans=0 GROUP BY t.id ")
//	Optional<Sold> getTotalDecaissement();
//	
//	@Query("SELECT new dto.Sold(SUM(t.sold)) from Caisse t WHERE type_trans=1 GROUP BY t.id ")
//	Optional<Sold> getTotalEncaissement();
}
