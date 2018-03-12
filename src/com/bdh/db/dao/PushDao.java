package com.bdh.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.bdh.db.entry.Fill;
import com.bdh.db.entry.PagableData;
import com.bdh.db.entry.Push;
import com.bdh.db.entry.PushOrder;
import com.bdh.db.entry.StatusBean;
import com.bdh.db.util.DBUtil;
import com.bdh.db.util.FunctionSet;


public class PushDao {
	/**
	 * 挂单
	 * **/
	public StatusBean PushPut(Push push){
		StatusBean bean=new StatusBean();
		bean.setFlag(0);
		Connection conn= null;
		PreparedStatement stmt = null;
		try {
			    conn= DBUtil.getInstance().getConnection();
			    Long time=System.currentTimeMillis();
				StringBuilder buffe=new StringBuilder();
				buffe.append("INSERT INTO push (formuserid,formaddr,formkey,askPrice,leftQty,flag,createTime) ");
				buffe.append(" values ('");
				buffe.append(FunctionSet.filt(push.getFormUserId()));
				buffe.append("','");
		        buffe.append(FunctionSet.filt(push.getFormAddr()));
		        buffe.append("','");
		        buffe.append(FunctionSet.filt(push.getFormKey()));
		        buffe.append("','");
		        buffe.append(FunctionSet.filt(push.getAskPrice()));
		        buffe.append("','");
		        buffe.append(push.getLeftqty());
		        buffe.append("',");
		        buffe.append("1");
		        buffe.append(",");
		        buffe.append(time);
		        buffe.append(")");
		      if(!push.getFormUserId().isEmpty() && !push.getFormAddr().isEmpty() && !push.getFormKey().isEmpty()
		    		   && !push.getAskPrice().isEmpty() && push.getLeftqty()>0){
		    	 
		    	  stmt=conn.prepareStatement(buffe.toString());
		       if(stmt.executeUpdate() > 0){
		    	   bean.setFlag(1);
		        }
			}
			} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return bean;
	}
	
	

