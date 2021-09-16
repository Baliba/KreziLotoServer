package com.monkata.lps.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.monkata.lps.Game.GameMaster;
import com.monkata.lps.Game.ModeGame;
import com.monkata.lps.entity.Notification;

@CrossOrigin("*")
@RepositoryRestResource
public interface NotDao extends JpaRepository<Notification, Long>{
	
	
	@Query("SELECT COUNT(n) from Notification n WHERE n.id_receiver =:id AND n.vu=0  ")
    Long  countNot(@Param("id") Long id);
	
	@Query("SELECT n from Notification n WHERE (date_not > CURRENT_DATE - 7 OR date_not IS NULL) AND  n.id_receiver =:id   ORDER BY n.date_not DESC")
    List<Notification> getNotByUser(@Param("id") Long id);
	
	
	@Modifying
	@Query(" UPDATE Notification n SET n.vu = 1 WHERE n.id=:id ")
	@Transactional
    void seeAll(@Param("id") Long id);
}
