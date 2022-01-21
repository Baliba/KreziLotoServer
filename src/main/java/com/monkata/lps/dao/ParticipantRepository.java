package com.monkata.lps.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.monkata.lps.entity.rafle.Participant;
import com.monkata.lps.entity.rafle.Rafle;



@CrossOrigin("*")
@RepositoryRestResource
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    
}
