package com.ex.db.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;

import com.bdh.db.entry.Following;
import com.bdh.db.dao.BalanceDao;
import com.bdh.db.dao.indexDao;
import com.bdh.db.entry.Account;
import com.bdh.db.entry.AccountBalance;
import com.bdh.db.entry.PagableData;
import com.bdh.db.entry.StatusBean;
import com.bdh.db.entry.Strategy;
import com.sun.jersey.api.json.JSONWithPadding;

@Path("/index")
@Produces({ "application/x-javascript", "application/json", "application/xml" })
public class indexRest {
	
	

	/* 	@GET
		@Produces("application/json")
		@Path("/getblance")
	    public JSONWithPadding getblance(@QueryParam("userid") String userid) throws Exception{
	    	return new JSONWithPadding(new GenericEntity<PagableData<Account>>(new BalanceDao().getAccount(userid)){
	    	},"getblance");
	    }*/
	
	 	
	/*	//��ȡ��ʾ����
		@GET
		@Produces("application/json")
		@Path("/getAccountSummary")
	    public JSONWithPadding getAccountSummary(@QueryParam("userid") String userid) throws Exception{
	    	return new JSONWithPadding(new GenericEntity<PagableData<Account>>(new BalanceDao().getAccountSummary(userid)){
	    	},"getAccountSummary");
	    }*/
	/**
	 *��ʾ���ܺ�Լ
	 * */	
	    @POST
		@Produces("application/json")
		@Path("/smartdeal")
	    public JSONWithPadding smartdeal(@FormParam("userid") String userid){
	    	return new JSONWithPadding(new GenericEntity<PagableData<Strategy>>(new indexDao().smartDeal(userid)){
	    	},"smartdeal");
	    }
	    /**
		 *查询买卖
		 * */	
		    @POST
			@Produces("application/json")
			@Path("/delsmart")
		    public JSONWithPadding delsmart(@FormParam("id") String id){
		    	return new JSONWithPadding(new GenericEntity<StatusBean>(new indexDao().delsmart(id)){
		    	},"delsmart");
		    }
		    //��ע�Ĵ�ʦ
		  
}
