package com.bdh.db.rest;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;

import com.bdh.db.dao.AuditDao;
import com.bdh.db.entry.Fill;
import com.bdh.db.entry.PagableData;
import com.bdh.db.entry.Push;
import com.bdh.db.entry.StatusBean;
import com.bdh.db.entry.User;
import com.bdh.db.entry.Withdraw;
import com.bdh.db.view.MyBDH;
import com.bdh.db.view.MyUser;
import com.sun.jersey.api.json.JSONWithPadding;
@Path("/adminPush")
@Produces({ "application/x-javascript", "application/json", "application/xml" })
public class AuditRest {
	
	/**
	 * 提现列表   管理员查看
	 * 
	 * */
	        @GET
			@Produces("application/json")
			@Path("/getPushList")
			public JSONWithPadding getPushList(@QueryParam("start") String start,@QueryParam("limit") String limit){
				return new JSONWithPadding(new GenericEntity<PagableData<Withdraw>>(new AuditDao().getPushList(start, limit)){
				},"getPushList");
			}
	        
	        
   /***
    * 用户冻结列表   管理员查看
    * **/     
	        @GET
			@Produces("application/json")
			@Path("/PushForst")
			public JSONWithPadding PushForst(@QueryParam("start") String start,@QueryParam("limit") String limit){
				return new JSONWithPadding(new GenericEntity<PagableData<Push>>(new AuditDao().pushForst(start, limit)){
				},"PushForst");
			}   
	        
    
    /**
     * 显示余额
     * */
	        @GET
			@Produces("application/json")
			@Path("/getblance")
			public JSONWithPadding getblance(@QueryParam("userid") String userid){
				return new JSONWithPadding(new GenericEntity<PagableData<Fill>>(new AuditDao().getbalance(userid)){
				},"getblance");
			}

	/**
	 * 点击提现添加一条记录
	 * */
	        @POST
			@Produces("application/json")
			@Path("/addWithdraw")
			public JSONWithPadding addWithdraw(@FormParam("userid") String userid,@FormParam("amount") String amount,@FormParam("toAccount") String toAccount,
											   @FormParam("bankName") String bankName,@FormParam("commission") String commission){
	        	Withdraw withdraw= new Withdraw();
	        	withdraw.setAmount(Double.parseDouble(amount));
	        	withdraw.setToAccount(toAccount);
	        	withdraw.setBankName(bankName);
	        	withdraw.setCommission(Double.parseDouble(commission));
				return new JSONWithPadding(new GenericEntity<StatusBean>(new AuditDao().addWithdraw(withdraw, userid)){
				},"addWithdraw");
			} 
	        
	        
    /**
     *  管理员 查询打款列表
     * */
	        @GET
			@Produces("application/json")
			@Path("/getWithdrawAll")
			public JSONWithPadding getWithdrawAll(@QueryParam("start") String start,@QueryParam("limit") String limit){
				return new JSONWithPadding(new GenericEntity<PagableData<Withdraw>>(new AuditDao().getWithdrawAll(start, limit)){
				},"getWithdrawAll");
			}	
	        
	        
    /**
     * 管理员确认打款
     * */
	        @POST
			@Produces("application/json")
			@Path("/OkWithdraw")
			public JSONWithPadding OkWithdraw(@FormParam("adminid") String adminid,@FormParam("confirmAmount") String confirmAmount,
											   @FormParam("txId") String txId,@FormParam("adminContext") String adminContext,
											   @FormParam("wid") String wid){
	        	
	        	Withdraw withdraw= new Withdraw();
	        	withdraw.setConfirmAmount(Double.parseDouble(confirmAmount));
	        	withdraw.setTxId(txId);
	        	withdraw.setAdminContext(adminContext);
	        	withdraw.setWid(wid);
				return new JSONWithPadding(new GenericEntity<StatusBean>(new AuditDao().OkWithdraw(withdraw, adminid)){
				},"OkWithdraw");
			} 
	        
    /**
     * 管理员拒绝打款
     * */
	        @POST
 			@Produces("application/json")
 			@Path("/OffWithdraw")
 			public JSONWithPadding OffWithdraw(@FormParam("adminid") String adminid,@FormParam("adminContext") String adminContext,
 											   @FormParam("wid") String wid){
 	        	Withdraw withdraw= new Withdraw();
 	        	withdraw.setAdminContext(adminContext);
 	        	withdraw.setWid(wid);
 				return new JSONWithPadding(new GenericEntity<StatusBean>(new AuditDao().OffWithdraw(withdraw, adminid)){
 				},"OffWithdraw");
 			} 
	        
	        
	/***
	 * 
	 * 申请中
	 * */
	        @GET
 			@Produces("application/json")
 			@Path("/applying")
 			public JSONWithPadding applying(@QueryParam("start") String start,@QueryParam("limit") String limit){
 				return new JSONWithPadding(new GenericEntity<PagableData<Withdraw>>(new AuditDao().applying(start, limit)){
 				},"applying");
 			}
    /**
     * 已完成
     * */
	        @GET
 			@Produces("application/json")
 			@Path("/okapply")
 			public JSONWithPadding okapply(@QueryParam("start") String start,@QueryParam("limit") String limit){
 				return new JSONWithPadding(new GenericEntity<PagableData<Withdraw>>(new AuditDao().okapply(start, limit)){
 				},"okapply");
 			} 
	        
	        
	        
/**
 * 总额资产
 * */
	        @GET
 			@Produces("application/json")
 			@Path("/getUserBalance")
 			public JSONWithPadding getUserBalance( @QueryParam("userid") String userid){
	        	
 				return new JSONWithPadding(new GenericEntity<MyUser>(new AuditDao().getUserBalance(userid)){
 				},"getUserBalance");
 			} 

/**
 * 剩余总额资产
 * **/
	        @GET
 			@Produces("application/json")
 			@Path("/getUserBalanceMoney")
 			public JSONWithPadding getUserBalanceMoney( @QueryParam("userid") String userid){
	        	
 				return new JSONWithPadding(new GenericEntity<PagableData<User>>(new AuditDao().getUserBalanceMoney(userid)){
 				},"getUserBalanceMoney");
 			}    
	        
	        
	        
/**
 * 用户冻结BDH
 * **/
        @GET
		@Produces("application/json")
		@Path("/getfreezing")
		public JSONWithPadding getfreezing(@QueryParam("userid") String userid){
			return new JSONWithPadding(new GenericEntity<MyBDH>(new AuditDao().getfreezing(userid)){
			},"getfreezing");
		} 
        
/**
 * 剩余冻结BDH
 * *//*      
        @GET
		@Produces("application/json")
		@Path("/getsellorder")
		public JSONWithPadding getsellorder(@QueryParam("userid") String userid){
			return new JSONWithPadding(new GenericEntity<PagableData<User>>(new AuditDao().getsellorder(userid)){
			},"getsellorder");
		}*/ 
	      //取消提现
        @POST
			@Produces("application/json")
			@Path("/isApply")
			public JSONWithPadding isApply(  @FormParam("wid") String wid){
				return new JSONWithPadding(new GenericEntity<StatusBean>(new AuditDao().isApply(wid)){
				},"isApply");
			} 

        
}
