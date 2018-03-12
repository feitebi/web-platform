package com.bdh.db.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;

import com.bdh.db.dao.ExchangDao;
import com.bdh.db.entry.Exchang;
import com.bdh.db.entry.PagableData;
import com.sun.jersey.api.json.JSONWithPadding;

@Path("/exchang")
@Produces({ "application/x-javascript", "application/json", "application/xml" })
public class ExchangRest {
	/**
	 * 查询交易所
	 * */
	@GET
	@Produces("application/json")
	@Path("/list")
	public JSONWithPadding getEXchangList(){
		return new JSONWithPadding(new GenericEntity<PagableData<Exchang>>(new ExchangDao().getExchang()){
		},"list");
	}
		
	
	
	
	

}
