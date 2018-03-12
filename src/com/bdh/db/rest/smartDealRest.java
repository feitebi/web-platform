/*package com.bdh.db.rest;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;

import com.bdh.db.dao.smartDealDao;
import com.bdh.db.entry.PagableData;
import com.bdh.db.entry.smartOrder;
import com.sun.jersey.api.json.JSONWithPadding;

@Path("/smart")
@Produces({ "application/x-javascript", "application/json", "application/xml" })
public class smartDealRest {

	@POST
	@Produces("application/json")
	@Path("/getsmart")
    public JSONWithPadding getsmart(@FormParam("userid") String userid){
    	return new JSONWithPadding(new GenericEntity<PagableData<smartOrder>>(new smartDealDao().getsmartorder(userid)){
    	},"getsmart");
    }
	
	
}
*/