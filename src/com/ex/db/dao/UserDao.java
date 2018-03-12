/*package com.ex.db.dao;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.asaph.twofactorauth.GoogleAuthenticator;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.ex.com.util.FunctionSet;
import com.ex.com.util.HttpUtils;
import com.ex.com.util.IdcardValidator;
import com.ex.db.entry.Apis;
import com.ex.db.entry.Certification;
import com.ex.db.entry.InToken;
import com.ex.db.entry.PagableData;
import com.ex.db.entry.User;
import com.ex.db.util.DBUtil;
import com.ex.msg.mob.SmsSender;
import com.ex.rest.jaxb.StatusBean;
import com.x.main.ExchangeProducer;

public class UserDao {

	public static String nameValidURI = "http://cellphone.haoservice.com/efficient/cellphone";

	public static Map<String, Apis> cachedApis = new ConcurrentHashMap<String, Apis>();

	public static void loadAllApis() {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.createStatement();

			String sql = "select * from apis where flag=1 and isEnable=1";

			List<Apis> ds = DBUtil.getInstance().convert(stmt.executeQuery(sql), Apis.class);
			for (Apis api : ds) {
				String kid = api.getUserId() + "_" + api.getPlatform();
				cachedApis.put(kid, api);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
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

	public static User addUser(String phone, String phoneValidCode) {
		Connection conn = null;
		Statement stmt = null;
		phone = FunctionSet.filt(phone);
		User user = new User();
		user.setError("注册失败");
		try {
			if (StringUtils.isBlank(phone)) {
				return user;
			}

			conn = DBUtil.getInstance().getConnection();
			stmt = conn.createStatement();
			Long time = System.currentTimeMillis();

			String sql = "select id,userId,phone from user where flag=1 and phone='" + phone + "' ";

			List<User> ds = DBUtil.getInstance().convert(stmt.executeQuery(sql), User.class);
			if (!ds.isEmpty()) {
				user = ds.get(0);

				String cSql = " update user set phoneValidCode='" + phoneValidCode + "',updateTime="
						+ System.currentTimeMillis() + " where phone='" + phone + "' and flag=1 ";

				stmt.executeUpdate(cSql);
			}

			if (user.getId() <= 0) {
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
					user.setError("");
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

				sql = "select * from inToken  " + " where id='" + user.getId();

				List<InToken> dds = DBUtil.getInstance().convert(stmt.executeQuery(sql), InToken.class);
				if (!dds.isEmpty()) {
					user.setInToken(dds.get(0));
				}
			} else {
				user.setError("用户名或者密码错误！");
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

	public static User register(String phone, String pwd, String phoneValidCode, String referAddr) {
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
				user.setError("无效的验证码！");
				return user;
			}

			pwd = FunctionSet.filt(pwd);
			pwd = DigestUtils.md5Hex(pwd);
			phone = FunctionSet.filt(phone);
			phoneValidCode = FunctionSet.filt(phoneValidCode);
			referAddr = FunctionSet.filt(referAddr);

			long time = System.currentTimeMillis();

			cSql = " update user set pwd='" + pwd + "',phoneValidFlag=1,updateTime=" + System.currentTimeMillis()
					+ ",referAddr='" + referAddr + "' where phone='" + phone + "' and phoneValidCode='" + phoneValidCode
					+ "' and flag=1 ";
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

	public static boolean sendValidCode(String phone, String phoneValidCode) {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.createStatement();

			phone = FunctionSet.filt(phone);
			phoneValidCode = FunctionSet.filt(phoneValidCode);

			if (SmsSender.sendSMS(phone, "短信验证码：" + phoneValidCode)) {

				String msg = addUser(phone, phoneValidCode).getError();

				if (StringUtils.isBlank(msg)) {
					return true;
				} else {
					return false;
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

	public static boolean sendPassword(String phone) {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.createStatement();

			phone = FunctionSet.filt(phone);

			String pwd = FunctionSet.getPasswordCode(6);

			String cSql = " update user set pwd='" + DigestUtils.md5Hex(pwd) + "' where phone='" + phone
					+ "' and flag=1 ";
			if (SmsSender.sendSMS(phone, "新登录密码：" + pwd)) {
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

			String newPwd = FunctionSet.filt(pwd);
			phone = FunctionSet.filt(phone);

			pwd = DigestUtils.md5Hex(pwd);
			code = FunctionSet.filt(code);

			String cSql = " update user set pwd='" + pwd + "',updateTime=" + System.currentTimeMillis()
					+ " where phone='" + phone + "' and flag=1 and phoneValidCode='" + code + "' ";
			if (stmt.executeUpdate(cSql) > 0) {
				user = getUser(phone, newPwd, "", stmt);
				System.out.println(user.getFlag());
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

	public static Certification getCertification(String userId) {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.createStatement();

			String cSql = " select * from certification where flag=1 and userId='" + FunctionSet.filt(userId) + "'";
			List<Certification> uList = DBUtil.getInstance().convert(stmt.executeQuery(cSql), Certification.class);
			if (!uList.isEmpty()) {
				return uList.get(uList.size() - 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return new Certification();
	}

	public static Certification addNameCertifaction(String userId, String phone, String realName, String idCard,
			String nameCertFlag) {
		Connection conn = null;
		Statement stmt = null;
		Certification bean = new Certification();
		try {
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.createStatement();
			conn.setAutoCommit(false);

			userId = FunctionSet.filt(userId);
			phone = FunctionSet.filt(phone);
			realName = FunctionSet.filt(realName);
			idCard = FunctionSet.filt(idCard);
			nameCertFlag = FunctionSet.filt(nameCertFlag);

			String cSql = " select * from certification where phone='" + phone + "' and userId='" + userId + "'";
			List<Certification> uList = DBUtil.getInstance().convert(stmt.executeQuery(cSql), Certification.class);
			if (!uList.isEmpty()) {
				Certification cf = uList.get(uList.size() - 1);
				if (cf.getValidFlag() == 1) {
					return bean;
				} else {
					String updateSql = "update certification set updateTime=" + System.currentTimeMillis() + ",phone='"
							+ phone + "',idCard='" + idCard + "',realName='" + realName + "',validFlag=" + nameCertFlag
							+ " where userId='" + userId + "'";
					if (stmt.executeUpdate(updateSql) > 0) {
						updateSql = "update user set nameCertFlag=" + nameCertFlag + " where userId='" + userId + "'";
						stmt.executeUpdate(updateSql);
					}
				}
			} else {

				long time = System.currentTimeMillis();
				StringBuilder buffer = new StringBuilder();

				buffer.append(
						"insert into certification(userId,phone,idCard,realName,validFlag,flag,createTime,updateTime) values('");
				buffer.append(userId);
				buffer.append("','");
				buffer.append(phone);
				buffer.append("','");
				buffer.append(idCard);
				buffer.append("','");
				buffer.append(realName);
				buffer.append("',");
				buffer.append(nameCertFlag);
				buffer.append(",1,");
				buffer.append(time);
				buffer.append(",");
				buffer.append(time);
				buffer.append(")");

				if (stmt.executeUpdate(buffer.toString()) > 0) {

					String updateSql = "update user set nameCertFlag=" + nameCertFlag + " where userId='" + userId
							+ "'";
					stmt.executeUpdate(updateSql);
				}
			}

			cSql = " select * from certification where phone='" + phone + "' and userId='" + userId + "'";
			uList = DBUtil.getInstance().convert(stmt.executeQuery(cSql), Certification.class);

			bean = uList.get(uList.size() - 1);

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}

		return bean;
	}

	private static String readStream(InputStream in) {
		char[] buf = new char[4096];
		int size = 0;
		String data = new String();
		InputStreamReader reader = new InputStreamReader(in);
		try {
			while ((size = reader.read(buf, 0, 4096)) > 0) {
				data = data + String.copyValueOf(buf, 0, size);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	public static Certification startRemoteValid(String userId, String phone, String realName, String idCard)
			throws Exception {
		String nameCertFlag = "0";
		if (IdcardValidator.isValidatedAllIdcard(idCard) && isMobileNO(phone) && realName != null
				&& realName.length() > 1 && StringUtils.isNotBlank(userId)) {

			String host = "http://cellphone.haoservice.com";
			String path = "/efficient/cellphone";
			String method = "GET";
			String appcode = "6e58e27f2c7949e8a79ef2ef39c8097c";
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Authorization", "APPCODE " + appcode);
			Map<String, String> querys = new HashMap<String, String>();
			querys.put("idCard", idCard);
			querys.put("mobile", phone);
			querys.put("realName", realName);

			try {
				HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);

				String result = readStream(response.getEntity().getContent());

				JSONObject json = new JSONObject(result);
				System.out.println(json);

				Object c = json.get("error_code");
				if (c != null && c.toString().equals("0")) {
					JSONObject r = json.getJSONObject("result");
					if (r != null && r.get("VerificationResult") != null) {
						nameCertFlag = r.get("VerificationResult").toString();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return addNameCertifaction(userId, phone, realName, idCard, nameCertFlag);
		}

		return new Certification();
	}

	public static boolean isMobileNO(String mobiles) {
		if (StringUtils.isBlank(mobiles)) {
			return false;
		}

		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(17[0-9])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	public static boolean updateReferAddr(String userId, String referAddr) {
		Connection conn = null;
		Statement stmt = null;

		try {
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.createStatement();

			userId = FunctionSet.filt(userId);
			referAddr = FunctionSet.filt(referAddr);

			String updateSql = "update user set updateTime=" + System.currentTimeMillis() + ",referAddr='" + referAddr
					+ "' where userId='" + userId + "'";

			return stmt.executeUpdate(updateSql) > 0;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return false;
	}

	public static PagableData<User> getUserList(String start, String limit) {
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
			BigDecimal totalSent = BigDecimal.ZERO;
			Map<String, Double> map = getBalanceOfAddr();
			for (User u : pd.getDataList()) {
				Double amt = map.get(u.getWalletAddr());
				if (amt != null) {
					u.setBirdToken(new BigDecimal(amt / 10000).setScale(4, RoundingMode.HALF_UP));
				} else {
					u.setBirdToken(BigDecimal.ZERO);
				}

				String phone = u.getPhone().substring(0, 3) + "****" + u.getPhone().substring(7, 11);
				u.setPhone(phone);

				totalSent = totalSent.add(u.getBirdToken());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pd;
	}

	public static String uri = "http://testnet.api.coloredcoins.org/v3/stakeholders/La8KNZTWQJGEUYfzxSEugGwpDQRUE8JCMeRSXE/1";

	public static Map<String, Double> getBalanceOfAddr() throws JSONException, Exception {
		JSONObject jsonObj = new JSONObject(ExchangeProducer.readJsonFromUrl(uri));
		JSONArray jArr = jsonObj.getJSONArray("holders");
		List<Double> aa = new ArrayList<Double>();
		Map<String, Double> balanceMap = new HashMap<String, Double>();
		for (int i = 0; i < jArr.length(); i++) {
			String addr = jArr.getJSONObject(i).getString("address");
			double amt = jArr.getJSONObject(i).getDouble("amount");
			balanceMap.put(addr, amt);
			aa.add(amt);

			*//**
			 * if (amt <= 0) continue;
			 * 
			 * JSONObject jsonArr = new JSONObject(ExchangeProducer
			 * .readJsonFromUrl("https://testnet.explorer.coloredcoins.org/api/getaddressinfo?address="
			 * + addr));
			 * 
			 * int bl = jsonArr.getInt("balance");
			 * 
			 * if (bl < 12000) { System.out.println("testnet-faucet " + addr + "
			 * 98000"); }
			 *//*
		}

		
		 * Double[] data = aa.toArray(new Double[] {});
		 * 
		 * Arrays.sort(data); for (Double double1 : data) { //
		 * System.out.println(new BigDecimal(double1.doubleValue() / //
		 * 10000).setScale(4, RoundingMode.CEILING)); }
		 

		return balanceMap;
	}

	public static void auditNotice(String url, Map params) {
		String result = null;
		HttpClient httpclient = new DefaultHttpClient();// 声明HttpClient实例
		// 设置请求超时间
		// httpclient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
		// Integer.parseInt(Global.getConfig("http.connectionTimeout")));
		// 设置读取超时时间
		httpclient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
		// 创建HttpPost对象，将要请求的URL通过构造方法传入HttpPost对象。
		HttpPost httpPost = new HttpPost(url.toString());
		// 建立一个NameValuePair数组，用于存储欲传送的参数
		List nvps = new ArrayList();
		Set keySet = params.keySet();
		for (Object key : keySet) {
			// 添加参数
			nvps.add(new BasicNameValuePair(key.toString(), params.get(key).toString()));
		}
		try {
			// 使用HttpPost方法提交HTTP POST请求，则需要使用HttpPost类的setEntity方法设置请
			// 求参数。参数则必须用NameValuePair[]数组存储。
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));// 设置编码
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			// 使用DefaultHttpClient类的execute方法发送HTTP GET或HTTP POST请求，
			HttpResponse response = httpclient.execute(httpPost);
			// 并返回HttpResponse对象。
			HttpEntity entity = response.getEntity();
			StatusLine statusLine = response.getStatusLine();
			// 确认发送
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {// 链接成功
				// 通过HttpResponse接口的getEntity方法返回响应信息，并进行相应的处理。
				result = EntityUtils.toString(entity);
				System.out.println(result);
			} else {
				// 发送失败时的处理
				System.out.println("error");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpPost.releaseConnection();// 关闭连接
		}
	}

	public StatusBean sendResetCode(String userid, String phone) {
		StatusBean status = new StatusBean();
		status.setFlag(0);
		Connection conn = null;
		PreparedStatement stmt = null;
		Random r = new Random();
		int tag[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		String four = "";
		int temp = 0;
		while (four.length() != 4) {
			temp = r.nextInt(10);// 随机获取0~9的数字
			if (tag[temp] == 0) {
				four += temp;
				tag[temp] = 1;
			}
		}
		try {
			conn = DBUtil.getInstance().getConnection();
			phone = FunctionSet.filt(phone);
			if (SmsSender.sendSMS(phone, "短信验证码:" + four)) {
				StringBuilder buffer = new StringBuilder();
				buffer.append("update user set phoneValidCode='" + four + "' WHERE flag=1 and phone='" + phone + "'");
				stmt = conn.prepareStatement(buffer.toString());
				if (stmt.executeUpdate() > 0) {
					status.setFlag(1);
				}
				status.setFlag(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return status;
	}

	public static void main(String[] args) throws JSONException, Exception {
		// getUserList("0", "5000");
		// getBalanceOfAddr();
		// d41d8cd98f00b204e9800998ecf8427e
		*//**
		 * String uid = UUID.randomUUID().toString().replaceAll("-", "");
		 * System.out.println(uid); ／ long time ／** =1500994942785L;
		 * System.out.println(FunctionSet.formatDateTime(time));
		 *//*
		// resetPassword("13122869919", "javaman", "3832");
		// String msg = "币大师1000W送币活动圆满结束，感谢您的支持。为了保障已获得大师币用户的后期权益，"
		// + "如果您正在持有大师币，请您尽快与群秘 (微信:C24310515 荡起双桨) 联系并加入到 “币大师PoA行动群”，"
		// + "以后内部事宜（包括投票，重要通知、行动参与等），仅在此群下发。";

		// PagableData<User> uList = getUserList("0", "3500");
		// for (User u : uList.getDataList()) {
		// System.out.println(u.getPhone()+"--"+u.getBirdToken());
		// }e6e407b1edb2cca3def82992c8ef32d9
		// e6e407b1edb2cca3def82992c8ef32d9

		// System.out.println(DigestUtils.md5Hex("qwer123456"));

		// String secretKey = GoogleAuthenticator.getRandomSecretKey();
		// String barCode =
		// GoogleAuthenticator.getGoogleAuthenticatorBarCode(secretKey,
		// "bitdash.net", "BitDash(币大师)");
		//
		// System.out.println(barCode);
		// 王燕
		 startRemoteValid("qqq", "13937190353", "袁英奎", "411425195706225439");
		// System.out.println("0b74b45551524487b4f59e72fbe44918".length());
	}

}
*/