package com.bdh.db.rest;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import com.bdh.db.dao.SignDao;
import com.bdh.db.entry.StatusBean;
import com.sun.jersey.api.json.JSONWithPadding;

@Path("/sign")
@Produces({ "application/x-javascript", "application/json", "application/xml" })
public class signUpOrInRest {
	@POST
    @Produces("application/json")
    @Path("/signUP")
    public JSONWithPadding signup(@FormParam("accountName") String accountName,
						    		   @FormParam("phone") String phone,
						    		   @FormParam("code") String code,
						    		   @FormParam("pwd") String pwd){
    	 return new JSONWithPadding(new GenericEntity<StatusBean>(new SignDao().signUP(accountName,phone,code,pwd)) {
         }, "signUP");
    	 }
	
	@POST
    @Produces("application/json")
    @Path("/signIN")
    public JSONWithPadding signin(@FormParam("nameOrphone") String nameOrphone,
						    	  @FormParam("signinpwd") String signinpwd){
    	 return new JSONWithPadding(new GenericEntity<StatusBean>(new SignDao().signIN(nameOrphone,signinpwd)) {
         }, "signIN");
    	 }
	
	
	
}
