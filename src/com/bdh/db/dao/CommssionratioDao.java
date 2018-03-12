package com.bdh.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.bdh.db.entry.Commssionratio;
import com.bdh.db.entry.PagableData;
import com.bdh.db.util.DBUtil;

public class CommssionratioDao {

	
	
	//²éÑ¯ÊÖÐø
	public PagableData<Commssionratio> AllCommssionratio(){
		Connection conn = null;
		PreparedStatement stmt= null;
		PagableData<Commssionratio> pd= new PagableData<Commssionratio>();
		try {
			StringBuilder sql=new StringBuilder();
			sql.append("SELECT * FROM commssionratio");
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.prepareStatement(sql.toString());
			List<Commssionratio> list=DBUtil.getInstance().convert(stmt.executeQuery(), Commssionratio.class);
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
