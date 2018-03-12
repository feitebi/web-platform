package com.bdh.db.entry;



//Ö§¸¶±¦¼ÇÂ¼±í
public class Checkingdata {
	private String id;
	private String userName;
	private String phone;
	private String tranNumber;
	private String tranTime;
	private String paymentTime;
	private String bankAccount;
	private String amount;
	private String comment;    
	private String adminName;
	public Checkingdata() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Checkingdata(String id, String userName, String phone,
			String tranNumber, String tranTime, String paymentTime,
			String bankAccount, String amount, String comment, String adminName) {
		super();
		this.id = id;
		this.userName = userName;
		this.phone = phone;
		this.tranNumber = tranNumber;
		this.tranTime = tranTime;
		this.paymentTime = paymentTime;
		this.bankAccount = bankAccount;
		this.amount = amount;
		this.comment = comment;
		this.adminName = adminName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getTranNumber() {
		return tranNumber;
	}
	public void setTranNumber(String tranNumber) {
		this.tranNumber = tranNumber;
	}
	public String getTranTime() {
		return tranTime;
	}
	public void setTranTime(String tranTime) {
		this.tranTime = tranTime;
	}
	public String getPaymentTime() {
		return paymentTime;
	}
	public void setPaymentTime(String paymentTime) {
		this.paymentTime = paymentTime;
	}
	public String getBankAccount() {
		return bankAccount;
	}
	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getAdminName() {
		return adminName;
	}
	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	
}
