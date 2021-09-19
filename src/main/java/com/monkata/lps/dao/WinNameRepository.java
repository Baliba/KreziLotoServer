package com.monkata.lps.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.monkata.lps.Game.WinName;
import com.monkata.lps.entity.Bank;



@CrossOrigin("*")
@RepositoryRestResource
public interface WinNameRepository extends JpaRepository<WinName, String> {
    
}
