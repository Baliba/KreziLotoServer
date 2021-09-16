package com.monkata.lps.dao;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.monkata.lps.Game.Boule;
import com.monkata.lps.Game.BouleClient;
import com.monkata.lps.Game.Game;
import com.monkata.lps.Game.Ticket;
import com.monkata.lps.entity.Bank;
import com.monkata.lps.entity.Depot;
import com.monkata.lps.entity.UserEntity;



@CrossOrigin("*")
@RepositoryRestResource
public interface BouleClientRepository extends JpaRepository<BouleClient, Long> {
	
	  @Query("SELECT SUM(bc.montant) FROM BouleClient bc WHERE  bc.ticketclient.sdatet =:date  AND bc.id_mg =:id_mg  AND bc.id_game=:id_game ")	
	  Optional<Double> getTotalLotsSell(@Param("date") String date,@Param("id_mg") Long id_mg, @Param("id_game") Long id_game);

  
}
