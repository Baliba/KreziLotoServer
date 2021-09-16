package com.monkata.lps.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.monkata.lps.Game.ModeGame;
import com.monkata.lps.Game.ModeGameMaster;
import com.monkata.lps.entity.Bank;
import com.monkata.lps.entity.UserEntity;



@CrossOrigin("*")
@RepositoryRestResource
public interface ModeGameMasterRepository extends JpaRepository<ModeGameMaster, String> {
	
	    ModeGameMaster findByCode(String code);
}
