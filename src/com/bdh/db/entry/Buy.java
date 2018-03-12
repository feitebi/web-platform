package com.bdh.db.entry;

public class Buy {
	private String buyID;
	private String userId;
	private String exchange;
	private String surplus;
	private String under;
	private String buy;
	private String num;
	private String sum;
	
	
	
	public Buy() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Buy(String buyID, String userId, String exchange, String surplus,
			String under, String buy, String num, String sum) {
		super();
		this.buyID = buyID;
		this.userId = userId;
		this.exchange = exchange;
		this.surplus = surplus;
		this.under = under;
		this.buy = buy;
		this.num = num;
		this.sum = sum;
	}
	public String getBuyID() {
		return buyID;
	}
	public void setBuyID(String buyID) {
		this.buyID = buyID;
	}
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
	public String getUnder() {
		return under;
	}
	public void setUnder(String under) {
		this.under = under;
	}
	public String getBuy() {
		return buy;
	}
	public void setBuy(String buy) {
		this.buy = buy;
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
}
