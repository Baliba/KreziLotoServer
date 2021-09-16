package com.monkata.lps.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.monkata.lps.entity.Commission;



@CrossOrigin("*")
@RepositoryRestResource
public interface ComRepository extends JpaRepository<Commission, Long> {
	
	  @Query("SELECT c FROM Commission c WHERE type_pay =:type_pay")
	  List<Commission> getListCommissionByType(@Param("type_pay") int type_pay );
    
}
