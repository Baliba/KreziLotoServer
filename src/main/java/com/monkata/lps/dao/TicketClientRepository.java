package com.monkata.lps.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;


import com.monkata.lps.Game.TicketClient;

import Specification.TicketClientSpecification;
import dto.NumberTracking;
import dto.Sold;
import dto.TcDto;



@CrossOrigin("*")
@RepositoryRestResource
public interface TicketClientRepository extends JpaRepository<TicketClient, Long>, JpaSpecificationExecutor<TicketClient>  {
	@Query("SELECT t from TicketClient t WHERE t.id_user = :id AND t.over=false ORDER BY date_ticket DESC")
	List<TicketClient> getMytickets(@Param("id") Long id );
	
	@Query("SELECT t from TicketClient t WHERE t.id_user = :idu  AND t.id=:id")
	TicketClient getTicketById(@Param("idu") Long idu,@Param("id")  Long id);
	
	@Query("SELECT t from TicketClient t WHERE t.id_user = :id AND t.over=true AND date_ticket > CURRENT_DATE - :d ")
	List<TicketClient> getMyPastTickets(@Param("id") Long id, @Param("d") int d);
	
	@Query("SELECT new dto.Sold(SUM(t.win_pay)) from TicketClient t WHERE t.id_user = :idu AND t.over = true ")
	Optional<dto.Sold> maxWinClient(@Param("idu") Long idu);
	
	@Query("SELECT new dto.Sold(SUM(t.total_price)) from TicketClient t WHERE t.id_user = :idu AND t.over = true ")
	Optional<dto.Sold> maxLostClient(@Param("idu") Long idu);

	@Query("SELECT new dto.Sold(SUM(t.total_price)) from TicketClient t WHERE  date_ticket > CURRENT_DATE - 1 AND id_gamemaster = :id   GROUP BY id_gamemaster  ")
	Optional<dto.Sold> getTotalSoldTicketToDay(@Param("id") Long id);
	
	@Query("SELECT new dto.Sold(SUM(t.total_price)) from TicketClient t WHERE  id_gamemaster = :id  GROUP BY id_gamemaster  ")
	Optional<dto.Sold> getTotalSoldTicketGlobal(@Param("id") Long id);
	
	@Query("SELECT new dto.Sold(SUM(t.win_pay)) from TicketClient t WHERE   date_ticket > CURRENT_DATE - 1 AND id_gamemaster = :id   GROUP BY id_gamemaster  ")
	Optional<Sold> getTotalLostSoldTicketToDay(@Param("id")  Long id);
	
	@Query("SELECT new dto.Sold(SUM(t.win_pay)) from TicketClient t WHERE  id_gamemaster = :id  GROUP BY id_gamemaster  ")
	Optional<Sold> getTotalLostSoldTicketGlobal(@Param("id")  Long id);
	
    // get ticket by date
	@Query("SELECT t from TicketClient t WHERE date_ticket > CURRENT_DATE - 1 AND id_gamemaster = :id ")
	List<TicketClient> getTicketOfTodayByGame(@Param("id")  Long id);
	
	@Query("SELECT t from TicketClient t WHERE date_ticket > CURRENT_DATE - :day  AND id_gamemaster = :id ")
	List<TicketClient> getTicketByGame(@Param("id") Long id, @Param("day") int day);
	// Over
	@Query("SELECT t from TicketClient t WHERE  date_ticket > CURRENT_DATE - 1  AND id_gamemaster = :id  AND over = true AND  win_pay>0 ")
	List<TicketClient> getTicketOfTodayByGameWin(@Param("id") Long game);
	
	@Query("SELECT t from TicketClient t WHERE date_ticket > CURRENT_DATE - 1  AND id_gamemaster = :id AND over = true AND win_pay=0  ")
	List<TicketClient> getTicketOfTodayByGameLost(@Param("id") Long game);
	
	@Query("SELECT t from TicketClient t WHERE date_ticket > CURRENT_DATE - :day  AND id_gamemaster = :id AND over = true AND win_pay>0 ")
	List<TicketClient> getTicketByGameWin(Long id, int day);
	
	@Query("SELECT t from TicketClient t WHERE date_ticket > CURRENT_DATE - :day  AND id_gamemaster = :id AND over = true AND win_pay=0 ")
	List<TicketClient> getTicketByGameLost(Long id, int day);
	
	@Query("SELECT new dto.NumberTracking(bc.game_name,bc.game_name,COUNT(bc.id_game), SUM(bc.total_price), SUM(bc.win_pay)) FROM TicketClient  bc WHERE bc.over=:live AND bc.date_ticket > CURRENT_DATE - :day GROUP BY bc.id_game, bc.game_name ORDER BY bc.game_name ASC ")	
	List<NumberTracking> getGameTrackingByDayOnly(@Param("day") int day,@Param("live")  boolean live);
	
	@Query("SELECT t from TicketClient t WHERE t.id_user = :id   AND t.over=false  AND date_ticket > CURRENT_DATE - :d ")
	List<TicketClient> getMyCurrentTickets(Long id, int d);
	
	@Query("SELECT new dto.Sold(SUM(t.total_price)) from TicketClient t WHERE  date_ticket BETWEEN :d AND :f   AND id_gamemaster = :id   GROUP BY id_gamemaster  ")
	Optional<Sold> getTotalSoldTicketToDay(Long id,@Param("d") LocalDateTime d, @Param("f") LocalDateTime f);
	
	@Query("SELECT COUNT(*) FROM TicketClient t WHERE  date_ticket BETWEEN :d AND :f   AND id_gamemaster = :id  ")
	long countSoldTicketToDay(Long id,@Param("d") LocalDateTime d, @Param("f") LocalDateTime f);
	
	@Query("SELECT new dto.Sold(SUM(t.win_pay)) from TicketClient t WHERE date_ticket BETWEEN :d AND :f  AND id_gamemaster = :id   GROUP BY id_gamemaster  ")
	Optional<Sold> getTotalLostSoldTicketToDay(@Param("id")  Long id, @Param("d") LocalDateTime d, @Param("f") LocalDateTime f);

	@Query("SELECT COUNT(*) from TicketClient t WHERE  id_gamemaster = :id ")
	long countTotalSoldTicketGlobal(Long id);

}
