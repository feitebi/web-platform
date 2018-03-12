package com.bdh.db.entry;

import java.math.BigDecimal;

public class setting extends User{

	private String sid;
	private String	userId;
	private int	google2Factor;
	//  账户设置   手机通知
	private int	notify4followed;//被关�?? 1为开�??  0为关闭状�?? 默认�??0
	private int	notify4Viewed;//被查�??    1为开�??  0为关闭状�??  默认�??0
	private int	notify4Received;//收到大师�??   1为开�??  0为关闭状�??  默认�??0
	private int	notify4Traded; //智能成交    1为开�??  0为关闭状�??  默认�??0
	
	private int	sns4Opened; //默认为开启状�??   1为开�??  0为关闭状�??
	private int	sns4Assets;   //默认为开启状�??   1为开�??  0为关闭状�??
	private int	sns4Strategy;  ////默认为开启状�??   1为开�??  0为关闭状�??
	private BigDecimal	sns4Price;
	private int	wallet4KeyBackup;
	private int	wallet4WordsBackup;
	private String goolgleSecretKey;
	private int isEnablegoolegleAutor;
	public setting() {
		super();
	}
	
	public setting(String sid, String userId, int google2Factor,
			int notify4followed, int notify4Viewed, int notify4Received,
			int notify4Traded, int sns4Opened, int sns4Assets,
			int sns4Strategy, BigDecimal sns4Price, int wallet4KeyBackup,
			int wallet4WordsBackup, String goolgleSecretKey,
			int isEnablegoolegleAutor) {
		super();
		this.sid = sid;
		this.userId = userId;
		this.google2Factor = google2Factor;
		this.notify4followed = notify4followed;
		this.notify4Viewed = notify4Viewed;
		this.notify4Received = notify4Received;
		this.notify4Traded = notify4Traded;
		this.sns4Opened = sns4Opened;
		this.sns4Assets = sns4Assets;
		this.sns4Strategy = sns4Strategy;
		this.sns4Price = sns4Price;
		this.wallet4KeyBackup = wallet4KeyBackup;
		this.wallet4WordsBackup = wallet4WordsBackup;
		this.goolgleSecretKey = goolgleSecretKey;
		this.isEnablegoolegleAutor = isEnablegoolegleAutor;
	}

	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getGoogle2Factor() {
		return google2Factor;
	}
	public void setGoogle2Factor(int google2Factor) {
		this.google2Factor = google2Factor;
	}
	public int getNotify4followed() {
		return notify4followed;
	}
	public void setNotify4followed(int notify4followed) {
		this.notify4followed = notify4followed;
	}
	public int getNotify4Viewed() {
		return notify4Viewed;
	}
	public void setNotify4Viewed(int notify4Viewed) {
		this.notify4Viewed = notify4Viewed;
	}
	public int getNotify4Received() {
		return notify4Received;
	}
	public void setNotify4Received(int notify4Received) {
		this.notify4Received = notify4Received;
	}
	public int getNotify4Traded() {
		return notify4Traded;
	}
	public void setNotify4Traded(int notify4Traded) {
		this.notify4Traded = notify4Traded;
	}
	public int getSns4Opened() {
		return sns4Opened;
	}
	public void setSns4Opened(int sns4Opened) {
		this.sns4Opened = sns4Opened;
	}
	public int getSns4Assets() {
		return sns4Assets;
	}
	public void setSns4Assets(int sns4Assets) {
		this.sns4Assets = sns4Assets;
	}
	public int getSns4Strategy() {
		return sns4Strategy;
	}
	public void setSns4Strategy(int sns4Strategy) {
		this.sns4Strategy = sns4Strategy;
	}
	public BigDecimal getSns4Price() {
		return sns4Price;
	}
	public void setSns4Price(BigDecimal sns4Price) {
		this.sns4Price = sns4Price;
	}
	public int getWallet4KeyBackup() {
		return wallet4KeyBackup;
	}
	public void setWallet4KeyBackup(int wallet4KeyBackup) {
		this.wallet4KeyBackup = wallet4KeyBackup;
	}
	public int getWallet4WordsBackup() {
		return wallet4WordsBackup;
	}
	public void setWallet4WordsBackup(int wallet4WordsBackup) {
		this.wallet4WordsBackup = wallet4WordsBackup;
	}

	public String getGoolgleSecretKey() {
		return goolgleSecretKey;
	}

	public void setGoolgleSecretKey(String goolgleSecretKey) {
		this.goolgleSecretKey = goolgleSecretKey;
	}

	public int getIsEnablegoolegleAutor() {
		return isEnablegoolegleAutor;
	}

	public void setIsEnablegoolegleAutor(int isEnablegoolegleAutor) {
		this.isEnablegoolegleAutor = isEnablegoolegleAutor;
	}
	
	

}
