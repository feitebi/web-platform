package com.bdh.db.rest;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;

import com.bdh.db.dao.BuyDao;
import com.bdh.db.entry.Buy;
import com.bdh.db.entry.PagableData;
import com.bdh.db.entry.StatusBean;
import com.sun.jersey.api.json.JSONWithPadding;


@Path("/buy")
@Produces({ "application/x-javascript", "application/json", "application/xml" })
public class BuyRest {

	@POST
    @Produces("application/json")
    @Path("/add")
    public JSONWithPadding add(@FormParam("userId") String userId,
					    		@FormParam("exchange") String exchange,
					    		@FormParam("surplus") String surplus,
								@FormParam("under") String under,
								@FormParam("buy") String buy,
								@FormParam("num") String num,
								@FormParam("sum") String sum){
    		Buy b=new Buy();
    		b.setUserId(userId);
    		b.setExchange(exchange);
    		b.setSurplus(surplus);
    		b.setUnder(under);
    		b.setBuy(buy);
    		b.setNum(num);
    		b.setSum(sum);
    	 return new JSONWithPadding(new GenericEntity<StatusBean>(new BuyDao().addbuy(b)) {
         }, "add");
    	 }
    
	@POST
    @Produces("application/json")
    @Path("/selectall")
    public JSONWithPadding getBuyList(@QueryParam("userId") String userId) {
        return new JSONWithPadding(
                new GenericEntity<PagableData<Buy>>(new BuyDao().getbuy(userId)) {
                }, "list");
    }
	
	@POST
    @Produces("application/json")
    @Path("/del")
    public JSONWithPadding delbuy(@QueryParam("buyID") String buyID) {
        return new JSONWithPadding(
                new GenericEntity<StatusBean>(new BuyDao().delbuy(buyID)) {
                }, "list");
    }
	
}
