package com.bdh.db.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.fest.assertions.api.Assertions.assertThat;
import net.sf.json.JSONException;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.h2.constant.SysProperties;
import org.junit.Test;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.bitstamp.BitstampExchange;
import org.knowm.xchange.bittrex.v1.dto.marketdata.BittrexTicker;
import org.knowm.xchange.bittrex.v1.service.BittrexMarketDataServiceRaw;
import org.knowm.xchange.bleutrade.dto.marketdata.BleutradeTicker;
import org.knowm.xchange.bleutrade.service.BleutradeMarketDataServiceRaw;
import org.knowm.xchange.btc38.dto.marketdata.Btc38Ticker;
import org.knowm.xchange.btc38.dto.marketdata.Btc38TickerReturn;
import org.knowm.xchange.btce.v3.service.BTCEMarketDataServiceRaw;
import org.knowm.xchange.btctrade.BTCTrade;
import org.knowm.xchange.btctrade.BTCTradeExchange;
import org.knowm.xchange.btctrade.dto.marketdata.BTCTradeTicker;
import org.knowm.xchange.btctrade.service.BTCTradeMarketDataService;
import org.knowm.xchange.bter.dto.marketdata.BTERTicker;
import org.knowm.xchange.bter.service.BTERMarketDataServiceRaw;
import org.knowm.xchange.ccex.dto.ticker.CCEXPrice;
import org.knowm.xchange.ccex.service.CCEXMarketDataServiceRaw;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.exceptions.NotAvailableFromExchangeException;
import org.knowm.xchange.exceptions.NotYetImplementedForExchangeException;
import org.knowm.xchange.hitbtc.dto.marketdata.HitbtcTicker;
import org.knowm.xchange.hitbtc.service.HitbtcMarketDataService;
import org.knowm.xchange.huobi.HuobiExchange;
import org.knowm.xchange.huobi.service.HuobiMarketDataService;
import org.knowm.xchange.kraken.KrakenExchange;
import org.knowm.xchange.kraken.service.KrakenMarketDataServiceRaw;
import org.knowm.xchange.livecoin.LivecoinExchange;
import org.knowm.xchange.poloniex.dto.marketdata.PoloniexMarketData;
import org.knowm.xchange.poloniex.service.PoloniexMarketDataServiceRaw;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.yobit.service.YoBitMarketDataService;

import com.bdh.db.entry.Account;
import com.bdh.db.entry.AccountBalance;
import com.bdh.db.entry.Apis;
import com.bdh.db.entry.Bookmark;
import com.bdh.db.entry.Btc38AllTicker;
import com.bdh.db.entry.Exchang;
import com.bdh.db.entry.Following;
import com.bdh.db.entry.Keysecret;
import com.bdh.db.entry.NewOpen;
import com.bdh.db.entry.PagableData;
import com.bdh.db.entry.indexMyfollowing;
import com.bdh.db.util.DBUtil;
import com.bdh.db.util.FunctionSet;
import com.bdh.db.view.Fansnumber;
import com.google.gson.Gson;

public class BalanceDao {

	public static final Map<String, Exchange> userExchangeCashedMap = new ConcurrentHashMap<String, Exchange>();

	public Exchange getExchangeByNames(String userId, String platform) {
		String kid = userId + "_" + platform;
		Exchange exchange = null;
		if (userExchangeCashedMap.containsKey(kid)) {
			return userExchangeCashedMap.get(kid);
		} else {
			if (UserDao.cachedApis.isEmpty()) {
				UserDao.loadAllApis();
			}
			Exchang api = UserDao.cachedApi.get(kid);
			if (api == null) {
				return null;
			}
			ExchangeSpecification spec = new ExchangeSpecification(
					class4Name(platform));
			spec.setApiKey(api.getApiKey());
			spec.setSecretKey(api.getApiSecret());
			exchange = ExchangeFactory.INSTANCE.createExchange(spec);
			userExchangeCashedMap.put(kid, exchange);
		}
		return exchange;

	}

