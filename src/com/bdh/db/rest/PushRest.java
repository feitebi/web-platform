package com.bdh.db.rest;

import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;

import com.bdh.db.dao.PushDao;
import com.bdh.db.entry.PagableData;
import com.bdh.db.entry.Push;
import com.bdh.db.entry.PushOrder;
import com.bdh.db.entry.StatusBean;
import com.sun.jersey.api.json.JSONWithPadding;

@Path("/Push")
@Produces({ "application/x-javascript", "application/json", "application/xml" })
public class PushRest {
	/**
	 * 挂单
	 * */
	    @POST
	    @Produces("application/json")
	    @Path("/PushPut")
	    public JSONWithPadding PushPut(@FormParam("userid") String userid,@FormParam("formAddr") String formAddr,
	    		@FormParam("formKey") String formKey,@FormParam("askPrice") String askPrice,@FormParam("leftqty") String leftqty){
	    	Push push= new Push();
	   push.setFormUserId(userid);
	   push.setFormAddr(formAddr);
	   push.setFormKey(formKey);
	   push.setAskPrice(askPrice);
	   push.setLeftqty(Double.parseDouble(leftqty));
	    	 return new JSONWithPadding(new GenericEntity<StatusBean>(new PushDao().PushPut(push)) {
	         }, "PushPut");
	    }
		/**
		 * 显示所有挂单
		 * **/
	    @GET
		@Produces("application/json")
		@Path("/PushAllList")
		public JSONWithPadding getPushAllList(@QueryParam("start") String start,@QueryParam("limit") String limit){
			return new JSONWithPadding(new GenericEntity<PagableData<Push>>(new PushDao().getPushAllList(start, limit)){
			},"getPushAllList");
		}
		/**
		 * 
		 * 显示我的挂单
		 * **/
	    @GET
		@Produces("application/json")
		@Path("/PushList")
		public JSONWithPadding getPushList(@QueryParam("start") String start,@QueryParam("limit") String limit,@QueryParam("userid") String userid){
			return new JSONWithPadding(new GenericEntity<PagableData<Push>>(new PushDao().getPushList(start, limit, userid)){
			},"getpushList");
		}
	    
	    /**
	     * 取消挂单
	     * */
	    @POST
	    @Produces("application/json")
	    @Path("/upPushOff")
	    public JSONWithPadding upPushOff(@FormParam("pid") String pid){
	    	 return new JSONWithPadding(new GenericEntity<StatusBean>(new PushDao().upPushOff(pid)) {
	         }, "upPushOff");
	    }
	    
	    
	    /***
		 * 点击购买
		 * */
	    
	    @POST
	    @Produces("application/json")
	    @Path("/BuyFill")
	    public JSONWithPadding BuyFill(@FormParam("userid") String userid,
	    		 					  @FormParam("price") String price,
	    		 					  @FormParam("num") String num,
	    		 					  @FormParam("commission") String commission,
	    		 					  @FormParam("leftqty") String leftqty,
	    		 					  @FormParam("qty") String qty,
	    		 					  @FormParam("pid") String pid,
	    		 					  @FormParam("formUserId") String formUserId
	    		 					 ){
	    	PushOrder buys= new PushOrder();
	    	buys.setPrice(Double.parseDouble(price));
	    	buys.setQty(Double.parseDouble(num));
	    	buys.setAmount(Double.parseDouble(price)*Double.parseDouble(num));
	    	buys.setCommission(Double.parseDouble(commission));
	    	 return new JSONWithPadding(new GenericEntity<StatusBean>(new PushDao().BuyFill(buys,userid,pid,formUserId,leftqty, qty)) {
	         }, "BuyFill");
	    }
	    
	    /**
		 * 根据id查询push数据
		 * **/
	    	@GET
	  		@Produces("application/json")
	  		@Path("/getIdList")
	  		public JSONWithPadding getIdList(@QueryParam("pid") String pid){
	  			return new JSONWithPadding(new GenericEntity<PagableData<Push>>(new PushDao().getIdList(pid)){
	  			},"getIdList");
	  		}
	    
	    
	    
}
