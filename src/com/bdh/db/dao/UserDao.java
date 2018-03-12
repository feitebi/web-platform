package com.bdh.db.dao;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.bdh.db.entry.Apis;
import com.bdh.db.entry.Exchang;
import com.bdh.db.entry.PagableData;
import com.bdh.db.entry.StatusBean;
import com.bdh.db.entry.User;
import com.bdh.db.mob.SendSMSValidCode;
import com.bdh.db.mob.SmsSender;
import com.bdh.db.rest.GoogleAuthenticator;
import com.bdh.db.util.DBUtil;
import com.bdh.db.util.FunctionSet;


public class UserDao {

	public static Map<String, Apis> cachedApis = new ConcurrentHashMap<String, Apis>();
	public static Map<String, Exchang> cachedApi = new ConcurrentHashMap<String, Exchang>();
	static String randomcode="";
	public static void loadAllApis() {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.createStatement();
			String sql = "select * from apis,exchang where apis.flag=1 and apis.isEnable=1 and apis.exchangid=exchang.exchangid";
			List<Exchang> ds = DBUtil.getInstance().convert(stmt.executeQuery(sql), Exchang.class);
			for (Exchang api : ds) {
				String kid = api.getUserid() + "_" + api.getLogo();
				cachedApi.put(kid, api);

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
	}

	public static User registe(String phone, String pwd, String phoneValidCode, String userId) {
		Connection conn = null;
		Statement stmt = null;

		User user = new User();
		try {
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.createStatement();

			String cSql = " select userId,phone from user where phone='" + phone + "' and phoneValidCode!='"
					+ phoneValidCode + "'  and flag=1 ";
			List<User> uList = DBUtil.getInstance().convert(stmt.executeQuery(cSql), User.class);
			if (!uList.isEmpty()) {
				user.setErrorMsg("无效的验证码！");
				return user;
			}

			pwd = FunctionSet.filt(pwd);
			pwd = DigestUtils.md5Hex(pwd);
			phone = FunctionSet.filt(phone);
			phoneValidCode = FunctionSet.filt(phoneValidCode);
			long time = System.currentTimeMillis();

			cSql = " update user set pwd='" + pwd + "',phoneValidFlag=1,updateTime='"+time
					+"' where phone='" + phone + "' and phoneValidCode='" +phoneValidCode
					+"' and flag=1 ";
			if (stmt.executeUpdate(cSql) > 0) {
				Statement stmts = null;
				stmts = conn.createStatement();
				StringBuilder beffer= new StringBuilder();
				beffer.append("insert into setting(userid) ");
				beffer.append("values('");
		        beffer.append(FunctionSet.filt(userId));
		        beffer.append("')");
		        if(stmts.executeUpdate(beffer.toString()) > 0){
				cSql = " select * from user where updateTime=" + time + " and phone='" + phone + "' and flag=1 ";
				uList = DBUtil.getInstance().convert(stmt.executeQuery(cSql), User.class);
				if (!uList.isEmpty()) {
					return uList.get(0);
				}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return user;
	}
	public static User login(String phone, String pwd) {
		User u = getUser(phone, pwd, "", null);
		return u;
	}
	public static User loginBy2FactorCode(String userId, String twoFactorCode) {
		User u = getUser(userId, null);
		if (StringUtils.isNotBlank(twoFactorCode) && StringUtils.isNotBlank(u.getTwoFactorKey())) {
			if (twoFactorCode.trim().equals(GoogleAuthenticator.getTOTPCode(u.getTwoFactorKey()))) {
				return u;
			}
		}
		return new User();
	}
	public static boolean enableTwoFactorKey(String userId, String twoFactorCode, String twoFactorKey) {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.createStatement();

			userId = FunctionSet.filt(userId);

			if (StringUtils.isNotBlank(twoFactorCode) && StringUtils.isNotBlank(twoFactorKey)) {
				if (twoFactorCode.trim().equals(GoogleAuthenticator.getTOTPCode(twoFactorKey))) {
					String cSql = " update user set twoFactorKey='" + twoFactorKey + "' where userId='" + userId
							+ "' and flag=1 ";
					if (stmt.executeUpdate(cSql) > 0) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return false;
	}
	public static boolean cancelTwoFactorKey(String userId, String twoFactorCode) {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.createStatement();

			userId = FunctionSet.filt(userId);

			User u = getUser(userId, stmt);

			if (StringUtils.isNotBlank(twoFactorCode) && StringUtils.isNotBlank(u.getTwoFactorKey())) {
				if (twoFactorCode.equals(GoogleAuthenticator.getTOTPCode(u.getTwoFactorKey()))) {
					String cSql = " update user set twoFactorKey='' where userId='" + userId + "' and flag=1 ";
					if (stmt.executeUpdate(cSql) > 0) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return false;
	}
	public static boolean delApi(String userId, String platform) {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.createStatement();

			platform = FunctionSet.filt(platform);
			userId = FunctionSet.filt(userId);

			String cSql = " update apis set flag=0 where userId='" + userId + "' and platform='" + platform + "' ";
			boolean flag = stmt.executeUpdate(cSql) > 0;
			if (flag) {
				cachedApis.remove(userId + "_" + platform);
			}
			return flag;
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return false;
	}

	public static Apis getApi(String userId, String platform) {
		Connection conn = null;
		Statement stmt = null;
		Apis apis = new Apis();
		try {
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.createStatement();

			String sql = "select * from apis where flag=1 and isEnable=1 and userId='" + FunctionSet.filt(userId)
					+ "' and platform='" + FunctionSet.filt(platform) + "'";
			List<Apis> ds = DBUtil.getInstance().convert(stmt.executeQuery(sql), Apis.class);
			if (!ds.isEmpty()) {
				apis = ds.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return apis;
	}

	public static List<Apis> getApiList(String userId) {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.createStatement();

			String sql = "select * from apis where flag=1 and isEnable=1 and userId='" + FunctionSet.filt(userId) + "'";

			List<Apis> ds = DBUtil.getInstance().convert(stmt.executeQuery(sql), Apis.class);
			return ds;
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return new ArrayList<Apis>(0);
	}

	public static Apis addApi(String userId, String platform, String key, String secret) {
		Connection conn = null;
		Statement stmt = null;
		userId = FunctionSet.filt(userId);
		platform = FunctionSet.filt(platform);
		key = FunctionSet.filt(key);
		secret = FunctionSet.filt(secret);
		Apis api = new Apis();
		try {
			if (StringUtils.isBlank(key) || StringUtils.isBlank(secret)) {
				return api;
			}

			conn = DBUtil.getInstance().getConnection();
			stmt = conn.createStatement();
			Long time = System.currentTimeMillis();

			String sql = "select * from apis where flag=1 and userId='" + userId + "' and platform='" + platform + "'";

			List<Apis> ds = DBUtil.getInstance().convert(stmt.executeQuery(sql), Apis.class);
			if (!ds.isEmpty()) {
				api = ds.get(0);

				String cSql = " update apis set apiKey='" + key + "',apiSecret='" + secret + "',updateTime="
						+ System.currentTimeMillis() + " where userId='" + userId + "' and platform='" + platform + "'";
				stmt.executeUpdate(cSql);
			}

			if (api.getId() <= 0) {
				StringBuilder buffer = new StringBuilder();

				buffer.append("insert into apis(userId,platform,apiKey,apiSecret,flag,createTime,updateTime) values('");
				buffer.append(userId);
				buffer.append("','");
				buffer.append(platform);
				buffer.append("','");
				buffer.append(key);
				buffer.append("','");
				buffer.append(secret);
				buffer.append("',1,");
				buffer.append(time);
				buffer.append(",");
				buffer.append(time);
				buffer.append(")");

				stmt.executeUpdate(buffer.toString());

				sql = "select id,userId,apiKey,isEnable from apis where flag=1 and platform='" + platform
						+ "' and userId='" + userId + "'";

				ds = DBUtil.getInstance().convert(stmt.executeQuery(sql), Apis.class);
				if (!ds.isEmpty()) {
					api = ds.get(0);
					cachedApis.put(userId + "_" + platform, api);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return api;
	}

	public static  User addUser(String phone, String phoneValidCode) {
		Connection conn = null;
		Statement stmt = null;
		phone = FunctionSet.filt(phone);
		User user = new User();
		user.setId("1");
		user.setErrorMsg("注册失败");
		try {
			if (phone.isEmpty()) {
				return user;
			}
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.createStatement();
			Long time = System.currentTimeMillis();
			String sql = "select id,userId,phone from user where flag=1 and phone='" + phone + "' ";
			List<User> ds = DBUtil.getInstance().convert(stmt.executeQuery(sql), User.class);
			if (!ds.isEmpty()) {
				user.setErrorMsg("手机号已注册");
				return user;
			}
			if (user.getId().equals("1")) {
				StringBuilder buffer = new StringBuilder();
				String uid = UUID.randomUUID().toString().replaceAll("-", "");
				buffer.append("insert into user(userId,phone,phoneValidCode,flag,createTime,updateTime) values('");
				buffer.append(uid);
				buffer.append("','");
				buffer.append(FunctionSet.filt(phone));
				buffer.append("','");
				buffer.append(FunctionSet.filt(phoneValidCode));
				buffer.append("',1,");
				buffer.append(time);
				buffer.append(",");
				buffer.append(time);
				buffer.append(")");
				stmt.executeUpdate(buffer.toString());
				sql = "select * from user where flag=1 and phone='" + phone + "'";
				ds = DBUtil.getInstance().convert(stmt.executeQuery(sql), User.class);
				if (!ds.isEmpty()) {
					user.setErrorMsg("");
					user = ds.get(0);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return user;
	}

	public static User getUser(String userId, Statement stmt) {
		return getUser("", "", userId, stmt);
	}

	public static User getUser(String phone, String pwd, String userId, Statement stmt) {
		Connection conn = null;
		boolean create = false;
		User user = new User();
		try {
			if (stmt == null) {
				conn = DBUtil.getInstance().getConnection();
				stmt = conn.createStatement();
				create = true;
			}

			userId = FunctionSet.filt(userId);

			phone = FunctionSet.filt(phone);
			pwd = FunctionSet.filt(pwd);

			boolean valid = StringUtils.isNotBlank(userId)
					|| (StringUtils.isNotBlank(phone) && StringUtils.isNotBlank(pwd));

			String cSql = "";
			if (StringUtils.isNotBlank(userId)) {
				cSql += " and userId='" + userId + "'";
			}

			if (StringUtils.isNotBlank(phone)) {
				cSql += " and phone='" + phone + "'";
			}
			if (StringUtils.isNotBlank(pwd)) {
				pwd = DigestUtils.md5Hex(pwd);
				cSql += " and pwd='" + pwd + "'";
			}

			if (StringUtils.isBlank(cSql) || !valid) {
				return user;
			}

			String sql = "select * from user where flag=1 " + cSql;
			List<User> ds = DBUtil.getInstance().convert(stmt.executeQuery(sql), User.class);
			if (!ds.isEmpty()) {
				user = ds.get(0);

				sql = "select userId,exchangid,isEnable,createTime,updateTime from apis  " + " where userId='"
						+ user.getUserId() + "' and flag=1 " + " order by createTime DESC LIMIT 100 ";

				List<Apis> dds = DBUtil.getInstance().convert(stmt.executeQuery(sql), Apis.class);
				if (!dds.isEmpty()) {
					user.setApis(dds);
				}
			} else {
				user.setErrorMsg("用户名或者密码错误！");
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (create) {
				DBUtil.getInstance().close(stmt);
				DBUtil.getInstance().close(conn);
			}
		}
		return user;
	}

	public static User login(String phone, String pwd, String twoFactorCode) {
		User u = getUser(phone, pwd, "", null);
		if (StringUtils.isNotBlank(twoFactorCode) && StringUtils.isNotBlank(u.getTwoFactorKey())) {
			if (twoFactorCode.equals(GoogleAuthenticator.getTOTPCode(u.getTwoFactorKey()))) {
				return u;
			} else {
				u = new User();
				u.setErrorMsg("谷歌双重验证错误");
			}
		}

		if (StringUtils.isBlank(twoFactorCode) && StringUtils.isNotBlank(u.getTwoFactorKey())) {
			u = new User();
			u.setErrorMsg("谷歌双重验证错误");
		}

		return u;
	}

	public static boolean addTwoFactorCode(String userId, String twoFactorKey) {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.createStatement();

			userId = FunctionSet.filt(userId);
			twoFactorKey = FunctionSet.filt(twoFactorKey);

			String cSql = " update user set twoFactorKey='" + twoFactorKey + "' where userId='" + userId
					+ "' and flag=1 ";
			return stmt.executeUpdate(cSql) > 0;

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return false;
	}

	public static User register(String phone, String pwd, String phoneValidCode) {
		Connection conn = null;
		Statement stmt = null;
		User user = new User();
		try {
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.createStatement();

			String cSql = " select userId,phone from user where phone='" + phone + "' and phoneValidCode!='"
					+ phoneValidCode + "'  and flag=1 ";
			List<User> uList = DBUtil.getInstance().convert(stmt.executeQuery(cSql), User.class);
			if (!uList.isEmpty()) {
				user.setErrorMsg("无效的验证码");
				return user;
			}

			pwd = FunctionSet.filt(pwd);
			pwd = DigestUtils.md5Hex(pwd);
			phone = FunctionSet.filt(phone);
			phoneValidCode = FunctionSet.filt(phoneValidCode);
			long time = System.currentTimeMillis();

			cSql = " update user set pwd='" + pwd + "',phoneValidFlag=1,updateTime=" + System.currentTimeMillis()
					+ " where phone='" + phone + "' and phoneValidCode='" + phoneValidCode + "' and flag=1 ";
			if (stmt.executeUpdate(cSql) > 0) {
				cSql = " select * from user where updateTime=" + time + " and phone='" + phone + "' and flag=1 ";
				uList = DBUtil.getInstance().convert(stmt.executeQuery(cSql), User.class);
				if (!uList.isEmpty()) {
					return uList.get(0);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return user;
	}

	public static  boolean sendValidCode(String phone, String phoneValidCode,String code) {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.createStatement();
			phone = FunctionSet.filt(phone);
			phoneValidCode = FunctionSet.filt(phoneValidCode);
			if(randomcode.equals(code)){
			if (SendSMSValidCode.sendSMS(phone,"【币大师】短信验证码是" + phoneValidCode)) {
				String msg = addUser(phone, phoneValidCode).getErrorMsg();
				if (StringUtils.isBlank(msg)) {
					return true;
				} else {
					return false;
				}
			}}
			else{
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return false;
	}
	public static  boolean sendValidCode1(String phone, String phoneValidCode,String code) {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.createStatement();
			phone = FunctionSet.filt(phone);
			phoneValidCode = FunctionSet.filt(phoneValidCode);
			String Sql = " select * from user where phone='"+ phone+"'";
			List<User> uList = DBUtil.getInstance().convert(stmt.executeQuery(Sql), User.class);
			if(!uList.isEmpty()){
			if (SendSMSValidCode.sendSMS(phone,"【币大师】短信验证码是" + phoneValidCode)) {
				String msg = updateUsercode(phone, phoneValidCode).getErrorMsg();
				if (StringUtils.isBlank(msg)) {
					return true;
				} else {
					return false;
				}
				}
			}
			else{
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return false;
	}

	private static User updateUsercode(String phone, String phoneValidCode) {
		Connection conn = null;
		Statement stmt = null;
		phone = FunctionSet.filt(phone);
		User user = new User();
		user.setId("1");
		user.setErrorMsg("重置失败");
		try {
			if (phone.isEmpty()) {
				return user;
			}
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.createStatement();
			if (user.getId().equals("1")) {
				String sql = "update user set phoneValidCode='"+phoneValidCode+"'  where flag=1 and phone='" + phone + "'";
				if (stmt.executeUpdate(sql)>0) {
					user.setErrorMsg("");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return user;
	}
	public static boolean sendPassword(String phone) {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.createStatement();

			phone = FunctionSet.filt(phone);

			String pwd = FunctionSet.filt("6");

			String cSql = " update user set pwd='" + DigestUtils.md5Hex(pwd) + "' where phone='" + phone
					+ "' and flag=1 ";
			if (SendSMSValidCode.sendSMS(phone, "新登录密码：" + pwd)) {
				return stmt.executeUpdate(cSql) > 0;
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return false;
	}

	public static User resetPassword(String phone, String pwd, String code) {
		Connection conn = null;
		Statement stmt = null;

		User user = new User();
		try {
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.createStatement();

			pwd = FunctionSet.filt(pwd);
			phone = FunctionSet.filt(phone);

			pwd = DigestUtils.md5Hex(pwd);
			code = FunctionSet.filt(code);

			String cSql = " update user set pwd='" + pwd + "',updateTime=" + System.currentTimeMillis()
					+ " where phone='" + phone + "' and flag=1 and phoneValidCode='" + code + "' ";
			if (stmt.executeUpdate(cSql) > 0) {
				return getUser(phone, "", "", stmt);
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return user;
	}

	public static User setPassword(String userId, String pwd, String oldPwd) {
		Connection conn = null;
		Statement stmt = null;

		User user = new User();
		try {
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.createStatement();

			pwd = FunctionSet.filt(pwd);
			userId = FunctionSet.filt(userId);

			pwd = DigestUtils.md5Hex(pwd);
			oldPwd = DigestUtils.md5Hex(oldPwd);

			String cSql = " update user set pwd='" + pwd + "',updateTime=" + System.currentTimeMillis()
					+ " where userId='" + userId + "' and flag=1 and pwd='" + oldPwd + "' ";
			if (stmt.executeUpdate(cSql) > 0) {
				return getUser(userId, stmt);
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return user;
	}

	public static boolean addWallet(String userId, String addr, String key, String birdToken) {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.createStatement();

			addr = FunctionSet.filt(addr);
			key = FunctionSet.filt(key);
			userId = FunctionSet.filt(userId);

			String cSql = " update user set walletAddr='" + addr + "', walletKey='" + key + "', birdToken='" + birdToken
					+ "',updateTime=" + System.currentTimeMillis() + " where userId='" + userId + "' and flag=1 ";
			if (stmt.executeUpdate(cSql) > 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return false;
	}

	public static User restoreUserBy(String addr, String key, String pwd) {
		Connection conn = null;
		Statement stmt = null;
		User user = new User();
		try {
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.createStatement();

			pwd = DigestUtils.md5Hex(pwd);

			String sql = "select * from user where walletKey='" + FunctionSet.filt(key) + "' and walletAddr='"
					+ FunctionSet.filt(addr) + "' and pwd='" + pwd + "'";
			List<User> uList = DBUtil.getInstance().convert(stmt.executeQuery(sql), User.class);
			if (!uList.isEmpty()) {
				return uList.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return user;
	}

	/*public static PagableData<User> getUserList(String start, String limit) {
		Connection conn = null;
		Statement stmt = null;
		start = FunctionSet.filt(start);
		limit = FunctionSet.filt(limit);
		PagableData<User> pd = new PagableData<User>();
		try {
			conn = DBUtil.getInstance(false).getConnection();
			stmt = conn.createStatement();

			String sql = "select walletAddr,phone,id,createTime from user  where flag=1 and walletAddr!='' order by createTime ASC limit "
					+ start + "," + limit;

			List<User> oList = DBUtil.getInstance().convert(stmt.executeQuery(sql), User.class);
			if (!oList.isEmpty()) {

				pd.setDataList(oList);

				String countSql = "select count(id) c from user  where flag=1 and walletAddr!=''";

				ResultSet rs = stmt.executeQuery(countSql);
				if (rs.next()) {
					pd.setTotalCount(rs.getInt("c"));
					pd.setItemCount(Integer.parseInt(limit));
					pd.setItemStart(Integer.parseInt(start));
				}
				rs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		try {
			Map<String, Double> map = getBalanceOfAddr();
			for (User u : pd.getDataList()) {
				Double amt = map.get(u.getWalletAdder());
				if (amt != null) {
					u.setBirdToken(new BigDecimal(amt / 10000).setScale(4, RoundingMode.HALF_UP));
				} else {
					u.setBirdToken(BigDecimal.ZERO);
				}
				
				String phone = u.getPhone().substring(0, 3) +"****"+ u.getPhone().substring(7, 11);
				u.setPhone(phone);

			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pd;
	}*/

	public static String uri = "http://testnet.api.coloredcoins.org/v3/stakeholders/La8KNZTWQJGEUYfzxSEugGwpDQRUE8JCMeRSXE/1";
	public static StatusBean getradomcode(String code) {
		StatusBean bean=new StatusBean();
		bean.setOtherCode(code);
		randomcode=code;
		return bean;
	}
}
