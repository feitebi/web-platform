package com.bdh.db.entry;

import java.util.List;

public class LimitOrder {
	
	private String limitPrice;
	private List<HistoryOrder> order;
	
	
	public String getLimitPrice() {
		return limitPrice;
	}
	public void setLimitPrice(String limitPrice) {
		this.limitPrice = limitPrice;
	}
	public List<HistoryOrder> getOrder() {
		return order;
	}
	public void setOrder(List<HistoryOrder> order) {
		this.order = order;
	}
	
}
