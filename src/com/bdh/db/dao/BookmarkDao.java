package com.bdh.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.bdh.db.entry.Bookmark;
import com.bdh.db.entry.PagableData;
import com.bdh.db.entry.StatusBean;
import com.bdh.db.util.DBUtil;
import com.bdh.db.util.FunctionSet;

public class BookmarkDao {
	
	Connection conn = null;
	PreparedStatement stmt= null;
	
	/*****
	BookMarkDao
	*******/
	/***
		 * 添加收藏的币
		 * 
		 * */
		public StatusBean AddBookMark(String userid,String platform,String coin){
			System.out.println(userid+" "+platform+" "+coin);
			StatusBean status =new StatusBean();
			status.setFlag(0);
		        try {
		        	StringBuilder sql=new StringBuilder();
		        	System.out.println(1);
		        	sql.append("SELECT * from bookmark WHERE flag=1 and userid='"+FunctionSet.filt(userid)+"' and currency='"+FunctionSet.filt(coin)+"' and platform='"+FunctionSet.filt(platform)+"'");
		        	System.out.println(2);
		        	conn = DBUtil.getInstance().getConnection();
					stmt = conn.prepareStatement(sql.toString());
					List<Bookmark> list=DBUtil.getInstance().convert(stmt.executeQuery(), Bookmark.class);
	
					if(list.isEmpty()){
							 System.out.println(123);
							PreparedStatement pstmt= null;	
							StringBuilder buffer = new StringBuilder();
							long time =System.currentTimeMillis();
							buffer.append("insert into bookmark (userid,platform,currency,createTime)");
							buffer.append("values ('");
							buffer.append(FunctionSet.filt(userid));
							buffer.append("','");
							buffer.append(FunctionSet.filt(platform));
							buffer.append("','");
							buffer.append(FunctionSet.filt(coin));
							buffer.append("',");
							buffer.append(time);
					        buffer.append(")");
					        pstmt=conn.prepareStatement(buffer.toString());
					        if(pstmt.executeUpdate() > 0){
					        	status.setFlag(1);
					        }
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					DBUtil.getInstance().close(stmt);
					DBUtil.getInstance().close(conn);
				}
			return status;
		}
		
	/**
	 * 显示收藏
	 * */
	public PagableData<Bookmark> getBookmark(String userId,String start,String limit){
		PagableData<Bookmark> pm=new PagableData<Bookmark>();
		try {
			start=FunctionSet.filt(start);		
			limit=FunctionSet.filt(limit);	
			StringBuilder sql=new StringBuilder();
			sql.append(" SELECT * from bookmark where userId='"+userId+"' and flag=1 Order By id asc limit ");
			sql.append(start);
			sql.append(",");
			sql.append(limit);
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.prepareStatement(sql.toString());
			
			List<Bookmark> list=DBUtil.getInstance().convert(stmt.executeQuery(), Bookmark.class);
		    if(!list.isEmpty()){
		    	pm.setDataList(list);
		    	String countsql="select count(id) c from bookmark where flag=1 and userId='" + userId+"'";
	    		ResultSet rs=stmt.executeQuery(countsql);
	    		if(rs.next()){
	    			pm.setTotalCount(rs.getInt("c"));
	    			pm.setItemCount(Integer.parseInt(limit));
	    			pm.setItemCount(Integer.parseInt(start));
	    		}
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
	
/*	public static void main(String[] args) {
		BookmarkDao dao= new BookmarkDao();
		List<Bookmark> list=dao.getBookmark("2021", "0", "3").getDataList();
		for (Bookmark bookmark : list) {
		}
		
	}*/
	/**
	 * 取消收藏
	 * */
	public StatusBean deleteBookmark(Bookmark bookmark){
		StatusBean status = new StatusBean();
        status.setFlag(0);
        try {
			conn = DBUtil.getInstance().getConnection();
			StringBuilder buffer = new StringBuilder();
			
			buffer.append("UPDATE bookmark SET flag=0,updateTime=NOW() where id='"+bookmark.getId()+"' and userid='"+bookmark.getUserId()+"'");
	        stmt=conn.prepareStatement(buffer.toString());
	        if(stmt.executeUpdate() > 0){
	        	status.setFlag(1);
	        }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
	return status;
	}
	
	

}
