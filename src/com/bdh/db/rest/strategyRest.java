package com.bdh.db.rest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;

import com.bdh.db.dao.strategyDao;
import com.bdh.db.entry.AccountBalance;
import com.bdh.db.entry.HistoryLimitOrder;
import com.bdh.db.entry.Order;
import com.bdh.db.entry.PagableData;
import com.bdh.db.entry.StatusBean;
import com.bdh.db.entry.Strategy;
import com.bdh.db.entry.UserTrade;
import com.bdh.db.entry.smartOrder;
import com.sun.jersey.api.json.JSONWithPadding;

@Path("/strategy")
@Produces({ "application/x-javascript", "application/json", "application/xml" })
public class strategyRest {

	// 查询只能买卖
	@POST
	@Produces("application/json")
	@Path("/getsmartorder")
	public JSONWithPadding getsmartorder(@FormParam("userid") String userid) {
		if(userid==null){
			return new JSONWithPadding("","getsmartorder");
		}
		return new JSONWithPadding(new GenericEntity<PagableData<smartOrder>>(
				new strategyDao().getsmartorder(userid)) {
		}, "getsmartorder");
	}

	// 取消智能买卖
	@POST
	@Produces("application/json")
	@Path("/cancelSmartorder")
	public JSONWithPadding cancelSmartorder(@FormParam("id") String id) {
		if(id==null){
			return new JSONWithPadding("","cancelSmartorder");
		}
		return new JSONWithPadding(new GenericEntity<StatusBean>(
				new strategyDao().cancelSmartorder(id)) {
		}, "cancelSmartorder");
	}

	/***
	 * 智能策略
	 * */
	@POST
	@Produces("application/json")
	@Path("/getsmartorders")
	public JSONWithPadding getsmartorders(@FormParam("userid") String userid,
			@FormParam("limit") String limit,
			@FormParam("start") String start) {
		if(userid==null){
			return new JSONWithPadding("","getsmartorders");
		}
		return new JSONWithPadding(new GenericEntity<PagableData<smartOrder>>(
				new strategyDao().getsmartorderTriker(userid, limit, start)) {
		}, "getsmartorders");
	}

	// ������
	@POST
	@Produces("application/json")
	@Path("/smartSell")
	public JSONWithPadding smartsell(@FormParam("strategyId") int strategyId,
			@FormParam("userId") String userId,
			@FormParam("platform") String platform,
			@FormParam("coinName") String coinName,
			@FormParam("askOrBid") String askOrBid,
			@FormParam("priceRule") String priceRule,
			@FormParam("askPrice") BigDecimal askPrice,
			@FormParam("qty") BigDecimal qty,
			@FormParam("ProfitRatio") BigDecimal ProfitRatio,
			@FormParam("topOrlow") BigDecimal topOrlow,
			@FormParam("strategyFlag") int strategyFlag,
			@FormParam("Price4Days") int Price4Days) {
		Strategy strategy = new Strategy();
		strategy.setStrategyId(strategyId);
		strategy.setUserId(userId);
		strategy.setPlatform(platform.toLowerCase());
		strategy.setCoinName(coinName);
		strategy.setAskOrBid(askOrBid);
		strategy.setPriceRule(priceRule);
		strategy.setAskPrice(askPrice);
		strategy.setQty(qty);
		strategy.setProfitRatio(ProfitRatio);
		strategy.setTopOrlow(topOrlow);
		strategy.setStrategyFlag(strategyFlag);
		strategy.setPrice4Days(Price4Days);
		return new JSONWithPadding(new GenericEntity<StatusBean>(
				new strategyDao().smartSell(strategy)) {
		}, "smartSell");
	}
	//卖得
	@POST
	@Produces("application/json")
	@Path("/smartSell")
	public JSONWithPadding smartsellTo(@FormParam("strategyId") int strategyId,
			@FormParam("userId") String userId,
			@FormParam("platform") String platform,
			@FormParam("coinName") String coinName,
			@FormParam("askOrBid") String askOrBid,
			@FormParam("priceRule") String priceRule,
			@FormParam("askPrice") BigDecimal askPrice,
			@FormParam("qty") BigDecimal qty,
			@FormParam("ProfitRatio") BigDecimal ProfitRatio,
			@FormParam("topOrlow") BigDecimal topOrlow,
			@FormParam("strategyFlag") int strategyFlag,
			@FormParam("Price4Days") int Price4Days,
			@FormParam("isNext") String isNext) {
		Strategy strategy = new Strategy();
		strategy.setStrategyId(strategyId);
		strategy.setUserId(userId);
		strategy.setPlatform(platform.toLowerCase());
		strategy.setCoinName(coinName);
		strategy.setAskOrBid(askOrBid);
		strategy.setPriceRule(priceRule);
		strategy.setAskPrice(askPrice);
		strategy.setQty(qty);
		strategy.setProfitRatio(ProfitRatio);
		strategy.setTopOrlow(topOrlow);
		strategy.setStrategyFlag(strategyFlag);
		strategy.setPrice4Days(Price4Days);
		return new JSONWithPadding(new GenericEntity<StatusBean>(
				new strategyDao().smartSellTo(strategy, isNext)) {
		}, "smartSellTo");
	}

	// ��������

