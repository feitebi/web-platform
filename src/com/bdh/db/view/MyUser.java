package com.bdh.db.view;

public class MyUser {
	private String username;//用户名
	private String Money; //总金额
	private String fundMoney;//冻结金额
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getMoney() {
		return Money;
	}
	public void setMoney(String money) {
		Money = money;
	}
	public String getFundMoney() {
		return fundMoney;
	}
	public void setFundMoney(String fundMoney) {
		this.fundMoney = fundMoney;
	}
	
	
	

}
