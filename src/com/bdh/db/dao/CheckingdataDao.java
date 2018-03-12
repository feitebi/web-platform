package com.bdh.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.bdh.db.entry.Checkingdata;
import com.bdh.db.entry.Fill;
import com.bdh.db.entry.PagableData;
import com.bdh.db.entry.StatusBean;
import com.bdh.db.util.DBUtil;
import com.bdh.db.util.FunctionSet;


//支付宝数据
public class CheckingdataDao {
	
	
	/*** 
	 * 支付宝列表
	 * **/
	public PagableData<Checkingdata> AllaccountList(String start,String limit){
		Connection conn = null;
		PreparedStatement stmt= null;
		PagableData<Checkingdata> pd= new PagableData<Checkingdata>();
		try {
			start=FunctionSet.filt(start);
			limit=FunctionSet.filt(limit);
			StringBuilder sql=new StringBuilder();
			sql.append("SELECT * FROM checkingdata Order By tranTime desc limit");
			sql.append(start);
			sql.append(",");
			sql.append(limit);
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.prepareStatement(sql.toString());
			List<Checkingdata> list=DBUtil.getInstance().convert(stmt.executeQuery(), Checkingdata.class);
			if (!list.isEmpty()) {
				pd.setDataList(list);
				String countsql="select count(id) c from checkingdata";
			     ResultSet rs=stmt.executeQuery(countsql);
			if(rs.next()){
				pd.setTotalCount(rs.getInt("c"));
				pd.setItemCount(Integer.parseInt(limit));
				pd.setItemCount(Integer.parseInt(start));
			}
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
	
	//导入支付宝数据
	public StatusBean saveCurrentaccount(Checkingdata account){
		StatusBean bean= new StatusBean();
		bean.setFlag(0);
		Connection conn= null;
		PreparedStatement stmt= null;
		try {
			conn=DBUtil.getInstance().getConnection();
			StringBuilder buffer= new StringBuilder();
			buffer.append("INSERT INTO currentaccount(userName,phone,tranNumber,tranTime,paymentTime,bankAccount,amount,comment,adminName) ");
			buffer.append(" VALUES ('");
			buffer.append(FunctionSet.filt(account.getUserName()));
			buffer.append("','");
			buffer.append(FunctionSet.filt(account.getPhone()));
			buffer.append("','");
			buffer.append(FunctionSet.filt(account.getTranNumber()));
			buffer.append("','");
			buffer.append(FunctionSet.filt(account.getTranTime()));
			buffer.append("','");
			buffer.append(FunctionSet.filt(account.getPaymentTime()));	
			buffer.append(",'");
			buffer.append(FunctionSet.filt(account.getBankAccount()));
			buffer.append(",'");
			buffer.append(FunctionSet.filt(account.getAmount()));
			buffer.append(",'");
			buffer.append(FunctionSet.filt(account.getComment()));
			buffer.append("','");
			buffer.append(FunctionSet.filt(account.getAdminName()));
			buffer.append("',");
			buffer.append(System.currentTimeMillis());
			buffer.append(")");
			stmt=conn.prepareStatement(buffer.toString());
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

}
