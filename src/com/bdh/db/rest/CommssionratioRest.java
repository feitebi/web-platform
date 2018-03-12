package com.bdh.db.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;

import com.bdh.db.dao.CommssionratioDao;
import com.bdh.db.entry.Commssionratio;
import com.bdh.db.entry.PagableData;
import com.sun.jersey.api.json.JSONWithPadding;

@Path("/Commssionratio")
@Produces({ "application/x-javascript", "application/json", "application/xml" })
public class CommssionratioRest {
	
	/**
	 * 查询手续费
	 * 
	 * */
	    @GET
  		@Produces("application/json")
  		@Path("/Commssionratio")
  	public JSONWithPadding getCommssionratio(){
  		return new JSONWithPadding(new GenericEntity<PagableData<Commssionratio>>(new CommssionratioDao().AllCommssionratio()){
  		},"Commssionratio");
  	}
}
