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
import com.monkata.lps.entity.Notification;

@CrossOrigin("*")
@Repository
public interface BonusDao extends JpaRepository<Bonus, Long>{
    
	@Query("SELECT b FROM Bonus b WHERE id_user = :id AND  date_created BETWEEN :debut AND :fin  ")
	List<Bonus> getBonusForUser(LocalDateTime debut, LocalDateTime fin, Long id);
	
	@Query("SELECT b FROM Bonus b WHERE date_created BETWEEN :debut AND :fin  ")
	List<Bonus> getBonus(LocalDateTime debut, LocalDateTime fin);
	
	@Query("SELECT b FROM Bonus b WHERE see_by_admin=0")
	List<Bonus> getAllNonValider();
	
}