	@POST
	@Produces("application/json")
	@Path("/smartBuy")
	public JSONWithPadding smartbuy(@FormParam("strategyId") int strategyId,
			@FormParam("userId") String userId,
			@FormParam("platform") String platform,
			@FormParam("coinName") String coinName,
			@FormParam("askOrBid") String askOrBid,
			@FormParam("priceRule") String priceRule,
			@FormParam("BidPrice") BigDecimal BidPrice,
			@FormParam("qty") BigDecimal qty,
			@FormParam("topOrlow") BigDecimal topOrlow,
			@FormParam("strategyFlag") int strategyFlag,
			@FormParam("Price4Days") int Price4Days) {
		Strategy strategy = new Strategy();
		strategy.setStrategyId(strategyId);
		strategy.setUserId(userId);
		strategy.setPlatform(platform.toLowerCase());
		strategy.setCoinName(coinName);
		strategy.setAskOrBid(askOrBid);
		strategy.setPriceRule(priceRule);
		strategy.setBidPrice(BidPrice);
		strategy.setQty(qty);
		strategy.setTopOrlow(topOrlow);
		strategy.setStrategyFlag(strategyFlag);
		strategy.setPrice4Days(Price4Days);

		return new JSONWithPadding(new GenericEntity<StatusBean>(
				new strategyDao().smartBuy(strategy)) {
		}, "smartBuy");
	}
	
	
//买的
	@POST
	@Produces("application/json")
	@Path("/smartBuy")
	public JSONWithPadding smartbuy(@FormParam("strategyId") int strategyId,
			@FormParam("userId") String userId,
			@FormParam("platform") String platform,
			@FormParam("coinName") String coinName,
			@FormParam("askOrBid") String askOrBid,
			@FormParam("priceRule") String priceRule,
			@FormParam("BidPrice") BigDecimal BidPrice,
			@FormParam("qty") BigDecimal qty,
			@FormParam("topOrlow") BigDecimal topOrlow,
			@FormParam("strategyFlag") int strategyFlag,
			@FormParam("Price4Days") int Price4Days,
			@FormParam("isNext") String isNext) {
		Strategy strategy = new Strategy();
		strategy.setStrategyId(strategyId);
		strategy.setUserId(userId);
		strategy.setPlatform(platform.toLowerCase());
		strategy.setCoinName(coinName);
		strategy.setAskOrBid(askOrBid);
		strategy.setPriceRule(priceRule);
		strategy.setBidPrice(BidPrice);
		strategy.setQty(qty);
		strategy.setTopOrlow(topOrlow);
		strategy.setStrategyFlag(strategyFlag);
		strategy.setPrice4Days(Price4Days);
		return new JSONWithPadding(new GenericEntity<StatusBean>(
				new strategyDao().smartBuyTo(strategy, isNext)) {
		}, "smartBuyTo");
	}

	/**
	 * 获取我的订单信息
	 * */
	@GET
	@Produces("application/json")
	@Path("/getMyAllOpenOrder")
	public JSONWithPadding getMyAllOpenOrderList(
			@QueryParam("userid") String userid) {
		if(userid==null){
			return new JSONWithPadding("","getMyAllOpenOrder");
		}
		return new JSONWithPadding(
				new GenericEntity<PagableData<HistoryLimitOrder>>(
						new strategyDao().getHistoryLimitOrder(userid)) {
				}, "getMyAllOpenOrder");
	}

	// ticker
	@POST
	@Produces("application/json")
	@Path("/getticker")
	public JSONWithPadding getticker(@FormParam("platform") String platform,
			@FormParam("userid") String userid,
			@FormParam("coinname") String coinname) {
		if(userid==null || platform==null || coinname==null){
			return new JSONWithPadding("","getticker");
		}
		return new JSONWithPadding(new GenericEntity<PagableData<String>>(
				new strategyDao().getticker(userid, platform, coinname)) {
		}, "getticker");
	}

	@POST
	@Produces("application/json")
	@Path("/getuserbalance")
	public JSONWithPadding getuserbalance(
			@FormParam("platform") String platform,
			@FormParam("userid") String userid) {
		if(userid==null || platform==null ){
			return new JSONWithPadding("","getuserbalance");
		}
		return new JSONWithPadding(
				new GenericEntity<PagableData<AccountBalance>>(
						new strategyDao().getuserbalance(platform, userid)) {
				}, "getuserbalance");
	}

	// 查询历史订单
	@POST
	@Produces("application/json")
	@Path("/getTradeHistory")
	public JSONWithPadding getTradeHistory(@FormParam("userid") String userid,
			@FormParam("platform") String platform,
			@FormParam("apikey") String apikey,
			@FormParam("apisecret") String apisecret) {
		if(userid==null || platform==null ||apikey==null || apisecret==null){
			return new JSONWithPadding("","getTradeHistory");
		}
		return new JSONWithPadding(
				new GenericEntity<Map<String, List<UserTrade>>>(
						new strategyDao().getTradeHistory(userid, platform,
								apikey, apisecret)) {
				}, "getTradeHistory");
	}

	// 查询交易订单
	@POST
	@Produces("application/json")
	@Path("/getMyAllOpenOrderList")
	public JSONWithPadding getMyAllOpenOrderList(
			@FormParam("userid") String userid,
			@FormParam("platform") String platform,
			@FormParam("apikey") String apikey,
			@FormParam("apisecret") String apisecret) {
		if(userid==null || platform==null ||apikey==null || apisecret==null){
			return new JSONWithPadding("","getMyAllOpenOrderList");
		}
		return new JSONWithPadding(new GenericEntity<Map<String, List<Order>>>(
				new strategyDao().getMyAllOpenOrderList(userid, platform,
						apikey, apisecret)) {
		}, "getMyAllOpenOrderList");
	}
}
