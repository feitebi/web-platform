package com.bdh.db.view;

import com.bdh.db.entry.User;

public class Sellfund extends User{

	private String pushUserid;
	private String fund;
	
	public String getPushUserid() {
		return pushUserid;
	}
	public void setPushUserid(String pushUserid) {
		this.pushUserid = pushUserid;
	}
	public String getFund() {
		return fund;
	}
	public void setFund(String fund) {
		this.fund = fund;
	}
	
	
	
	
}
