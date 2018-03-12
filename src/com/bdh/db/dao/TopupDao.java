package com.bdh.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.bdh.db.entry.Fill;
import com.bdh.db.entry.PagableData;
import com.bdh.db.entry.StatusBean;
import com.bdh.db.util.DBUtil;
import com.bdh.db.util.FunctionSet;




public class TopupDao {

	// 充值中心

	public StatusBean Voucher(Fill fill,String userId){
		StatusBean bean=new StatusBean();
		bean.setFlag(0);
		Connection conn= null;
		PreparedStatement stmt = null;
		try {
			conn= DBUtil.getInstance().getConnection();
			Long time=System.currentTimeMillis();
				PreparedStatement pstmt = null;
				StringBuilder buffe=new StringBuilder();
				buffe.append("INSERT INTO fill (userid,amount,rechargeCode,createtime) ");
				buffe.append(" values ('");
				buffe.append(FunctionSet.filt(userId));
		        buffe.append("','");
		        buffe.append(fill.getAmount());
		        buffe.append("','");
		        buffe.append(FunctionSet.filt(fill.getRechargeCode()));
		        buffe.append("',");
		        buffe.append(time);
		        buffe.append(")");
		    	pstmt=conn.prepareStatement(buffe.toString());
		    	if(fill.getAmount()>0&&!fill.getRechargeCode().isEmpty()){
				       if(pstmt.executeUpdate() > 0){
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
	
	/***
	 * 管理员界面   查询充值表
	 * */
	/***
	 * 管理员界面   查询充值表
	 * */
	public PagableData<Fill> AllFillList(Fill fill){
		
		Connection conn = null;
		PreparedStatement stmt= null;
		PagableData<Fill> pd= new PagableData<Fill>();
		try {
			if(fill.getAmount()==0){
			StringBuilder sql=new StringBuilder();
			sql.append("SELECT fill.*,user.name ,user.phone FROM fill,user where fill.userId=user.userId and fill.statusFlag=0 and user.name LIKE '%"+ fill.getName()+"%' and fill.rechargeCode LIKE '%"+fill.getRechargeCode()+"%'");
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.prepareStatement(sql.toString());
		
				List<Fill> list=DBUtil.getInstance().convert(stmt.executeQuery(), Fill.class);
				if (!list.isEmpty()) {
					pd.setDataList(list);
				}	
				sql= null;
			}else{
				StringBuilder sql1=new StringBuilder();
				sql1.append("SELECT fill.*,user.name,user.phone FROM fill,user where fill.userId=user.userId and fill.statusFlag=0 and user.name LIKE '%"+ fill.getName()+"%' and fill.rechargeCode LIKE '%"+fill.getRechargeCode()+"%' "
						+ " and fill.amount LIKE '%"+(int)fill.getAmount()+"%'");
				conn = DBUtil.getInstance().getConnection();
				stmt = conn.prepareStatement(sql1.toString());
			
					List<Fill> slist=DBUtil.getInstance().convert(stmt.executeQuery(), Fill.class);
					if (!slist.isEmpty()) {
						pd.setDataList(slist);
					}	
					sql1= null;
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

	
	
	//管理员操作    确认添加
	public StatusBean OKVoucher(Fill fill){
		
		StatusBean bean=new StatusBean();
		bean.setFlag(0);
		Connection conn= null;
		PreparedStatement stmt = null;
		try {
			    Long time=System.currentTimeMillis();
		        conn= DBUtil.getInstance().getConnection();
		        StringBuilder sql=new StringBuilder();
				sql.append("SELECT * FROM fill where statusFlag=0 and id='"+FunctionSet.filt(fill.getId())+"' and userid='"+FunctionSet.filt(fill.getUserId())+"'");
			    stmt=conn.prepareStatement(sql.toString());
				List<Fill> list=DBUtil.getInstance().convert(stmt.executeQuery(), Fill.class);
			if(!list.isEmpty()){
				double amount1=0;
				for (int i = 0; i < list.size(); i++) {
					amount1=list.get(i).getAmount();
				}
				
				PreparedStatement pstmt = null;
				StringBuilder buffe=new StringBuilder();
				//加上管理员信息
				buffe.append("update fill set statusFlag=1,adminid=?,admin=?,"
						+ " admintime="+time+",serial=?,updateTime="+time+" where id=? and userid=?");
				if(amount1==fill.getAmount()){
					
				if(!fill.getAdminId().isEmpty()&&!fill.getAdmin().isEmpty()&&!fill.getId().isEmpty()&&!fill.getUserId().isEmpty()&&!fill.getSerial().isEmpty()){
					pstmt=conn.prepareStatement(buffe.toString());
					pstmt.setString(1, FunctionSet.filt(fill.getAdminId()));
					pstmt.setString(2, FunctionSet.filt(fill.getAdmin()));
					pstmt.setString(3, FunctionSet.filt(fill.getSerial()));
					pstmt.setString(4, FunctionSet.filt(fill.getId()));
					pstmt.setString(5, FunctionSet.filt(fill.getUserId()));
			       if(pstmt.executeUpdate() > 0){
			    	   bean.setFlag(1);
			    	   
			        }
				}}
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
