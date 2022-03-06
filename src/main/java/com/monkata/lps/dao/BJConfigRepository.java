package com.monkata.lps.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.monkata.lps.entity.Bank;
import com.monkata.lps.entity.blackjack.BjConfig;



@CrossOrigin("*")
@Repository
public interface BJConfigRepository extends JpaRepository<BjConfig, Long> {
    
}
