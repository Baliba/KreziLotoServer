package com.monkata.lps.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.monkata.lps.entity.Bank;
import com.monkata.lps.entity.Keno;
import com.monkata.lps.entity.KenoConfig;



@CrossOrigin("*")
@RepositoryRestResource
public interface KenoConfigRepository extends JpaRepository<KenoConfig, Long> {
    
}
