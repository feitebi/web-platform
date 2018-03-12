package com.bdh.db.rest;

import java.math.BigDecimal;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;

import com.bdh.db.dao.NewOpenDao;
import com.bdh.db.entry.NewOpen;
import com.bdh.db.entry.PagableData;
import com.bdh.db.entry.StatusBean;
import com.sun.jersey.api.json.JSONWithPadding;

@Path("/NewOpen")
@Produces({ "application/x-javascript", "application/json", "application/xml" })
public class NewOpenRest {
	        @POST
			@Produces("application/json")
			@Path("/NewOpenList")
		    public JSONWithPadding getNewOpenList(@FormParam("start") String start,@FormParam("limit") String limit){
		    	return new JSONWithPadding(new GenericEntity<PagableData<NewOpen>>(new NewOpenDao().getNewOpenList(start, limit)){
		    	},"newOpenList");
		    }
	        
	        @POST
	        @Produces("application/json")
	        @Path("/addNewOpen")
	        public JSONWithPadding NewOpenadd(@FormParam("name") String name,@FormParam("kaiPrice") String kaiPrice,
	        		@FormParam("nowPrice") String nowPrice,@FormParam("platform") String platform,@FormParam("time") String time){
	        	  NewOpen open= new NewOpen();
	              open.setName(name);
	              open.setKaiPrice(kaiPrice);
	              open.setNowPrice(nowPrice);
	              open.setPlatform(platform);
	              open.setTime(time);
	        	 return new JSONWithPadding(new GenericEntity<StatusBean>(new NewOpenDao().addNewopen(open)) {
	             }, "addNewOpen");
	        	 }
	        
}
