package com.monkata.lps.dao;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.monkata.lps.Game.Game;
import com.monkata.lps.Game.ParamsGame;
import com.monkata.lps.entity.Bank;
import com.monkata.lps.entity.Role;

import dto.NParamsGame;
import lombok.Data;



@CrossOrigin("*")
@RepositoryRestResource
public interface ParamsGameRepository extends JpaRepository<ParamsGame, Long> {
	
	Role findByName( String name);
    @Query("Select new dto.NParamsGame(r.id,r.name) from ParamsGame AS r")
    List<NParamsGame> getPG();
    
    @Query("Select new dto.NParamsGame(r.id,r.name) from ParamsGame AS r WHERE r.is_for_client = 1 ")
    List<NParamsGame> getPGForClient();
    
}