	public PagableData<Exchang> queryApis(String userid,String selectAll) {
		Connection conn = null;
		PreparedStatement stmt = null;
		PagableData<Exchang> pd = new PagableData<Exchang>();
		try {
			String sql="";
			if(selectAll.equals("yes")){
			sql = "SELECT exchang.logo,apis.apiKey,apis.apiSecret FROM user,apis,exchang where user.userId=apis.userId "
					+ "and apis.isEnable=1 and apis.flag=1 AND apis.exchangid=exchang.exchangid and user.userId='"
					+ FunctionSet.filt(userid) + "'";
			}
			else{
				sql = "SELECT exchang.logo,apis.apiKey,apis.apiSecret FROM user,apis,exchang where user.userId=apis.userId "
						+ "and apis.isEnable=1 and apis.flag=1 AND apis.exchangid=exchang.exchangid and user.userId='"
						+ FunctionSet.filt(userid) + "' limit 0,4";
			}
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.prepareStatement(sql.toString());
			List<Exchang> ds = DBUtil.getInstance().convert(
					stmt.executeQuery(), Exchang.class);
			if (!ds.isEmpty()) {
				pd.setDataList(ds);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return pd;
	}

	public Map<String, Collection<AccountBalance>> getAllPlatformSummary(
			String userId, String platformindex, String apikey, String apisecret) {
		if(userId==null || userId.equals("")
				||platformindex==null || platformindex.equals("") 
				|| apikey==null || apikey.equals("")
				|| apisecret==null || apisecret.equals("")){
			return new HashMap<String, Collection<AccountBalance>>();
		}
		Map<String, Collection<AccountBalance>> allPlatformBalanceMap = new LinkedHashMap<String, Collection<AccountBalance>>();
		ExchangeSpecification spec = new ExchangeSpecification(
				class4Name(platformindex));
		spec.setApiKey(apikey);
		spec.setSecretKey(apisecret);
		Exchange exchange = ExchangeFactory.INSTANCE.createExchange(spec);
		Collection<AccountBalance> balanceList = new ArrayList<AccountBalance>();
		Map<Currency, Balance> blanceMap = new LinkedHashMap<Currency, Balance>();
		try {
			try {
				blanceMap = exchange.getAccountService().getAccountInfo()
						.getWallet().getBalances();
			} catch (IOException i) {
				blanceMap = new LinkedHashMap<Currency, Balance>();
			}
		} catch (Exception e) {
			blanceMap = new LinkedHashMap<Currency, Balance>();
		}
		if (!blanceMap.isEmpty()) {
			for (Balance b : blanceMap.values()) {
				if (b.getTotal().floatValue() > 0) {
					AccountBalance ab = new AccountBalance();
					ab.setAvailable(b.getAvailable());
					ab.setBorrowed(b.getBorrowed());
					ab.setDepositing(b.getDepositing());
					ab.setFrozen(b.getFrozen());
					ab.setTotal(b.getTotal());
					ab.setWithdrawing(b.getWithdrawing());
					ab.setLoaned(b.getLoaned());
					ab.setCurrency(b.getCurrency().getCurrencyCode());
					balanceList.add(ab);
				}
			}
		}
		if (!balanceList.isEmpty()) {
			allPlatformBalanceMap.put(platformindex, balanceList);
		}
		return allPlatformBalanceMap;
	}

	/***
	 * 首页单个交易所数据
	 */
	public PagableData<Account> getPlatformSummary(String userId,
			String platformindex, String apikey, String apisecret) {
		if(platformindex==null || platformindex.equals("") 
				|| apikey==null || apikey.equals("")
				|| apisecret==null || apisecret.equals("")){
			return new PagableData<Account>();
		}
		PagableData<Account> acc = new PagableData<Account>();
		Account account = new Account();
		List<Account> acccc = new ArrayList<Account>();
		Map<String, List<AccountBalance>> allPlatformBalanceMap = new LinkedHashMap<String, List<AccountBalance>>();
		ExchangeSpecification spec = new ExchangeSpecification(
				class4Name(platformindex));
		spec.setApiKey(apikey);
		spec.setSecretKey(apisecret);
		Exchange exchange = ExchangeFactory.INSTANCE.createExchange(spec);
		List<AccountBalance> balanceList = new ArrayList<AccountBalance>();
		Map<Currency, Balance> blanceMap;
		try {
			blanceMap = exchange.getAccountService().getAccountInfo()
					.getWallet().getBalances();
		} catch (IOException e) {
			blanceMap = new LinkedHashMap<Currency, Balance>();
			// TODO Auto-generated catch block
		} catch (RuntimeException r) {
			blanceMap = new LinkedHashMap<Currency, Balance>();
			// TODO Auto-generated catch block
		} catch (Exception e) {
			blanceMap = new LinkedHashMap<Currency, Balance>();
			// TODO Auto-generated catch block
		}
		Map<String, PoloniexMarketData> tickermap = getAllTickerList(
				platformindex.substring(0, 1).toUpperCase()
						+ platformindex.substring(1));
		BigDecimal last = BigDecimal.ZERO;
		BigDecimal frozentotal = BigDecimal.ZERO;
		BigDecimal btctotal = BigDecimal.ZERO;
		BigDecimal btccount = BigDecimal.ZERO;
		BigDecimal trunbtc = BigDecimal.ZERO;
		BigDecimal frozenBTC = BigDecimal.ZERO;
		BigDecimal frozentrunbtc = BigDecimal.ZERO;
		BigDecimal totlecny = BigDecimal.ZERO;
		BigDecimal Frozencny = BigDecimal.ZERO;
		if (!blanceMap.isEmpty()) {
			for (Balance b : blanceMap.values()) {
					
					if (b.getTotal() != null
							&& (b.getTotal().floatValue() > 0
									|| b.getFrozen().floatValue() > 0 || b
									.getAvailable().floatValue() > 0)) {
						if (platformindex.equals("jubi")
								|| platformindex.equals("btc38")) {
							if (b.getCurrency().getCurrencyCode().equals("BTC")) {
								last = tickermap.get("btc").getLast();
								btccount = b.getTotal();
								frozenBTC = frozenBTC.add(b.getFrozen());
							}
                            if(b.getCurrency().getCurrencyCode().equals("CNY")){
            					totlecny = b.getTotal();
            					Frozencny = b.getFrozen();
                            	BigDecimal cnylast=new BigDecimal(1);
                            	last = cnylast.divide(tickermap.get("btc").getLast(),8,BigDecimal.ROUND_HALF_UP);
                            }else  if (tickermap.get(b.getCurrency().getCurrencyCode().toLowerCase()) == null) {
							
							} else {
								last = (tickermap.get(b.getCurrency().getCurrencyCode().toLowerCase()).getLast())
										.divide(tickermap.get("btc").getLast(),8,BigDecimal.ROUND_HALF_UP);
							}
						} else if(platformindex.equals("bittrex")){
							if(b.getCurrency().getCurrencyCode().equals("BTC")){
								last = new BigDecimal(1);
									btccount = b.getTotal();
									frozenBTC = frozenBTC.add(b.getFrozen());
							}else {
							last = tickermap.get("BTC-"+b.getCurrency().getCurrencyCode())
									.getLast();
							}
						}else if (platformindex.equals("bitfinex")
								|| platformindex.equals("therocktrading")) {
							if(b.getCurrency().getCurrencyCode().equals("BTC")){
								last = new BigDecimal(1);
									btccount = b.getTotal();
									frozenBTC = frozenBTC.add(b.getFrozen());
							}else {
							last = tickermap.get(
									b.getCurrency().getCurrencyCode() + "BTC")
									.getLast();}
						} else if (platformindex.equals("bleutrade")) {
							if(b.getCurrency().getCurrencyCode().equals("BTC")){
								last = new BigDecimal(1);
									btccount = b.getTotal();
									frozenBTC = frozenBTC.add(b.getFrozen());
							}else {
							last = tickermap.get(
									b.getCurrency().getCurrencyCode() + "_BTC")
									.getLast();}
						} else if (platformindex.equals("bter")
								|| platformindex.equals("livecoin")) {
							if(b.getCurrency().getCurrencyCode().equals("BTC")){
								last = new BigDecimal(1);
									btccount = b.getTotal();
									frozenBTC = frozenBTC.add(b.getFrozen());
							}else {
							last = tickermap.get(
									b.getCurrency().getCurrencyCode() + "/BTC")
									.getLast();}
						} else if (platformindex.equals("poloniex")) {
							if(b.getCurrency().getCurrencyCode().equals("BTC")){
								last = new BigDecimal(1);
									btccount = b.getTotal();
									frozenBTC = frozenBTC.add(b.getFrozen());
							}else {
							last = tickermap.get(
									"BTC_" + b.getCurrency().getCurrencyCode())
									.getLast();
							}
						}
						AccountBalance ab = new AccountBalance();
						ab.setAvailable(b.getAvailable());
						ab.setBorrowed(b.getBorrowed());
						ab.setDepositing(b.getDepositing());
						ab.setFrozen(b.getFrozen());
						ab.setLast(last);
						
						ab.setTotal(b.getTotal());
						ab.setWithdrawing(b.getWithdrawing());
						ab.setLoaned(b.getLoaned());
						ab.setCurrency(b.getCurrency().getCurrencyCode());
						balanceList.add(ab);
					}
					if(!b.getCurrency().getCurrencyCode().equals("BTC") || !b.getCurrency().getCurrencyCode().equals("CNY")){
						trunbtc = trunbtc.add(b.getTotal().multiply(last));//兑换成btc的量
						frozentrunbtc = frozentrunbtc.add(
								b.getFrozen().multiply(last)).setScale(4,
								BigDecimal.ROUND_HALF_UP);//可用BTC的量
					}
			
				/*}*/
				
			}
			allPlatformBalanceMap.put(platformindex, balanceList);
		} else {
			allPlatformBalanceMap.put(platformindex,
					new ArrayList<AccountBalance>(0));
		}
		btctotal = trunbtc.add(btccount);
		frozentotal = frozentrunbtc.add(frozenBTC);

		account.setTotlecny(totlecny);
		account.setFrozencny(Frozencny);
		account.setOpenOrdercount(String.valueOf(frozentotal));
		account.setBtcAount(String.valueOf(btctotal));// btc
		account.setList(allPlatformBalanceMap);
		acccc.add(account);
		acc.setDataList(acccc);
		return acc;
	}
	/**
	 * ��ѯ�û�keyֵ
	 * */
	public List<Exchang> loadAllApis(String userid) {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.createStatement();
			String sql = "SELECT exchang.logo,apis.apiKey,apis.apiSecret FROM user,apis,exchang where user.userId=apis.userId "
					+ "and apis.isEnable=1 and apis.flag=1 AND apis.exchangid=exchang.exchangid and user.userId='"
					+ FunctionSet.filt(userid) + "'";
			List<Exchang> ds = DBUtil.getInstance().convert(
					stmt.executeQuery(sql), Exchang.class);
			return ds;
		} catch (Exception e) {

		} finally {
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return new ArrayList<Exchang>(0);
	}


	/***
	 * 首页我关注的币
	 * ******************************************************************
	 * */
	
	/*public static void main(String[] args) {
		BalanceDao dao= new BalanceDao();
		PagableData<Bookmark> pd= dao.MyBookmark("103417c8d47843aca10b2ed18ae39d4d", "10", "0");
		for (Bookmark book : pd.getDataList()) {
			System.out.println(book.getCurrency()+"币"+book.getPlatform()+"交易所");
		}
	}*/
	public PagableData<Bookmark> MyBookmark(String userid,String limit,String start) {
		Connection conn = null;
		Statement stmt = null;
		/*System.out.println(userid);*/
		PagableData<Bookmark> pd = new PagableData<Bookmark>();
		try {
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.createStatement();
			StringBuilder sql=new StringBuilder();
			/*sql.append("SELECT * from bookmark WHERE bookmark.userId='"+FunctionSet.filt(userid)+"' and  platform in(SELECT exchang.logo FROM apis,exchang WHERE exchang.exchangid=apis.exchangid and apis.isEnable=1 and apis.flag=1 and apis.userId='"+FunctionSet.filt(userid)+"')");
		    sql.append(" limit ");
			sql.append(start);
			sql.append(",");
			sql.append(limit);
			*/
		/*<!---  标记   --->*/
		
			sql.append(" SELECT * from bookmark where userId='"+userid+"' and flag=1 Order By id asc limit ");
			sql.append(start);
			sql.append(",");
			sql.append(limit);
			List<Bookmark> ds = DBUtil.getInstance().convert(
					stmt.executeQuery(sql.toString()), Bookmark.class);
			if (!ds.isEmpty()) {
				String countsql="select count(id) c from bookmark where flag=1 and userid='"
					+ FunctionSet.filt(userid) + "'";
			    ResultSet rs=stmt.executeQuery(countsql);
					if(rs.next()){
						pd.setTotalCount(rs.getInt("c"));
						pd.setItemCount(Integer.parseInt(limit));
						pd.setItemCount(Integer.parseInt(start));
					}
				pd.setDataList(ds);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block

		} finally {
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return pd;
	}
	

	//获取该货币的讯息
		public PagableData<Bookmark> getBookMarkCoin(String userid,String platfrom,String coin){
			PagableData<Bookmark>pd=new PagableData<Bookmark>();
			List<Bookmark> list=new ArrayList<Bookmark>();
			if(coin==null || coin.equals("") || platfrom==null || platfrom.equals("")){
				return new PagableData<Bookmark>();
			}
			 Map<String, PoloniexMarketData> tickermap = getAllTickerList(
					 platfrom.substring(0, 1).toUpperCase()+platfrom.substring(1).toLowerCase());
			Map<String, String>map=new HashMap<String, String>();
			map.put((platfrom.substring(0, 1).toUpperCase()+platfrom.substring(1).toLowerCase()), coin.toLowerCase());
			if(tickermap.isEmpty()){
				/*System.out.println("ticker");*/
				return new PagableData<Bookmark>();
			}
			if(map.isEmpty()){
				/*System.out.println(map);*/
				return new PagableData<Bookmark>();
			}
			     for(String coinname:map.keySet()){
								BigDecimal lastPrice = BigDecimal.ZERO;
								BigDecimal hightPrive = BigDecimal.ZERO;
								BigDecimal lowPrice = BigDecimal.ZERO;
								BigDecimal volumeCount = BigDecimal.ZERO;
								
								if (coinname.toLowerCase().equals("jubi")
										|| coinname.equals("btc38")) {
									if(map.get(coinname).toLowerCase().equals("btc")){
										lastPrice =tickermap.get("btc").getLast();
										hightPrive = tickermap.get("btc").getHighestBid();
										lowPrice = tickermap.get("btc").getLowestAsk();
										volumeCount = tickermap.get("btc").getQuoteVolume();	
									}else{
										lastPrice = (tickermap.get(map.get(coinname).toLowerCase()).getLast())
												.divide(tickermap.get("btc").getLast(),8,BigDecimal.ROUND_HALF_UP);
										hightPrive = (tickermap.get(map.get(coinname).toLowerCase()).getHighestBid())
												.divide(tickermap.get("btc").getLast(),8,BigDecimal.ROUND_HALF_UP);
										lowPrice = (tickermap.get(map.get(coinname).toLowerCase()).getLowestAsk())
												.divide(tickermap.get("btc").getLast(),8,BigDecimal.ROUND_HALF_UP);
										volumeCount = tickermap.get(map.get(coinname).toLowerCase()).getQuoteVolume();
									}
								} else if(coinname.toLowerCase().equals("bittrex")){
									lastPrice = tickermap.get("BTC-"+map.get(coinname).toUpperCase()
													).getLast();
									hightPrive = tickermap.get(
											"BTC-"+map.get(coinname).toUpperCase()
													).getHighestBid();
									lowPrice = tickermap.get(
											"BTC-"+map.get(coinname).toUpperCase()
													).getLowestAsk();
									volumeCount = tickermap.get(
											"BTC-"+map.get(coinname).toUpperCase()
													).getQuoteVolume();
								}else if(coinname.toLowerCase().equals("bitfinex")
										       || coinname.equals("therocktrading")) {
									lastPrice = tickermap.get(
											map.get(coinname).toUpperCase()
													+ "BTC").getLast();
									hightPrive = tickermap.get(
											map.get(coinname).toUpperCase()
													+ "BTC").getHighestBid();
									lowPrice = tickermap.get(
											map.get(coinname).toUpperCase()
													+ "BTC").getLowestAsk();
									volumeCount = tickermap.get(
											map.get(coinname).toUpperCase()
													+ "BTC").getQuoteVolume();
								} else if (coinname.toLowerCase().equals("bleutrade")) {
									lastPrice = tickermap.get(
											map.get(coinname).toUpperCase()
													+ "_BTC").getLast();
									hightPrive = tickermap.get(
											map.get(coinname).toUpperCase()
													+ "_BTC").getHighestBid();
									lowPrice = tickermap.get(
											map.get(coinname).toUpperCase()
													+ "_BTC").getLowestAsk();
									volumeCount = tickermap.get(
											map.get(coinname).toUpperCase()
													+ "_BTC").getQuoteVolume();
								} else if (coinname.toLowerCase().equals("bter")
										|| coinname.equals("livecoin")) {
									lastPrice = tickermap.get(
											map.get(coinname).toUpperCase()
													+ "/BTC").getLast();
									hightPrive = tickermap.get(
											map.get(coinname).toUpperCase()
													+ "/BTC").getHighestBid();
									lowPrice = tickermap.get(
											map.get(coinname).toUpperCase()
													+ "/BTC").getLowestAsk();
									volumeCount = tickermap.get(
											map.get(coinname).toUpperCase()
													+ "/BTC").getQuoteVolume();

								}else if ((coinname.substring(0, 1).toUpperCase()+coinname.substring(1).toLowerCase()).equals("Poloniex")) {
									lastPrice = tickermap.get("BTC_"
											+ map.get(coinname).toUpperCase()).getLast();
									hightPrive = tickermap.get("BTC_"
											+ map.get(coinname).toUpperCase()).getHighestBid();
									lowPrice = tickermap.get("BTC_"
											+ map.get(coinname).toUpperCase()).getLowestAsk();
									volumeCount = tickermap.get("BTC_"
											+ map.get(coinname).toUpperCase()).getQuoteVolume();
								} 
								Bookmark bookmark=new Bookmark();
								bookmark.setLastPrice(lastPrice.toString());
								bookmark.setHightPrive(hightPrive.toString());
								bookmark.setLowPrice(lowPrice.toString());
								bookmark.setVolumeCount(volumeCount.toString());
	                            list.add(bookmark);
	                          
			}
	          	pd.setDataList(list);
			return pd;
		}

		
	public String class4Name(String name) {
		// bitflyer cryptopia localbitcoins litebit bitlish exmo usecryptos
		// bitport.net cryptomate
		/*
		 * if (name.equalsIgnoreCase("c-cex")) { return "org.knowm.xchange." +
		 * name.substring(0, 1) + name.substring(2) + "." + name.substring(0,
		 * 1).toUpperCase() + name.substring(2).toUpperCase() + "Exchange"; }
		 */
/*	System.out.println(name);*/
		if (name.equalsIgnoreCase("cex.io")) {
			return "org.knowm.xchange." + name.substring(0, 1)
					+ name.substring(1, 3) + name.substring(4, 6) + "."
					+ name.substring(0, 1).toUpperCase() + name.substring(1, 3)
					+ name.substring(4, 6).toUpperCase() + "Exchange";
		}
		if (name.equalsIgnoreCase("bter")) {
			return "org.knowm.xchange." + name + "." + name.toUpperCase()
					+ "Exchange";
		}
		if (name.equalsIgnoreCase("bitfinex")) {
			return "org.knowm.xchange." + name.toLowerCase() + ".v1."
					+ name.substring(0, 1).toUpperCase() + name.substring(1)
					+ "Exchange";
		}
		if (name.equalsIgnoreCase("bittrex")) {
			return "org.knowm.xchange." + name.toLowerCase() + ".v1."
					+ name.substring(0, 1).toUpperCase() + name.substring(1)
					+ "Exchange";
		}
		if (name.equalsIgnoreCase("livecoin")) {
			return "org.knowm.xchange." + name.toLowerCase() + "."
					+ name.substring(0, 1).toUpperCase() + name.substring(1)
					+ "Exchange";
		}
		/*
		 * if (name.equalsIgnoreCase("empoex")) { return "org.knowm.xchange." +
		 * name.toLowerCase() + "." + name.substring(0, 1).toUpperCase() +
		 * name.substring(1, 4) + name.substring(4, 6).toUpperCase() +
		 * "Exchange"; }
		 */
		if (name.equalsIgnoreCase("therocktrading")) {
			return "org.knowm.xchange." + name.substring(0, 7) + "."
					+ name.substring(0, 1).toUpperCase() + name.substring(1, 3)
					+ name.substring(3, 4).toUpperCase() + name.substring(4, 7)
					+ "Exchange";
		}
		/*
		 * if (name.equalsIgnoreCase("btctrade")) { return "org.knowm.xchange."
		 * + name+ "." + name.substring(0, 4).toUpperCase() + name.substring(4)
		 * + "Exchange"; }
		 */

		return "org.knowm.xchange." + name.toLowerCase() + "."
				+ name.substring(0, 1).toUpperCase() + name.substring(1)
				+ "Exchange";
	}

	private Exchange getExchangeByName(String userId, String platform) {
		String kid = userId + "_" + platform;
		Exchange exchange = null;
		if(userId==null || userId.equals("") || platform==null || platform.equals("")){
			return exchange;
		}
	
		List<Exchang> api = loadAllApis(userId);
		if (api == null) {
			return null;
		}
		ExchangeSpecification spec = null;
		for (int i = 0; i < api.size(); i++) {
			spec = new ExchangeSpecification(class4Name(platform.toLowerCase()));
			spec.setApiKey(api.get(i).getApiKey());
			spec.setSecretKey(api.get(i).getApiSecret());
		}
		if(spec!=null){
			exchange = ExchangeFactory.INSTANCE.createExchange(spec);
		}
		else{
			return exchange;
		}
		
		/* userExchangeCashedMap.put(kid, exchange); */
		return exchange;

	}
	private Exchange getExchangeTickers(/*String userId,*/ String platform) {
		/*String kid = userId + "_" + platform;*/
		Exchange exchange = null;
		ExchangeSpecification spec = null;
			spec = new ExchangeSpecification(class4Name(platform.toLowerCase()));
			exchange = ExchangeFactory.INSTANCE.createExchange(spec);
		return exchange;
	}

	
	public List<LimitOrder> getMyOpenOrderList(String userId, String platform) {
		Exchange exchange = getExchangeByName(userId, platform);
		if (exchange == null) {
			return new ArrayList<>();
		}
		OpenOrders uts = null;
		try {
			uts = exchange.getTradeService().getOpenOrders();
		} catch (Exception e) {
			uts = new OpenOrders(new ArrayList<LimitOrder>(0));
		}
		if (uts != null) {
			return uts.getOpenOrders();
		} else {
			return uts.getOpenOrders();
		}

	}


	// 我关注的大师关注量
		public PagableData<Fansnumber> fansnumber(String userid,String start,String limit) {
		/*	System.out.println(userid);*/
			Connection conn = null;
			Statement stmt = null;
			PagableData<Fansnumber> pd = new PagableData<Fansnumber>();
			try {
				conn = DBUtil.getInstance().getConnection();
				stmt = conn.createStatement();
				String sql = "SELECT fansnumber.followeduserid as userid,user.walletAddr as walletAddr,fansnumber.fansnumber as fansnumber"
                           +" FROM user,fansnumber WHERE fansnumber.followeduserid=user.userId  and user.userId in(SELECT following.followedUserId from following WHERE "
                            +"following.userId='"+FunctionSet.filt(userid)+"') GROUP BY user.userId limit "+FunctionSet.filt(start)+","+FunctionSet.filt(limit)+"";
				List<Fansnumber> fanslist = DBUtil.getInstance().convert(
						              stmt.executeQuery(sql), Fansnumber.class);
				Statement pstmt = null;
				pstmt = conn.createStatement();
				String sqlto = "SELECT count(strategy.strategyId) as strategynum,user.walletAddr FROM strategy,user where user.userId=strategy.userId and strategy.strategyId='0' and strategy.userId in(SELECT following.followedUserId from following WHERE"
                               +" following.userId='"+FunctionSet.filt(userid)+"') GROUP BY strategy.userId";
				List<Fansnumber> numberlist = DBUtil.getInstance().convert(
						      pstmt.executeQuery(sqlto), Fansnumber.class);
				if(!fanslist.isEmpty()){
						List<Fansnumber>fansnumberlist= new ArrayList<Fansnumber>();
					for (int i = 0; i < fanslist.size(); i++) {
						Fansnumber number= new Fansnumber();
						number.setWalletAddr(fanslist.get(i).getWalletAddr());
						number.setFansnumber(fanslist.get(i).getFansnumber());
						
						if(!numberlist.isEmpty()){
						for (int j = 0; j < numberlist.size(); j++) {
						/*	System.out.println(numberlist.get(j).getWalletAddr());
							System.out.println(fanslist.get(i).getWalletAddr());*/
								if(numberlist.get(j).getWalletAddr().equals(fanslist.get(i).getWalletAddr())){
									number.setStrategynum(numberlist.get(j).getStrategynum());
								}
								else{
									/*number.setStrategynum("0");*/
									continue;
								}
						
							fansnumberlist.add(number);
					/*		System.out.println("非空"+fansnumberlist);*/
					            } 
						}	
						else{
							number.setStrategynum("0");
							fansnumberlist.add(number);
						/*	System.out.println("空"+fansnumberlist);*/
						}
					}
					pd.setDataList(fansnumberlist);
					String countsql="SELECT count(following.followedUserId) c from following WHERE  following.userId='"+FunctionSet.filt(userid)+"'";
				     ResultSet rs=stmt.executeQuery(countsql);
				if(rs.next()){
					pd.setTotalCount(rs.getInt("c"));
					pd.setItemCount(Integer.parseInt(limit));
					pd.setItemCount(Integer.parseInt(start));
				}
				}
			} catch (Exception e) {

			} finally {
				DBUtil.getInstance().close(stmt);
				DBUtil.getInstance().close(conn);
			}
			return pd;
		}

	/***
	 * 我关注的大师apis
	 * */
	public List<Exchang> myfollowApis(String userid) {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = DBUtil.getInstance().getConnection();
			stmt = conn.createStatement();
			String sql = "SELECT * FROM apis,exchang where isEnable=1 and flag=1 and  apis.exchangid=exchang.exchangid and userId "
					+ " in(SELECT followedUserId from following WHERE userId='"
					+ FunctionSet.filt(userid) + "');";
			List<Exchang> ds = DBUtil.getInstance().convert(
					stmt.executeQuery(sql), Exchang.class);
			return ds;
		} catch (Exception e) {

		} finally {
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return new ArrayList<Exchang>(0);
	}

	/**
	 * 关注的大师
	 * */
	public List<indexMyfollowing> getMymaster(String userid) {
		List<indexMyfollowing> followinglist = new ArrayList<indexMyfollowing>();
		if(userid==null || userid.equals("")){
			return followinglist;
		}
		indexMyfollowing following = new indexMyfollowing();
		Map<String, List<indexMyfollowing>> map = new LinkedHashMap<String, List<indexMyfollowing>>();
		List<Exchang> list = myfollowApis(userid);
		Map<Currency, Balance> blanceMap = null;
		Map<String, PoloniexMarketData> tickermap = null;
		BigDecimal btc = BigDecimal.ZERO;
		BigDecimal convertiblebtc = BigDecimal.ZERO;
		BigDecimal sumbtc = BigDecimal.ZERO;
		BigDecimal last = BigDecimal.ZERO;
		BigDecimal sumcny = BigDecimal.ZERO;
		String[] a = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			ExchangeSpecification spec = new ExchangeSpecification(
					class4Name(list.get(i).getLogo()));
			spec.setUserName(userid);
			spec.setApiKey(list.get(i).getApiKey());
			spec.setSecretKey(list.get(i).getApiSecret());
			a[i] = list.get(i).getLogo();
			Exchange bitstamp = ExchangeFactory.INSTANCE.createExchange(spec);
			AccountService accountService = bitstamp.getAccountService();
			AccountInfo accountInfo;
			try {
				try {
					accountInfo = accountService.getAccountInfo();
				} catch (IOException io) {
					accountInfo = new AccountInfo();
				}
			} catch (Exception e) {
				accountInfo = new AccountInfo();
			}
			blanceMap = accountInfo.getWallet().getBalances();
			tickermap = getAllTickerList(list.get(i).getLogo()
					.substring(0, 1).toUpperCase()
					+ list.get(i).getLogo().substring(1));
			List<indexMyfollowing> followlist = new ArrayList<indexMyfollowing>();
			if (!blanceMap.isEmpty()) {
				for (Balance b : blanceMap.values()) {
					if (b.getCurrency().getCurrencyCode().equals("BTC")) {
						btc = b.getTotal();
					} else {
						if (b.getTotal() != null
								&& (b.getTotal().floatValue() > 0
										|| b.getFrozen().floatValue() > 0 || b
										.getAvailable().floatValue() > 0)) {

							if (a[i].equals("jubi")
									|| a[i].equals("btc38")) {
	                            if(b.getCurrency().getCurrencyCode().equals("CNY")){
	                            	BigDecimal cnylast=new BigDecimal(1);
	                            	last = cnylast.divide(tickermap.get("btc").getLast(),8,BigDecimal.ROUND_HALF_UP);
	                            }else if (tickermap.get(b.getCurrency().getCurrencyCode().toLowerCase()) == null) {
								
								} else {
									last = (tickermap.get(b.getCurrency().getCurrencyCode().toLowerCase()).getLast())
											.divide(tickermap.get("btc").getLast(),8,BigDecimal.ROUND_HALF_UP);
								}
							} else if(a[i].equals("bittrex")){
								last = tickermap.get("BTC-"+b.getCurrency().getCurrencyCode())
										.getLast();
							}else  if (a[i].equals("bitfinex")
									|| a[i].equals("therocktrading")) {
								last = tickermap.get(
										b.getCurrency().getCurrencyCode()
												+ "BTC").getLast();
							} else if (a[i].equals("bleutrade")) {
								last = tickermap.get(
										b.getCurrency().getCurrencyCode()
												+ "_BTC").getLast();
							} else if (a[i].equals("bter")
									|| a[i].equals("livecoin")) {
								last = tickermap.get(
										b.getCurrency().getCurrencyCode()
												+ "/BTC").getLast();
							} else if (b.getCurrency().equals("BCC")) {
								last = tickermap.get(
										"BTC_"
												+ b.getCurrency()
														.getCurrencyCode())
										.getLast();
							} else {
								last = tickermap.get(
										"BTC_"
												+ b.getCurrency()
														.getCurrencyCode())
										.getLast();
							}

						}
						convertiblebtc = b.getTotal().multiply(last);
					}
					if (b.getCurrency().getCurrencyCode().equals("CNY")) {
						sumcny = b.getTotal();
					}
				}
			}
			sumbtc = convertiblebtc.add(btc);
		}
		following.setAsset(sumbtc.toString());
		following.setAccetCNY(sumcny.toString());
		followinglist.add(following);
		return followinglist;
	}


	public Map<String, Collection<LimitOrder>> getMyAllOpenOrderList(
			String userId) {
		Map<String, Collection<LimitOrder>> allPlatformOrderMap = new LinkedHashMap<String, Collection<LimitOrder>>();
		Set<String> keys = userExchangeCashedMap.keySet();
		for (String key : keys) {
			if (key.indexOf(userId + "_") >= 0) {

				String platform = key.split("_")[1];
				Exchange exchange = getExchangeByName(userId, platform);
				if (exchange == null) {
					return allPlatformOrderMap;
				}
				OpenOrders uts;
				try {
					uts = exchange.getTradeService().getOpenOrders();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					uts = new OpenOrders(new ArrayList<LimitOrder>(0));
				}
				allPlatformOrderMap.put(platform, uts.getOpenOrders());
			}
		}
		return allPlatformOrderMap;
	}

	/*
	 * @Test public void tickerFetchTest() throws Exception {
	 * 
	 * Exchange exchange = ExchangeFactory.INSTANCE
	 * .createExchange(HuobiExchange.class.getName()); MarketDataService
	 * marketDataService = exchange.getMarketDataService(); Ticker ticker =
	 * marketDataService.getTicker(new CurrencyPair("BTC", "CNY"));
	 * assertThat(ticker).isNotNull(); }
	 */

	/*  public static void main(String[] args) { BalanceDao dao = new
			  BalanceDao(); dao.getAllTickerList("103417c8d47843aca10b2ed18ae39d4d","Poloniex"
			 ); 
			  }*/
	
	public static void main(String[] args) {
	 BalanceDao dao=new BalanceDao();
	 dao.getAllTickerList("Hitbtc");
	}
	// ticker
	public Map getAllTickerList(/*String userId,*/ String exchangeName) {
		Exchange exchange = getExchangeTickers(/*userId,*/ exchangeName);
		if (exchange == null) {
			return new HashMap();
		}
		/*Exchange exchange = getExchangeByName(userId, exchangeName);
		if (exchange == null) {
			return new HashMap();
		}
		*/
		
		if(exchangeName.equalsIgnoreCase("therocktrading")){
			JSONArray data1 ;
			try {
				String json=readJsonFromUrl("https://api.therocktrading.com/v1/funds/tickers");
				JSONObject data = new JSONObject(json);
				 data1 = data.getJSONArray("tickers");
			} catch (org.codehaus.jettison.json.JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				 data1 = new JSONArray();
			}
			Map<String, PoloniexMarketData> commMap = new HashMap<String, PoloniexMarketData>();
			for (int i = 0; i < data1.length(); i++) {
				JSONObject t;
				try {
					t = data1.getJSONObject(i);
				} catch (org.codehaus.jettison.json.JSONException e) {
					// TODO Auto-generated catch block
					t = new JSONObject();
				}
				PoloniexMarketData pmd = new PoloniexMarketData();
				try {
					pmd.setBaseVolume(new BigDecimal(t.getString("volume")));
				} catch (org.codehaus.jettison.json.JSONException e) {
					// TODO Auto-generated catch block
					pmd.setBaseVolume(BigDecimal.ZERO);
				}
				try {
					pmd.setLast(new BigDecimal(t.getString("last")));
				} catch (org.codehaus.jettison.json.JSONException e) {
					// TODO Auto-generated catch block
					pmd.setLast(BigDecimal.ZERO);
				}
				try {
					pmd.setLowestAsk(new BigDecimal(t.getString("ask")));
				} catch (org.codehaus.jettison.json.JSONException e) {
					// TODO Auto-generated catch block
					pmd.setLowestAsk(BigDecimal.ZERO);
				}
				try {
					pmd.setHigh24hr(new BigDecimal(t.getString("high")));
				} catch (org.codehaus.jettison.json.JSONException e) {
					// TODO Auto-generated catch block
					pmd.setHigh24hr(BigDecimal.ZERO);
				}
				try {
					pmd.setLow24hr(new BigDecimal(t.getString("low")));
				} catch (org.codehaus.jettison.json.JSONException e) {
					// TODO Auto-generated catch block
					pmd.setLow24hr(BigDecimal.ZERO);
				}
				try {
					pmd.setQuoteVolume(new BigDecimal(t.getString("volume_traded")));
				} catch (org.codehaus.jettison.json.JSONException e) {
					// TODO Auto-generated catch block
					pmd.setQuoteVolume(BigDecimal.ZERO);
				}
				try {
					pmd.setHighestBid(new BigDecimal(t.getString("bid")));
				} catch (org.codehaus.jettison.json.JSONException e) {
					// TODO Auto-generated catch block
					pmd.setHighestBid(BigDecimal.ZERO);
				}
				if (pmd.getLast().doubleValue() > 0) {
					BigDecimal pp = (pmd.getLast().setScale(8,
							RoundingMode.CEILING).subtract(pmd.getLow24hr()
							.setScale(8, RoundingMode.CEILING))).divide(pmd
							.getLow24hr().setScale(8, RoundingMode.CEILING),
							RoundingMode.CEILING);
					pmd.setPercentChange(pp);
				}
				try {
					commMap.put(t.getString("fund_id"), pmd);
				} catch (org.codehaus.jettison.json.JSONException e) {
					// TODO Auto-generated catch block
					commMap.put("null", pmd);
				}
			}
			/*System.out.println(commMap);*/
			return commMap;

			
		}else if (exchangeName.equalsIgnoreCase("Livecoin")) {

			JSONArray jsonArr;
			try {
				jsonArr = new JSONArray(
						readJsonFromUrl("https://api.livecoin.net/exchange/ticker"));
			} catch (org.codehaus.jettison.json.JSONException e) {
				// TODO Auto-generated catch block
				jsonArr = new JSONArray();
			}
			Map<String, PoloniexMarketData> commMap = new HashMap<String, PoloniexMarketData>();
			for (int i = 0; i < jsonArr.length(); i++) {
				JSONObject t;
				try {
					t = jsonArr.getJSONObject(i);
				} catch (org.codehaus.jettison.json.JSONException e) {
					// TODO Auto-generated catch block
					t = new JSONObject();
				}
				PoloniexMarketData pmd = new PoloniexMarketData();

				try {
					pmd.setBaseVolume(new BigDecimal(t.getDouble("volume")));
				} catch (org.codehaus.jettison.json.JSONException e) {
					// TODO Auto-generated catch block
					pmd.setBaseVolume(BigDecimal.ZERO);
				}
				try {
					pmd.setLast(new BigDecimal(t.getDouble("last")));
				} catch (org.codehaus.jettison.json.JSONException e) {
					// TODO Auto-generated catch block
					pmd.setLast(BigDecimal.ZERO);
				}
				try {
					pmd.setLowestAsk(new BigDecimal(t.getDouble("min_ask")));
				} catch (org.codehaus.jettison.json.JSONException e) {
					// TODO Auto-generated catch block
					pmd.setLowestAsk(BigDecimal.ZERO);
				}
				try {
					pmd.setHigh24hr(new BigDecimal(t.getDouble("high")));
				} catch (org.codehaus.jettison.json.JSONException e) {
					// TODO Auto-generated catch block
					pmd.setHigh24hr(BigDecimal.ZERO);
				}
				try {
					pmd.setLow24hr(new BigDecimal(t.getDouble("low")));
				} catch (org.codehaus.jettison.json.JSONException e) {
					// TODO Auto-generated catch block
					pmd.setLow24hr(BigDecimal.ZERO);
				}
				try {
					pmd.setQuoteVolume(new BigDecimal(t.getDouble("volume")));
				} catch (org.codehaus.jettison.json.JSONException e) {
					// TODO Auto-generated catch block
					pmd.setQuoteVolume(BigDecimal.ZERO);
				}
				try {
					pmd.setHighestBid(new BigDecimal(t.getDouble("max_bid")));
				} catch (org.codehaus.jettison.json.JSONException e) {
					// TODO Auto-generated catch block
					pmd.setHighestBid(BigDecimal.ZERO);
				}
				if (pmd.getLast().doubleValue() > 0) {
					BigDecimal pp = (pmd.getLast().setScale(8,
							RoundingMode.CEILING).subtract(pmd.getLow24hr()
							.setScale(8, RoundingMode.CEILING))).divide(pmd
							.getLow24hr().setScale(8, RoundingMode.CEILING),
							RoundingMode.CEILING);
					pmd.setPercentChange(pp);
				}
				try {
					commMap.put(t.getString("symbol"), pmd);
				} catch (org.codehaus.jettison.json.JSONException e) {
					// TODO Auto-generated catch block
					commMap.put("null", pmd);
				}
			}
		/*	System.out.println(commMap);*/
			return commMap;

		} else if(exchangeName.equalsIgnoreCase("cex.io")){
			//https://cex.io/api/tickers/USD/EUR/RUB/BTC
			JSONArray data1 ;
			try {
				String json=readJsonFromUrl("https://cex.io/api/tickers/USD/EUR/RUB/BTC");
				JSONObject data = new JSONObject(json);
				 data1 = data.getJSONArray("e");
			} catch (org.codehaus.jettison.json.JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				 data1 = new JSONArray();
			}

		}else if(exchangeName.equals("empoex")){
			//https://api.empoex.com/marketinfo
			JSONArray jsonArr;
			try {
				jsonArr = new JSONArray(
						readJsonFromUrl("https://api.empoex.com/marketinfo"));
			} catch (org.codehaus.jettison.json.JSONException e) {
				// TODO Auto-generated catch block
				jsonArr = new JSONArray();
			}
		}else if (exchangeName.equalsIgnoreCase("Bitfinex")) {

			JSONArray jsonArr;
			try {
				jsonArr = new JSONArray(
						readJsonFromUrl("https://api.bitfinex.com/v2/tickers?symbols=tBTCUSD,tLTCUSD,tLTCBTC,tETHUSD,tETHBTC,tETCBTC,tETCUSD,tRRTUSD,tRRTBTC,tZECUSD,tZECBTC,tXMRUSD,tXMRBTC,tDSHUSD,tDSHBTC,tBCCBTC,tBCUBTC,tBCCUSD,tBCUUSD,tXRPUSD,tXRPBTC,tIOTUSD,tIOTBTC"));
			} catch (org.codehaus.jettison.json.JSONException e) {
				// TODO Auto-generated catch block
				jsonArr = new JSONArray();
			}

			Map<String, PoloniexMarketData> commMap = new HashMap<String, PoloniexMarketData>();
			for (int i = 0; i < jsonArr.length(); i++) {
				JSONArray value;
				try {
					value = jsonArr.getJSONArray(i);
				} catch (org.codehaus.jettison.json.JSONException e) {
					// TODO Auto-generated catch block
					value = new JSONArray();
				}
				PoloniexMarketData pmd = new PoloniexMarketData();
				String k;
				try {
					k = value.getString(0);
				} catch (org.codehaus.jettison.json.JSONException e) {
					// TODO Auto-generated catch block
					k = "0";
				}

				BigDecimal high;
				try {
					high = new BigDecimal(value.get(9).toString());
				} catch (org.codehaus.jettison.json.JSONException e) {
					// TODO Auto-generated catch block
					high = BigDecimal.ZERO;
				}
				BigDecimal low;
				try {
					low = new BigDecimal(value.get(10).toString());
				} catch (org.codehaus.jettison.json.JSONException e) {
					// TODO Auto-generated catch block
					low = BigDecimal.ZERO;
				}
				BigDecimal buy;
				try {
					buy = new BigDecimal(value.get(1).toString());
				} catch (org.codehaus.jettison.json.JSONException e) {
					// TODO Auto-generated catch block
					buy = BigDecimal.ZERO;
				}
				BigDecimal sell;
				try {
					sell = new BigDecimal(value.get(3).toString());
				} catch (org.codehaus.jettison.json.JSONException e) {
					// TODO Auto-generated catch block
					sell = BigDecimal.ZERO;
				}
				BigDecimal last;
				try {
					last = new BigDecimal(value.get(7).toString());
				} catch (org.codehaus.jettison.json.JSONException e) {
					// TODO Auto-generated catch block
					last = BigDecimal.ZERO;
				}
				BigDecimal vol;
				try {
					vol = new BigDecimal(value.get(8).toString());
				} catch (org.codehaus.jettison.json.JSONException e) {
					// TODO Auto-generated catch block
					vol = BigDecimal.ZERO;
				}
				BigDecimal pp;
				try {
					pp = new BigDecimal(value.get(6).toString());
				} catch (org.codehaus.jettison.json.JSONException e) {
					// TODO Auto-generated catch block
					pp = BigDecimal.ZERO;
				}

				pmd.setBaseVolume(vol);
				pmd.setLast(last);
				pmd.setLowestAsk(buy);
				pmd.setHigh24hr(high);
				pmd.setLow24hr(low);
				pmd.setQuoteVolume(vol);
				pmd.setHighestBid(sell);

				pmd.setPercentChange(pp);
				commMap.put(k.substring(1), pmd);
			}
			return commMap;

		} else if (exchangeName.equalsIgnoreCase("Btc38")) {

			Btc38AllTicker btc38 = new Btc38AllTicker(exchange);
			Map<String, PoloniexMarketData> commMap = new HashMap<String, PoloniexMarketData>();
			Map<String, Btc38TickerReturn> tikers;
			try {
				tikers = btc38.getBtc38AllTickers("CNY");
			} catch (ConnectException q) {
				// TODO Auto-generated catch block
				return new HashMap();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				tikers = new HashMap<String, Btc38TickerReturn>();
			}
			for (String k : tikers.keySet()) {
				Btc38Ticker t = tikers.get(k).getTicker();
				PoloniexMarketData pmd = new PoloniexMarketData();

				pmd.setBaseVolume(t.getVol());
				pmd.setLast(t.getLast());
				pmd.setLowestAsk(t.getSell());
				pmd.setHigh24hr(t.getHigh());
				pmd.setLow24hr(t.getLow());
				pmd.setQuoteVolume(t.getVol());
				pmd.setHighestBid(t.getHigh());
				if (t.getLast() != null && t.getLast().doubleValue() > 0) {
					BigDecimal pp = (t.getLast().setScale(8,
							RoundingMode.CEILING).subtract(t.getLow().setScale(
							8, RoundingMode.CEILING))).divide(t.getLow()
							.setScale(8, RoundingMode.CEILING),
							RoundingMode.CEILING);
					pmd.setPercentChange(pp);
				}
				commMap.put(k.toString(), pmd);
			}

			return commMap;

		} else if (exchangeName.equalsIgnoreCase("BTER")) {
			BTERMarketDataServiceRaw pmds = new BTERMarketDataServiceRaw(
					exchange);
			Map<String, PoloniexMarketData> commMap = new HashMap<String, PoloniexMarketData>();
			Map<CurrencyPair, BTERTicker> tikers;
			try {
				tikers = pmds.getBTERTickers();
			} catch (ConnectException q) {
				// TODO Auto-generated catch block
				return new HashMap();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				tikers = new HashMap<CurrencyPair, BTERTicker>();
			}
			for (CurrencyPair k : tikers.keySet()) {
				BTERTicker t = tikers.get(k);
				PoloniexMarketData pmd = new PoloniexMarketData();

				BigDecimal vol = t.getVolume("CNY");
				if (vol == null) {
					vol = t.getVolume("BTC");
				}
				if (vol == null) {
					vol = t.getVolume(k.toString().split("/")[0]);
				}
				pmd.setBaseVolume(vol);
				pmd.setLast(t.getLast());
				pmd.setLowestAsk(t.getSell());
				pmd.setHigh24hr(t.getHigh());
				pmd.setLow24hr(t.getLow());
				pmd.setQuoteVolume(vol);
				pmd.setHighestBid(t.getHigh());
				if (t.getLast().doubleValue() > 0) {
					BigDecimal pp = (t.getLast().setScale(8,
							RoundingMode.CEILING).subtract(t.getAvg().setScale(
							8, RoundingMode.CEILING))).divide(t.getAvg()
							.setScale(8, RoundingMode.CEILING),
							RoundingMode.CEILING);
					pmd.setPercentChange(pp);
				}
				commMap.put(k.toString(), pmd);
			}
			return commMap;

		} else if (exchangeName.equals("Poloniex")) {
	
			PoloniexMarketDataServiceRaw pmds = new PoloniexMarketDataServiceRaw(
					exchange);
			try {
				/*System.out.println(pmds.getAllPoloniexTickers());*/
				return pmds.getAllPoloniexTickers();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return new HashMap();
			}
			
		} else if (exchangeName.equalsIgnoreCase("Bittrex")) {
			BittrexMarketDataServiceRaw bwdsr = new BittrexMarketDataServiceRaw(
					exchange);
			List<BittrexTicker> tikers;
			try {
				tikers = bwdsr.getBittrexTickers();
			} catch (ConnectException q) {
				// TODO Auto-generated catch block
				return new HashMap();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return new HashMap();
			}
			Map<String, PoloniexMarketData> commMap = new HashMap<String, PoloniexMarketData>();
			for (BittrexTicker t : tikers) {
				PoloniexMarketData pmd = new PoloniexMarketData();
				pmd.setBaseVolume(t.getBaseVolume());
				pmd.setLast(t.getLast());
				pmd.setLowestAsk(t.getAsk());
				pmd.setHigh24hr(t.getHigh());
				pmd.setLow24hr(t.getLow());
				pmd.setQuoteVolume(t.getVolume());
				pmd.setHighestBid(t.getBid());
				if (t.getPrevDay().doubleValue() > 0) {
					BigDecimal pp = (t.getLast().setScale(8,
							RoundingMode.CEILING).subtract(t.getPrevDay()
							.setScale(8, RoundingMode.CEILING))).divide(t
							.getPrevDay().setScale(8, RoundingMode.CEILING),
							RoundingMode.CEILING);
					pmd.setPercentChange(pp);
				}
				commMap.put(t.getMarketName(), pmd);
			}
			/*System.out.println(commMap);*/
			return commMap;
		
		} else if (exchangeName.equalsIgnoreCase("Bleutrade")) {
			BleutradeMarketDataServiceRaw bwdsr = new BleutradeMarketDataServiceRaw(
					exchange);
			List<BleutradeTicker> tikers;
			try {
				tikers = bwdsr.getBleutradeTickers();
			} catch (ConnectException q) {
				// TODO Auto-generated catch block
				return new HashMap();
			} catch (IOException e) {
				tikers = new ArrayList<BleutradeTicker>();
			}
			Map<String, PoloniexMarketData> commMap = new HashMap<String, PoloniexMarketData>();
			for (BleutradeTicker t : tikers) {
				PoloniexMarketData pmd = new PoloniexMarketData();
				pmd.setBaseVolume(t.getBaseVolume());
				pmd.setLast(t.getLast());
				pmd.setLowestAsk(t.getAsk());
				pmd.setHigh24hr(t.getHigh());
				pmd.setLow24hr(t.getLow());
				pmd.setQuoteVolume(t.getVolume());
				pmd.setHighestBid(t.getBid());
				if (t.getPrevDay().doubleValue() > 0) {
					BigDecimal pp = (t.getLast().setScale(8,
							RoundingMode.CEILING).subtract(t.getPrevDay()
							.setScale(8, RoundingMode.CEILING))).divide(t
							.getPrevDay().setScale(8, RoundingMode.CEILING),
							RoundingMode.CEILING);
					pmd.setPercentChange(pp);
				}
				commMap.put(t.getMarketName(), pmd);
			}
			return commMap;
		} else if (exchangeName.equalsIgnoreCase("BTCE")) {
			BTCEMarketDataServiceRaw bwdsr = new BTCEMarketDataServiceRaw(
					exchange);
			// return bwdsr.getBTCETicker(currencyPair);
		} else if (exchangeName.equalsIgnoreCase("BTCTrade")) {
			/*BTCTradeMarketDataService bwdsr = (BTCTradeMarketDataService) exchange
					.getMarketDataService();*/
		} else if (exchangeName.equalsIgnoreCase("Hitbtc")) {
		/*	System.out.println(12456);*/
			HitbtcMarketDataService bwdsr = (HitbtcMarketDataService) exchange
					.getMarketDataService();
			Map<String, HitbtcTicker> tikers;
			try {
				tikers = bwdsr.getHitbtcTickers();
			} catch (ConnectException q) {
				// TODO Auto-generated catch block
				return new HashMap();
			} catch (IOException e) {
				tikers = new HashMap<String, HitbtcTicker>();
			}
			Map<String, PoloniexMarketData> commMap = new HashMap<String, PoloniexMarketData>();
			for (String k : tikers.keySet()) {
				HitbtcTicker t = tikers.get(k);

				PoloniexMarketData pmd = new PoloniexMarketData();
				pmd.setBaseVolume(t.getVolume());
				pmd.setLast(t.getLast());
				pmd.setLowestAsk(t.getAsk());
				pmd.setHigh24hr(t.getHigh());
				pmd.setLow24hr(t.getLow());
				pmd.setQuoteVolume(t.getVolumeQuote());
				pmd.setHighestBid(t.getBid());
				/*System.err.println(t.getOpen());*/
				if(t.getOpen()==null){
					
				}
				else{
					BigDecimal pp = (t.getLast().setScale(8,
							RoundingMode.CEILING).subtract(t.getOpen()
							.setScale(8, RoundingMode.CEILING))).divide(t
							.getOpen().setScale(8, RoundingMode.CEILING),
							RoundingMode.CEILING);
					pmd.setPercentChange(pp);
				}
				commMap.put(k, pmd);
			}
			/*System.out.println(commMap);*/
			return commMap;
		} else if (exchangeName.equalsIgnoreCase("Huobi")) {
			HuobiMarketDataService bwdsr = (HuobiMarketDataService) exchange
					.getMarketDataService();
			// return bwdsr.getTicker(currencyPair);
		} else if (exchangeName.equalsIgnoreCase("Jubi")) {
			// https://www.jubi.com/coin/allcoin?t=0.7755636541251603
			JSONObject json;
			try {
				json = new JSONObject(
						readJsonFromUrl("https://www.jubi.com/coin/allcoin"));
			} catch (org.codehaus.jettison.json.JSONException e) {
				// TODO Auto-generated catch block
				json = new JSONObject();
			}

			Map<String, PoloniexMarketData> commMap = new HashMap<String, PoloniexMarketData>();
			Iterator iterator = json.keys();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				JSONArray value = null;
				try {
					value = json.getJSONArray(key);
				} catch (org.codehaus.jettison.json.JSONException e) {
					// TODO Auto-generated catch block
					value = new JSONArray();
				}

				PoloniexMarketData pmd = new PoloniexMarketData();
				try {
					pmd.setAdditionalProperty("name", value.get(0).toString()
							.toUpperCase());
				} catch (org.codehaus.jettison.json.JSONException e) {
					// TODO Auto-generated catch block
					pmd.setAdditionalProperty("name", 0);
				}
				BigDecimal high;
				try {
					high = new BigDecimal(value.get(4).toString());
				} catch (org.codehaus.jettison.json.JSONException e) {
					// TODO Auto-generated catch block
					high = new BigDecimal(0);
				}
				BigDecimal low;
				try {
					low = new BigDecimal(value.get(5).toString());
				} catch (org.codehaus.jettison.json.JSONException e) {
					// TODO Auto-generated catch block
					low = new BigDecimal(0);
				}
				BigDecimal buy;
				try {
					buy = new BigDecimal(value.get(1).toString());
				} catch (org.codehaus.jettison.json.JSONException e) {
					// TODO Auto-generated catch block
					buy = new BigDecimal(0);
				}
				BigDecimal sell;
				try {
					sell = new BigDecimal(value.get(3).toString());
				} catch (org.codehaus.jettison.json.JSONException e) {
					// TODO Auto-generated catch block
					sell = new BigDecimal(0);
				}
				BigDecimal last;
				try {
					last = new BigDecimal(value.get(2).toString());
				} catch (org.codehaus.jettison.json.JSONException e) {
					// TODO Auto-generated catch block
					last = new BigDecimal(0);
				}
				BigDecimal vol;
				try {
					vol = new BigDecimal(value.get(6).toString());
				} catch (org.codehaus.jettison.json.JSONException e) {
					// TODO Auto-generated catch block
					vol = new BigDecimal(0);
				}
				BigDecimal qVol;
				try {
					qVol = new BigDecimal(value.get(7).toString());
				} catch (org.codehaus.jettison.json.JSONException e) {
					// TODO Auto-generated catch block
					qVol = new BigDecimal(0);
				}

				pmd.setBaseVolume(vol.divide(BigDecimal.valueOf(10000),
						RoundingMode.CEILING).setScale(2, RoundingMode.CEILING));
				pmd.setLast(last);
				pmd.setLowestAsk(buy);
				pmd.setHigh24hr(high);
				pmd.setLow24hr(low);
				pmd.setQuoteVolume(qVol.divide(BigDecimal.valueOf(10000),
						RoundingMode.CEILING).setScale(2, RoundingMode.CEILING));
				pmd.setHighestBid(sell);

				/*BigDecimal pp = (last.setScale(8, RoundingMode.CEILING)
						.subtract(low.setScale(8, RoundingMode.CEILING)))
						.divide(low.setScale(8, RoundingMode.CEILING),
								RoundingMode.CEILING);
				pmd.setPercentChange(pp);*/

				commMap.put(key, pmd);
			}
			return commMap;

			// return bwdsr.getTicker(currencyPair);
		} else if (exchangeName.equalsIgnoreCase("Yobit")) {
			// https://yobit.net/api/3/ticker/ltc_btc-nmc_btc
			YoBitMarketDataService bwdsr = (YoBitMarketDataService) exchange
					.getMarketDataService();
			// return bwdsr.getTicker(currencyPair);
		} else if (exchangeName.equalsIgnoreCase("Kraken")) {
			// https://api.kraken.com/0/public/Ticker?pair_name=btc
			KrakenMarketDataServiceRaw bwdsr = new KrakenMarketDataServiceRaw(
					exchange);
		}
		return new HashMap();
	}


	private String readAll(BufferedReader rd) {
		StringBuilder sb = new StringBuilder();
		try {

			String line = null;
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			rd.close();
			return sb.toString();
		} catch (Exception e) {
			// TODO: handle exception
			return sb.toString();
		}
	}

	public String readJsonFromUrl(String url) {
		try {

			InputStream is = new URL(url).openStream();
			try {
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						is, Charset.forName("UTF-8")));
				return readAll(rd);
			} finally {
				is.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
			return "";
		}
	}

	public static String loadJson(String url) {
		StringBuilder json = new StringBuilder();
		try {
			URL urlObject = new URL(url);
			URLConnection uc = urlObject.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					uc.getInputStream(), "utf-8"));
			String inputLine = null;
			while ((inputLine = in.readLine()) != null) {
				json.append(inputLine);
			}
			in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json.toString();
	}

