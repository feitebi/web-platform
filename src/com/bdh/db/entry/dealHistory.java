package com.bdh.db.entry;

import java.math.BigDecimal;

public class dealHistory {

	private String id;
	private String dealType;
	private String platform;
	private String name;
	private BigDecimal avgPrice;
	private BigDecimal dealnum;
	private BigDecimal dealamount;
	private String dealtime;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDealType() {
		return dealType;
	}
	public void setDealType(String dealType) {
		this.dealType = dealType;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getAvgPrice() {
		return avgPrice;
	}
	public void setAvgPrice(BigDecimal avgPrice) {
		this.avgPrice = avgPrice;
	}
	public BigDecimal getDealnum() {
		return dealnum;
	}
	public void setDealnum(BigDecimal dealnum) {
		this.dealnum = dealnum;
	}
	public BigDecimal getDealamount() {
		return dealamount;
	}
	public void setDealamount(BigDecimal dealamount) {
		this.dealamount = dealamount;
	}
	public String getDealtime() {
		return dealtime;
	}
	public void setDealtime(String dealtime) {
		this.dealtime = dealtime;
	}
	public dealHistory(String id, String dealType, String platform,
			String name, BigDecimal avgPrice, BigDecimal dealnum,
			BigDecimal dealamount, String dealtime) {
		super();
		this.id = id;
		this.dealType = dealType;
		this.platform = platform;
		this.name = name;
		this.avgPrice = avgPrice;
		this.dealnum = dealnum;
		this.dealamount = dealamount;
		this.dealtime = dealtime;
	}
	public dealHistory() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
