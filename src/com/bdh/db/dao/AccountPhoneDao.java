package com.bdh.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import com.bdh.db.entry.PagableData;
import com.bdh.db.entry.StatusBean;
import com.bdh.db.entry.setting;
import com.bdh.db.util.DBUtil;
import com.bdh.db.util.FunctionSet;



public class AccountPhoneDao {   //账户设置
	
	Connection conn = null;
	PreparedStatement stmt= null;
	/**
	 * 手机通知    被关�?
	 * **/
	public StatusBean notifyMssage(String fieldName,String fieldValue,String userId){
		StatusBean bean= new StatusBean();
		bean.setFlag(0);
         try {  	 
   		     conn = DBUtil.getInstance().getConnection();
	    	 PreparedStatement pstmt= null;
    		 StringBuilder buffer=new StringBuilder();
 	         buffer.append("update setting set "+fieldName+"=? where userId=?");
 	         pstmt=conn.prepareStatement(buffer.toString());
 	         pstmt.setString(1, FunctionSet.filt(fieldValue));
 	         pstmt.setString(2, userId);
 	        if(pstmt.executeUpdate() > 0){
 	        	bean.setFlag(1);
			}
 	       buffer=null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}	
		return bean;
	}

	public PagableData<setting> getNotify(String userId){
		Connection conn = null;
		PreparedStatement stmt= null;
		PagableData<setting> pd= new PagableData<setting>();
		try {
			StringBuilder sql=new StringBuilder();
			sql.append("SELECT * FROM setting where userId=?");
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.prepareStatement(sql.toString());
			stmt.setString(1, FunctionSet.filt(userId));
			List<setting> list=DBUtil.getInstance().convert(stmt.executeQuery(), setting.class);
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
	
	
	        
}
