package com.bdh.db.dao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;


import com.bdh.db.entry.Fill;
import com.bdh.db.entry.PagableData;
import com.bdh.db.entry.Push;
import com.bdh.db.entry.StatusBean;
import com.bdh.db.entry.User;
import com.bdh.db.entry.Withdraw;
import com.bdh.db.util.DBUtil;
import com.bdh.db.util.FunctionSet;
import com.bdh.db.view.Buyfund;
import com.bdh.db.view.Frozenmoney;
import com.bdh.db.view.Frozenqty;
import com.bdh.db.view.MyBDH;
import com.bdh.db.view.MyUser;
import com.bdh.db.view.Sellfund;
import com.bdh.db.view.Userfund;
import com.bdh.db.view.Withdrawfund;
import com.bdh.db.view.Withdrawnofund;

/**
 * ����
 * 
 * */
public class AuditDao {

	/***
	 * 
	 * �����б� ����Ա��
	 * 
	 * */
	public PagableData<Withdraw> getPushList(String start, String limit) {
		Connection conn = null;
		PreparedStatement stmt = null;
		PagableData<Withdraw> pd = new PagableData<Withdraw>();
		try {
			start = FunctionSet.filt(start);
			limit = FunctionSet.filt(limit);
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT * FROM withdraw Order By wid asc limit ");
			sql.append(start);
			sql.append(",");
			sql.append(limit);
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.prepareStatement(sql.toString());
			List<Withdraw> list = DBUtil.getInstance().convert(
					stmt.executeQuery(), Withdraw.class);
			if (!list.isEmpty()) {
				pd.setDataList(list);
				String countsql = "SELECT COUNT(DISTINCT wid) c FROM withdraw";
				ResultSet rs = stmt.executeQuery(countsql);
				if (rs.next()) {
					pd.setTotalCount(rs.getInt("c"));
					pd.setItemCount(Integer.parseInt(limit));
					pd.setItemCount(Integer.parseInt(start));
				}
			}
			sql = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return pd;
	}

	/**
	 * ��ʾ���
	 * */
	public PagableData<Fill> getbalance(String userid) {
		Connection conn = null;
		PreparedStatement stmt = null;
		PagableData<Fill> pd = new PagableData<Fill>();
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT * FROM fill where userid= '"
					+ FunctionSet.filt(userid) + "'");
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.prepareStatement(sql.toString());
			List<Fill> list = DBUtil.getInstance().convert(stmt.executeQuery(),
					Fill.class);
			if (!list.isEmpty()) {
				pd.setDataList(list);
			}
			sql = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return pd;
	}

