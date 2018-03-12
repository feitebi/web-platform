package com.bdh.db.rest;

import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;

import com.bdh.db.dao.DaShiDao;
import com.bdh.db.entry.Following;
import com.bdh.db.entry.Master;
import com.bdh.db.entry.PagableData;
import com.bdh.db.entry.StatusBean;
import com.sun.jersey.api.json.JSONWithPadding;

@Path("/DaShiView")
@Produces({ "application/x-javascript", "application/json", "application/xml" })
public class DaShiRest {
/**
 * 添加关注大师
 * */
         @POST
         @Produces("application/json")
         @Path("/saveDaShiGuanZhu")
 	    public JSONWithPadding saveDaShiGuanZhu(@FormParam("userId") String userId,
 	    		              @FormParam("followedUserId") String followedUserId){
         	Following following= new Following();
         	following.setUserId(userId);
         	following.setFollowedUserId(followedUserId);
         	return new JSONWithPadding(new GenericEntity<StatusBean>(new DaShiDao().addDaShiGuanZhu(following)){
         	},"saveDaShiGuanzhu");
         }

 /**
  * 取消关注的大�?
  * */
           @POST
           @Produces("application/json")
           @Path("/deldaShiGuanZhu")
   	    public JSONWithPadding delDaShiGuanZhu(
   	    		@FormParam("id") String id,@FormParam("userid") String userid){
           	return new JSONWithPadding(new GenericEntity<StatusBean>(new DaShiDao().delDaShiGuanZhu(id, userid)){
           	},"delDaShiGuanzhu");
           }  
           
   /**
    * 显示关注的大�?
    * */
            @GET
       		@Produces("application/json")
       		@Path("/getfollowlist")
       	public JSONWithPadding getFollowingList(@QueryParam("userId") String userId,
       										@QueryParam("start") String start,
       										@QueryParam("limit") String limit){
       		return new JSONWithPadding(new GenericEntity<PagableData<Following>>(new DaShiDao().getfollowList(userId,start, limit)){
       		},"getFollowlist");
       	}

	
/**
 * 显示大师列表
 * */
		        @GET
				@Produces("application/json")
				@Path("/getDashiView/view")
			public JSONWithPadding getdashiList(@QueryParam("view") String view,
												@QueryParam("userid") String userid,
												@QueryParam("start") String start,
												@QueryParam("limit") String limit){
				return new JSONWithPadding(new GenericEntity<PagableData<Master>>(new DaShiDao().getDaShiView(userid, view, start, limit)){
				},"getdashi");
			}
        
		        	
		   
}
