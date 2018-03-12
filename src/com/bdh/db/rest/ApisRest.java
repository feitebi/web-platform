package com.bdh.db.rest;


import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;

import com.bdh.db.dao.Apisdao;
import com.bdh.db.entry.Apis;
import com.bdh.db.entry.Exchang;
import com.bdh.db.entry.PagableData;
import com.bdh.db.entry.StatusBean;
import com.sun.jersey.api.json.JSONWithPadding;



@Path("/apis")
@Produces({ "application/x-javascript", "application/json", "application/xml" })
public class ApisRest {
	/**
	 *查询交易�?
	 * */	
	  /*  @GET
		@Produces("application/json")
		@Path("/exchanglist")
	    public JSONWithPadding getExchangApisList(@QueryParam("userid") String userid){
	    	return new JSONWithPadding(new GenericEntity<PagableData<Exchang>>(new Apisdao().getExchangApisList(userid)){
	    	},"exchangApisList");
	    }*/
	    
	    @GET
	  		@Produces("application/json")
	  		@Path("/exchanglist")
	  	    public JSONWithPadding getApisList(@QueryParam("userid") String userid){
	  	    	return new JSONWithPadding(new GenericEntity<PagableData<Exchang>>(new Apisdao().getApisList(userid)){
	  	    	},"exchangApisList");
	  	    }
	  
/**
 * 查询用户拥有的api
 * */
	    @GET
	    @Produces("application/json")
	    @Path("/apislist")
	    public JSONWithPadding getUserApis(@QueryParam("userid") String userid) {
	        return new JSONWithPadding(
	                new GenericEntity<PagableData<Apis>>(new Apisdao().getApisUserid(userid)) {
	                }, "apislist");
	    }
	   
	
/**
 * 锟斤拷询锟斤拷锟斤拷
 * */
    @GET
    @Produces("application/json")
    @Path("/list")
    public JSONWithPadding getApisList(@QueryParam("userid") String userid, @QueryParam("start") String start,
            @QueryParam("limit") String limit) {
        return new JSONWithPadding(
                new GenericEntity<PagableData<Apis>>(new Apisdao().getApis(userid, start, limit)) {
                }, "list");
    }
    
    
   
    
 
    /**
     *  添加api
     * */
    @POST
    @Produces("application/json")
    @Path("/addapis")
    public JSONWithPadding Userapisadd(@FormParam("userid") String userid,@FormParam("exchangid") String exchangid,
    		@FormParam("apikey") String apikey,@FormParam("apisecret") String apisecret){
    	Apis apis= new Apis();
    	apis.setUserid(userid);
    	apis.setExchangid(Integer.valueOf(exchangid));
    	apis.setApiKey(apikey);
        apis.setApiSecret(apisecret);
    	 return new JSONWithPadding(new GenericEntity<StatusBean>(new Apisdao().addApis(apis)) {
         }, "apisadd");
    	 }
  
    /**
     * 修改api
     * */
    @POST
    @Produces("application/json")
    @Path("/update")
   public JSONWithPadding updateApis(
		   @FormParam("userId") String userId,
		   @FormParam("exchangid") String exchangid,
		   @FormParam("apikey") String apikey,
		   @FormParam("apisecret") String apisecret){
    	Apis apis= new Apis();
    	apis.setUserid(userId);
    	apis.setExchangid(Integer.valueOf(exchangid));
    	apis.setApiKey(apikey);
        apis.setApiSecret(apisecret);
        
    	return new JSONWithPadding(new GenericEntity<StatusBean>(new Apisdao().upadteapis(apis, userId, exchangid)) {
    	},"Apisupdate");
    }
    
    /**
     * �?启访�? or 暂停访问
     * */
    @POST
    @Produces("application/json")
    @Path("/isflag")
    public JSONWithPadding updateflag(@FormParam("userId") String userId,@FormParam("exchangid") String exchangid){
    	 return new JSONWithPadding(new GenericEntity<StatusBean>(new Apisdao().updateflag(userId, exchangid)) {
         }, "isflag");
    	 }
    /**
     * 删除api
     * */
    @POST
    @Produces("application/json")
    @Path("/upadteIsEnable")
    public JSONWithPadding upadteIsEnable(@FormParam("userId") String userId,@FormParam("exchangid") String exchangid){
    	 return new JSONWithPadding(new GenericEntity<StatusBean>(new Apisdao().upadteIsEnable(userId, exchangid)) {
     }, "upadteIsEnable");
    	 }
}
