package com.monkata.lps.dao;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.monkata.lps.Game.Game;
import com.monkata.lps.Game.GameMaster;
import com.monkata.lps.entity.Bank;
import com.monkata.lps.entity.UserEntity;



@CrossOrigin("*")
@RepositoryRestResource
public interface GameMasterRepository extends JpaRepository<GameMaster, Long> {	   
	@Query("SELECT gm FROM GameMaster gm ORDER BY index ASC")
	List<GameMaster> getGames() ;
	
	@Query("SELECT gm FROM GameMaster gm WHERE enabled = true ORDER BY index ASC")
	List<GameMaster> getActiveGames() ;
}
