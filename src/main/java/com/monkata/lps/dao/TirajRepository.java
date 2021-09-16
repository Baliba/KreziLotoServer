package com.monkata.lps.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.monkata.lps.Tiraj.Tiraj;


@CrossOrigin("*")
@RepositoryRestResource
public interface TirajRepository extends JpaRepository<Tiraj, Long> {
	
  @Query("SELECT T FROM Tiraj T WHERE  id_game =:idg AND date_tiraj=:dateg")	
  public Tiraj  isGameDrawToday(@Param("dateg") String date, @Param("idg") Long id_game);

  @Query("SELECT T FROM Tiraj T WHERE date_tiraj =:dateg")	
  List<Tiraj>  getDrawsByDate(@Param("dateg") String date);
  
  @Query("SELECT tr FROM Tiraj tr WHERE to_date(date_tiraj,'DD-MM-YYYY') > CURRENT_DATE - :d  ")	
  List<Tiraj> getLastTiraj(@Param("d") int d);
 
}
