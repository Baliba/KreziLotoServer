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
			public CouponDto() {
				super();
			}
		   
	}
  