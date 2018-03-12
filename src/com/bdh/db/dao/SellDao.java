package com.bdh.db.dao;

import java.sql.Connection;
import java.sql.Statement;

import com.bdh.db.entry.StatusBean;
import com.bdh.db.entry.sell;
import com.bdh.db.util.DBUtil;
import com.bdh.db.util.FunctionSet;


public class SellDao {

	public StatusBean addsell(sell sell) {
		StatusBean b1=new StatusBean();
		b1.setFlag(0);
		Connection conn= null;
		Statement stmt = null;
		try {
			conn= DBUtil.getInstance().getConnection();
			stmt = conn.createStatement();
			StringBuilder beffer= new StringBuilder();
			beffer.append("insert into sell(userId,exchange,surplus,price,high,num,sum) ");
			beffer.append("values ('");
	        beffer.append(FunctionSet.filt(sell.getUserId()));
	        beffer.append("','");
	        beffer.append(FunctionSet.filt(sell.getExchange()));
	        beffer.append("','");
	        beffer.append(FunctionSet.filt(sell.getSurplus()));
	        beffer.append("','");
	        beffer.append(FunctionSet.filt(sell.getPrice()));
	        beffer.append("','");
	        beffer.append(FunctionSet.filt(sell.getHigh()));
	        beffer.append("','");
	        beffer.append(FunctionSet.filt(sell.getNum()));
	        beffer.append("','");
	        beffer.append(FunctionSet.filt(sell.getSum()));
	        beffer.append("'");
	        beffer.append(")");
	       if(stmt.executeUpdate(beffer.toString())> 0){
	   		b1.setFlag(1);
	        }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return b1;
	}

}
