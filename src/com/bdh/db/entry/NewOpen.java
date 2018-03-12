package com.bdh.db.entry;

import java.math.BigDecimal;

public class NewOpen {
	private int	id;
	private String	name;
	private String kaiPrice;
	private String nowPrice;
	private String volume;
	private String platform;
	private String time;
	
	
	
	
	
	public NewOpen() {
		super();
	}
	public NewOpen(int id, String name, String kaiPrice, String nowPrice,
			String volume, String platform, String time) {
		super();
		this.id = id;
		this.name = name;
		this.kaiPrice = kaiPrice;
		this.nowPrice = nowPrice;
		this.volume = volume;
		this.platform = platform;
		this.time = time;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getKaiPrice() {
		return kaiPrice;
	}
	public void setKaiPrice(String kaiPrice) {
		this.kaiPrice = kaiPrice;
	}
	public String getNowPrice() {
		return nowPrice;
	}
	public void setNowPrice(String nowPrice) {
		this.nowPrice = nowPrice;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}

	

}
