package com.bdh.db.view;

import com.bdh.db.entry.User;

public class Buyfund extends User{

	private String userId;
	private String fund;
	
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getFund() {
		return fund;
	}
	public void setFund(String fund) {
		this.fund = fund;
	}
	
	
}
