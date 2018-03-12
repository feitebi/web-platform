package com.bdh.db.entry;

import java.math.BigDecimal;

public class smartDeal {

	private String id;
	private String platform;
	private String coinName;
	private BigDecimal nowprice;
	private BigDecimal inprice;
	private BigDecimal nowprofit;
	private String smartdeal;
	private String datatime;
	private String type;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getCoinName() {
		return coinName;
	}
	public void setCoinName(String coinName) {
		this.coinName = coinName;
	}
	public BigDecimal getNowprice() {
		return nowprice;
	}
	public void setNowprice(BigDecimal nowprice) {
		this.nowprice = nowprice;
	}
	public BigDecimal getInprice() {
		return inprice;
	}
	public void setInprice(BigDecimal inprice) {
		this.inprice = inprice;
	}
	public BigDecimal getNowprofit() {
		return nowprofit;
	}
	public void setNowprofit(BigDecimal nowprofit) {
		this.nowprofit = nowprofit;
	}
	public String getSmartdeal() {
		return smartdeal;
	}
	public void setSmartdeal(String smartdeal) {
		this.smartdeal = smartdeal;
	}
	public String getDatatime() {
		return datatime;
	}
	public void setDatatime(String datatime) {
		this.datatime = datatime;
	}
	public smartDeal(String id, String platform, String coinName,
			BigDecimal nowprice, BigDecimal inprice, BigDecimal nowprofit,
			String smartdeal, String datatime, String type) {
		super();
		this.id = id;
		this.platform = platform;
		this.coinName = coinName;
		this.nowprice = nowprice;
		this.inprice = inprice;
		this.nowprofit = nowprofit;
		this.smartdeal = smartdeal;
		this.datatime = datatime;
		this.type = type;
	}
	public smartDeal() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
