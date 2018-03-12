package com.bdh.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import com.bdh.db.entry.Bookmark;
import com.bdh.db.entry.PagableData;
import com.bdh.db.entry.event;
import com.bdh.db.util.DBUtil;
import com.bdh.db.util.FunctionSet;

public class CalendarDao {

	
	/**
	 * 显示事件
	 * */
	public  PagableData<event> getevent(){
		Connection conn = null;
		PreparedStatement stmt= null;
		PagableData<event> pm=new PagableData<event>();
		try {
			StringBuilder sql=new StringBuilder();
			sql.append(" SELECT * from event");
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.prepareStatement(sql.toString());
			List<event> list=DBUtil.getInstance().convert(stmt.executeQuery(), event.class);
		    if(!list.isEmpty()){
		    	pm.setDataList(list);
		    }
		    sql=null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return pm;
	}
	
}
