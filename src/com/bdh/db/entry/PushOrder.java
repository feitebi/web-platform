package com.bdh.db.entry;



//购买
public class PushOrder {

	private String id;
	private String pushId;
	private String pushUserId;
	private String userId;
	private double price;
	private double commission;
	private double qty;
	private double amount;
	private String paidTime;
	private int	flag;//0锟斤拷锟斤拷锟�1锟斤拷锟斤拷锟斤拷锟�?
	private String adminId;
	private String conformed;
	private String createTime;
	private String updateTime;
	
	
	
	
	public double getQty() {
		return qty;
	}

	public void setQty(double qty) {
		this.qty = qty;
	}

	public String getId() {
		return id;
	}
	
	public PushOrder() {
		super();
	}

	public PushOrder(String id, String pushId, String pushUserId, String userId,
			double price, double commission, double amount, String paidTime,
			int flag, String adminId, String conformed, String createTime,
			String updateTime) {
		super();
		this.id = id;
		this.pushId = pushId;
		this.pushUserId = pushUserId;
		this.userId = userId;
		this.price = price;
		this.commission = commission;
		this.amount = amount;
		this.paidTime = paidTime;
		this.flag = flag;
		this.adminId = adminId;
		this.conformed = conformed;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPushId() {
		return pushId;
	}
	public void setPushId(String pushId) {
		this.pushId = pushId;
	}
	public String getPushUserId() {
		return pushUserId;
	}
	public void setPushUserId(String pushUserId) {
		this.pushUserId = pushUserId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getCommission() {
		return commission;
	}
	public void setCommission(double commission) {
		this.commission = commission;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getPaidTime() {
		return paidTime;
	}
	public void setPaidTime(String paidTime) {
		this.paidTime = paidTime;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getAdminId() {
		return adminId;
	}
	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}
	public String getConformed() {
		return conformed;
	}
	public void setConformed(String conformed) {
		this.conformed = conformed;
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
