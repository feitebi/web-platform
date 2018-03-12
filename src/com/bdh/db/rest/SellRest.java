package com.bdh.db.rest;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;

import com.bdh.db.dao.SellDao;
import com.bdh.db.entry.StatusBean;
import com.bdh.db.entry.sell;
import com.sun.jersey.api.json.JSONWithPadding;

@Path("/sell")
@Produces({ "application/x-javascript", "application/json", "application/xml" })
public class SellRest {
	@POST
    @Produces("application/json")
    @Path("/add")
    public JSONWithPadding add(@FormParam("userId") String userId,
					    		@FormParam("exchange") String exchange,
					    		@FormParam("surplus") String surplus,
								@FormParam("price") String price,
								@FormParam("high") String high,
								@FormParam("num") String num,
								@FormParam("sum") String sum){
    		sell sell=new sell();
    		sell.setUserId(userId);
    		sell.setExchange(exchange);
    		sell.setSurplus(surplus);
    		sell.setPrice(price);
    		sell.setHigh(high);
    		sell.setNum(num);
    		sell.setSum(sum);
    	 return new JSONWithPadding(new GenericEntity<StatusBean>(new SellDao().addsell(sell)) {
         }, "add");
    	 }
}
