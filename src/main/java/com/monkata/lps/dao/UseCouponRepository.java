package com.monkata.lps.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.monkata.lps.Game.ModeGameMaster;
import com.monkata.lps.Tiraj.Tiraj;
import com.monkata.lps.entity.Bank;
import com.monkata.lps.entity.Coupon;
import com.monkata.lps.entity.UseCoupon;



@CrossOrigin("*")
@RepositoryRestResource
public interface UseCouponRepository extends JpaRepository<UseCoupon, Long> {
	
	//@Query("SELECT uc FROM UseCoupon uc WHERE date_order>= to_date(':debut 00:00:00.000','YYYY-MM-DD HH:mn:ss') AND date_order<= to_date(':fin 23:59:59', 'YYYY-MM-DD HH:mn:ss') ")
	
	
	@Query("SELECT uc FROM UseCoupon uc WHERE  date_order BETWEEN :debut  AND :fin ")
	List<UseCoupon> getUseCoupon(@Param("debut") LocalDateTime debut, @Param("fin") LocalDateTime fin);
	
	@Query("SELECT uc FROM UseCoupon uc WHERE  date_order BETWEEN :debut  AND :fin AND type_game=:mg ")
	List<UseCoupon> getUseCoupon( @Param("debut") LocalDateTime deb, @Param("fin") LocalDateTime fn, @Param("mg") int mg);

	@Query("SELECT uc FROM UseCoupon uc WHERE id_agent=:id  AND is_agent_pay=:ip ")
	List<UseCoupon> getCurrentUC(@Param("id")  Long id, @Param("ip")  int is_pay);
	
	@Query("SELECT uc FROM UseCoupon uc WHERE id_agent=:id  AND is_agent_pay=:ip AND  date_order BETWEEN :debut  AND :fin  ")
	List<UseCoupon> getPastUC(@Param("id") Long id, @Param("ip")  int is_pay, @Param("debut") LocalDateTime deb, @Param("fin") LocalDateTime fin);
	
}
