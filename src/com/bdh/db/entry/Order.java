package com.bdh.db.entry;

public class Order{
	
	private String limitPrice;
	private String type;
	private String tradableAmount;
	private String averagePrice;
	private String currencyPair;
	private String id;
	private String timestamp;
	private String status;
	public String getLimitPrice() {
		return limitPrice;
	}
	public void setLimitPrice(String limitPrice) {
		this.limitPrice = limitPrice;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTradableAmount() {
		return tradableAmount;
	}
	public void setTradableAmount(String tradableAmount) {
		this.tradableAmount = tradableAmount;
	}
	public String getAveragePrice() {
		return averagePrice;
	}
	public void setAveragePrice(String averagePrice) {
		this.averagePrice = averagePrice;
	}
	public String getCurrencyPair() {
		return currencyPair;
	}
	public void setCurrencyPair(String currencyPair) {
		this.currencyPair = currencyPair;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Order(String limitPrice, String type, String tradableAmount,
			String averagePrice, String currencyPair, String id,
			String timestamp, String status) {
		super();
		this.limitPrice = limitPrice;
		this.type = type;
		this.tradableAmount = tradableAmount;
		this.averagePrice = averagePrice;
		this.currencyPair = currencyPair;
		this.id = id;
		this.timestamp = timestamp;
		this.status = status;
	}
	public Order() {
		super();
	}

	
	
}
