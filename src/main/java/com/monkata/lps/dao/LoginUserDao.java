package com.monkata.lps.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.monkata.lps.Game.GameMaster;
import com.monkata.lps.Game.ModeGame;
import com.monkata.lps.entity.LoginUser;
import com.monkata.lps.entity.Notification;

@CrossOrigin("*")
@RepositoryRestResource
public interface LoginUserDao extends JpaRepository<LoginUser, Long>{

}
