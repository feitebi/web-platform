package com.bdh.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.bdh.db.entry.Following;
import com.bdh.db.entry.Master;
import com.bdh.db.entry.PagableData;
import com.bdh.db.entry.StatusBean;
import com.bdh.db.util.DBUtil;
import com.bdh.db.util.FunctionSet;



public class DaShiDao {
	
	/**
	 * 添加关注
	 * */
	public StatusBean addDaShiGuanZhu(Following following){
		StatusBean statusBean= new StatusBean();
		statusBean.setFlag(0);
		Connection conn= null;
		Statement stmt=null;
		try {
		    conn=DBUtil.getInstance().getConnection();
		    stmt = conn.createStatement();
	    	StringBuilder buffer = new StringBuilder();
			 buffer.append("INSERT INTO following (userId,followedUserid,flag,creaTetime) ");
			 buffer.append("values('");	 
			 buffer.append(FunctionSet.filt(following.getUserId()));
			 buffer.append("','");
			 buffer.append(FunctionSet.filt(following.getFollowedUserId()));
			 buffer.append("',");
			 buffer.append("1");
			 buffer.append(",");
			 buffer.append("now()");
			 buffer.append(")");
			 if(stmt.executeUpdate(buffer.toString()) > 0){
		    	statusBean.setFlag(1);
		    }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return statusBean;
	}
	/**
	 *取消关注
	 * */
	public StatusBean delDaShiGuanZhu(String followedUserId,String userid){
		
		StatusBean statusBean= new StatusBean();
		statusBean.setFlag(0);
		Connection conn= null;
		PreparedStatement stmt = null;
		try {
			conn = DBUtil.getInstance().getConnection();
			StringBuilder buffer = new StringBuilder();
			buffer.append("DELETE FROM following WHERE  followedUserId='"+followedUserId+"' and userid='"+userid+"'");
			stmt=conn.prepareStatement(buffer.toString());
			if(stmt.executeUpdate() > 0){
				statusBean.setFlag(1);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return statusBean; 
	}
	
		
    /**
     *显示关注
     * */
	public PagableData<Following> getfollowList(String userId,String start, String limit){
		Connection conn = null;
		PreparedStatement stmt= null;
		PagableData<Following> pf=new PagableData<Following>(); 
		try {
			userId=FunctionSet.filt(userId);
			start=FunctionSet.filt(start);
			limit=FunctionSet.filt(limit);
			StringBuilder sql=new StringBuilder();
			sql.append("SELECT * from `user` u,following f WHERE f.followedUserId=u.userId and f.flag=1 and f.userId=");
			sql.append("'"+userId+"'");
		    sql.append(" Order By f.createTime DESC limit ");
			sql.append(start);
			sql.append(",");
			sql.append(limit);
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.prepareStatement(sql.toString());		
			List<Following> list=DBUtil.getInstance().convert(stmt.executeQuery(), Following.class);
			if(!list.isEmpty()){
				pf.setDataList(list);
				String countsql=" SELECT count(following.userId) c from following,user WHERE following.userId=user.userId AND following.flag=1 and following.userId='"+userId+"'";
				ResultSet rs=stmt.executeQuery(countsql);
			if(rs.next()){
				pf.setTotalCount(rs.getInt("c"));
				pf.setItemCount(Integer.parseInt(limit));
				pf.setItemCount(Integer.parseInt(start));
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
		return pf;
	} 

	
	/**
	 *显示大师
	 * *//*
	public PagableData<Master> getDaShiView(String userid,String view,String start,String limit){
		
		Connection conn = null;
		PreparedStatement stmt= null;
		PagableData<Master> pd= new PagableData<Master>();
		try {
			start=FunctionSet.filt(start);
			limit=FunctionSet.filt(limit);
			view=FunctionSet.filt(view);
			StringBuilder sql=new StringBuilder();
			sql.append("SELECT * from user,setting where user.userId=setting.userId and  user.userid!=");
			sql.append("'"+userid+"'");
			sql.append(" Order By user.");
			sql.append(view);
			sql.append(" asc limit ");
			sql.append(start);
			sql.append(",");
			sql.append(limit);
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.prepareStatement(sql.toString());
			List<Master> list=DBUtil.getInstance().convert(stmt.executeQuery(), Master.class);
			if (!list.isEmpty()) {
				pd.setDataList(list);
				String countsql="SELECT count(user.userId) c from user,setting where user.userId=setting.userId";
			     ResultSet rs=stmt.executeQuery(countsql);
			if(rs.next()){
				pd.setTotalCount(rs.getInt("c"));
				pd.setItemCount(Integer.parseInt(limit));
				pd.setItemCount(Integer.parseInt(start));
			}
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
*/
	/**
	 *显示大师
	 * */
	public PagableData<Master> getDaShiView(String userid,String view,String start,String limit){
		Connection conn = null;
		PreparedStatement stmt= null;
		PagableData<Master> pd= new PagableData<Master>();
		try {
			start=FunctionSet.filt(start);
			limit=FunctionSet.filt(limit);
			view=FunctionSet.filt(view);
			conn = DBUtil.getInstance().getConnection();
			StringBuilder sql=new StringBuilder();
			sql.append("SELECT * from user,setting where user.userId=setting.userId and  user.userid!=");
			sql.append("'"+userid+"'");
			sql.append(" Order By user.");
			sql.append(view);
			sql.append(" asc limit ");
			sql.append(start);
			sql.append(",");
			sql.append(limit);
			stmt = conn.prepareStatement(sql.toString());
			List<Master> list=DBUtil.getInstance().convert(stmt.executeQuery(), Master.class);
			PreparedStatement stmt1= null;
			StringBuilder fsql=new StringBuilder();
			fsql.append("SELECT following.* from user,following where user.userId=following.userId and following.flag=1 "
					+ " and user.userId='"+FunctionSet.filt(userid)+"'");
			stmt1 = conn.prepareStatement(fsql.toString());
			List<Following> ulist=DBUtil.getInstance().convert(stmt1.executeQuery(), Following.class);
			if(!list.isEmpty() || !ulist.isEmpty()){
				for (int i = 0; i < ulist.size(); i++) {
				  for (int j = 0; j < list.size(); j++) {
					if(list.get(j).getUserId().equals(ulist.get(i).getFollowedUserId())){
					      list.get(j).setIsfollowed(1);
					}
				  }
				}
				pd.setDataList(list);
				String countsql="SELECT count(user.userId) c from user,setting where user.userId=setting.userId";
			    ResultSet rs=stmt.executeQuery(countsql);
			if(rs.next()){
				pd.setTotalCount(rs.getInt("c"));
				pd.setItemCount(Integer.parseInt(limit));
				pd.setItemCount(Integer.parseInt(start));
			}
			}	
	
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
