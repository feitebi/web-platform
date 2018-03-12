package com.bdh.db.entry;

import java.util.List;
import java.util.Map;

public class smartOrder {

	private String id;
	private String strategyId;
	private String userid;
	private String platform;
	private String coinName;
	private String askOrBid;
	private String priceRule;
	private String askPrice;
	private String BidPrice;
	private String qty;
	private String tickerlast;
	private String ProfitRatio;
	private String topOrlow;
	private int	strategyFlag;
	private int	Price4Days;
	private String createTime;
	private String updateTime;
	private List<smartDeal> smartdeal;
	private List<dealHistory> dealhistory;
	Map<String, List<smartOrder>> mapsmartOrder;
	
	public Map<String, List<smartOrder>> getMapsmartOrder() {
		return mapsmartOrder;
	}
	public void setMapsmartOrder(Map<String, List<smartOrder>> mapsmartOrder) {
		this.mapsmartOrder = mapsmartOrder;
	}
	public String getTickerlast() {
		return tickerlast;
	}
	public void setTickerlast(String tickerlast) {
		this.tickerlast = tickerlast;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStrategyId() {
		return strategyId;
	}
	public void setStrategyId(String strategyId) {
		this.strategyId = strategyId;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
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
	public String getAskPrice() {
		return askPrice;
	}
	public void setAskPrice(String askPrice) {
		this.askPrice = askPrice;
	}
	public String getBidPrice() {
		return BidPrice;
	}
	public void setBidPrice(String bidPrice) {
		BidPrice = bidPrice;
	}
	public String getQty() {
		return qty;
	}
	public void setQty(String qty) {
		this.qty = qty;
	}
	public String getProfitRatio() {
		return ProfitRatio;
	}
	public void setProfitRatio(String profitRatio) {
		ProfitRatio = profitRatio;
	}
	public String getTopOrlow() {
		return topOrlow;
	}
	public void setTopOrlow(String topOrlow) {
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
	public List<smartDeal> getSmartdeal() {
		return smartdeal;
	}
	public void setSmartdeal(List<smartDeal> smartdeal) {
		this.smartdeal = smartdeal;
	}
	public List<dealHistory> getDealhistory() {
		return dealhistory;
	}
	public void setDealhistory(List<dealHistory> dealhistory) {
		this.dealhistory = dealhistory;
	}
	public smartOrder(String id, String strategyId, String userid,
			String platform, String coinName, String askOrBid,
			String priceRule, String askPrice, String bidPrice, String qty,
			String profitRatio, String topOrlow, int strategyFlag,
			int price4Days, String createTime, String updateTime,
			List<smartDeal> smartdeal, List<dealHistory> dealhistory) {
		super();
		this.id = id;
		this.strategyId = strategyId;
		this.userid = userid;
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
		this.smartdeal = smartdeal;
		this.dealhistory = dealhistory;
	}
	public smartOrder() {
		super();
	}
	
	
}
