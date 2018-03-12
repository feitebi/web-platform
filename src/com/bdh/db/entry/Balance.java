package com.bdh.db.entry;

public class Balance extends setting{
	private String id;
    private	String userId;
    private	String platform;
    private	String currency;
	private double balance;
 	private String createTime;
	public Balance() {
		super();
	}
	public Balance(String id, String userId, String platform, String currency,
			double balance, String createTime) {
		super();
		this.id = id;
		this.userId = userId;
		this.platform = platform;
		this.currency = currency;
		this.balance = balance;
		this.createTime = createTime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
 	
 	

}
