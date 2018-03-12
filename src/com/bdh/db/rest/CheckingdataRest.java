package com.bdh.db.rest;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;

import com.bdh.db.dao.CheckingdataDao;
import com.bdh.db.entry.Checkingdata;
import com.bdh.db.entry.PagableData;
import com.bdh.db.entry.StatusBean;
import com.sun.jersey.api.json.JSONWithPadding;

@Path("/exchang/adminPush")
@Produces({ "application/x-javascript", "application/json", "application/xml" })
public class CheckingdataRest {
	/***
	 * 
	 * 查询支付宝数据
	 * */
		@GET
		@Produces("application/json")
		@Path("/AllaccountList")
	    public JSONWithPadding AllaccountList(@QueryParam("start") String start,@QueryParam("limit") String limit){
	    	return new JSONWithPadding(new GenericEntity<PagableData<Checkingdata>>(new CheckingdataDao().AllaccountList(start, limit)){
	    	},"AllaccountList");
	    }
	
	/**
	 * 支付宝导入数据
	 * */
	    @POST
		@Produces("application/json")
		@Path("/saveCurrentaccount")
	    public JSONWithPadding saveCurrentaccount(@FormParam("userName") String userName,
	    										  @FormParam("phone") String phone,
	    										  @FormParam("tranNumber") String tranNumber,		
									    		  @FormParam("tranTime") String tranTime,
				                                  @FormParam("paymentTime") String paymentTime,
									    		  @FormParam("bankAccount") String bankAccount,
				                                  @FormParam("amount") String amount,
				                                  @FormParam("comment") String comment,        
				                                  @FormParam("adminName") String adminName){
	    	Checkingdata account= new Checkingdata();
	    	account.setUserName(userName);
	    	account.setPhone(phone);
	    	account.setTranNumber(tranNumber);
	    	account.setTranTime(tranTime);
	    	account.setPaymentTime(paymentTime);
	    	account.setBankAccount(bankAccount);
	    	account.setAmount(amount);
	    	account.setComment(comment);
	    	account.setAdminName(adminName);
         return new JSONWithPadding(new GenericEntity<StatusBean>(new CheckingdataDao().saveCurrentaccount(account)){
          },"saveCurrentaccount");
        } 

}
