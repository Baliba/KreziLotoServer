package com.monkata.lps.dao;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.monkata.lps.Game.GameMaster;
import com.monkata.lps.Game.ModeGame;
import com.monkata.lps.entity.Bonus;
import com.monkata.lps.entity.LogAccess;
import com.monkata.lps.entity.Notification;

@CrossOrigin("*")
@Repository
public interface LogAccessDao extends JpaRepository<LogAccess, Long>{
	@Query("SELECT uc FROM LogAccess uc WHERE id_user=:id  AND state= :state AND  date_emis BETWEEN :debut  AND :fin ")
	List<LogAccess> getFullLogAccess(Long id, boolean state, LocalDateTime debut, LocalDateTime fin);
	
	@Query("SELECT uc FROM LogAccess uc WHERE id_user=:id  AND  date_emis BETWEEN :debut  AND :fin  ")
	List<LogAccess> getUserLogAccess(Long id, LocalDateTime debut, LocalDateTime fin);
	
	@Query("SELECT uc FROM LogAccess uc WHERE state=:state AND  date_emis BETWEEN :debut  AND :fin  ")
	List<LogAccess> getStateLogAccess( boolean state, LocalDateTime debut, LocalDateTime fin);
	
	@Query("SELECT uc FROM LogAccess uc WHERE date_emis BETWEEN :debut AND :fin  ")
	List<LogAccess> getLogAccess(LocalDateTime debut, LocalDateTime fin);
	
	
	
}
