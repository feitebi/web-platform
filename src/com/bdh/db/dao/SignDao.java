package com.bdh.db.dao;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;

import com.bdh.db.entry.StatusBean;
import com.bdh.db.entry.User;
import com.bdh.db.util.DBUtil;
import com.bdh.db.util.FunctionSet;


public class SignDao {
	
	public static void main(String[] args) {
		System.out.println(DigestUtils.md5Hex("feitebi"));
	}
	public StatusBean signUP(String accountName, String phone, String code, String pwd) {
		StatusBean bean=new StatusBean();
		bean.setFlag(0);
		String uid = UUID.randomUUID().toString().replaceAll("-", "");
		if(accountName!=null&&phone!=null&&code!=null&&pwd!=null){
			accountName=FunctionSet.filt(accountName);
			phone=FunctionSet.filt(phone);
			pwd=FunctionSet.filt(pwd);
			pwd = DigestUtils.md5Hex(pwd);
			Connection conn= null;
			Statement stmt = null;
			try {
				conn= DBUtil.getInstance().getConnection();
			    stmt = conn.createStatement();
				StringBuilder beffer= new StringBuilder();
				Long time=System.currentTimeMillis();
				String sql = "select * from user where flag=1 and name='" + accountName + "' or phone='"+phone+"' and pwd='"+pwd+"' ";
				List<User> ds = DBUtil.getInstance().convert(stmt.executeQuery(sql), User.class);
				if (!ds.isEmpty()) {
					bean.setErrorMsg("此账户已被注册!");
					return bean;
				}
				beffer.append("insert into user(userid,name,phone,pwd,createtime) ");
				beffer.append("values('");   
				beffer.append(uid);
				beffer.append("','");
				beffer.append(accountName);
				beffer.append("','");
				beffer.append(phone);
				beffer.append("','");
				beffer.append(pwd);
				beffer.append("',");
				beffer.append(time);
				beffer.append(")");
		       if(stmt.executeUpdate(beffer.toString()) > 0){
		    	   String setsql = "insert into setting(userId) values('"+uid+"')";
		    	   if(stmt.executeUpdate(setsql.toString()) > 0){
		    		   	   bean.setFlag(1);
				    	   bean.setOtherCode(uid);
		    	   }
		        }
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				DBUtil.getInstance().close(stmt);
				DBUtil.getInstance().close(conn);
			}
		}
	
		return bean;
	}

	public StatusBean signIN(String nameOrphone, String signinpwd) {
		StatusBean bean=new StatusBean();
		bean.setFlag(0);
		if(nameOrphone!=null&&signinpwd!=null){
		nameOrphone=FunctionSet.filt(nameOrphone);
		signinpwd=FunctionSet.filt(signinpwd);
		signinpwd = DigestUtils.md5Hex(signinpwd);
		
		Connection conn= null;
		Statement stmt = null;
		try {
		conn= DBUtil.getInstance().getConnection();
		stmt = conn.createStatement();
		String sql = "select * from user where flag=1 and name='" + nameOrphone + "' or phone='"+nameOrphone+"' and pwd='"+signinpwd+"' ";
		List<User> ds = DBUtil.getInstance().convert(stmt.executeQuery(sql), User.class);
		
		if (ds.isEmpty()) {
			bean.setErrorMsg("账户或密码错误，请重新输入!");
			return bean;
		}
		else{
			bean.setFlag(1);
			bean.setOtherCode(ds.get(0).getUserId());
		}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		}
		return bean;
	 }
	

}
