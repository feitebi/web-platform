package com.bdh.db.rest;

import java.math.BigDecimal;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;

import com.bdh.db.dao.SnsDao;
import com.bdh.db.entry.PagableData;
import com.bdh.db.entry.StatusBean;
import com.bdh.db.entry.setting;
import com.sun.jersey.api.json.JSONWithPadding;

@Path("/snsNetworking")
@Produces({ "application/x-javascript", "application/json", "application/xml" })
public class SnsRest {
	/**查询是否打开
	 * */
        @GET
		@Produces("application/json")
		@Path("/getfollowlist")
	public JSONWithPadding getsnsNetworking(@QueryParam("userId") String userId){
		return new JSONWithPadding(new GenericEntity<PagableData<setting>>(new SnsDao().getsetting(userId)){
		},"getsnsNetworking");
        }
        
        
        /**
         * 社交
         * */ 
      @POST
      @Produces("application/json")
      @Path("/updateopen")
      public JSONWithPadding upopen(@FormParam("userId") String userId){
            	return new JSONWithPadding(new GenericEntity<setting>(new SnsDao().updateOpen(userId)){
            	},"updateopen");
            } 

     /**
  	 * 共享挂单或智能投资策�? 关闭
  	 * */
      @POST
      @Produces("application/json")
      @Path("/udpateSnsAssetsclose")
      public JSONWithPadding udpateSnsAsset(@FormParam("userId") String userId){
            	return new JSONWithPadding(new GenericEntity<setting>(new SnsDao().updateSnsAssets(userId)){
            	},"udpateSnsAssetsclose");
            } 
      
      /**
   	 * 匿名共享资产以及盈利状�??  关闭  默认�?�?
   	 * */
       @POST
       @Produces("application/json")
       @Path("/updatesnsStrategyclose")
       public JSONWithPadding updateSnsStrategy(@FormParam("userId") String userId){
             	return new JSONWithPadding(new GenericEntity<setting>(new SnsDao().updateSnsStrategy(userId)){
             	},"updatesnsStrategyClose");
             } 

        
 	/**
 	 * 修改查看价格
 	 * */
     @POST
     @Produces("application/json")
     @Path("/updateSnsStrategyopen")
     public JSONWithPadding updateSnsStrategyopen(@FormParam("sns4Price") BigDecimal sns4Price,@FormParam("userId") String userId){
    	setting setting= new setting();
    	setting.setSns4Price(sns4Price);
	     return new JSONWithPadding(new GenericEntity<StatusBean>(new SnsDao().updateSnsPrice(setting, userId)){
           	},"updateSnsStrategyOpen");
           } 
     
}
