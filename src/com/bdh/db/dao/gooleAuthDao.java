package com.bdh.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import com.bdh.db.entry.setting;
import com.bdh.db.rest.GoogleAuthenticator;
import com.bdh.db.util.DBUtil;


public class gooleAuthDao {

	public setting getGooleAuth(String userid) {
		Connection conn = null;
		PreparedStatement stmt= null;
		setting set=new setting();
		try {
			StringBuilder sql=new StringBuilder();
			sql.append("SELECT * from setting where userId=?");
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.prepareStatement(sql.toString());
			stmt.setString(1, userid);
			List<setting> list=DBUtil.getInstance().convert(stmt.executeQuery(), setting.class);
			if (!list.isEmpty()) {
				if(list.get(0).getGoolgleSecretKey()==null){
					PreparedStatement pstmt= null;
					GoogleAuthenticator authenticator= new GoogleAuthenticator();
					String barCode = authenticator.getGoogleAuthenticatorBarCode(authenticator.getRandomSecretKey(), "BitDash.net", "BitDash");
					String sql2="update setting set goolgleSecretKey=? where userId=?";
					pstmt=conn.prepareStatement(sql2);
					pstmt.setString(1,barCode);
					pstmt.setString(2,userid);
					if(pstmt.executeUpdate() > 0){
						set.setGoolgleSecretKey(barCode);
					}
				}
				else{
					set.setGoolgleSecretKey(list.get(0).getGoolgleSecretKey());
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
		return set;
	}

	public setting getisGooleAuth(String userId, String code) {
		Connection conn = null;
		PreparedStatement stmt= null;
		setting set=new setting();
		try {
			StringBuilder sql=new StringBuilder();
			sql.append("SELECT * from setting where userId=?");
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.prepareStatement(sql.toString());
			stmt.setString(1, userId);
			List<setting> list=DBUtil.getInstance().convert(stmt.executeQuery(), setting.class);
			if (!list.isEmpty()) {
				PreparedStatement pstmt= null;
				String googleScret=list.get(0).getGoolgleSecretKey();
				GoogleAuthenticator authenticator= new GoogleAuthenticator();
				String secret=googleScret.substring((googleScret.indexOf('=')+1),googleScret.indexOf('&'));
				String pageCode=authenticator.getTOTPCode(secret);
				if(code.equals(pageCode)){
					String sql3="update setting set isEnablegoolegleAutor=1 where userId=?";
					pstmt=conn.prepareStatement(sql3);
					pstmt.setString(1,userId);
					if(pstmt.executeUpdate() > 0){
						set.setIsEnablegoolegleAutor(1);
					}
				}
				else{
					set.setIsEnablegoolegleAutor(0);
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
		return set;
	}

	public setting getredegoogle(String userId) {
		Connection conn = null;
		PreparedStatement stmt= null;
		setting set=new setting();
		try {
			StringBuilder sql=new StringBuilder();
			sql.append("SELECT * from setting where userId=?");
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.prepareStatement(sql.toString());
			stmt.setString(1, userId);
			List<setting> list=DBUtil.getInstance().convert(stmt.executeQuery(), setting.class);
			if (!list.isEmpty()) {
				int isgoogle=list.get(0).getIsEnablegoolegleAutor();
				if(isgoogle==1){
					set.setIsEnablegoolegleAutor(1);
				}
				else{
					set.setIsEnablegoolegleAutor(0);
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
		return set;
	}

	public setting getstopgoogle(String userId) {
		Connection conn = null;
		PreparedStatement stmt= null;
		setting set=new setting();
		try {
			StringBuilder sql=new StringBuilder();
			sql.append("SELECT * from setting where userId=?");
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.prepareStatement(sql.toString());
			stmt.setString(1, userId);
			List<setting> list=DBUtil.getInstance().convert(stmt.executeQuery(), setting.class);
			if (!list.isEmpty()) {
				int isgoogle=list.get(0).getIsEnablegoolegleAutor();
				PreparedStatement pstmt= null;
				if(isgoogle==1){
					String sql3="update setting set isEnablegoolegleAutor=0 where userId=?";
					pstmt=conn.prepareStatement(sql3);
					pstmt.setString(1,userId);
					if(pstmt.executeUpdate() > 0){
						set.setIsEnablegoolegleAutor(0);
					}
				}
				else{
					set.setIsEnablegoolegleAutor(1);
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
		return set;
	}

}
