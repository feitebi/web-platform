package com.bdh.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.StampedLock;

import org.apache.commons.codec.digest.DigestUtils;

import com.bdh.db.entry.PagableData;
import com.bdh.db.entry.StatusBean;
import com.bdh.db.entry.User;
import com.bdh.db.mob.SendSMSValidCode;
import com.bdh.db.mob.SmsDemo;
import com.bdh.db.mob.SmsSender;
import com.bdh.db.util.DBUtil;
import com.bdh.db.util.FunctionSet;

public class UserAccountDao {
	

	/**
	 * 用户查询
	 * */
	public PagableData<User> getUserList(String userid){
		Connection conn = null;
		PreparedStatement stmt= null;
		PagableData<User> pd= new PagableData<User>();
		try {
			StringBuilder sql=new StringBuilder();
			sql.append("SELECT * FROM user where userid='"+FunctionSet.filt(userid)+"'");
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.prepareStatement(sql.toString());
			List<User> list=DBUtil.getInstance().convert(stmt.executeQuery(), User.class);
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
	 * 发�?�验证码
	 * */
	public StatusBean sendCode(String userid,String phone){	
		System.out.println(userid+"         "+phone);
		StatusBean status = new StatusBean();
		status.setFlag(0);
		Connection conn = null;
		PreparedStatement stmt= null;
		PreparedStatement stmt1= null;
		 Random r=new Random();
	       int tag[]={0,0,0,0,0,0,0,0,0,0};
	       String four="";
	       int temp=0;
	       while(four.length()!=4){
	               temp=r.nextInt(10);//随机获取0~9的数�?
	               if(tag[temp]==0){
	                     four+=temp;
	                    tag[temp]=1;
	               }
	       }
		try { 
			conn = DBUtil.getInstance().getConnection();
			phone = FunctionSet.filt(phone);
			String Sql = "select * from user where phone='"+ phone+"' and userId='"+userid+"'";
			stmt1 = conn.prepareStatement(Sql.toString());
			List<User> uList = DBUtil.getInstance().convert(stmt1.executeQuery(), User.class);
			if(uList.isEmpty()){
				System.out.println(uList);
				return status;
			}
			else{
			if (SmsDemo.sendSms(phone,four)) {
				StringBuilder buffer= new StringBuilder();
				buffer.append("update user set phoneValidCode='"+four+"' WHERE userId='"+userid+"' AND phone='"+phone+"'");
				stmt=conn.prepareStatement(buffer.toString());
				  if(stmt.executeUpdate() > 0){
			        	status.setFlag(1);
			        }
				status.setFlag(1);
				status.setOtherCode(four);
			}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		
		return status;
		
		
	}
	
	/**
	 * 修改手机�?
	 * **/
	public StatusBean updatePhoneNumber(String userid,String code){	
		StatusBean status = new StatusBean();
		status.setFlag(0);
	  	Connection conn = null;
    	PreparedStatement stmt= null;
    	
		try {
			StringBuilder buffer=new StringBuilder();
	        buffer.append("select phoneValidCode from user where userid='"+FunctionSet.filt(userid)+"'");
	        conn = DBUtil.getInstance().getConnection();
			stmt = conn.prepareStatement(buffer.toString());
	        List<User> list=DBUtil.getInstance().convert(stmt.executeQuery(), User.class);
	        if (!list.isEmpty()) {
	        	 if(list.get(0).getPhoneValidCode().equals(code)){
	        		status.setFlag(1); 
	        	 }
			}	
	        buffer= null;
	      /*  if(stmt.executeUpdate() > 0){
	        	status.setFlag(1);
	        }*/
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return status;
	}
	
	public StatusBean updatePhone(String userid,String phone,String code){	
		StatusBean status = new StatusBean();
		status.setFlag(0);
	  	Connection conn = null;
    	PreparedStatement stmt= null;
		try {
			conn = DBUtil.getInstance().getConnection();
    		StringBuilder buffer=new StringBuilder();
 	        buffer.append("update user set phone=? where userId=? and phoneValidCode=?");
 	        stmt=conn.prepareStatement(buffer.toString());
 	        stmt.setString(1,FunctionSet.filt(phone));
 	        stmt.setString(2, FunctionSet.filt(userid));
 	        stmt.setString(3, FunctionSet.filt(code));
 	       if(stmt.executeUpdate() > 0){
 	    	  status.setFlag(1);
			}
 	      buffer=null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return status;
	}
	public StatusBean updatePwd(String userid, String newPwd, String code) {
		StatusBean status = new StatusBean();
		status.setFlag(0);
	  	Connection conn = null;
    	PreparedStatement stmt= null;
    	newPwd = FunctionSet.filt(newPwd);
		newPwd = DigestUtils.md5Hex(newPwd);
		try {
			conn = DBUtil.getInstance().getConnection();
    		StringBuilder buffer=new StringBuilder();
 	        buffer.append("update user set pwd=? where userId=? and phoneValidCode=?");
 	        stmt=conn.prepareStatement(buffer.toString());
 	        stmt.setString(1,newPwd);
 	        stmt.setString(2, FunctionSet.filt(userid));
 	        stmt.setString(3, FunctionSet.filt(code));
 	       if(stmt.executeUpdate() > 0){
 	    	  status.setFlag(1);
			}
 	      buffer=null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return status;
	}
	
	
	/**
	 * 测试方法
	 * **/
	
}
