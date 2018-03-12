package com.bdh.db.entry;
//Ã·œ÷
public class Withdraw extends User{
	
	private User user;
	private String wid;
	private String userid;
	private double amount;
	private String toAccount;
	private String bankName;
	private double commission;
	private int flag;
	private String adminId;
	private double confirmAmount;
	private String txId;
	private int	applyFlag;
	private int	adminFlag;
	private String adminTime;
	private String adminContext;
	private String createTime;
	private String upateTime;
	public Withdraw() {
		super();
	}
	public Withdraw(User user, String wid, String userid, double amount,
			String toAccount, String bankName, double commission, int flag,
			String adminId, double confirmAmount, String txId, int applyFlag,
			int adminFlag, String adminTime, String adminContext,
			String createTime, String upateTime) {
		super();
		this.user = user;
		this.wid = wid;
		this.userid = userid;
		this.amount = amount;
		this.toAccount = toAccount;
		this.bankName = bankName;
		this.commission = commission;
		this.flag = flag;
		this.adminId = adminId;
		this.confirmAmount = confirmAmount;
		this.txId = txId;
		this.applyFlag = applyFlag;
		this.adminFlag = adminFlag;
		this.adminTime = adminTime;
		this.adminContext = adminContext;
		this.createTime = createTime;
		this.upateTime = upateTime;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getWid() {
		return wid;
	}
	public void setWid(String wid) {
		this.wid = wid;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getToAccount() {
		return toAccount;
	}
	public void setToAccount(String toAccount) {
		this.toAccount = toAccount;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public double getCommission() {
		return commission;
	}
	public void setCommission(double commission) {
		this.commission = commission;
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
	public double getConfirmAmount() {
		return confirmAmount;
	}
	public void setConfirmAmount(double confirmAmount) {
		this.confirmAmount = confirmAmount;
	}
	public String getTxId() {
		return txId;
	}
	public void setTxId(String txId) {
		this.txId = txId;
	}
	public int getApplyFlag() {
		return applyFlag;
	}
	public void setApplyFlag(int applyFlag) {
		this.applyFlag = applyFlag;
	}
	public int getAdminFlag() {
		return adminFlag;
	}
	public void setAdminFlag(int adminFlag) {
		this.adminFlag = adminFlag;
	}
	public String getAdminTime() {
		return adminTime;
	}
	public void setAdminTime(String adminTime) {
		this.adminTime = adminTime;
	}
	public String getAdminContext() {
		return adminContext;
	}
	public void setAdminContext(String adminContext) {
		this.adminContext = adminContext;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpateTime() {
		return upateTime;
	}
	public void setUpateTime(String upateTime) {
		this.upateTime = upateTime;
	}
	
	
	
	

}
