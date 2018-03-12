/**
 * 
 */
package com.bdh.db.rest;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;

import org.apache.commons.lang.StringUtils;

import com.bdh.db.dao.UserDao;
import com.bdh.db.entry.PagableData;
import com.bdh.db.entry.StatusBean;
import com.bdh.db.entry.User;
import com.bdh.db.rest.GoogleAuthenticator;
import com.bdh.db.util.FunctionSet;
//import org.asaph.twofactorauth.GoogleAuthenticator;

import com.bdh.db.util.FunctionSet;
//import com.bdh.db.dao.HomeDao;
import com.bdh.db.dao.UserDao;
//import com.bdh.db.entry.Certification;
//import com.bdh.db.entry.HomeBean;
import com.bdh.db.entry.PagableData;
import com.bdh.db.entry.User;
//import com.bdh.rest.jaxb.StatusBean;
import com.sun.jersey.api.json.JSONWithPadding;

@Path("/xchange/user")
@Produces({ "application/x-javascript", "application/json", "application/xml" })
public class UserService {

	public static final StatusBean success = new StatusBean();
	public static final StatusBean failed = new StatusBean(0, "");

	@POST
	@Produces({ "application/json" })
	@Path("/login")
	public JSONWithPadding login(@FormParam("phone") String phone, @FormParam("pwd") String pwd) throws Exception {
		return new JSONWithPadding(new GenericEntity<User>(UserDao.login(phone, pwd)) {
		}, "user");
	}
	@POST
	@Produces({ "application/json" })
	@Path("/login_factor2")
	public JSONWithPadding checkFactor2(@FormParam("userId") String userId,
			@FormParam("twoFactorCode") String twoFactorCode) throws Exception {
		return new JSONWithPadding(new GenericEntity<User>(UserDao.loginBy2FactorCode(userId, twoFactorCode)) {
		}, "user");
	}

	@POST
	@Produces({ "application/json" })
	@Path("/enable_factor2")
	public JSONWithPadding enableFactor2(@FormParam("userId") String userId,
			@FormParam("twoFactorKey") String twoFactorKey, @FormParam("twoFactorCode") String twoFactorCode)
			throws Exception {
		if (UserDao.enableTwoFactorKey(userId, twoFactorCode, twoFactorKey)) {
			return new JSONWithPadding(success);
		} else {
			return new JSONWithPadding(failed);
		}
	}

	@POST
	@Produces({ "application/json" })
	@Path("/cancel_factor2")
	public JSONWithPadding cancelFactor2(@FormParam("userId") String userId,
			@FormParam("twoFactorCode") String twoFactorCode) throws Exception {
		if (UserDao.cancelTwoFactorKey(userId, twoFactorCode)) {
			return new JSONWithPadding(success);
		} else {
			return new JSONWithPadding(failed);
		}
	}

	@POST
	@Produces({ "application/json" })
	@Path("/two_factor_qr")
	public JSONWithPadding twoFactorQR(@FormParam("userId") String userId) throws Exception {
		StatusBean bean = new StatusBean();
		User u = UserDao.getUser(userId, null);
		String twoFactorKey = "";
		if (StringUtils.isNotBlank(u.getTwoFactorKey())) {
			bean.setTargetId(Long.parseLong(u.getId()));
			twoFactorKey = u.getTwoFactorKey();
		} else {
			bean.setTargetId(-1L);
			twoFactorKey = GoogleAuthenticator.getRandomSecretKey();
		}
		if (StringUtils.isNotBlank(twoFactorKey)) {

			String barCode = GoogleAuthenticator.getGoogleAuthenticatorBarCode(twoFactorKey, "bitdash.net", "BDH(币大师)");
			bean.setOtherCode(barCode);
			bean.SetOtherCode1(twoFactorKey);
			return new JSONWithPadding(bean);
		} else {
			return new JSONWithPadding(failed);
		}
	}

	/*@POST
	@Produces({ "application/json" })
	@Path("/home_data")
	public JSONWithPadding home(@FormParam("userId") String userId, @FormParam("platform") String platform)
			throws Exception {
		return new JSONWithPadding(new GenericEntity<HomeBean>(HomeDao.getHomeBean(platform, userId)) {
		}, "home");
	}*/

	@POST
	@Produces({ "application/json" })
	@Path("/detail")
	public JSONWithPadding detail(@FormParam("userId") String userId) throws Exception {
		return new JSONWithPadding(new GenericEntity<User>(UserDao.getUser(userId, null)) {
		}, "user");
	}

