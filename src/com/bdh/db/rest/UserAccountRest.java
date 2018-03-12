package com.bdh.db.rest;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;

import com.bdh.db.dao.UserAccountDao;
import com.bdh.db.entry.PagableData;
import com.bdh.db.entry.StatusBean;
import com.bdh.db.entry.User;
import com.sun.jersey.api.json.JSONWithPadding;

@Path("/UserAccountRest")
@Produces({ "application/x-javascript", "application/json", "application/xml" })
public class UserAccountRest {
	
	
	/**
	 * 用户查询
	 * */
    @GET
    @Produces("application/json")
    @Path("/userlist")
    public JSONWithPadding getuserlist(@QueryParam("userid") String userid) {
        return new JSONWithPadding(
                new GenericEntity<PagableData<User>>(new UserAccountDao().getUserList(userid)) {
                }, "userlist");
    }
	
	/**
	 * 发�?�验证码 
	 * */
	    @POST
	    @Produces("application/json")
	    @Path("/sendCode")
	    public JSONWithPadding sendCode(@FormParam("userid") String userid,@FormParam("phone") String phone){
	    	 
	    	 return new JSONWithPadding(new GenericEntity<StatusBean>(new UserAccountDao().sendCode(userid, phone)) {
	         }, "sendcode");
	    	 }
	    
		/**
		 * 修改手机�?
		 * **/
	    /*@POST
	    @Produces("application/json")
	    @Path("/updatePhoneNumber")
	    public JSONWithPadding updatePhoneNumber(@FormParam("userid") String userid,@FormParam("code") String code){
	    	 return new JSONWithPadding(new GenericEntity<StatusBean>(new UserAccountDao().updatePhoneNumber(userid, code)) {
	         }, "updatePhoneNumber");
	    	 }*/
	    
	    @POST
	    @Produces("application/json")
	    @Path("/updatePhone")
	    public JSONWithPadding updatePhone(@FormParam("userid") String userid,@FormParam("phone") String phone,
	    		@FormParam("code") String code){
	    	User user=new User();
	    	user.setUserId(userid);
	    	user.setPhone(phone);
	    	 return new JSONWithPadding(new GenericEntity<StatusBean>(new UserAccountDao().updatePhone(userid, phone,code)) {
	         }, "updatePhone");
	    	 }
	    @POST
	    @Produces("application/json")
	    @Path("/updatePwd")
	    public JSONWithPadding updatePwd(@FormParam("userid") String userid,@FormParam("code") String code,
	    		@FormParam("newPwd") String newPwd){
	    	 return new JSONWithPadding(new GenericEntity<StatusBean>(new UserAccountDao().updatePwd(userid, newPwd,code)) {
	         }, "updatePwd");
	    	 }
	    
}
