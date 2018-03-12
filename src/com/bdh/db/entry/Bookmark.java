package com.bdh.db.entry;

import java.sql.Date;

public class Bookmark {
	
	
	
	private String id;
	private String userId;
	private String platform;
	private String currency;
	private int flag=1;
	private String createTime;
	private String updateTime;
	private String lastPrice;//当前价
	private String hightPrive;//最高价
	private String lowPrice;//最低价
	private String volumeCount;//成交量
	
	
	
	public String getLastPrice() {
		return lastPrice;
	}
	public void setLastPrice(String lastPrice) {
		this.lastPrice = lastPrice;
	}
	public String getHightPrive() {
		return hightPrive;
	}
	public void setHightPrive(String hightPrive) {
		this.hightPrive = hightPrive;
	}
	public String getLowPrice() {
		return lowPrice;
	}
	public void setLowPrice(String lowPrice) {
		this.lowPrice = lowPrice;
	}
	public String getVolumeCount() {
		return volumeCount;
	}
	public void setVolumeCount(String volumeCount) {
		this.volumeCount = volumeCount;
	}
	public Bookmark() {
		super();
	}
	public Bookmark(String id, String userId, String platform, String currency,
			int flag, String createTime, String updateTime) {
		super();
		this.id = id;
		this.userId = userId;
		this.platform = platform;
		this.currency = currency;
		this.flag = flag;
		this.createTime = createTime;
		this.updateTime = updateTime;
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
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
	
	

}
