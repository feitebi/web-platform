package com.bdh.db.rest;


import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;

import com.bdh.db.dao.BalanceDao;
import com.bdh.db.dao.indexDao;
import com.bdh.db.entry.Account;
import com.bdh.db.entry.Bookmark;
import com.bdh.db.entry.Exchang;
import com.bdh.db.entry.NewOpen;
import com.bdh.db.entry.PagableData;
import com.bdh.db.entry.StatusBean;
import com.bdh.db.entry.Strategy;
import com.bdh.db.entry.event;
import com.bdh.db.entry.indexMyfollowing;
import com.bdh.db.view.Fansnumber;
import com.sun.jersey.api.json.JSONWithPadding;

@Path("/index")
@Produces({ "application/x-javascript", "application/json", "application/xml" })
public class indexRest {
	/***
	 * api
	 * **/
	@GET
	@Produces("application/json")
	@Path("/platform_api")
	public JSONWithPadding queryApis(@QueryParam("userId") String userId,@QueryParam("selectAll") String selectAll)
			{
		if(userId==null || userId.equals("")){
			return new JSONWithPadding("", "platform_api");
		}
		return new JSONWithPadding(new GenericEntity<PagableData<Exchang>>(
				new BalanceDao().queryApis(userId,selectAll)) {
		}, "platform_api");
	}
	/**
	 * 新开盘的价格和交易量
	 * **/
	@GET
	@Produces("application/json")
	@Path("/newopenLast")
	public JSONWithPadding newopenLast(@QueryParam("userid") String userid,
			@QueryParam("exchangName") String exchangName,
			@QueryParam("coin") String coin)
			{
		if(userid==null || userid.equals("") || exchangName==null || exchangName.equals("") || coin==null || coin.equals("")){
			return new JSONWithPadding("", "newopenLast");
		}
		return new JSONWithPadding(new GenericEntity<List<NewOpen>>(
				new BalanceDao().newopen(userid, exchangName, coin)) {
		}, "newopenLast");
	}

	@GET
	@Produces("application/json")
	@Path("/getevent")
	public JSONWithPadding getevent(){
		return new JSONWithPadding(new GenericEntity<PagableData<event>>(
				new indexDao().getevent()) {
		}, "getevent");
	}

	/**
	 * balance数据
	 * **/
	@POST
	@Produces("application/json")
	@Path("/platform_getPlatformSummary")
	public JSONWithPadding getPlatformSummary(
			@FormParam("userId") String userId,
			@FormParam("platformindex") String platformindex,
			@FormParam("apikey") String apikey,
			@FormParam("apisecret") String apisecret){
		if(userId==null || userId.equals("") 
				|| platformindex==null || platformindex.equals("") 
				|| apikey==null || apikey.equals("") 
				|| apisecret==null || apisecret.equals("")){
			return new JSONWithPadding("","getPlatformSummary");
		}
		return new JSONWithPadding(new GenericEntity<PagableData<Account>>(
				new BalanceDao().getPlatformSummary(userId, platformindex,
						apikey, apisecret)) {
		}, "getPlatformSummary");
	}

	/**
	 * 显示我关注的币
	 * **/
	@POST
	@Produces("application/json")
	@Path("/MyBookmark")
	public JSONWithPadding MyBookmark(@FormParam("userid") String userid,
			@FormParam("limit") String limit,
			@FormParam("start") String start) {
		if(userid==null || userid.equals("")){
			return new JSONWithPadding("","MyBookmark");
		}
		return new JSONWithPadding(new GenericEntity<PagableData<Bookmark>>(
				new BalanceDao().MyBookmark(userid, limit, start)) {
		}, "MyBookmark");
	}
	/***
	 * 获取该货币的讯息
	 */
	@POST
	@Produces("application/json")
	@Path("/getBookMarkCoin")
	public JSONWithPadding getBookMarkCoin(@FormParam("userid") String userid,
			@FormParam("platfrom") String platfrom,
			@FormParam("coin") String coin) {
		if(userid==null || userid.equals("") || platfrom==null || platfrom.equals("") || coin==null || coin.equals("")){
			System.out.println(1);
			return  new JSONWithPadding("","getBookMarkCoin");
		}
		System.out.println(2);
		return new JSONWithPadding(new GenericEntity<PagableData<Bookmark>>(
				new BalanceDao().getBookMarkCoin(userid, platfrom, coin)) {
		}, "getBookMarkCoin");
	}
	// 显示智能合约

	@POST
	@Produces("application/json")
	@Path("/smartdeal")
	public JSONWithPadding smartdeal(@FormParam("userid") String userid) {
		if(userid==null || userid.equals("")){
			return new JSONWithPadding("", "smartdeal");
		}
		return new JSONWithPadding(new GenericEntity<PagableData<Strategy>>(
				new indexDao().smartDeal(userid)) {
		}, "smartdeal");
	}

	/**
	 * 查询买卖
	 * */
	@POST
	@Produces("application/json")
	@Path("/delsmart")
	public JSONWithPadding delsmart(@FormParam("id") String id) {
		if(id==null || id.equals("")){
			return new JSONWithPadding("","delsmart");
		}
		return new JSONWithPadding(new GenericEntity<StatusBean>(
				new indexDao().delsmart(id)) {
		}, "delsmart");
	}

	// 获取关注大师
	/*
	 * @GET
	 * 
	 * @Produces("application/json")
	 * 
	 * @Path("/getMymaster") public JSONWithPadding
	 * getMymaster(@QueryParam("userid") String userid) throws Exception{ return
	 * new JSONWithPadding(new GenericEntity<List<indexMyfollowing>>(new
	 * BalanceDao().getMymaster(userid)){ },"getMymaster"); }
	 */
	@GET
	@Produces("application/json")
	@Path("/getMymaster")
	public JSONWithPadding fansnumber(@QueryParam("userid") String userid,
			@QueryParam("start") String start,
			@QueryParam("limit") String limit)
			{
		if(userid==null || userid.equals("")){
			return new JSONWithPadding("", "getMymaster");
		}
		return new JSONWithPadding(new GenericEntity<PagableData<Fansnumber>>(
				new BalanceDao().fansnumber(userid, start, limit)) {
		}, "getMymaster");
	}

	/**
	 * 获取我的订单信息
	 * */


}
