package com.bdh.db.entry;

public class Fill extends User{
	private String id;
	private String userId;
	private double amount;
	private String rechargeCode;
	private int statusFlag;
	private String serial;//��ˮ��
	private String adminId;
	private String admin;
	private String adminTime;
	private String comment;
	private String createTime;
	private String updateTime;
	
	public String getSerial() {
		return serial;
	}
	public void setSerial(String serial) {
		this.serial = serial;
	}
	public Fill() {
		super();
	}
	public Fill(String id, String userId, double amount, String rechargeCode,
			int statusFlag, String adminId, String admin, String adminTime,
			String comment, String createTime, String updateTime) {
		super();
		this.id = id;
		this.userId = userId;
		this.amount = amount;
		this.rechargeCode = rechargeCode;
		this.statusFlag = statusFlag;
		this.adminId = adminId;
		this.admin = admin;
		this.adminTime = adminTime;
		this.comment = comment;
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
	
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public String getRechargeCode() {
		return rechargeCode;
	}
	public void setRechargeCode(String rechargeCode) {
		this.rechargeCode = rechargeCode;
	}
	public int getStatusFlag() {
		return statusFlag;
	}
	public void setStatusFlag(int statusFlag) {
		this.statusFlag = statusFlag;
	}
	public String getAdminId() {
		return adminId;
	}
	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}
	public String getAdmin() {
		return admin;
	}
	public void setAdmin(String admin) {
		this.admin = admin;
	}
	public String getAdminTime() {
		return adminTime;
	}
	public void setAdminTime(String adminTime) {
		this.adminTime = adminTime;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
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
