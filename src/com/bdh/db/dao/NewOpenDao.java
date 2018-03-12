package com.bdh.db.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import com.bdh.db.entry.NewOpen;
import com.bdh.db.entry.PagableData;
import com.bdh.db.entry.StatusBean;
import com.bdh.db.util.DBUtil;
import com.bdh.db.util.FunctionSet;

/*import edu.umd.cs.findbugs.annotations.NoWarning;*/

public class NewOpenDao {

	public PagableData<NewOpen> getNewOpenList(String start,String limit){
		Connection conn = null;
		PreparedStatement stmt= null;
		PagableData<NewOpen> pd= new PagableData<NewOpen>();
		try {
			start=FunctionSet.filt(start);
			limit=FunctionSet.filt(limit);
			StringBuilder sql=new StringBuilder();
			sql.append("SELECT * FROM newopen Order By id asc limit ");
			sql.append(start);
			sql.append(",");
			sql.append(limit);
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.prepareStatement(sql.toString());
			List<NewOpen> list=DBUtil.getInstance().convert(stmt.executeQuery(), NewOpen.class);
			if (!list.isEmpty()) {
				pd.setDataList(list);
				String countsql="select count(id) c from newopen";
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
	
	
	public StatusBean addNewopen(NewOpen newopen){
		StatusBean bean=new StatusBean();
		bean.setFlag(0);
		Connection conn= null;
		Statement stmt = null;
		try {
			conn= DBUtil.getInstance().getConnection();
			stmt = conn.createStatement();
			StringBuilder beffer= new StringBuilder();
			beffer.append("insert into newopen(name,kaiPrice,nowPrice,platform,time) ");
			beffer.append("values('");
	        beffer.append(FunctionSet.filt(newopen.getName()));
	        beffer.append("','");
	        beffer.append(FunctionSet.filt(newopen.getKaiPrice()));
	        beffer.append("','");
	        beffer.append(FunctionSet.filt(newopen.getNowPrice()));
	        beffer.append("','");
	        beffer.append(FunctionSet.filt(newopen.getPlatform()));
	        beffer.append("','");
            beffer.append(FunctionSet.filt(newopen.getTime()));
	       beffer.append("')");
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
	
	
	


}
