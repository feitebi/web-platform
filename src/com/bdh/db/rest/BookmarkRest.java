package com.bdh.db.rest;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;

import com.bdh.db.dao.BookmarkDao;
import com.bdh.db.entry.Bookmark;
import com.bdh.db.entry.PagableData;
import com.bdh.db.entry.StatusBean;
import com.sun.jersey.api.json.JSONWithPadding;

@Path("/Bookmark")
@Produces({ "application/x-javascript", "application/json", "application/xml" })
public class BookmarkRest {
	
	/**显示
	 * */
	    @POST
	    @Produces("application/json")
	    @Path("/Bookmarklist")
	    public JSONWithPadding getApisList(@FormParam("userId") String userId, @FormParam("start") String start,
	    		@FormParam("limit") String limit) {
	        return new JSONWithPadding(
	                new GenericEntity<PagableData<Bookmark>>(new BookmarkDao().getBookmark(userId, start, limit)) {
	                }, "Bookmarklist");
	    }
	    
	    /*******
	    BookMarkRest

	    *****/

	      /**
	    	     * 添加收藏的币 
	    	     * 
	    	     * 参数  userid  platform coin
	    	     * */
	    	    @POST
	    	    @Produces("application/json")
	    	    @Path("/AddBookMark")
	    	    public JSONWithPadding AddBookMark(@FormParam("userid") String userid, @FormParam("platform") String platform,
	    	    		@FormParam("coin") String coin) {
	    	    	if(!userid.isEmpty() || !platform.isEmpty() || !coin.isEmpty()){
	    	    		 return new JSONWithPadding(
	    	 	                new GenericEntity<StatusBean>(new BookmarkDao().AddBookMark(userid, platform.toLowerCase(), coin)) {
	    	 	                }, "AddBookMark");
	    	    	}
	    	        return new JSONWithPadding("", "AddBookMark");
	    	    } 


	    
/**
 * 取消收藏
 * */
	    @POST
	    @Produces("application/json")
	    @Path("/isabel")
	    public JSONWithPadding delbook(@FormParam("id") String id,@FormParam("userId") String userId){
	    	Bookmark bookmark= new Bookmark();
	    	bookmark.setId(id);
	    	bookmark.setUserId(userId);
	    	return new JSONWithPadding(new GenericEntity<StatusBean>(new BookmarkDao().deleteBookmark(bookmark)) {
	    	},"bookmarkupdate");
	    }
	    
	    
}
