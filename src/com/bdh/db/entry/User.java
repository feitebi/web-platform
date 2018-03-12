package com.bdh.db.entry;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class User {
	
	private String id;
	private String userId;
	private String name;
	private String phone;
	private String phoneValidCode;
	private int phoneValidFlag;
	private String pwd;
	private String city;
	private String walletKey;
	private String walletAdder;
	private String twoFactorKey;
	private int flag;
	private String createTime;
	private String updateTime;
	private String errorMsg = "";
	private setting setting;
	private int isfollowed;  //0为未关注  1为已关注
	private String balances;
	private String addr = "";
	private String referAddr="";
	private Integer nameCertFlag = 0;
	private BigDecimal birdToken = BigDecimal.ZERO;
	/*private InToken inToken = new InToken();*/
	private List<Apis> ApiList = new ArrayList<Apis>(0);


	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getReferAddr() {
		return referAddr;
	}
	public void setReferAddr(String referAddr) {
		this.referAddr = referAddr;
	}
	public Integer getNameCertFlag() {
		return nameCertFlag;
	}
	public void setNameCertFlag(Integer nameCertFlag) {
		this.nameCertFlag = nameCertFlag;
	}
	public BigDecimal getBirdToken() {
		return birdToken;
	}
	public void setBirdToken(BigDecimal birdToken) {
		this.birdToken = birdToken;
	}
	public List<Apis> getApiList() {
		return ApiList;
	}
	public void setApiList(List<Apis> apiList) {
		ApiList = apiList;
	}
	public String getBalances() {
		return balances;
	}
	public void setBalances(String balances) {
		this.balances = balances;
	}
	private List<Apis> Apis;
	

	public int getIsfollowed() {
		return isfollowed;
	}
	public void setIsfollowed(int isfollowed) {
		this.isfollowed = isfollowed;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public List<Apis> getApis() {
		return Apis;
	}
	public void setApis(List<Apis> apis) {
		Apis = apis;
	}
	public setting getSetting() {
		return setting;
	}
	public void setSetting(setting setting) {
		this.setting = setting;
	}
	public User() {
		super();
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPhoneValidCode() {
		return phoneValidCode;
	}
	public void setPhoneValidCode(String phoneValidCode) {
		this.phoneValidCode = phoneValidCode;
	}
	public int getPhoneValidFlag() {
		return phoneValidFlag;
	}
	public void setPhoneValidFlag(int phoneValidFlag) {
		this.phoneValidFlag = phoneValidFlag;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getWalletKey() {
		return walletKey;
	}
	public void setWalletKey(String walletKey) {
		this.walletKey = walletKey;
	}
	public String getWalletAdder() {
		return walletAdder;
	}
	public void setWalletAdder(String walletAdder) {
		this.walletAdder = walletAdder;
	}
	public String getTwoFactorKey() {
		return twoFactorKey;
	}
	public void setTwoFactorKey(String twoFactorKey) {
		this.twoFactorKey = twoFactorKey;
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
	public User(String id, String userId, String name, String phone,
			String phoneValidCode, int phoneValidFlag, String pwd, String city,
			String walletKey, String walletAdder, String twoFactorKey,
			int flag, String createTime, String updateTime, String errorMsg,
			com.bdh.db.entry.setting setting, int isfollowed, String balances,
			String addr, String referAddr, Integer nameCertFlag,
			BigDecimal birdToken, List<com.bdh.db.entry.Apis> apiList,
			List<com.bdh.db.entry.Apis> apis) {
		super();
		this.id = id;
		this.userId = userId;
		this.name = name;
		this.phone = phone;
		this.phoneValidCode = phoneValidCode;
		this.phoneValidFlag = phoneValidFlag;
		this.pwd = pwd;
		this.city = city;
		this.walletKey = walletKey;
		this.walletAdder = walletAdder;
		this.twoFactorKey = twoFactorKey;
		this.flag = flag;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.errorMsg = errorMsg;
		this.setting = setting;
		this.isfollowed = isfollowed;
		this.balances = balances;
		this.addr = addr;
		this.referAddr = referAddr;
		this.nameCertFlag = nameCertFlag;
		this.birdToken = birdToken;
		ApiList = apiList;
		Apis = apis;
	}
	
	
}
