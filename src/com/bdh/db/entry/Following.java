package com.bdh.db.entry;

public class Following extends Master{

	private String id; 
	private String userId; 
	private String followedUserId;
	private int flag;      
	private String createTime;  
	private String updateTime;  
	private String coin;//
	private String asset;//
	private String platform;//
	private String Dailyincreases;//
	private String fansnumber;
	private String sumcny;
	
	
	public String getSumcny() {
		return sumcny;
	}

	public void setSumcny(String sumcny) {
		this.sumcny = sumcny;
	}

	public String getFansnumber() {
		return fansnumber;
	}

	public void setFansnumber(String fansnumber) {
		this.fansnumber = fansnumber;
	}

	public Following() {
		super();
	}
	
	public Following(String id, String userId, String followedUserId, int flag,
			String createTime, String updateTime) {
		super();
		this.id = id;
		this.userId = userId;
		this.followedUserId = followedUserId;
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
	public String getFollowedUserId() {
		return followedUserId;
	}
	public void setFollowedUserId(String followedUserId) {
		this.followedUserId = followedUserId;
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

	
	
	
	public String getCoin() {
		return coin;
	}
	public void setCoin(String coin) {
		this.coin = coin;
	}
	public String getAsset() {
		return asset;
	}
	public void setAsset(String asset) {
		this.asset = asset;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getDailyincreases() {
		return Dailyincreases;
	}
	public void setDailyincreases(String dailyincreases) {
		Dailyincreases = dailyincreases;
	}
	
	
	
	
}