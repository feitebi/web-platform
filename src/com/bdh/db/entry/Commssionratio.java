package com.bdh.db.entry;


//ÊÖÐø·Ñ
public class Commssionratio {
	
	private String id;
	private double fillRatio;
	private double WithdrawRatio;
	private double PushRatio;
	private double PushOrderRatio;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public double getFillRatio() {
		return fillRatio;
	}
	public void setFillRatio(double fillRatio) {
		this.fillRatio = fillRatio;
	}
	public double getWithdrawRatio() {
		return WithdrawRatio;
	}
	public void setWithdrawRatio(double withdrawRatio) {
		WithdrawRatio = withdrawRatio;
	}
	public double getPushRatio() {
		return PushRatio;
	}
	public void setPushRatio(double pushRatio) {
		PushRatio = pushRatio;
	}
	public double getPushOrderRatio() {
		return PushOrderRatio;
	}
	public void setPushOrderRatio(double pushOrderRatio) {
		PushOrderRatio = pushOrderRatio;
	}


}
