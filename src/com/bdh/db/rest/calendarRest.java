package com.bdh.db.rest;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;

import com.bdh.db.dao.CalendarDao;
import com.bdh.db.entry.PagableData;
import com.bdh.db.entry.event;
import com.sun.jersey.api.json.JSONWithPadding;

@Path("/Calendar")
@Produces({ "application/x-javascript", "application/json", "application/xml" })
public class calendarRest {

	@POST
	  @Produces("application/json")
	  @Path("/getcalendar")
	  public JSONWithPadding NotifyMessage(){
	   return new JSONWithPadding(new GenericEntity<PagableData<event>>(new CalendarDao().getevent()){
  	  },"getcalendar");
     } 
	
}
