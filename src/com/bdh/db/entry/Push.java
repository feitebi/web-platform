package com.bdh.db.entry;



//
public class Push extends User{
	private String pid;
	private String formUserId;
	private String formAddr;
	private String formKey;
	private String askPrice;
	private double leftqty;
	private double qty;
	private String amount;
	private String commision;
	private String toUserId;
	private String toAddr;
	private int flag;
	private String createTime;
	private String updateTime;
	private String orderTime;
	private String txId;
	private int orderFlag;
	private User user;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Push() {
		super();
	}

	public Push(String pid, String formUserId, String formAddr, String formKey,
			String askPrice, double leftqty, double qty, String amount,
			String commision, String toUserId, String toAddr, int flag,
			String createTime, String updateTime, String orderTime,
			String txId, int orderFlag, User user) {
		super();
		this.pid = pid;
		this.formUserId = formUserId;
		this.formAddr = formAddr;
		this.formKey = formKey;
		this.askPrice = askPrice;
		this.leftqty = leftqty;
		this.qty = qty;
		this.amount = amount;
		this.commision = commision;
		this.toUserId = toUserId;
		this.toAddr = toAddr;
		this.flag = flag;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.orderTime = orderTime;
		this.txId = txId;
		this.orderFlag = orderFlag;
		this.user = user;
	}
	public double getLeftqty() {
		return leftqty;
	}
	public void setLeftqty(double leftqty) {
		this.leftqty = leftqty;
	}
	/*public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}*/
	
	public String getFormUserId() {
		return formUserId;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public void setFormUserId(String formUserId) {
		this.formUserId = formUserId;
	}
	public String getFormAddr() {
		return formAddr;
	}
	public void setFormAddr(String formAddr) {
		this.formAddr = formAddr;
	}
	public String getFormKey() {
		return formKey;
	}
	public void setFormKey(String formKey) {
		this.formKey = formKey;
	}
	public String getAskPrice() {
		return askPrice;
	}
	public void setAskPrice(String askPrice) {
		this.askPrice = askPrice;
	}
	public double getQty() {
		return qty;
	}
	public void setQty(double qty) {
		this.qty = qty;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getCommision() {
		return commision;
	}
	public void setCommision(String commision) {
		this.commision = commision;
	}
	public String getToUserId() {
		return toUserId;
	}
	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}
	public String getToAddr() {
		return toAddr;
	}
	public void setToAddr(String toAddr) {
		this.toAddr = toAddr;
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
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	public String getTxId() {
		return txId;
	}
	public void setTxId(String txId) {
		this.txId = txId;
	}
	public int getOrderFlag() {
		return orderFlag;
	}
	public void setOrderFlag(int orderFlag) {
		this.orderFlag = orderFlag;
	}
	
	
	
	
	

}
