package com.monkata.lps.dao;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.monkata.lps.Game.Game;
import com.monkata.lps.Game.Ticket;
import com.monkata.lps.entity.Bank;
import com.monkata.lps.entity.UserEntity;



@CrossOrigin("*")
@RepositoryRestResource
public interface TicketRepository extends JpaRepository<Ticket, Long> {
	
    @Query("Select T from Ticket  AS T WHERE id_game = :id AND sdatet=:sdate ")
    Page<Ticket> getTicketByPage(Pageable paging,@Param("id") Long id, @Param("sdate") String d);
	
}