	@POST
	@Produces({ "application/json" })
	@Path("/sendverifycode")
	public JSONWithPadding sms(@FormParam("phone") String phone,
							@FormParam("value") String value,
							@FormParam("code") String code) throws Exception {
		if(value.equals("signup")){
		if (UserDao.sendValidCode(phone, FunctionSet.getVerifyCode(4),code)){
			return new JSONWithPadding(success);
		} else {
			return new JSONWithPadding(failed);
		}
		}
		else if(value.equals("reset")){
			if (UserDao.sendValidCode1(phone, FunctionSet.getVerifyCode(4),code)){
				return new JSONWithPadding(success);
			} else {
				return new JSONWithPadding(failed);
			}
		}
		else{
		return null;
		}
	}
	

	@POST
	@Produces({ "application/json" })
	@Path("/send_pwd")
	public JSONWithPadding sendPwd(@FormParam("phone") String phone) throws Exception {
		if (UserDao.sendPassword(phone)) {
			return new JSONWithPadding(success);
		} else {
			return new JSONWithPadding(failed);
		}
	}

	@POST
	@Produces({ "application/json" })
	@Path("/reset_password")
	public JSONWithPadding resetpwd(@FormParam("phone") String userId, @FormParam("pwd") String pwd,
			@FormParam("code") String code) throws Exception {
		return new JSONWithPadding(new GenericEntity<User>(UserDao.resetPassword(userId, pwd, code)) {
		}, "user");
	}

	@POST
	@Produces({ "application/json" })
	@Path("/register")
	public JSONWithPadding register(@FormParam("userId") String userId, @FormParam("phone") String phone,
			@FormParam("pwd") String pwd, @FormParam("phoneValidCode") String phoneValidCode
			) throws Exception {
		return new JSONWithPadding(new GenericEntity<User>(UserDao.registe(phone, pwd, phoneValidCode,userId)) {
		}, "user");
	}

	@POST
	@Produces({ "application/json" })
	@Path("/change_password")
	public JSONWithPadding setpwd(@FormParam("userId") String userId, @FormParam("pwd") String pwd,
			@FormParam("oldPwd") String oldPwd) throws Exception {
		return new JSONWithPadding(new GenericEntity<User>(UserDao.setPassword(userId, pwd, oldPwd)) {
		}, "user");
	}

	@POST
	@Produces({ "application/json" })
	@Path("/add_wallet")
	public JSONWithPadding addWallet(@FormParam("userId") String userId, @FormParam("addr") String addr,
			@FormParam("key") String key, @FormParam("birdToken") String birdToken) throws Exception {
		if (UserDao.addWallet(userId, addr, key, birdToken)) {
			return new JSONWithPadding(success);
		} else {
			return new JSONWithPadding(failed);
		}
	}

	@POST
	@Produces("application/json")
	@Path("/resetPwd")
	public JSONWithPadding sendCode(@FormParam("phone") String phone, @FormParam("code") String code,
			@FormParam("newPwd") String newPwd) {
		return new JSONWithPadding(new GenericEntity<User>(UserDao.resetPassword(phone, code, newPwd)) {
		}, "resetPwd");
	}
/*
	@POST
	@Produces({ "application/json" })
	@Path("/token_list")
	public JSONWithPadding token_list(@FormParam("start") String start, @FormParam("limit") String limit)
			throws Exception {
		return new JSONWithPadding(new GenericEntity<PagableData<User>>(UserDao.getUserList(start, limit)) {
		}, "user");
	}*/

	/*@POST
	@Produces({ "application/json" })
	@Path("/add_realname")
	public JSONWithPadding realNameValid(@FormParam("userId") String userId, @FormParam("phone") String phone,
			@FormParam("realName") String realName, @FormParam("idCard") String idCard) throws Exception {
		return new JSONWithPadding(
				new GenericEntity<Certification>(UserDao.startRemoteValid(userId, phone, realName, idCard)) {
				}, "user");
	}*/

	/*@POST
	@Produces({ "application/json" })
	@Path("/validataion_info")
	public JSONWithPadding getNameCert(@FormParam("userId") String userId) throws Exception {
		return new JSONWithPadding(new GenericEntity<Certification>(UserDao.getCertification(userId)) {
		}, "user");
	}*/
//滑块验证/*
	@POST
	@Produces({ "application/json" })
	@Path("/silder")
	public JSONWithPadding silder(@FormParam("code") String code) throws Exception {
		return new JSONWithPadding(new GenericEntity<StatusBean>(UserDao.getradomcode(code)) {
		}, "silder");
	}
	
}
