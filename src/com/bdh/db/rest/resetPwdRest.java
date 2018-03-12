package com.bdh.db.rest;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;

import com.bdh.db.dao.resetPwdDao;
import com.bdh.db.entry.StatusBean;
import com.sun.jersey.api.json.JSONWithPadding;


@Path("/resetPwd")
@Produces({ "application/x-javascript", "application/json", "application/xml" })
public class resetPwdRest {
	 	@POST
	    @Produces("application/json")
	    @Path("/resetPwdCode")
	    public JSONWithPadding sendCode(@FormParam("userid") String userid,@FormParam("phone") String phone){
	    	 return new JSONWithPadding(new GenericEntity<StatusBean>(new resetPwdDao().sendResetCode(userid, phone)) {
	        }, "resetPwdCode");
	    }
	 	@POST
	    @Produces("application/json")
	    @Path("/resetPwd")
	    public JSONWithPadding sendCode(@FormParam("userid") String userid,
	    								@FormParam("code") String code,
	    								@FormParam("newPwd") String newPwd){
	    	 return new JSONWithPadding(new GenericEntity<StatusBean>(new resetPwdDao().resetPwd(userid, code, newPwd)) {
	        }, "resetPwd");
	    }
	
}
