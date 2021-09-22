package com.monkata.lps.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import com.monkata.lps.entity.Depot;
import com.monkata.lps.response.JwtResponse;

import dto.Sold;



@CrossOrigin("*")
@RepositoryRestResource
public interface DepoDao extends JpaRepository<Depot, Long> {
	
  @Query("SELECT d FROM Depot d WHERE token_order =:tko ORDER BY created_at DESC")	
  Optional<Depot> findDepoByTKO(@Param("tko") String tko);
  
  @Query("SELECT d FROM Depot d WHERE id_user =:id AND  created_at > CURRENT_DATE - :day ORDER BY created_at DESC ")
  List<Depot> getPastDepot(@Param("id") Long id, @Param("day") int d);
  
  @Query("SELECT d FROM Depot d WHERE type_depot=:index AND created_at > CURRENT_DATE - :day ORDER BY created_at DESC ")
  List<Depot> getPastDepotByAdmin( @Param("day") int d, int index);
  
  
  @Query("SELECT  new dto.Sold(SUM(d.montant)) from Depot d  ")
  Optional<Sold> getTotalDepo();
	
  @Query("SELECT new dto.Sold(SUM(d.montant)) from Depot d WHERE type_depot=:index ")
  Optional<Sold> getTotalDepoOther(int index);
  
}
