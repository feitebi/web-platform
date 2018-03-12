package com.bdh.db.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.filter.FromContainsFilter;

import com.bdh.db.entry.Account;
import com.bdh.db.entry.AccountBalance;
import com.bdh.db.entry.Bookmark;
import com.bdh.db.entry.PagableData;
import com.bdh.db.entry.StatusBean;
import com.bdh.db.entry.Strategy;
import com.bdh.db.entry.User;
import com.bdh.db.entry.event;
import com.bdh.db.util.DBUtil;
import com.bdh.db.util.FunctionSet;
import com.mysql.fabric.xmlrpc.base.Array;


public class indexDao {

	//��ʾ���ܺ�Լ
	public PagableData<Strategy> smartDeal(String userid){
		PagableData<Strategy> smartdeal=new PagableData<Strategy>();
		Connection conn = null;
		PreparedStatement stmt= null;	
		try {
			StringBuilder sql=new StringBuilder();
			sql.append("SELECT * FROM strategy where strategyFlag=0 and userId='"+FunctionSet.filt(userid)+"'");
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.prepareStatement(sql.toString());
			List<Strategy> strategy=DBUtil.getInstance().convert(stmt.executeQuery(), Strategy.class);
			if (!strategy.isEmpty()) {
				smartdeal.setDataList(strategy);
			}	
			sql= null;		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}	
		return smartdeal;
	}

	public PagableData<event> getevent(){
		PagableData<event> smartdeal=new PagableData<event>();
		Connection conn = null;
		PreparedStatement stmt= null;	
		try {
			StringBuilder sql=new StringBuilder();
			sql.append("select * from event");
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.prepareStatement(sql.toString());
			List<event> strategy=DBUtil.getInstance().convert(stmt.executeQuery(), event.class);
			if (!strategy.isEmpty()) {
				smartdeal.setDataList(strategy);
			}	
			sql= null;		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}	
		return smartdeal;
	}
	
	public StatusBean delsmart(String id) {
		StatusBean st=new StatusBean();
		st.setFlag(0);
		Connection conn = null;
		PreparedStatement stmt= null;	
		try {
			String sql="update strategy set strategyFlag=2 where id="+FunctionSet.filt(id);
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.prepareStatement(sql.toString());
			if (stmt.executeUpdate()>0) {
				st.setFlag(1);
			}	
			sql= null;		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}	
		return st;
	}
	
	

}
