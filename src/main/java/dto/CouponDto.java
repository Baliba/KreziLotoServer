package dto;

import com.monkata.lps.entity.Coupon;

import lombok.Data;

@Data
public class CouponDto {
		
		    private String code;
		    private int price;
		    public boolean type_coupon;
		    public boolean active;
		    private Long date_exp;
		   	private int min;
		    private Long id_user;
		    private double win_agent;
		    private int type_game;
		    private int mode_pay;
			public CouponDto() {
				super();
			}
		   
	}
  