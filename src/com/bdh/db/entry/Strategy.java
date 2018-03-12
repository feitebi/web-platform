/**
 * 
 */
package com.bdh.db.entry;

import java.math.BigDecimal;

/**
 * @author NRG
 *
 */
public class Strategy {
	
	private int id;
	private int strategyId;
	private String userId;
	private String platform;
	private String coinName;
	private String askOrBid;
	private String priceRule;
	private BigDecimal askPrice;
	private BigDecimal BidPrice;
    private BigDecimal qty;
    private BigDecimal ProfitRatio;
    private BigDecimal topOrlow;
    private int strategyFlag;
	private int Price4Days;
	private String createTime;
	private String updateTime;
	
	public BigDecimal getProfitRatio() {
		return ProfitRatio;
	}
	public void setProfitRatio(BigDecimal profitRatio) {
		ProfitRatio = profitRatio;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getStrategyId() {
		return strategyId;
	}
	public void setStrategyId(int strategyId) {
		this.strategyId = strategyId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getCoinName() {
		return coinName;
	}
	public void setCoinName(String coinName) {
		this.coinName = coinName;
	}
	public String getAskOrBid() {
		return askOrBid;
	}
	public void setAskOrBid(String askOrBid) {
		this.askOrBid = askOrBid;
	}
	public String getPriceRule() {
		return priceRule;
	}
	public void setPriceRule(String priceRule) {
		this.priceRule = priceRule;
	}
	public BigDecimal getAskPrice() {
		return askPrice;
	}
	public void setAskPrice(BigDecimal askPrice) {
		this.askPrice = askPrice;
	}
	public BigDecimal getBidPrice() {
		return BidPrice;
	}
	public void setBidPrice(BigDecimal bidPrice) {
		BidPrice = bidPrice;
	}
	public BigDecimal getQty() {
		return qty;
	}
	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}
	public BigDecimal getTopOrlow() {
		return topOrlow;
	}
	public void setTopOrlow(BigDecimal topOrlow) {
		this.topOrlow = topOrlow;
	}
	public int getStrategyFlag() {
		return strategyFlag;
	}
	public void setStrategyFlag(int strategyFlag) {
		this.strategyFlag = strategyFlag;
	}
	public int getPrice4Days() {
		return Price4Days;
	}
	public void setPrice4Days(int price4Days) {
		Price4Days = price4Days;
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
	
	public Strategy(int id, int strategyId, String userId, String platform,
			String coinName, String askOrBid, String priceRule,
			BigDecimal askPrice, BigDecimal bidPrice, BigDecimal qty,
			BigDecimal profitRatio, BigDecimal topOrlow, int strategyFlag,
			int price4Days, String createTime, String updateTime) {
		super();
		this.id = id;
		this.strategyId = strategyId;
		this.userId = userId;
		this.platform = platform;
		this.coinName = coinName;
		this.askOrBid = askOrBid;
		this.priceRule = priceRule;
		this.askPrice = askPrice;
		BidPrice = bidPrice;
		this.qty = qty;
		ProfitRatio = profitRatio;
		this.topOrlow = topOrlow;
		this.strategyFlag = strategyFlag;
		Price4Days = price4Days;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}
	public Strategy() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
