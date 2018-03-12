package com.bdh.db.entry;

import java.util.List;



public class HistoryLimitOrder {
	
	private String platform;
	
	private List<LimitOrder> limitOrder;

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public List<LimitOrder> getLimitOrder() {
		return limitOrder;
	}

	public void setLimitOrder(List<LimitOrder> limitOrder) {
		this.limitOrder = limitOrder;
	}
	
	

}
