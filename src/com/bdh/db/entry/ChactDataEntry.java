package com.bdh.db.entry;

import java.math.BigDecimal;

public class ChactDataEntry {
	
	
	private String date;
	private BigDecimal open;
	private BigDecimal high;
	private BigDecimal low;
	private BigDecimal close;
	private BigDecimal volume;
	
	
	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public BigDecimal getOpen() {
		return open;
	}


	public void setOpen(BigDecimal open) {
		this.open = open;
	}


	public BigDecimal getHigh() {
		return high;
	}


	public void setHigh(BigDecimal high) {
		this.high = high;
	}


	public BigDecimal getLow() {
		return low;
	}


	public void setLow(BigDecimal low) {
		this.low = low;
	}


	public BigDecimal getClose() {
		return close;
	}


	public void setClose(BigDecimal close) {
		this.close = close;
	}


	public BigDecimal getVolume() {
		return volume;
	}


	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}


	@Override
	public String toString() {
		return "ChactDataEntry [date=" + date + ", open=" + open + ", high="
				+ high + ", low=" + low + ", close=" + close + ", volume="
				+ volume + "]";
	}
	
	

}
