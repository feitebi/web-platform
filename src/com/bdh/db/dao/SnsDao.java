package com.bdh.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.bdh.db.entry.PagableData;
import com.bdh.db.entry.StatusBean;
import com.bdh.db.entry.setting;
import com.bdh.db.util.DBUtil;


public class SnsDao {
	
	Connection conn = null;
	PreparedStatement stmt= null;
	/**
	 * �?启或者关闭社交状态，关闭状�?�下，个人资产信息将全部隐藏，与世隔离状态�?�默认为sns4Opened�?启状�?
	 * 
	 * 查询setting
	 * */
	public PagableData<setting> getsetting(String userId){
        PagableData<setting> pd = new PagableData<setting>();
        try {
		      StringBuilder sqlBf = new StringBuilder();
		      sqlBf.append("SELECT * FROM setting where userId=?");
			  conn = DBUtil.getInstance().getConnection();
	          stmt = conn.prepareStatement(sqlBf.toString());
	          stmt.setString(1, userId);
	            List<setting> list = DBUtil.getInstance().convert(stmt.executeQuery(), setting.class);
            if (!list.isEmpty()) {
                pd.setDataList(list);
            }
            sqlBf = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			    DBUtil.getInstance().close(stmt);
	            DBUtil.getInstance().close(conn);
		}
        return pd;
	}
	/**
	 * 社交
	 * */
	public setting updateOpen(String userId){
		setting setting= new setting();
         try {
        	 StringBuilder sqlBf = new StringBuilder();
   	         sqlBf.append("SELECT * FROM setting where userId=?");
   		     conn = DBUtil.getInstance().getConnection();
			 stmt = conn.prepareStatement(sqlBf.toString());
			 stmt.setString(1, userId);
	         List<setting> list = DBUtil.getInstance().convert(stmt.executeQuery(), setting.class);
	         if (!list.isEmpty()) {
	        	 if(list.get(0).getSns4Opened()==1){
	        		 PreparedStatement pstmt= null;
	        		 StringBuilder buffer=new StringBuilder();
	     	        buffer.append("update setting set sns4Opened=? where userId=?");
	     	        pstmt=conn.prepareStatement(buffer.toString());
	     	        pstmt.setInt(1, 0);
	     	        pstmt.setString(2, userId);
	     	       if(pstmt.executeUpdate() > 0){
	     	    	  setting.setSns4Opened(0);;
					}
	        	 }else if(list.get(0).getSns4Opened()==0){
	        		 PreparedStatement pstmts= null;
	        		 StringBuilder buffer=new StringBuilder();
	     	        buffer.append("update setting set sns4Opened=? where userId=?");
	     	        pstmts=conn.prepareStatement(buffer.toString());
	     	        pstmts.setInt(1, 1);
	     	        pstmts.setString(2, userId);
	     	       if(pstmts.executeUpdate() > 0){
	     	    	  setting.setSns4Opened(1);;
					}
	        	 }
	          
	            }
	         sqlBf=null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}	
		return setting;
	}
	
	/**
	 *共享挂单或智能投资策�? 关闭  默认�?�?
	 * */
	public setting updateSnsAssets(String userId){
		setting setting= new setting();
         try {
        	 StringBuilder sqlBf = new StringBuilder();
   	         sqlBf.append("SELECT * FROM setting where userId=?");
   		     conn = DBUtil.getInstance().getConnection();
			 stmt = conn.prepareStatement(sqlBf.toString());
			 stmt.setString(1, userId);
	         List<setting> list = DBUtil.getInstance().convert(stmt.executeQuery(), setting.class);
	         if (!list.isEmpty()) {
	        	 if(list.get(0).getSns4Assets()==1){
	        		 PreparedStatement pstmt= null;
	        		 StringBuilder buffer=new StringBuilder();
	     	        buffer.append("update setting set sns4Assets=? where userId=?");
	     	       pstmt=conn.prepareStatement(buffer.toString());
	     	       pstmt.setInt(1, 0);
	     	       pstmt.setString(2, userId);
	     	       if(pstmt.executeUpdate() > 0){
	     	    	  setting.setSns4Assets(0);;
					}
	        	 }else if(list.get(0).getSns4Assets()==0){
	        		 PreparedStatement pstmts= null;
	        		 StringBuilder buffer=new StringBuilder();
		     	        buffer.append("update setting set sns4Assets=? where userId=?");
		     	       pstmts=conn.prepareStatement(buffer.toString());
		     	      pstmts.setInt(1, 1);
		     	     pstmts.setString(2, userId);
		     	       if(pstmts.executeUpdate() > 0){
		     	    	  setting.setSns4Assets(1);;
						}
	        	 }
	            }
	         sqlBf=null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}	
		return setting;
	}

	/**
	 * 匿名共享资产以及盈利状�??  关闭  默认�?�?
	 * */
	public setting updateSnsStrategy(String userId){
		setting setting= new setting();
         try {
        	 StringBuilder sqlBf = new StringBuilder();
   	         sqlBf.append("SELECT * FROM setting where userId=?");
   		     conn = DBUtil.getInstance().getConnection();
			 stmt = conn.prepareStatement(sqlBf.toString());
			 stmt.setString(1, userId);
	         List<setting> list = DBUtil.getInstance().convert(stmt.executeQuery(), setting.class);
	         if (!list.isEmpty()) {
	        	 if(list.get(0).getSns4Strategy()==1){
	        		 PreparedStatement pstmt= null;
	        			StringBuilder buffer=new StringBuilder();
	        	        buffer.append("update setting set sns4Strategy=? where userId=?");
	        	        pstmt=conn.prepareStatement(buffer.toString());
	        	        pstmt.setInt(1, 0);
	        	        pstmt.setString(2, userId);
	     	       if(pstmt.executeUpdate() > 0){
	     	    	  setting.setSns4Assets(0);;
					}
	        	 }else if(list.get(0).getSns4Strategy()==0){
	        		    PreparedStatement pstmts= null;
	        			StringBuilder buffer=new StringBuilder();
	        	        buffer.append("update setting set sns4Strategy=? where userId=?");
	        	        pstmts=conn.prepareStatement(buffer.toString());
	        	        pstmts.setInt(1, 1);
	        	        pstmts.setString(2, userId);
		     	       if(pstmts.executeUpdate() > 0){
		     	    	  setting.setSns4Strategy(1);;
						}
	        	 }
	            }
	         sqlBf=null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}	
		return setting;
	}
     /**
	 * 查看价格
	 * */

	public StatusBean updateSnsPrice(setting seting,String userId){
		StatusBean status = new StatusBean();
        status.setFlag(0);
        try {
			conn=DBUtil.getInstance().getConnection();
			StringBuilder buffer=new StringBuilder();
	        buffer.append("update setting set sns4Price=? where userId=?");
	        stmt=conn.prepareStatement(buffer.toString());
	        stmt.setBigDecimal(1, seting.getSns4Price());
	        stmt.setString(2, userId);
	        if(stmt.executeUpdate() > 0){
	        	status.setFlag(1);
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
        return status;
	}
}
