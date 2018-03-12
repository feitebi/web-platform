package com.bdh.db.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.h2.expression.Alias;
import org.iq80.leveldb.DB;








import com.bdh.db.entry.Apis;
import com.bdh.db.entry.Exchang;
import com.bdh.db.entry.PagableData;
import com.bdh.db.entry.StaticConstants;
import com.bdh.db.entry.StatusBean;
import com.bdh.db.entry.setting;
import com.bdh.db.util.DBUtil;
import com.bdh.db.util.FunctionSet;
import com.gson.util.Status;

public class Apisdao {
	/**
	 *查询交易�?
	 * @param userid 
	 * */
	
	public PagableData<Exchang> getExchangApisList(String userid){
		Connection conn = null;
		PreparedStatement stmt= null;
		PagableData<Exchang> pd= new PagableData<Exchang>();
		try {
			StringBuilder sql=new StringBuilder();
			sql.append("SELECT * FROM exchang where flag=1 Order By sortBy desc");
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.prepareStatement(sql.toString());
			List<Exchang> list=DBUtil.getInstance().convert(stmt.executeQuery(), Exchang.class);
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
	
	
	
	public PagableData<Exchang> getApisList(String userid){
		Connection conn = null;
		PreparedStatement stmt= null;
		PagableData<Exchang> pd= new PagableData<Exchang>();
		 List<Apis> apilist= new ArrayList<Apis>();
		 List<Exchang> exchanglist= new ArrayList<Exchang>();
		try {
			StringBuilder sql=new StringBuilder();
			sql.append("SELECT * FROM apis where userId='"+FunctionSet.filt(userid)+"'");
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.prepareStatement(sql.toString());
		    apilist=DBUtil.getInstance().convert(stmt.executeQuery(), Apis.class);
		    PreparedStatement stmt1= null;
			StringBuilder sql1=new StringBuilder();
			sql1.append("SELECT * FROM exchang where flag=1");
			stmt1 = conn.prepareStatement(sql1.toString());
		    exchanglist=DBUtil.getInstance().convert(stmt1.executeQuery(), Exchang.class);
				if(!apilist.isEmpty() && !exchanglist.isEmpty()){
					for (int i = 0; i < apilist.size(); i++) {
						if(apilist.get(i).getIsEnable().equals("1") && apilist.get(i).getFlag()!=0){
							for (int j = 0; j < exchanglist.size(); j++) {
								if(apilist.get(i).getExchangid()==exchanglist.get(j).getExchangid()){
									exchanglist.get(j).setStatus("1");
								}
							}
						}
					}
				}
				Exchang[] array=exchanglist.toArray(new Exchang[]{});
				Arrays.sort(array);
				pd.setDataList(Arrays.asList(array));
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
	 * 查询用户拥有的api
	 * */
	public PagableData<Apis> getApisUserid(String userid){
		Connection conn = null;
		PreparedStatement stmt= null;
		PagableData<Apis> pd= new PagableData<Apis>();
		try {
			StringBuilder sql=new StringBuilder();
			sql.append("SELECT * FROM apis where isEnable='1' and userid='"+FunctionSet.filt(userid)+"'");
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.prepareStatement(sql.toString());
			List<Apis> list=DBUtil.getInstance().convert(stmt.executeQuery(), Apis.class);
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
	/**
	 * 锟斤拷页锟斤拷询锟斤拷锟斤拷
	 * */
	public PagableData<Apis> getApis(String userid,String start,String limit){
		Connection conn = null;
		PreparedStatement stmt= null;
		PagableData<Apis> pd= new PagableData<Apis>();
		try {
			Long.parseLong(userid);
			start=FunctionSet.filt(start);
			limit=FunctionSet.filt(limit);
			StringBuilder sql=new StringBuilder();
			sql.append(" SELECT * from apis where userId=? and flag=1 Order By id asc limit ");
			sql.append(start);
			sql.append(",");
			sql.append(limit);
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.prepareStatement(sql.toString());
			stmt.setLong(1, Long.parseLong(userid));
			List<Apis> list=DBUtil.getInstance().convert(stmt.executeQuery(), Apis.class);
				if (!list.isEmpty()) {
					pd.setDataList(list);
					String countsql="select count(id) c from apis where flag=1 and userid='"+userid+"'";
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
		} finally{
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
 		
		return pd;
	}
	 

	
	
	/**
	 * 添加api
	 * */
	public StatusBean addApis(Apis apis){
		StatusBean bean=new StatusBean();
		bean.setFlag(0);
		Connection conn= null;
		Statement stmt = null;
		try {
			conn= DBUtil.getInstance().getConnection();
		    stmt = conn.createStatement();
			StringBuilder beffer= new StringBuilder();
			Long time=System.currentTimeMillis();
			beffer.append("insert into apis(userid,exchangid,apikey,apisecret,createtime,updatetime) ");
			beffer.append("values('");
	        beffer.append(FunctionSet.filt(apis.getUserid()));
	        beffer.append("','");
	        beffer.append(apis.getExchangid());
	        beffer.append("','");
	        beffer.append(FunctionSet.filt(apis.getApiKey()));
	        beffer.append("','");
	        beffer.append(FunctionSet.filt(apis.getApiSecret()));
	        beffer.append("',");
	        beffer.append(time);
	        beffer.append(",");
	        beffer.append(time);
	        beffer.append(")");
	       if(stmt.executeUpdate(beffer.toString()) > 0){
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
	
	/**
	 * 修改api
	 * */
	    public StatusBean upadteapis(Apis apis,String userId,String exchangid){
			  StatusBean status = new StatusBean();
		        status.setFlag(0);
		        Connection conn = null;
		        PreparedStatement stmt = null;
		        try {
					conn = DBUtil.getInstance().getConnection();
					StringBuilder buffer = new StringBuilder();
					Long time = System.currentTimeMillis();
			        buffer.append("UPDATE apis SET apiKey=?,apiSecret=?,updateTime=? where userId='"+userId+"' and exchangid="+exchangid);
			        stmt=conn.prepareStatement(buffer.toString());
			        stmt.setString(1, FunctionSet.filt(apis.getApiKey()));
			        stmt.setString(2, FunctionSet.filt(apis.getApiSecret()));
			        stmt.setLong(3, time);
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
	    
	    /**
	     * �?启访�? or 暂停访问
	     * */
	    public StatusBean updateflag(String userId,String exchangid){

	    	StatusBean status = new StatusBean();
		        status.setFlag(0);
			    Connection conn = null;
		        PreparedStatement stmt = null;
	         try {
	        	 StringBuilder sqlBf = new StringBuilder();
	   	         sqlBf.append("SELECT * FROM apis where userId='"+FunctionSet.filt(userId)+"' and exchangid='"+FunctionSet.filt(exchangid)+"'");
	   		     conn = DBUtil.getInstance().getConnection();
				 stmt = conn.prepareStatement(sqlBf.toString());
		         List<Apis> list = DBUtil.getInstance().convert(stmt.executeQuery(), Apis.class);
		         
		         if (!list.isEmpty()) {
		        	 if(list.get(0).getFlag()==1){
		        		PreparedStatement pstmt= null;
		        		StringBuilder buffer=new StringBuilder();
		     	        buffer.append("UPDATE apis SET flag=0 where userId='"+FunctionSet.filt(userId)+"' and isEnable='1' and exchangid="+FunctionSet.filt(exchangid));
		     	        pstmt=conn.prepareStatement(buffer.toString());
		     	       if(pstmt.executeUpdate() > 0){
		     	    	  status.setFlag(1);
						}
		        	 }else{
		        		 PreparedStatement pstmts= null;
		        		 StringBuilder sql=new StringBuilder();
		        		 sql.append("UPDATE apis SET flag=1 where userId='"+FunctionSet.filt(userId)+"'  and isEnable='1'  and exchangid="+FunctionSet.filt(exchangid));
		     	        pstmts=conn.prepareStatement(sql.toString());
		     	       if(pstmts.executeUpdate() > 0){
		     	    	  status.setFlag(1);
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
			return status;
		}
	   
	    /**
	     * 删除api
	     * */
	    public static StatusBean upadteIsEnable(String userId,String exchangid){
			  StatusBean status = new StatusBean();
		        status.setFlag(0);
		        Connection conn = null;
		        PreparedStatement stmt = null;
		        try {
					conn = DBUtil.getInstance().getConnection();
					StringBuilder buffer = new StringBuilder();
					Long time = System.currentTimeMillis();
			        buffer.append(" UPDATE apis SET isEnable='0',flag='0',updateTime='"+time+"' where userId='"+FunctionSet.filt(userId)+"' and exchangid="+FunctionSet.filt(exchangid));
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
