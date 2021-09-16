package com.monkata.lps.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.monkata.lps.Game.ModeGameMaster;
import com.monkata.lps.entity.Bank;
import com.monkata.lps.entity.Coupon;



@CrossOrigin("*")
@RepositoryRestResource
public interface CouponRepository extends JpaRepository<Coupon, Long> {
	
	   Coupon findByCode(String code);
}
