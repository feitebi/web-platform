package com.bdh.db.rest;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;

import com.bdh.db.dao.AccountPhoneDao;
import com.bdh.db.dao.gooleAuthDao;
import com.bdh.db.entry.PagableData;
import com.bdh.db.entry.StatusBean;
import com.bdh.db.entry.setting;
import com.sun.jersey.api.json.JSONWithPadding;


@Path("/Account")
@Produces({ "application/x-javascript", "application/json", "application/xml" })
public class AccountPhoneRest {
	
	/**
	 * 手机通知    被关�?
	 * **/
	  @POST
	  @Produces("application/json")
	  @Path("/notifyMessage")
	  public JSONWithPadding NotifyMessage(@FormParam("fieldValue") String fieldValue,@FormParam("fieldName") String fieldName,@FormParam("userId") String userId){
	        	return new JSONWithPadding(new GenericEntity<StatusBean>(new AccountPhoneDao().notifyMssage(fieldName, fieldValue, userId)){
    	  },"notifyMessage");
       } 
	  
	  /**
		 * 手机通知    被关�?
		 * **/
		  @POST
		  @Produces("application/json")
		  @Path("/checke")
		  public JSONWithPadding Notifychecked(@FormParam("userId") String userId){
		        	return new JSONWithPadding(new GenericEntity<PagableData<setting>>(new AccountPhoneDao().getNotify(userId)){
	    	  },"checked");
	       } 
	  
	  
	  
	  
	  /**
		 * 获取二维�?
		 * **/
	  @GET
		@Produces("application/json")
		@Path("/google")
	public JSONWithPadding getgoogle(
										@QueryParam("userid") String userid){
		return new JSONWithPadding(new GenericEntity<setting>(new gooleAuthDao().getGooleAuth(userid)){
		},"getgoogle");
	}
        
	  /**
		 * 是否已启用GOOGLE验证
		 * **/
      	@POST
         @Produces("application/json")
         @Path("/isgoogle")
 	    public JSONWithPadding isgoogle(
 	    		@FormParam("userId") String userid,
 	    		@FormParam("code") String code){
         	return new JSONWithPadding(new GenericEntity<setting>(new gooleAuthDao().getisGooleAuth(userid,code)){
         	},"isgoogle");
         } 
      	/**
    	 * 页面打开获取Google状�??
    	 * **/
      	@POST
	           @Produces("application/json")
	           @Path("/redegoogle")
	   	    public JSONWithPadding redegoogle(
	   	    		@FormParam("userId") String userid){
	           	return new JSONWithPadding(new GenericEntity<setting>(new gooleAuthDao().getredegoogle(userid)){
	           	},"redegoogle");
	           }  
      	
      	/**
    	 * 取消启用
    	 * **/
      	@POST
	           @Produces("application/json")
	           @Path("/stopgoogle")
	   	    public JSONWithPadding stopgoogle(
	   	    		@FormParam("userId") String userid){
	           	return new JSONWithPadding(new GenericEntity<setting>(new gooleAuthDao().getstopgoogle(userid)){
	           	},"stopgoogle");
	           } 

}
