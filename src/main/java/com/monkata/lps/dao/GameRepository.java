package com.monkata.lps.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.monkata.lps.Game.Game;



@CrossOrigin("*")
@RepositoryRestResource
public interface GameRepository extends JpaRepository<Game, Long> {
	
}
