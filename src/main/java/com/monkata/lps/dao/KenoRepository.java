package com.monkata.lps.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.monkata.lps.entity.Keno;

import dto.Sold;

@CrossOrigin("*")
@RepositoryRestResource
public interface KenoRepository extends JpaRepository<Keno, Long> {
	
	@Query("SELECT new dto.Sold(SUM(k.bet)) from Keno k WHERE k.id_user = :idu AND  is_win=false AND k.over = true  GROUP BY k.id_user")
	Optional<dto.Sold> getMyGlobalLost(@Param("idu") Long idu);
	
	@Query("SELECT new dto.Sold(SUM(k.win_sold)) from Keno k WHERE k.id_user = :idu AND k.over = true AND  is_win=false  GROUP BY k.id_user")
	Optional<dto.Sold> getMyGlobalWin(@Param("idu") Long idu);
	
	@Query("SELECT new dto.Sold(SUM(k.win_sold)) from Keno k WHERE  k.over = true AND  is_win = true  GROUP BY k.over ")
	Optional<dto.Sold> getGlobalLost();
	
	@Query("SELECT new dto.Sold(SUM(k.bet)) from Keno k WHERE k.over = true  GROUP BY k.over")
	Optional<dto.Sold> getGlobalWin();
	
	@Query("SELECT k from Keno k WHERE  created_at > CURRENT_DATE - :day  ")
	List<Keno> getKenoGameByDay(int day);
	
	@Query("SELECT k from Keno k WHERE  created_at > CURRENT_DATE - :day AND k.id_user=:id ")
	List<Keno> getKenoGameByDay(int day, Long id);
}
