package com.bdh.db.rest;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;

import com.bdh.db.dao.Apisdao;
import com.bdh.db.dao.TopupDao;
import com.bdh.db.entry.Exchang;
import com.bdh.db.entry.Fill;
import com.bdh.db.entry.PagableData;
import com.bdh.db.entry.StatusBean;
import com.sun.jersey.api.json.JSONWithPadding;
@Path("/topup")
@Produces({ "application/x-javascript", "application/json", "application/xml" })
public class TopupRest {
	
	//充值中心

    @POST
    @Produces("application/json")
    @Path("/Voucher")
    public JSONWithPadding Voucher(@FormParam("userid") String userid,
    								@FormParam("amount") String amount,
    		                       @FormParam("rechargeCode") String rechargeCode){
    	Fill fill= new Fill();
		fill.setAmount(Double.parseDouble(amount));
		fill.setRechargeCode(rechargeCode);
    	 return new JSONWithPadding(new GenericEntity<StatusBean>(new TopupDao().Voucher(fill, userid)) {
         }, "Voucher");
    	 }
    
    /**
     *  管理员界面   查询充值表
     * */
        @GET
  		@Produces("application/json")
  		@Path("/AllFillList")
  	    public JSONWithPadding AllFillList(@QueryParam("name") String name,@QueryParam("rechargeCode") String rechargeCode,
  	    		@QueryParam("amount") String amount){
      
        	Fill fill= new Fill();
    		fill.setName(name);
    		fill.setRechargeCode(rechargeCode);
    	  	if(!amount.isEmpty()){
    		fill.setAmount(Double.parseDouble(amount));
        	}
  	    	return new JSONWithPadding(new GenericEntity<PagableData<Fill>>(new TopupDao().AllFillList(fill)){
  	    	},"AllFillList");
  	    }

    
	//管理员操作    确认充值
    @POST
    @Produces("application/json")
    @Path("/OKVoucher")
    public JSONWithPadding OKVoucher(@FormParam("id") String id,
						    		 @FormParam("userid") String userid,
						    		 @FormParam("adminId") String adminId,
						    		 @FormParam("admin") String admin,
						    		 @FormParam("amount") String amount,
						    		 @FormParam("serial") String serial){
    	Fill fill= new Fill();
		fill.setId(id);
		fill.setUserId(userid);
		fill.setAdminId(adminId);
		fill.setAdmin(admin);
		fill.setAmount(Double.parseDouble(amount));
		fill.setSerial(serial);
    	 return new JSONWithPadding(new GenericEntity<StatusBean>(new TopupDao().OKVoucher(fill)) {
         }, "OKVoucher");
    	 }
}
