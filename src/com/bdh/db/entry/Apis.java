package com.bdh.db.entry;

import java.util.List;


public class Apis {
	private long id;
	private String userid;
/*	private String platform;*/
	private int exchangid;
	private String apiKey;
	private String apiSecret;
	private String isEnable;
	private int flag;
	private String createTime;
	private String updateTime;
	private User user;
	private List<Exchang> exchang;
	
	

	



	public Apis() {
		super();
	}
	public Apis(long id, String userid, int exchangid, String apiKey,
			String apiSecret, String isEnable, int flag, String createTime,
			String updateTime) {
		super();
		this.id = id;
		this.userid = userid;
		this.exchangid = exchangid;
		this.apiKey = apiKey;
		this.apiSecret = apiSecret;
		this.isEnable = isEnable;
		this.flag = flag;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}








	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}

	public int getExchangid() {
		return exchangid;
	}


	public void setExchangid(int exchangid) {
		this.exchangid = exchangid;
	}


	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	public String getApiSecret() {
		return apiSecret;
	}
	public void setApiSecret(String apiSecret) {
		this.apiSecret = apiSecret;
	}
	public String getIsEnable() {
		return isEnable;
	}
	public void setIsEnable(String isEnable) {
		this.isEnable = isEnable;
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
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<Exchang> getExchang() {
		return exchang;
	}
	public void setExchang(List<Exchang> exchang) {
		this.exchang = exchang;
	}
	
	
	
	

}
