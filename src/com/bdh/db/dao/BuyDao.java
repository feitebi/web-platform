package com.bdh.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import com.bdh.db.entry.Apis;
import com.bdh.db.entry.Buy;
import com.bdh.db.entry.PagableData;
import com.bdh.db.entry.StatusBean;
import com.bdh.db.entry.sell;
import com.bdh.db.util.DBUtil;
import com.bdh.db.util.FunctionSet;

public class BuyDao {
	//add

	public StatusBean addbuy(Buy b) {
		StatusBean b1=new StatusBean();
		b1.setFlag(0);
		Connection conn= null;
		Statement stmt = null;
		try {
			conn= DBUtil.getInstance().getConnection();
			stmt = conn.createStatement();
			StringBuilder beffer= new StringBuilder();
			beffer.append("insert into buy(userId,exchange,surplus,under,buy,num,sum) ");
			beffer.append("values ('");
	        beffer.append(FunctionSet.filt(b.getUserId()));
	        beffer.append("','");
	        beffer.append(FunctionSet.filt(b.getExchange()));
	        beffer.append("','");
	        beffer.append(FunctionSet.filt(b.getSurplus()));
	        beffer.append("','");
	        beffer.append(FunctionSet.filt(b.getUnder()));
	        beffer.append("','");
	        beffer.append(FunctionSet.filt(b.getBuy()));
	        beffer.append("','");
	        beffer.append(FunctionSet.filt(b.getNum()));
	        beffer.append("','");
	        beffer.append(FunctionSet.filt(b.getSum()));
	        beffer.append("'");
	        beffer.append(")");
	       
	       if(stmt.executeUpdate(beffer.toString()) > 0){
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

	public PagableData<Buy> getbuy(String userId) {
		Connection conn = null;
		PreparedStatement stmt= null;
		PagableData<Buy> pd= new PagableData<Buy>();
		try {
			StringBuilder sql=new StringBuilder();
			sql.append(" SELECT * from buy where userId="+userId);
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.prepareStatement(sql.toString());
			List<Buy> list=DBUtil.getInstance().convert(stmt.executeQuery(), Buy.class);
			if (!list.isEmpty()) {
				pd.setDataList(list);
			}	
			sql= null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return pd;
	}

	public StatusBean delbuy(String buyID) {
		StatusBean b2=new StatusBean();
		b2.setFlag(0);
		Connection conn= null;
		Statement stmt = null;
		try {
			conn= DBUtil.getInstance().getConnection();
			stmt = conn.createStatement();
			StringBuilder beffer= new StringBuilder();
			beffer.append("delete from buy where buyID="+buyID);
	       
	       if(stmt.executeUpdate(beffer.toString()) > 0){
	   		b2.setFlag(1);
	        }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return b2;
	}
}


