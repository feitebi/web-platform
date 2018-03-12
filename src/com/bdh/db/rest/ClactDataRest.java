package com.bdh.db.rest;

import java.util.List;
import java.util.Map;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;

import com.bdh.db.dao.BalanceDao;
import com.bdh.db.dao.ClactData;
import com.bdh.db.entry.User;
import com.sun.jersey.api.json.JSONWithPadding;

@Path("/ClactData")
@Produces({ "application/x-javascript", "application/json", "application/xml" })
public class ClactDataRest {
	
	  @GET
	  @Produces("application/json")
	  @Path("/ChartData_Kine")
	  public JSONWithPadding ChartDataKine(
		  								  @QueryParam("exchangeName") String exchangeName,
										  @QueryParam("currency") String currency,
										  @QueryParam("Pair") String Pair,
										  @QueryParam("period") String period){
	
		  if(Pair!=null){
			  if(period!=null){
				  if(exchangeName.equals("BTER")){
						  if(Pair.equals("CNY")){
							  return new JSONWithPadding(new GenericEntity<List>(new ClactData().ChartDataKine(exchangeName, currency, Pair, period)){
							  },"ChartDataKine");
						  }
						   return new JSONWithPadding(new GenericEntity<List>(new ClactData().ChartDataKine( exchangeName,Pair ,currency , period)){
							  },"ChartDataKine");
				  }else{
					   return new JSONWithPadding(new GenericEntity<List>(new ClactData().ChartDataKine( exchangeName, currency, Pair, period)){
					  },"ChartDataKine");
				  }
		  }else{
			  String errorperiod="86400";
			  if(exchangeName.equals("BTER")){
					  if(Pair.equals("CNY")){
						  return new JSONWithPadding(new GenericEntity<List>(new ClactData().ChartDataKine(exchangeName, currency, Pair, errorperiod)){
						  },"ChartDataKine");
					  }
					   return new JSONWithPadding(new GenericEntity<List>(new ClactData().ChartDataKine( exchangeName,Pair ,currency , errorperiod)){
						  },"ChartDataKine");
			  }else{
				   return new JSONWithPadding(new GenericEntity<List>(new ClactData().ChartDataKine( exchangeName, currency, Pair, errorperiod)){
				  },"ChartDataKine");
			  }
		  }
	  }
		  return new JSONWithPadding("error","ChartDataKine");
   } 

	  @POST
	  @Produces("application/json")
	  @Path("/allTicker")
	  public JSONWithPadding ChartDataKine(@FormParam("userId") String userId,
			  @FormParam("platform") String platform
			){
		  if(platform==null){
			  return new JSONWithPadding("", "allTicker");
		  }
	   return new JSONWithPadding(new GenericEntity<Map>(new BalanceDao().getAllTickerList(platform)){
	  },"allTicker");
   } 
	  
}
