package com.bdh.db.entry;

public class sell {
	private String userId;
	private String exchange;
	private String surplus;
	private String price;
	private String high;
	private String num;
	private String sum;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getExchange() {
		return exchange;
	}
	public void setExchange(String exchange) {
		this.exchange = exchange;
	}
	public String getSurplus() {
		return surplus;
	}
	public void setSurplus(String surplus) {
		this.surplus = surplus;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getHigh() {
		return high;
	}
	public void setHigh(String high) {
		this.high = high;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getSum() {
		return sum;
	}
	public void setSum(String sum) {
		this.sum = sum;
	}
	public sell(String userId, String exchange, String surplus, String price,
			String high, String num, String sum) {
		super();
		this.userId = userId;
		this.exchange = exchange;
		this.surplus = surplus;
		this.price = price;
		this.high = high;
		this.num = num;
		this.sum = sum;
	}
	public sell() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