	/***
	 * 新开盘价格
	 * */

	public List<NewOpen> newopen(String userid,String exchangName,String coin){
		
		if(userid==null || userid.equals("") || exchangName==null || exchangName.equals("") || coin==null || coin.equals("")){
			return new ArrayList<NewOpen>(0);
		}
		
		Map<String, PoloniexMarketData>tickermap = getAllTickerList(exchangName);
		if(tickermap.isEmpty()){
			return new ArrayList<NewOpen>(0);
		}
		BigDecimal last = BigDecimal.ZERO;
		BigDecimal volume = BigDecimal.ZERO;
		if (exchangName.equals("jubi")
				|| exchangName.equals("btc38")) {
            if(coin.equals("CNY")){
            	BigDecimal cnylast=new BigDecimal(1);
            	last = cnylast.divide(tickermap.get("btc").getLast(),8,BigDecimal.ROUND_HALF_UP);
            }else if (tickermap.get(coin.toLowerCase()) == null) {
			
			} else {
				last = (tickermap.get(coin.toLowerCase()).getLast()).divide(tickermap.get("btc").getLast(),8,BigDecimal.ROUND_HALF_UP);
				volume=(tickermap.get(coin.toLowerCase()).getLast()).divide(tickermap.get("btc").getBaseVolume(),8,BigDecimal.ROUND_HALF_UP);
			}
			} else if(exchangName.equals("bittrex")){
				last = tickermap.get("BTC-"+coin).getLast();
				volume=tickermap.get("BTC-"+coin).getBaseVolume();
			}else  if (exchangName.equals("bitfinex")
					||exchangName.equals("therocktrading")) {
				last = tickermap.get(coin+ "BTC").getLast();
				volume=tickermap.get(coin+ "BTC").getBaseVolume();
			} else if (exchangName.equals("bleutrade")) {
				last = tickermap.get(coin+ "_BTC").getLast();
				volume=tickermap.get(coin+ "_BTC").getBaseVolume();
			} else if (exchangName.equals("bter")
					|| exchangName.equals("livecoin")) {
				last = tickermap.get(coin+ "/BTC").getLast();
				volume=tickermap.get(coin+ "/BTC").getBaseVolume();
			} else if (coin.equals("BCC")) {
				last = tickermap.get("BTC_"+coin).getLast();
				volume=tickermap.get("BTC_"+coin).getBaseVolume();
			} else {//Poloniex
				last = tickermap.get("BTC_"+ coin.toUpperCase()).getLast();
				volume=tickermap.get("BTC_"+coin.toUpperCase()).getBaseVolume();
			}
		List<NewOpen>list=new ArrayList<NewOpen>();
		NewOpen newopen= new NewOpen();
		newopen.setNowPrice(last.toString());
		newopen.setVolume(volume.toString());
		list.add(newopen);
		return list;
	}
}