	/**
	 * 显示所有挂单
	 * **/
	public PagableData<Push> getPushAllList(String start,String limit){
		Connection conn = null;
		PreparedStatement stmt= null;
		PagableData<Push> pd= new PagableData<Push>();
		try {
			start=FunctionSet.filt(start);
			limit=FunctionSet.filt(limit);
			StringBuilder sql=new StringBuilder();
			sql.append("SELECT * FROM push,user where push.formuserId=user.userId and push.flag=1 Order By push.pid asc limit ");
			sql.append(start);
			sql.append(",");
			sql.append(limit);
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.prepareStatement(sql.toString());
			List<Push> list= DBUtil.getInstance().convert(stmt.executeQuery(), Push.class);
			if(!list.isEmpty()){
				pd.setDataList(list);
				String countsql="select count(push.formuserId) c from push,user where push.formuserId=user.userId and push.flag=1";
			     ResultSet rs=stmt.executeQuery(countsql);
			if(rs.next()){
				pd.setTotalCount(rs.getInt("c"));
				pd.setItemCount(Integer.parseInt(limit));
				pd.setItemCount(Integer.parseInt(start));
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
    	return pd;
	    }
	
	
	
	
	/**
	 * 显示我自己的挂单
	 * */
	public PagableData<Push> getPushList(String start,String limit,String userid){
		Connection conn = null;
		PreparedStatement stmt= null;
		PagableData<Push> pd= new PagableData<Push>();
		try {
			userid=FunctionSet.filt(userid);
			start=FunctionSet.filt(start);
			limit=FunctionSet.filt(limit);
			StringBuilder sql=new StringBuilder();
			sql.append("SELECT push.*,user.name FROM push,user where push.formuserId=user.userId and push.flag=1 and push.formuserId="+userid+" Order By push.pid asc limit ");
			sql.append(start);
			sql.append(",");
			sql.append(limit);
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.prepareStatement(sql.toString());
			List<Push> list= DBUtil.getInstance().convert(stmt.executeQuery(), Push.class);
			if(!list.isEmpty()){
				pd.setDataList(list);
				String countsql="select count(push.formuserId) c from push,user where push.formuserId=user.userId and push.flag=1 and push.formuserId="+userid;
			     ResultSet rs=stmt.executeQuery(countsql);
			if(rs.next()){
				pd.setTotalCount(rs.getInt("c"));
				pd.setItemCount(Integer.parseInt(limit));
				pd.setItemCount(Integer.parseInt(start));
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
    	return pd;
	    }

	
	/**
	 * 
	 * 取消挂单
	 * **/
		public StatusBean upPushOff(String pid){
			StatusBean bean=new StatusBean();
			bean.setFlag(0);
			Connection conn= null;
			PreparedStatement stmt = null;
			try {
				conn= DBUtil.getInstance().getConnection();
				StringBuilder beffer= new StringBuilder();
				beffer.append("UPDATE push set flag='0' WHERE pid='"+FunctionSet.filt(pid)+"'");
				stmt=conn.prepareStatement(beffer.toString());
		 	       if(stmt.executeUpdate() > 0){
		 	    	  bean.setFlag(1);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					DBUtil.getInstance().close(stmt);
					DBUtil.getInstance().close(conn);
				}
				return bean;
			}
	

	
	/***
	 * 点击购买
	 * */
	public StatusBean BuyFill(PushOrder pushOrder,String userid,String pid,String formUserId
			,String leftqty,String qty){

		StatusBean bean=new StatusBean();
		bean.setFlag(0);
		Connection conn= null;
		PreparedStatement stmt = null;
		try {
			Long time=System.currentTimeMillis();
			StringBuilder sql=new StringBuilder();
			sql.append("SELECT * FROM fill,user where fill.userid=user.userid and fill.userid='"+FunctionSet.filt(userid)+"'");
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.prepareStatement(sql.toString());
			List<Fill> list= DBUtil.getInstance().convert(stmt.executeQuery(), Fill.class);
			if(!list.isEmpty()){
				PreparedStatement pstmt = null;
				StringBuilder buffe=new StringBuilder();
				buffe.append("INSERT INTO pushorder(pushid,pushuserid,userid,price,qty,amount,paidTime,commission,flag,createTime) ");
				buffe.append(" VALUES ('");
				buffe.append(FunctionSet.filt(pid));
				buffe.append("','");
				buffe.append(FunctionSet.filt(formUserId));
				buffe.append("','");
				buffe.append(FunctionSet.filt(userid));
				buffe.append("','");
		        buffe.append(pushOrder.getPrice());
		        buffe.append("','");
		        buffe.append(pushOrder.getQty());
		        buffe.append("','");
		        buffe.append(pushOrder.getAmount());
		        buffe.append("',");
		        buffe.append(time);
		        buffe.append(",'");
		        buffe.append(pushOrder.getCommission());
		        buffe.append("','");
		        buffe.append("1");
		        buffe.append("',");
		        buffe.append(time);
		        buffe.append(")");
		        if(pushOrder.getPrice()>0&&pushOrder.getAmount()>0&&pushOrder.getCommission()>0){
		    	pstmt=conn.prepareStatement(buffe.toString());
		       if(pstmt.executeUpdate() > 0){
		    	   bean.setFlag(1);
		    	 //购买完成修改push挂单表
		    	    PreparedStatement pstmts = null;
					StringBuilder buffer=new StringBuilder();
		    	    buffer.append("UPDATE push set leftqty='"+leftqty+"',qty='"+qty+"',orderTime="+time+" WHERE pid='"+pid+"' and formUserId='"+formUserId+"'");
		    	   if(!leftqty.isEmpty()&&!qty.isEmpty()&&!pid.isEmpty()&&!formUserId.isEmpty()){
		    	    pstmts=conn.prepareStatement(buffer.toString());
					  if(pstmts.executeUpdate() > 0){
				    	   bean.setFlag(2);
				        }
					  }
		        }
			}
		        }else{
		        	
		        }
			sql=null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
    	return bean;
	    }
	
	/**
	 * 根据id查询push数据
	 * **/
	public PagableData<Push> getIdList(String pid){
		Connection conn = null;
		PreparedStatement stmt= null;
		PagableData<Push> pd= new PagableData<Push>();
		try {
			StringBuilder sql=new StringBuilder();
			sql.append("SELECT * FROM push,user where push.formuserId=user.userId and pid="+FunctionSet.filt(pid)+"");
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.prepareStatement(sql.toString());
			List<Push> list=DBUtil.getInstance().convert(stmt.executeQuery(), Push.class);
			if (!list.isEmpty()) {
				pd.setDataList(list);
				
			}	
			sql= null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return pd;

	}

}