	/**
	 * ����������һ����¼
	 * */
	public StatusBean addWithdraw(Withdraw withdraw, String userid) {
	
		
		StatusBean bean = new StatusBean();
		bean.setFlag(0);
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			Long time = System.currentTimeMillis();
			conn = DBUtil.getInstance().getConnection();
			StringBuilder buffe = new StringBuilder();
			buffe.append("INSERT INTO withdraw (userid,amount,toAccount,bankName,commission,createTime,flag,applyFlag) ");
			buffe.append(" VALUES ('");
			buffe.append(FunctionSet.filt(userid));
			buffe.append("','");
			buffe.append(withdraw.getAmount());
			buffe.append("','");
			buffe.append(FunctionSet.filt(withdraw.getToAccount()));
			buffe.append("','");
			buffe.append(FunctionSet.filt(withdraw.getBankName()));
			buffe.append("','");
			buffe.append(withdraw.getCommission());
			buffe.append("',");
			buffe.append(time);
			buffe.append(",");
			buffe.append("1");
			buffe.append(",");
			buffe.append("0");
			buffe.append(")");
			if(!withdraw.getToAccount().isEmpty()&&!withdraw.getBankName().isEmpty()&&withdraw.getCommission()>0&&withdraw.getAmount()>0){
				stmt = conn.prepareStatement(buffe.toString());
				if (stmt.executeUpdate() > 0) {
					bean.setFlag(1);
				}
			}
			else{
				bean.setFlag(0);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return bean;
	}
	/**
	 * �ܶ��ʲ�
	 * */
	public MyUser getUserBalance(String userid) {
		Connection conn = null;
		PreparedStatement stmt = null;
		MyUser user= new MyUser();
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select * from user where userid='"
					+ FunctionSet.filt(userid) + "'");
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.prepareStatement(sql.toString());
			List<User> userList = DBUtil.getInstance().convert(
					stmt.executeQuery(), User.class);
			if (!userList.isEmpty()) {
				
			
				String username= userList.get(0).getName();
				user.setUsername(username);
				

				List<Userfund> list = DBUtil
						.getInstance()
						.convert(
								stmt.executeQuery("SELECT userfund.userId,userfund.fund FROM user RIGHT JOIN userfund "
										+ "on user.userId=userfund.userId where user.userId='"
										+ FunctionSet.filt(userid) + "'"),
								Userfund.class);
				String userfund = "";
				if (list.size() > 0) {
					userfund = list.get(0).getFund();
				} else {
					userfund = "0";
				}
				BigDecimal ufund = new BigDecimal(userfund).setScale(4,
						RoundingMode.HALF_UP);
				List<Sellfund> slist = DBUtil
						.getInstance()
						.convert(
								stmt.executeQuery("SELECT sellfund.pushUserId,sellfund.fund FROM user RIGHT JOIN sellfund "
										+ "on user.userId=sellfund.pushUserId where user.userId='"
										+ FunctionSet.filt(userid) + "'"),
								Sellfund.class);
				String sellfund = "";
				if (slist.size() > 0) {
					sellfund = slist.get(0).getFund();
				} else {
					sellfund = "0";
				}
				BigDecimal sfund = new BigDecimal(sellfund).setScale(4,
						RoundingMode.HALF_UP);
				List<Buyfund> ulist = DBUtil
						.getInstance()
						.convert(
								stmt.executeQuery("SELECT buyfund.userId,buyfund.fund FROM user RIGHT JOIN buyfund "
										+ "on user.userId=buyfund.userId where user.userId='"
										+ FunctionSet.filt(userid) + "'"),
								Buyfund.class);
				String buyfund = "";
				if (ulist.size() > 0) {
					buyfund = ulist.get(0).getFund();
				} else {
					buyfund = "0";
				}
				BigDecimal bfund = new BigDecimal(buyfund).setScale(4,
						RoundingMode.HALF_UP);

				List<Withdrawfund> wlist = DBUtil
						.getInstance()
						.convert(
								stmt.executeQuery("SELECT withdrawfund.userid,withdrawfund.fund FROM user RIGHT JOIN withdrawfund "
										+ "on `user`.userId= withdrawfund.userid where user.userId='"
										+ FunctionSet.filt(userid) + "'"),
								Withdrawfund.class);
				String withdrawfund = "";
				if (wlist.size() > 0) {
					withdrawfund = wlist.get(0).getFund();
				} else {
					withdrawfund = "0";
				}

				BigDecimal wfund = new BigDecimal(withdrawfund).setScale(4,
						RoundingMode.HALF_UP);

				BigDecimal fund1 = ufund.add(sfund).setScale(4,
						RoundingMode.HALF_UP);
				BigDecimal fund2 = fund1.subtract(wfund).setScale(4,
						RoundingMode.HALF_UP);
				BigDecimal fund = fund2.subtract(bfund).setScale(4,
						RoundingMode.HALF_UP);
				String str = fund.toString();//�ܽ��
				
				user.setMoney(str);
				
				//������
				List<Frozenmoney> qlist = DBUtil
						.getInstance()
						.convert(
								 stmt.executeQuery("SELECT frozenmoney.userid,frozenmoney.fund FROM user RIGHT JOIN frozenmoney "
										+ " on user.userId=frozenmoney.userid where user.userid='"
										+ FunctionSet.filt(userid) + "'"),
										Frozenmoney.class);

				String leftqty = "";
				if (qlist.size() > 0) {
					leftqty = qlist.get(0).getFund();
				} else {
					leftqty = "0";
				}
				BigDecimal lqty = new BigDecimal(leftqty).setScale(4,
						RoundingMode.HALF_UP);
				String strs = lqty.toString();
				
				user.setFundMoney(strs);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return user;
	}

	/**
	 * ʣ���ܶ��ʲ�
	 * */
	public PagableData<User> getUserBalanceMoney(String userid) {
		Connection conn = null;
		PreparedStatement stmt = null;
		PagableData<User> pd = new PagableData<User>();
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select * from user where userid='"
					+ FunctionSet.filt(userid) + "'");
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.prepareStatement(sql.toString());
			List<User> userList = DBUtil.getInstance().convert(
					stmt.executeQuery(), User.class);
			if (!userList.isEmpty()) {

				List<Userfund> list = DBUtil
						.getInstance()
						.convert(
								stmt.executeQuery("SELECT userfund.userId,userfund.fund FROM user RIGHT JOIN userfund "
										+ "on user.userId=userfund.userId where user.userId='"
										+ FunctionSet.filt(userid) + "'"),
								Userfund.class);
				String userfund = "";
				if (list.size() > 0) {
					userfund = list.get(0).getFund();
				} else {
					userfund = "0";
				}
				BigDecimal ufund = new BigDecimal(userfund).setScale(4,
						RoundingMode.HALF_UP);
				List<Sellfund> slist = DBUtil
						.getInstance()
						.convert(
								stmt.executeQuery("SELECT sellfund.pushUserId,sellfund.fund FROM user RIGHT JOIN sellfund "
										+ "on user.userId=sellfund.pushUserId where user.userId='"
										+ FunctionSet.filt(userid) + "'"),
								Sellfund.class);
				String sellfund = "";
				if (slist.size() > 0) {
					sellfund = slist.get(0).getFund();
				} else {
					sellfund = "0";
				}
				BigDecimal sfund = new BigDecimal(sellfund).setScale(4,
						RoundingMode.HALF_UP);
				List<Buyfund> ulist = DBUtil
						.getInstance()
						.convert(
								stmt.executeQuery("SELECT buyfund.userId,buyfund.fund FROM user RIGHT JOIN buyfund "
										+ "on user.userId=buyfund.userId where user.userId='"
										+ FunctionSet.filt(userid) + "'"),
								Buyfund.class);
				String buyfund = "";
				if (ulist.size() > 0) {
					buyfund = ulist.get(0).getFund();
				} else {
					buyfund = "0";
				}
				BigDecimal bfund = new BigDecimal(buyfund).setScale(4,
						RoundingMode.HALF_UP);

				List<Withdrawfund> wlist = DBUtil
						.getInstance()
						.convert(
								stmt.executeQuery("SELECT withdrawfund.userid,withdrawfund.fund FROM user RIGHT JOIN withdrawfund "
										+ "on `user`.userId= withdrawfund.userid where user.userId='"
										+ FunctionSet.filt(userid) + "'"),
								Withdrawfund.class);
				String withdrawfund = "";
				if (wlist.size() > 0) {
					withdrawfund = wlist.get(0).getFund();
				} else {
					withdrawfund = "0";
				}
				
				BigDecimal wfund = new BigDecimal(withdrawfund).setScale(4,
						RoundingMode.HALF_UP);
				List<Withdrawnofund> wnolist = DBUtil
						.getInstance()
						.convert(
								stmt.executeQuery("SELECT withdrawnofund.userid,withdrawnofund.fund FROM user RIGHT JOIN withdrawnofund "
										+ "on `user`.userId= withdrawnofund.userid where user.userId='"
										+ FunctionSet.filt(userid) + "'"),
										Withdrawnofund.class);
				String withdrawnofund = "";
				if (wnolist.size() > 0) {
					withdrawnofund = wnolist.get(0).getFund();
				} else {
					withdrawnofund = "0";
				}
				BigDecimal wnofund = new BigDecimal(withdrawnofund).setScale(4,
						RoundingMode.HALF_UP);
				List<Frozenmoney> qlist = DBUtil
						.getInstance()
						.convert(
								stmt.executeQuery("SELECT frozenmoney.userid,frozenmoney.fund FROM user RIGHT JOIN frozenmoney "
										+ " on user.userId=frozenmoney.userid where user.userid='"
										+ FunctionSet.filt(userid) + "'"),
										Frozenmoney.class);

				String leftqty = "";
				if (qlist.size() > 0) {
					leftqty = qlist.get(0).getFund();
				} else {
					leftqty = "0";
				}
				BigDecimal lqty = new BigDecimal(leftqty).setScale(4,
						RoundingMode.HALF_UP);

				

				BigDecimal fund1 = ufund.add(bfund).setScale(4,
						RoundingMode.HALF_UP);
				BigDecimal fund2 = fund1.subtract(wfund).setScale(4,
						RoundingMode.HALF_UP);
				BigDecimal fund3 = fund2.subtract(wnofund).setScale(4,
						RoundingMode.HALF_UP);
				BigDecimal fund = fund3.subtract(sfund).setScale(4,
						RoundingMode.HALF_UP);
				BigDecimal getmoney = fund.subtract(lqty).setScale(4,
						RoundingMode.HALF_UP);
				String str = getmoney.toString();

				for (int i = 0; i < userList.size(); i++) {
					userList.get(i).setBalances(str);
				}
				pd.setDataList(userList);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return pd;
	}
	
	/**
	 * ����BDH����
	 * **/
	//Ǯ��BDH

	
	public MyBDH getfreezing(String userid) {
		Connection conn = null;
		PreparedStatement stmt = null;
		MyBDH mybdh= new MyBDH();
		try {
			
			StringBuilder sql = new StringBuilder();
			sql.append("select * from user where userid='"
					+ FunctionSet.filt(userid) + "'");
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.prepareStatement(sql.toString());
			List<User> userList = DBUtil.getInstance().convert(
					stmt.executeQuery(), User.class);
			if (!userList.isEmpty()) {
				mybdh.setUsername(userList.get(0).getName());
				//����
				List<Frozenqty> qlist = DBUtil
						.getInstance()
						.convert(
								stmt.executeQuery("SELECT frozenqty.formUserId,frozenqty.leftQty FROM frozenqty LEFT JOIN user "
										+ " on frozenqty.formUserId=user.userid where frozenqty.formUserId='"
										+ FunctionSet.filt(userid) + "'"),
								Frozenqty.class);

				String leftqty = "";
				if (qlist.size() > 0) {
					leftqty = qlist.get(0).getLeftQty();
				} else {
					leftqty = "0";
				}
				BigDecimal lqty = new BigDecimal(leftqty).setScale(4,
						RoundingMode.HALF_UP);
				
				String str = lqty.toString();
				mybdh.setFundBDH(str);
				
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return mybdh;
	}
	
	
/**
 * ʣ��BDH��
 * */
	/*public PagableData<User> getsellorder(String userid) {
		Connection conn = null;
		PreparedStatement stmt = null;
		PagableData<User> pd = new PagableData<User>();
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select * from user where userid='"
					+ FunctionSet.filt(userid) + "'");
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.prepareStatement(sql.toString());
			List<User> userList = DBUtil.getInstance().convert(
					stmt.executeQuery(), User.class);
			if (!userList.isEmpty()) {
				List<Frozenqty> qlist = DBUtil
						.getInstance()
						.convert(
								stmt.executeQuery("SELECT frozenqty.formUserId,frozenqty.leftQty FROM frozenqty LEFT JOIN user "
										+ " on frozenqty.formUserId=user.userid where frozenqty.formUserId='"
										+ FunctionSet.filt(userid) + "'"),
								Frozenqty.class);

				String leftqty = "";
				if (qlist.size() > 0) {
					leftqty = qlist.get(0).getLeftQty();
				} else {
					leftqty = "0";
				}
				BigDecimal lqty = new BigDecimal(leftqty).setScale(4,
						RoundingMode.HALF_UP);
				
				
				List<Sellorder> slist = DBUtil
						.getInstance()
						.convert(
								stmt.executeQuery("SELECT sellorder.pushuserId,sellorder.sellamount FROM sellorder LEFT JOIN user "
										+ " on sellorder.pushuserId=user.userid where sellorder.pushuserId='"
										+ FunctionSet.filt(userid) + "'"),
										Sellorder.class);

				String amount = "";
				if (slist.size() > 0) {
					amount = slist.get(0).getSellamount();
				} else {
					amount = "0";
				}
				
				
				BigDecimal oamount = new BigDecimal(amount).setScale(4,
						RoundingMode.HALF_UP);
				
				BigDecimal getamount = lqty.subtract(oamount).setScale(4,
						RoundingMode.HALF_UP);
				String str = getamount.toString();

				for (int i = 0; i < userList.size(); i++) {
					userList.get(i).setBalances(str);
				}
				pd.setDataList(userList);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return pd;
	}*/
	
	/*public static void main(String[] args) {
		AuditDao dao = new AuditDao();
		List<User> list = dao.getsellorder("2023").getDataList();
		for (User user : list) {
		}
	}	*/
	
	

	/***
	 * ����Ա�鿴 �û��ʽ𶳽��б�
	 * */
	public PagableData<Push> pushForst(String start, String limit) {
		Connection conn = null;
		PreparedStatement stmt = null;
		PagableData<Push> pd = new PagableData<Push>();
		try {
			start = FunctionSet.filt(start);
			limit = FunctionSet.filt(limit);
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT  *,SUM(Qty) qty FROM push,user where push.formUserId=user.userId and  push.formUserId in(SELECT userId FROM user) GROUP BY "
					+ "  push.formUserId ");
			sql.append(" Order By push.formUserId asc limit ");
			sql.append(start);
			sql.append(",");
			sql.append(limit);
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.prepareStatement(sql.toString());
			List<Push> list = DBUtil.getInstance().convert(stmt.executeQuery(),
					Push.class);
			if (!list.isEmpty()) {
				pd.setDataList(list);
				String countsql = "SELECT COUNT(DISTINCT formuserid) c FROM push";
				ResultSet rs = stmt.executeQuery(countsql);
				if (rs.next()) {
					pd.setTotalCount(rs.getInt("c"));
					pd.setItemCount(Integer.parseInt(limit));
					pd.setItemCount(Integer.parseInt(start));
				}
			}
			sql = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return pd;
	}

	/**
	 * ��ѯ����б�
	 * */
	public PagableData<Withdraw> getWithdrawAll(String start, String limit) {
		Connection conn = null;
		PreparedStatement stmt = null;
		PagableData<Withdraw> pd = new PagableData<Withdraw>();
		try {
			start = FunctionSet.filt(start);
			limit = FunctionSet.filt(limit);
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT withdraw.*,user.name FROM withdraw,user WHERE user.userId=withdraw.userid ORDER BY withdraw.userid asc LIMIT ");
			sql.append(start);
			sql.append(",");
			sql.append(limit);
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.prepareStatement(sql.toString());
			List<Withdraw> list = DBUtil.getInstance().convert(
					stmt.executeQuery(), Withdraw.class);
			if (!list.isEmpty()) {
				pd.setDataList(list);
				String countsql = "SELECT COUNT(withdraw.userid) c FROM withdraw,user WHERE user.userId=withdraw.userid";
				ResultSet rs = stmt.executeQuery(countsql);
				if (rs.next()) {
					pd.setTotalCount(rs.getInt("c"));
					pd.setItemCount(Integer.parseInt(limit));
					pd.setItemCount(Integer.parseInt(start));
				}
			}
			sql = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return pd;
	}

	/**
	 * ����Աȷ�ϴ��
	 * */
	public StatusBean OkWithdraw(Withdraw withdraw, String adminId) {
	
		StatusBean bean = new StatusBean();
		bean.setFlag(0);
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = DBUtil.getInstance().getConnection();
			Long time=System.currentTimeMillis();
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT * FROM withdraw WHERE wid='"+FunctionSet.filt(withdraw.getWid())+"'");
			stmt = conn.prepareStatement(sql.toString());
			List<Withdraw> list = DBUtil.getInstance().convert(
					stmt.executeQuery(), Withdraw.class);
			if (!list.isEmpty()) {
			     double amount=0;
				for (int i = 0; i < list.size(); i++) {
					amount=list.get(i).getAmount();
				}
				PreparedStatement pstmt = null;
				StringBuilder buffe = new StringBuilder();
				buffe.append("UPDATE withdraw set adminId=?,confirmAmount=?,txId=?,applyFlag=1,adminTime="+time+",adminContext=? where wid=?");
				
			if(withdraw.getConfirmAmount()== amount){
				pstmt = conn.prepareStatement(buffe.toString());
				pstmt.setString(1, FunctionSet.filt(adminId));
				pstmt.setDouble(2, withdraw.getConfirmAmount());
				pstmt.setString(3, FunctionSet.filt(withdraw.getTxId()));
				pstmt.setString(4, FunctionSet.filt(withdraw.getAdminContext()));
				pstmt.setString(5, FunctionSet.filt(withdraw.getWid()));
				if (pstmt.executeUpdate() > 0) {
					bean.setFlag(1);
				}
				buffe = null;
			}
				
			}
		
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return bean;
	}
	
	
	public static void main(String[] args) {
		AuditDao dao= new AuditDao();
		Withdraw withdraw= new Withdraw();
		withdraw.setWid("1");
		withdraw.setConfirmAmount(200.781);;
		dao.OkWithdraw(withdraw, "11");
	}

	/**
	 * ����Ա�ܾ����
	 * */
	public StatusBean OffWithdraw(Withdraw withdraw, String adminId) {
		StatusBean bean = new StatusBean();
		bean.setFlag(0);
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			Long time= System.currentTimeMillis();
			conn = DBUtil.getInstance().getConnection();
			StringBuilder buffe = new StringBuilder();
			buffe.append("UPDATE withdraw set adminId=?,applyFlag=2,adminTime="+time+",adminContext=? where wid=?");
			stmt = conn.prepareStatement(buffe.toString());
			stmt.setString(1, FunctionSet.filt(adminId));
			stmt.setString(2, FunctionSet.filt(withdraw.getAdminContext()));
			stmt.setString(3, FunctionSet.filt(withdraw.getWid()));
			if (stmt.executeUpdate() > 0) {
				bean.setFlag(1);
			}
			buffe = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return bean;
	}

	// ������
	public PagableData<Withdraw> applying(String start, String limit) {
		Connection conn = null;
		PreparedStatement stmt = null;
		PagableData<Withdraw> pd = new PagableData<Withdraw>();
		try {
			start = FunctionSet.filt(start);
			limit = FunctionSet.filt(limit);
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT withdraw.* FROM withdraw,user WHERE user.userId=withdraw.userid and withdraw.applyFlag=0 and withdraw.flag=1 ORDER BY withdraw.createTime asc LIMIT ");
			sql.append(start);
			sql.append(",");
			sql.append(limit);
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.prepareStatement(sql.toString());
			List<Withdraw> list = DBUtil.getInstance().convert(
					stmt.executeQuery(), Withdraw.class);
			if (!list.isEmpty()) {
				pd.setDataList(list);
				String countsql = "SELECT COUNT(withdraw.wid) c FROM withdraw,user WHERE user.userId=withdraw.userid and withdraw.applyFlag=0";
				ResultSet rs = stmt.executeQuery(countsql);
				if (rs.next()) {
					pd.setTotalCount(rs.getInt("c"));
					pd.setItemCount(Integer.parseInt(limit));
					pd.setItemCount(Integer.parseInt(start));
				}
			}
			sql = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return pd;
	}

	// �����
	public PagableData<Withdraw> okapply(String start, String limit) {
		Connection conn = null;
		PreparedStatement stmt = null;
		PagableData<Withdraw> pd = new PagableData<Withdraw>();
		try {
			start = FunctionSet.filt(start);
			limit = FunctionSet.filt(limit);
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT withdraw.* FROM withdraw,user WHERE user.userId=withdraw.userid and withdraw.applyFlag=1 ORDER BY withdraw.createTime asc LIMIT ");
			sql.append(start);
			sql.append(",");
			sql.append(limit);
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.prepareStatement(sql.toString());
			List<Withdraw> list = DBUtil.getInstance().convert(
					stmt.executeQuery(), Withdraw.class);
			if (!list.isEmpty()) {
				pd.setDataList(list);
				String countsql = "SELECT COUNT(withdraw.wid) c FROM withdraw,user WHERE user.userId=withdraw.userid and withdraw.applyFlag=1";
				ResultSet rs = stmt.executeQuery(countsql);
				if (rs.next()) {
					pd.setTotalCount(rs.getInt("c"));
					pd.setItemCount(Integer.parseInt(limit));
					pd.setItemCount(Integer.parseInt(start));
				}
			}
			sql = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return pd;
	}
	//ȡ������
			public StatusBean isApply(String wid){
				StatusBean bean=new StatusBean();
				bean.setFlag(0);
				Connection conn= null;
				PreparedStatement stmt = null;
				try {
					conn = DBUtil.getInstance().getConnection();
					StringBuilder buffe=new StringBuilder();
					buffe.append("update withdraw set flag='0',adminTime=0,adminFlag=0 where wid='"+FunctionSet.filt(wid)+"'");
					stmt=conn.prepareStatement(buffe.toString());
			 	    if(stmt.executeUpdate() > 0){
			 	    	  bean.setFlag(1);
						}
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					DBUtil.getInstance().close(stmt);
					DBUtil.getInstance().close(conn);
				}
				return bean;
			}

}
