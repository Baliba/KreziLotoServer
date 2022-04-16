package com.monkata.lps.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.monkata.lps.Game.ModeGameMaster;
import com.monkata.lps.entity.Bank;
import com.monkata.lps.entity.Coupon;
import com.monkata.lps.entity.UseCoupon;



@CrossOrigin("*")
@RepositoryRestResource
public interface CouponRepository extends JpaRepository<Coupon, Long> {
	
	   @Query("SELECT c from Coupon c WHERE code=:code")
	   Coupon findCouponByCode(String code);
	   
	   Optional<Coupon> findByCode(String code);
}
