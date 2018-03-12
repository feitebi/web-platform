package com.bdh.db.view;

import com.bdh.db.entry.User;

public class Withdrawfund extends User{

	private String userid;
	private String fund;
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getFund() {
		return fund;
	}
	public void setFund(String fund) {
		this.fund = fund;
	}
	
	
	
}
