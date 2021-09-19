package com.monkata.lps.dao;

import java.util.Date;
import java.util.List;
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

import dto.NumberTracking;



@CrossOrigin("*")
@RepositoryRestResource
public interface BouleClientRepository extends JpaRepository<BouleClient, Long> {
	
	  @Query("SELECT SUM(bc.montant) FROM BouleClient bc WHERE  bc.ticketclient.sdatet =:date  AND bc.id_mg =:id_mg  AND bc.id_game=:id_game AND bc.lot=:lot ")	
	  Optional<Double> getTotalLotsSell(@Param("lot") String lot, @Param("date") String date,@Param("id_mg") Long id_mg, @Param("id_game") Long id_game);

	  
//	  @Query("SELECT SUM(bc.montant) FROM BouleClient bc WHERE  bc.ticketclient.sdatet =:date  AND bc.id_mg =:id_mg  AND bc.id_game=:id_game AND bc.lot=:lot ")	
//	  Optional<Double> getTotalSellByLot( @Param("date") String date,@Param("id_mg") Long id_mg, @Param("id_game") Long id_game);

	  
	  @Query("SELECT new dto.NumberTracking(bc.code_mg,bc.lot, COUNT(bc.lot), SUM(bc.montant), SUM(bc.win_price)) FROM BouleClient  bc WHERE  bc.ticketclient.over=:live AND  bc.ticketclient.date_ticket > CURRENT_DATE - :day GROUP BY bc.lot, code_mg ORDER BY bc.lot ASC  ")	
	  List<NumberTracking> getNumberTrackingByDayOnly(@Param("day") int day,@Param("live")  boolean live);
	  
	  @Query("SELECT new dto.NumberTracking(bc.code_mg,bc.lot, COUNT(bc.lot), SUM(bc.montant), SUM(bc.win_price)) FROM BouleClient bc WHERE bc.ticketclient.over=:live AND bc.ticketclient.date_ticket > CURRENT_DATE - :day AND bc.ticketclient.id_gamemaster = :game GROUP BY bc.lot, code_mg ORDER BY bc.lot ASC ")	
	  List<NumberTracking> getNumberTrackingByGameAndDay(@Param("day")  int day,@Param("game")  Long game,@Param("live")  boolean live);

  
}
