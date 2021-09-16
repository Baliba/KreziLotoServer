package com.monkata.lps.response;

import java.util.Date;

import lombok.Data;

@Data
public class Order {
    public Long id_order;
    public int amount;
    public String date_order;
    public String date_order_real;
    public Long id_transaction;
    public int is_over;
    public String date_trans;
    public String valider;
    public String see_by_admin;
    public String id_user;
    public int type_order;
    public String id_app_order;
    public String method_payment;
    public String token_order;
    public int is_fail;
    public String btn_link;
    public String phone;
    public String coupon;
    public  int bonis;
	public void addBonisToAmount(int price) {
	   this.amount += price;
	}
}

