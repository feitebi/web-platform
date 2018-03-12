package com.bdh.db.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.ConnectException;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.bittrex.v1.dto.marketdata.BittrexTicker;
import org.knowm.xchange.bittrex.v1.service.BittrexMarketDataServiceRaw;
import org.knowm.xchange.bleutrade.dto.marketdata.BleutradeTicker;
import org.knowm.xchange.bleutrade.service.BleutradeMarketDataServiceRaw;
import org.knowm.xchange.btc38.dto.marketdata.Btc38Ticker;
import org.knowm.xchange.btc38.dto.marketdata.Btc38TickerReturn;
import org.knowm.xchange.btce.v3.service.BTCEMarketDataServiceRaw;
import org.knowm.xchange.btctrade.service.BTCTradeMarketDataService;
import org.knowm.xchange.bter.dto.marketdata.BTERTicker;
import org.knowm.xchange.bter.service.BTERMarketDataServiceRaw;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.exceptions.NotAvailableFromExchangeException;
import org.knowm.xchange.exceptions.NotYetImplementedForExchangeException;
import org.knowm.xchange.hitbtc.dto.marketdata.HitbtcTicker;
import org.knowm.xchange.hitbtc.service.HitbtcMarketDataService;
import org.knowm.xchange.huobi.service.HuobiMarketDataService;
import org.knowm.xchange.jubi.JubiExchange;
import org.knowm.xchange.jubi.service.JubiTradeServiceRaw;
import org.knowm.xchange.kraken.service.KrakenMarketDataServiceRaw;
import org.knowm.xchange.poloniex.dto.marketdata.PoloniexMarketData;
import org.knowm.xchange.poloniex.service.PoloniexMarketDataServiceRaw;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.yobit.service.YoBitMarketDataService;

import com.alibaba.fastjson.JSONException;
import com.bdh.db.entry.AccountBalance;
import com.bdh.db.entry.Apis;
import com.bdh.db.entry.Btc38AllTicker;
import com.bdh.db.entry.Exchang;
import com.bdh.db.entry.Following;
import com.bdh.db.entry.HistoryLimitOrder;
import com.bdh.db.entry.HistoryOrder;
import com.bdh.db.entry.Order;
import com.bdh.db.entry.PagableData;
import com.bdh.db.entry.StatusBean;
import com.bdh.db.entry.Strategy;
import com.bdh.db.entry.UserTrade;
import com.bdh.db.entry.smartOrder;
import com.bdh.db.util.DBUtil;
import com.bdh.db.util.FunctionSet;

import org.junit.Test;
import org.knowm.xchange.bitstamp.BitstampExchange;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trade;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.knowm.xchange.poloniex.PoloniexExchange;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.service.trade.TradeService;
import org.knowm.xchange.service.trade.params.TradeHistoryParamsAll;

import com.bdh.db.entry.Account;

public class strategyDao {

	//查询智能买卖
	public PagableData<smartOrder> getsmartorder(String userid){
		Connection conn= null;
		PreparedStatement stmt= null;
		PagableData<smartOrder> pd=new PagableData<smartOrder>();
		try {
			conn = DBUtil.getInstance().getConnection();
			StringBuffer sql=new StringBuffer();
			sql.append("SELECT * from strategy where strategyFlag=0 and userid='"+FunctionSet.filt(userid)+"'");
			stmt=conn.prepareStatement(sql.toString());
			List<smartOrder> olist=DBUtil.getInstance().convert(stmt.executeQuery(), smartOrder.class);
			
			if(!olist.isEmpty()){
				pd.setDataList(olist);
			}
			sql=null;
		} catch (Exception e) {
			
			return pd;
		}finally{
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return pd;
	}	
	
	/**首页智能策略**/
	public PagableData<smartOrder> getsmartorderTriker(String userid,String limit,String start){
		Connection conn= null;
		PreparedStatement stmt= null;
		PagableData<smartOrder> pd=new PagableData<smartOrder>();
		try {
			conn = DBUtil.getInstance().getConnection();
			StringBuffer sql=new StringBuffer();
			sql.append("SELECT * from strategy WHERE strategy.userId='"+FunctionSet.filt(userid)+"' and platform in(SELECT exchang.logo FROM apis,exchang WHERE exchang.exchangid=apis.exchangid and apis.isEnable=1 and apis.flag=1 and apis.userId='"+FunctionSet.filt(userid)+"') ");
		    sql.append(" limit ");
			sql.append(start);
			sql.append(" , ");
			sql.append(limit);
			stmt=conn.prepareStatement(sql.toString());
			List<smartOrder> olist=DBUtil.getInstance().convert(stmt.executeQuery(), smartOrder.class);
			if(!olist.isEmpty()){
				pd.setDataList(olist);
				String countsql="select count(id) c from strategy where userid='"+FunctionSet.filt(userid)+"'";
				    ResultSet rs=stmt.executeQuery(countsql);
				if(rs.next()){
					pd.setTotalCount(rs.getInt("c"));
					pd.setItemCount(Integer.parseInt(limit));
					pd.setItemCount(Integer.parseInt(start));
				}
			}
			sql=null;
		} catch (Exception e) {
			
			return pd;
		}finally{
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return pd;
	}

	
	
	//取消智能买卖
	public StatusBean cancelSmartorder(String id){
		StatusBean bean=new StatusBean();
		bean.setFlag(0);
		Connection conn= null;
		Statement stmt = null;
		try {
			conn= DBUtil.getInstance().getConnection();
			stmt = conn.createStatement();
			StringBuffer sql= new StringBuffer();
			sql.append("UPDATE strategy SET strategyFlag=2 where id='"+FunctionSet.filt(id)+"'");
			if(stmt.executeUpdate(sql.toString()) > 0){
		    	   bean.setFlag(1);
		        }
		} catch (Exception e) {
			return bean;
		}finally{
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return bean;
	}
	

		
	//��������
	
	
	
	
	
	
	/**
	 * 查询api
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

	
	public String class4Name(String name) {
		// bitflyer cryptopia localbitcoins litebit bitlish exmo usecryptos
		// bitport.net cryptomate
		/*if (name.equalsIgnoreCase("c-cex")) {
			return "org.knowm.xchange." + name.substring(0, 1)
					+ name.substring(2) + "."
					+ name.substring(0, 1).toUpperCase()
					+ name.substring(2).toUpperCase() + "Exchange";
		}*/
		/*if (name.equalsIgnoreCase("cex.io")) {
			return "org.knowm.xchange." + name.substring(0, 1)
					+ name.substring(1, 3) + name.substring(4, 6) + "."
					+ name.substring(0, 1).toUpperCase() + name.substring(1, 3)
					+ name.substring(4, 6).toUpperCase() + "Exchange";
		}*/
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
		/*if (name.equalsIgnoreCase("empoex")) {
			return "org.knowm.xchange." + name.toLowerCase() + "."
					+ name.substring(0, 1).toUpperCase() + name.substring(1, 4)
					+ name.substring(4, 6).toUpperCase() + "Exchange";
		}*/
		if (name.equalsIgnoreCase("therocktrading")) {
			return "org.knowm.xchange." + name.substring(0, 7) + "."
					+ name.substring(0, 1).toUpperCase() + name.substring(1, 3)
					+ name.substring(3, 4).toUpperCase() + name.substring(4, 7)
					+ "Exchange";
		}
	/*	if (name.equalsIgnoreCase("btctrade")) {
			return "org.knowm.xchange." + name+ "."
					+ name.substring(0, 4).toUpperCase() + name.substring(4)
					+ "Exchange";
		}*/
		
		return "org.knowm.xchange." + name.toLowerCase() + "."
				+ name.substring(0, 1).toUpperCase() + name.substring(1)
				+ "Exchange";
	}

	
	/***
	 * 查询订单
	 * */
public PagableData<HistoryLimitOrder> getHistoryLimitOrder(String userId) {
		PagableData<HistoryLimitOrder> pd=new PagableData<HistoryLimitOrder>();
		if(userId==null){
			return pd;
		}
		List<Exchang> list=loadAllApis(userId);
		if(list.isEmpty()){
			return pd;
		}
		List<HistoryLimitOrder> historyLimitOrder= new ArrayList<HistoryLimitOrder>();
		Map<String, List<LimitOrder>> allPlatformOrderMap = new LinkedHashMap<String, List<LimitOrder>>();
		String[] a = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			ExchangeSpecification spec = new ExchangeSpecification(
					class4Name(list.get(i).getLogo()));
			spec.setUserName(userId);
			spec.setApiKey(list.get(i).getApiKey());
			spec.setSecretKey(list.get(i).getApiSecret());
			a[i] = list.get(i).getLogo();
			Exchange bitstamp = ExchangeFactory.INSTANCE
					.createExchange(spec);
			OpenOrders uts;
			try {
				uts = bitstamp.getTradeService().getOpenOrders();
				} catch (IOException io) {
					uts= new OpenOrders(new ArrayList<LimitOrder>(0));
			    } catch (Exception e) {
				    uts= new OpenOrders(new ArrayList<LimitOrder>(0));
			   }
			if(!uts.getOpenOrders().isEmpty()){
			allPlatformOrderMap.put(list.get(i).getLogo(), uts.getOpenOrders());
			List<com.bdh.db.entry.LimitOrder> limitOrder= new ArrayList<com.bdh.db.entry.LimitOrder>();
			for (LimitOrder lo : uts.getOpenOrders()) {
				List<HistoryOrder> historyOrder= new ArrayList<HistoryOrder>();
				HistoryOrder h= new HistoryOrder();
				h.setAveragePrice(String.valueOf(lo.getAveragePrice()));
				h.setType(String.valueOf(lo.getType()));
				h.setCurrencyPair(String.valueOf(lo.getCurrencyPair()));
				h.setId(String.valueOf(lo.getId()));
				h.setStatus(String.valueOf(lo.getStatus()));
				h.setTimestamp(String.valueOf(lo.getTimestamp()));
				h.setTradableAmount(String.valueOf(lo.getTradableAmount()));
				historyOrder.add(h);
				com.bdh.db.entry.LimitOrder limitorder= new com.bdh.db.entry.LimitOrder();
				limitorder.setLimitPrice(String.valueOf(lo.getLimitPrice()));
				limitorder.setOrder(historyOrder);
				limitOrder.add(limitorder);
			}
			HistoryLimitOrder hlOrder= new HistoryLimitOrder();
			hlOrder.setPlatform(a[i]);
			hlOrder.setLimitOrder(limitOrder);
			historyLimitOrder.add(hlOrder);
			}
		}
		pd.setDataList(historyLimitOrder);
		return pd;
	}
private Exchange getExchangeTickers(String platform) {

	Exchange exchange = null;
	ExchangeSpecification spec = null;
		spec = new ExchangeSpecification(class4Name(platform.toLowerCase()));
		exchange = ExchangeFactory.INSTANCE.createExchange(spec);
	return exchange;
}
	
//ticker
	public Map getAllTickerList(String exchangeName) {
		/*Exchange exchange = getExchangeByName(userId, exchangeName);
		if (exchange == null) {
			return new HashMap();
		}*/
		Exchange exchange = getExchangeTickers(exchangeName);
		if (exchange == null) {
			return new HashMap();
		}
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
				return pmds.getAllPoloniexTickers();
				
			/*} catch (ConnectException q) {
				// TODO Auto-generated catch block
				return new HashMap();
			}*/}catch (IOException e) {
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
				/*if (t.getOpen().doubleValue() > 0) {
					BigDecimal pp = (t.getLast().setScale(8,
							RoundingMode.CEILING).subtract(t.getOpen()
							.setScale(8, RoundingMode.CEILING))).divide(t
							.getOpen().setScale(8, RoundingMode.CEILING),
							RoundingMode.CEILING);
					pmd.setPercentChange(pp);
				}*/
				commMap.put(k, pmd);
			}
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


private static String readAll(BufferedReader rd) {
	StringBuilder sb = new StringBuilder();
try {


	String line = null;
	while ((line = rd.readLine()) != null) {
		sb.append(line);
	}
	rd.close();
	return sb.toString();
} catch (Exception e) {
return sb.toString();
}
}

public static String readJsonFromUrl(String url)  {
	try {
		
	
	InputStream is = new URL(url).openStream();
	try {
		BufferedReader rd = new BufferedReader(new InputStreamReader(is,
				Charset.forName("UTF-8")));
		return readAll(rd);
	} finally {
		is.close();
	}
	} catch (Exception e) {
		return "";
	}
}
	public StatusBean smartSell(Strategy strategy){
		StatusBean bean=new StatusBean();
		bean.setFlag(-1);
		Connection conn= null;
		Statement stmt = null;
		try {
			conn= DBUtil.getInstance().getConnection();
			   stmt = conn.createStatement();
			StringBuilder beffer= new StringBuilder();
			Long time=System.currentTimeMillis();
			beffer.append("insert into strategy(strategyId,userId,platform,coinName,askOrBid,priceRule,askPrice,qty,ProfitRatio,topOrlow,strategyFlag,Price4Days,createTime) ");
			beffer.append("values('");
	        beffer.append(strategy.getStrategyId());
	        beffer.append("','");
	        beffer.append(strategy.getUserId());
	        beffer.append("','");
	        beffer.append(FunctionSet.filt(strategy.getPlatform()));
	        beffer.append("','");
	        beffer.append(FunctionSet.filt(strategy.getCoinName()));
	        beffer.append("','");
	        beffer.append(FunctionSet.filt(strategy.getAskOrBid()));
	        beffer.append("','");
	        beffer.append(FunctionSet.filt(strategy.getPriceRule()));
	        beffer.append("','");
	        beffer.append(strategy.getAskPrice());
	        beffer.append("','");
	        beffer.append(strategy.getQty());
	        beffer.append("','");
	        beffer.append(strategy.getProfitRatio());
	        beffer.append("','");
	        beffer.append(strategy.getTopOrlow());
	        beffer.append("','");
	        beffer.append(0);
	        beffer.append("','");
	        beffer.append(strategy.getPrice4Days());
	        beffer.append("',");
	        beffer.append(time);
	       beffer.append(")");
	       if(stmt.executeUpdate(beffer.toString()) > 0){
	    	   if(strategy.getStrategyFlag()==1){
	    		   String sql="SELECT * FROM strategy WHERE userId='"+strategy.getUserId()+"' and createTime='"+time+"'";
	    		   List<Strategy> strategies = DBUtil.getInstance().convert(stmt.executeQuery(sql), Strategy.class);
	    		   if(!strategies.isEmpty()){
	    			   bean.setFlag(strategies.get(0).getId());
	    		   }
	    	   }
	    	   else{
	    		   bean.setFlag(0);
	    	   }
	        }
		} catch (Exception e) {
			
			
		}finally{
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return bean;
	}
	
	
	
	//买的
	public StatusBean smartBuyTo(Strategy strategy,String isNext){
		StatusBean bean=new StatusBean();
		bean.setFlag(0);
		Connection conn= null;
		Statement stmt = null;
		try {
			conn= DBUtil.getInstance().getConnection();
			   stmt = conn.createStatement();
			StringBuilder beffer= new StringBuilder();
			Long time=System.currentTimeMillis();
			beffer.append("insert into strategy(strategyId,userId,platform,coinName,askOrBid,priceRule,BidPrice,qty,topOrlow,strategyFlag,Price4Days,createTime) ");
			beffer.append("values('");
	        beffer.append(strategy.getStrategyId());
	        beffer.append("','");
	        beffer.append(strategy.getUserId());
	        beffer.append("','");
	        beffer.append(FunctionSet.filt(strategy.getPlatform()));
	        beffer.append("','");
	        beffer.append(FunctionSet.filt(strategy.getCoinName()));
	        beffer.append("','");
	        beffer.append(FunctionSet.filt(strategy.getAskOrBid()));
	        beffer.append("','");
	        beffer.append(FunctionSet.filt(strategy.getPriceRule()));
	        beffer.append("','");
	        beffer.append(strategy.getBidPrice());
	        beffer.append("','");
	        beffer.append(strategy.getQty());
	        beffer.append("','");
	        beffer.append(strategy.getTopOrlow());
	        beffer.append("','");
	        beffer.append(strategy.getStrategyFlag());
	        beffer.append("','");
	        beffer.append(strategy.getPrice4Days());
	        beffer.append("',");
	        beffer.append(time);
	       beffer.append(")");
	       if(stmt.executeUpdate(beffer.toString()) > 0){
	    	
	    	   if(isNext.equals("yes")){
	    		   String sql="SELECT * FROM strategy WHERE userId='"+strategy.getUserId()+"' and createTime='"+time+"'";
	    		   List<Strategy> strategies = DBUtil.getInstance().convert(stmt.executeQuery(sql), Strategy.class);
	    		   if(!strategies.isEmpty()){
	    			   bean.setOtherCode(String.valueOf(strategies.get(0).getId()));
	    		   }
		    	   }else if(isNext.equals("no")){
		    		   bean.setOtherCode("noid");
		    	   }
	    	   bean.setFlag(1);
	        }
		} catch (Exception e) {
			
			
		}finally{
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return bean;
	}
	
	//卖得
	public StatusBean smartSellTo(Strategy strategy,String isNext){
		StatusBean bean=new StatusBean();
		bean.setFlag(0);
		Connection conn= null;
		Statement stmt = null;
		try {
			conn= DBUtil.getInstance().getConnection();
			   stmt = conn.createStatement();
			StringBuilder beffer= new StringBuilder();
			Long time=System.currentTimeMillis();
			beffer.append("insert into strategy(strategyId,userId,platform,coinName,askOrBid,priceRule,askPrice,qty,ProfitRatio,topOrlow,strategyFlag,Price4Days,createTime) ");
			beffer.append("values('");
	        beffer.append(strategy.getStrategyId());
	        beffer.append("','");
	        beffer.append(strategy.getUserId());
	        beffer.append("','");
	        beffer.append(FunctionSet.filt(strategy.getPlatform()));
	        beffer.append("','");
	        beffer.append(FunctionSet.filt(strategy.getCoinName()));
	        beffer.append("','");
	        beffer.append(FunctionSet.filt(strategy.getAskOrBid()));
	        beffer.append("','");
	        beffer.append(FunctionSet.filt(strategy.getPriceRule()));
	        beffer.append("','");
	        beffer.append(strategy.getAskPrice());
	        beffer.append("','");
	        beffer.append(strategy.getQty());
	        beffer.append("','");
	        beffer.append(strategy.getProfitRatio());
	        beffer.append("','");
	        beffer.append(strategy.getTopOrlow());
	        beffer.append("','");
	        beffer.append(0);
	        beffer.append("','");
	        beffer.append(strategy.getPrice4Days());
	        beffer.append("',");
	        beffer.append(time);
	        beffer.append(")");
	       if(stmt.executeUpdate(beffer.toString()) > 0){
	    	
	    	   if(isNext.equals("yes")){
	    		   String sql="SELECT * FROM strategy WHERE userId='"+strategy.getUserId()+"' and createTime='"+time+"'";
	    		   List<Strategy> strategies = DBUtil.getInstance().convert(stmt.executeQuery(sql), Strategy.class);
	    		   if(!strategies.isEmpty()){
	    			   bean.setOtherCode(String.valueOf(strategies.get(0).getId()));
	    		   }
		    	   }else if(isNext.equals("no")){
		    		   bean.setOtherCode("noid");
		    	   }
	    	   bean.setFlag(1);
	        }
		} catch (Exception e) {
			
			
		}finally{
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return bean;
	}
	//��������
	public StatusBean smartBuy(Strategy strategy){
		StatusBean bean=new StatusBean();
		bean.setFlag(0);
		Connection conn= null;
		Statement stmt = null;
		try {
			conn= DBUtil.getInstance().getConnection();
			   stmt = conn.createStatement();
			StringBuilder beffer= new StringBuilder();
			Long time=System.currentTimeMillis();
			beffer.append("insert into strategy(strategyId,userId,platform,coinName,askOrBid,priceRule,BidPrice,qty,topOrlow,strategyFlag,Price4Days,createTime) ");
			beffer.append("values('");
	        beffer.append(strategy.getStrategyId());
	        beffer.append("','");
	        beffer.append(strategy.getUserId());
	        beffer.append("','");
	        beffer.append(FunctionSet.filt(strategy.getPlatform()));
	        beffer.append("','");
	        beffer.append(FunctionSet.filt(strategy.getCoinName()));
	        beffer.append("','");
	        beffer.append(FunctionSet.filt(strategy.getAskOrBid()));
	        beffer.append("','");
	        beffer.append(FunctionSet.filt(strategy.getPriceRule()));
	        beffer.append("','");
	        beffer.append(strategy.getBidPrice());
	        beffer.append("','");
	        beffer.append(strategy.getQty());
	        beffer.append("','");
	        beffer.append(strategy.getTopOrlow());
	        beffer.append("','");
	        beffer.append(strategy.getStrategyFlag());
	        beffer.append("','");
	        beffer.append(strategy.getPrice4Days());
	        beffer.append("',");
	        beffer.append(time);
	       beffer.append(")");
	       if(stmt.executeUpdate(beffer.toString()) > 0){
	    	   bean.setFlag(1);
	        }
		} catch (Exception e) {
			
			
		}finally{
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return bean;
	}
	
		//取消智能买卖
		public StatusBean cancelSmartorder(String id, String userid){
			StatusBean bean=new StatusBean();
			bean.setFlag(0);
			Connection conn= null;
			Statement stmt = null;
			try {
				conn= DBUtil.getInstance().getConnection();
				stmt = conn.createStatement();
				StringBuffer sql= new StringBuffer();
				if(userid!=""||userid!=null){
				sql.append("UPDATE strategy SET strategyFlag=2 where id='"+FunctionSet.filt(id)+"' and userid='"+FunctionSet.filt(userid)+"'");
				}
				if(stmt.executeUpdate(sql.toString()) > 0){
			    	   bean.setFlag(1);
			        }
			} catch (Exception e) {
				
			}finally{
				DBUtil.getInstance().close(stmt);
				DBUtil.getInstance().close(conn);
			}
			return bean;
		}
		
		//读取各交易所key
		
		public  PagableData<AccountBalance> getuserbalance(String platform, String userid) {
			PagableData<AccountBalance>pd=new PagableData<AccountBalance>();
			Connection conn = null;
			Statement stmt = null;
			List<AccountBalance>list=null;
			try {
				conn = DBUtil.getInstance().getConnection();
				stmt = conn.createStatement();
				String sql = "SELECT*from apis WHERE exchangid in(SELECT exchangid from exchang WHERE name='"+FunctionSet.filt(platform)+"') AND userId='"+userid+"'";
				List<Apis> ds = DBUtil.getInstance().convert(stmt.executeQuery(sql), Apis.class);
				if(!ds.isEmpty()){
					ExchangeSpecification spec = new ExchangeSpecification(class4Name(platform));
					spec.setUserName(ds.get(0).getUserid());
					spec.setApiKey(ds.get(0).getApiKey());
					spec.setSecretKey(ds.get(0).getApiSecret());
					list=getBalanceByKey(spec);
				}	
				if(!list.isEmpty()){
					pd.setDataList(list);
				}
			} catch (Exception e) {
				
			} finally {
				DBUtil.getInstance().close(stmt);
				DBUtil.getInstance().close(conn);
			}			
			return pd;
		}
		
		//读取balance
		public  List<AccountBalance> getBalanceByKey(ExchangeSpecification spec){
			List<AccountBalance> list=new ArrayList<AccountBalance>();
			Map<Currency, Balance> blanceMap = null;
			if(spec==null){
				return new ArrayList<AccountBalance>();
			}
			Exchange bitstamp = ExchangeFactory.INSTANCE.createExchange(spec);
			AccountService accountService = bitstamp.getAccountService();
			AccountInfo accountInfo;
			try {
				accountInfo = accountService.getAccountInfo();
				blanceMap = accountInfo.getWallet().getBalances();
			} catch (Exception e) {
				blanceMap=new LinkedHashMap<Currency, Balance>(0);
			}
			if(blanceMap.isEmpty()){
				return list;
			}
	
			for (Balance b : blanceMap.values()) {
				if(b.getTotal()!= null && (b.getTotal().floatValue() > 0
						|| b.getFrozen().floatValue() > 0
						|| b.getAvailable().floatValue() > 0)){
				list.add(new AccountBalance(b.getAvailable(),b.getBorrowed(),b.getDepositing(),
						b.getFrozen(),b.getTotal(),b.getWithdrawing(),b.getLoaned(),b.getCurrency().toString()));
				}		
			}
			return list;	
		}
		
		
		
		 
		// ticker
		public  PagableData<String> getticker(String userid,String platform,String coinname){
			 PagableData<String> pd=new PagableData<String>();
			 if(platform==null || platform.equals("") || coinname==null || coinname.equals("")){
				 return pd;
			 }
				Map<String, PoloniexMarketData> tickermap = null;
				tickermap = getAllTickerList(platform);
				if(tickermap.isEmpty()){
					return pd;
				}
				List<String> lastprice=new ArrayList<String>();
				if(platform.toLowerCase().equals("jubi")
						|| platform.toLowerCase().equals("btc38")){
					if(coinname.toUpperCase().equals("BTC")){
						lastprice.add("1"); 
					}
					if(tickermap.get(coinname.toUpperCase())==null){
					}else{				
						lastprice.add(tickermap.get(coinname.toUpperCase()).getLast().toString());
					}
				} else  if(platform.toLowerCase().equals("bittrex")){
					if(coinname.equals("BTC")){
						lastprice.add("1"); 
					}else {
					lastprice.add(tickermap.get("BTC-"+coinname)
							.getLast().toString());
					}
				}else if(platform.toLowerCase().equals("bitfinex")
						|| platform.equals("therocktrading")){
					if(coinname.equals("BTC")){
						lastprice.add("1"); 
					}else {
					lastprice.add(tickermap.get(coinname + "BTC").getLast().toString());
					}
				} else if(platform.toLowerCase().equals("bleutrade")){
					if(coinname.equals("BTC")){
						lastprice.add("1"); 
					}else {
					lastprice.add(tickermap.get(coinname + "_BTC").getLast().toString());
					}
				}else if(platform.toLowerCase().equals("bter")
						|| platform.equals("livecoin")){
					if(coinname.equals("BTC")){
						lastprice.add("1"); 
					}else {
					lastprice.add(tickermap.get(coinname + "/BTC").getLast().toString());
					}
				}else if(platform.toLowerCase().equals("poloniex")){ 
					if(coinname.toUpperCase().equals("BTC")){
						lastprice.add("1"); 
					}else {
					lastprice.add(tickermap.get("BTC_" + coinname.toUpperCase()).getLast().toString());
					}
				}	
				if(!lastprice.isEmpty()){
				pd.setDataList(lastprice);
				}
				return pd;
		}
		
		
		
		
	/*	private  Exchange getExchangeByName(String userId, String platform)
				 {
			Exchange exchange = null;
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
			return exchange;
		}
		*/
		
		

		/***
		 * 
		 * 历史订单
		 * **/
		public  Map<String, List<UserTrade>> getTradeHistory(String userId,String platform,
				String apikey,String apisecret){
			Map<String, List<UserTrade>> allPlatformBalanceMap = new LinkedHashMap<String, List<UserTrade>>();
			if(platform==null || apikey==null || apisecret==null){
				return allPlatformBalanceMap;
			}
		
			
				Map<Currency, Balance> blanceMap = null;
				ExchangeSpecification spec = new ExchangeSpecification(
						class4Name(platform));
				spec.setUserName(userId);
				spec.setApiKey(apikey);
				spec.setSecretKey(apisecret);
				Exchange bitstamp = ExchangeFactory.INSTANCE
						.createExchange(spec);
				AccountService accountService = bitstamp.getAccountService();
				AccountInfo accountInfo;
				try {
					accountInfo = accountService.getAccountInfo();
					blanceMap = accountInfo.getWallet().getBalances();
				} catch (IOException io) {
					blanceMap=new LinkedHashMap<Currency, Balance>(0);
				} catch (Exception e) {
					blanceMap=new LinkedHashMap<Currency, Balance>(0);
				} 
				if(!blanceMap.isEmpty()){
				List<UserTrade> tL = new ArrayList<UserTrade>();
				for (Balance b : blanceMap.values()) {
						String currency=b.getCurrency().getCurrencyCode();
					
						CurrencyPair currencyPair;
						currencyPair=new CurrencyPair("BCH",currency);
						currencyPair=new CurrencyPair("BTC",currency);
						currencyPair=new CurrencyPair("DOGE",currency);
						currencyPair=new CurrencyPair("ETH",currency);
						currencyPair=new CurrencyPair("ETC",currency);
						currencyPair=new CurrencyPair("EUR",currency);
						currencyPair=new CurrencyPair("FTC",currency);
						currencyPair=new CurrencyPair("LTC",currency);
						currencyPair=new CurrencyPair("NMC",currency);
						currencyPair=new CurrencyPair("NVC",currency);
						currencyPair=new CurrencyPair("PPC",currency);
						currencyPair=new CurrencyPair("USD",currency);
						currencyPair=new CurrencyPair("UTC",currency);
						currencyPair=new CurrencyPair(currency,"CNY");
						currencyPair=new CurrencyPair(currency,"BCH");
						currencyPair=new CurrencyPair(currency,"ETC");
						currencyPair=new CurrencyPair(currency,"BTC");
						currencyPair=new CurrencyPair(currency,"DOGE");
						currencyPair=new CurrencyPair(currency,"ETH");
						currencyPair=new CurrencyPair(currency,"EUR");
						currencyPair=new CurrencyPair(currency,"FTC");
						currencyPair=new CurrencyPair(currency,"NMC");
						currencyPair=new CurrencyPair(currency,"NVC");
						currencyPair=new CurrencyPair(currency,"USD");
						currencyPair=new CurrencyPair(currency,"UTC");
						if(platform.equals("jubi")){
							currencyPair=new CurrencyPair(currency,"CNY");
						}
						else{
							if(b.getCurrency().getCurrencyCode().equals("BTC")){
							continue;
							}
							currencyPair=new CurrencyPair(currency,"BTC");
						}
					Exchange exchange;
					try {
						exchange = ExchangeFactory.INSTANCE.createExchangeWithApiKeys(class4Name(platform), apikey, apisecret);
					} catch (Exception e) {
						// TODO: handle exception
						exchange=null;
					}
					
						TradeHistoryParamsAll thp = new TradeHistoryParamsAll();
						if (currencyPair != null) {
							thp.setCurrencyPair(currencyPair);
						}
						long start = System.currentTimeMillis() / 1000 - 60 * 24 * 60 * 600;
						thp.setStartTime(new Date(start));
						thp.setEndTime(new Date(System.currentTimeMillis() / 1000));
						thp.setPageLength(60);
						List<org.knowm.xchange.dto.marketdata.Trade> trades;
						try {
							trades = exchange.getTradeService().getTradeHistory(thp)
									.getTrades();
						} catch (IOException io) {
							trades = new ArrayList<org.knowm.xchange.dto.marketdata.Trade>();
						} catch (Exception e) {
							trades = new ArrayList<org.knowm.xchange.dto.marketdata.Trade>();
						}
						
						for (org.knowm.xchange.dto.marketdata.Trade trade : trades) {
							UserTrade ut = new UserTrade();
							ut.setCurrencyPair(trade.getCurrencyPair().toString());
							ut.setId(trade.getId());
							ut.setPrice(trade.getPrice());
							ut.setTimestamp(trade.getTimestamp());
							ut.setTradableAmount(trade.getTradableAmount());
							ut.setType(trade.getType());
							tL.add(ut);
						}
						
				}
				if(!tL.isEmpty()){
					allPlatformBalanceMap.put(platform, tL);
				}
		}
			return allPlatformBalanceMap;
		}
	//交易订单
		public Map<String, List<com.bdh.db.entry.Order>> getMyAllOpenOrderList(
				String userId,String platform,String apikey,String apisecret){
			Map<String, List<com.bdh.db.entry.Order>> allPlatformOrderMap = new HashMap<String, List<com.bdh.db.entry.Order>>();

			if(platform==null || apikey==null || apisecret==null){
				return allPlatformOrderMap;
			}
		
				
			if(platform.equals("jubi")){
					BalanceDao dao= new BalanceDao();
					Map<String, Collection<AccountBalance>> allPlatformBalanceMap=dao.getAllPlatformSummary(userId, "jubi", apikey,apisecret);
					for (Collection<AccountBalance> b : allPlatformBalanceMap.values()) {
						for (AccountBalance accountBalance : b) {
							 Exchange exchange = ExchangeFactory.INSTANCE.createExchangeWithApiKeys(JubiExchange.class.getName(), apikey,apisecret);
							    TradeService tradeService = exchange.getTradeService();
							    OpenOrders openOrders;
								try {
									openOrders = tradeService.getOpenOrders(((JubiTradeServiceRaw)tradeService)
										  .createJubiOpenOrdersParams(new CurrencyPair(accountBalance.getCurrency(), accountBalance.getCurrency())));
								} catch (IOException io) {
									openOrders=new OpenOrders(new ArrayList<LimitOrder>(0));
									} catch (Exception e) {
									openOrders=new OpenOrders(new ArrayList<LimitOrder>(0));;
								} 
							 
							  if(openOrders.toString().equals("No open orders!") || !openOrders.getOpenOrders().isEmpty()){
								  allPlatformOrderMap.put(platform, new ArrayList<Order>(0));
							  }else{
									List<com.bdh.db.entry.Order>lists= new ArrayList<com.bdh.db.entry.Order>();
									
									for (int j = 0; j < openOrders.getOpenOrders().size(); j++) {
										com.bdh.db.entry.Order order= new com.bdh.db.entry.Order();
									     if(openOrders.getOpenOrders().get(j).getAveragePrice()==null
									    		 || openOrders.getOpenOrders().get(j).getStatus()==null){
									    	 order.setAveragePrice("0");
									    	 order.setStatus("0");
									     }else{
									    	 order.setAveragePrice(openOrders.getOpenOrders().get(j).getAveragePrice().toString());
									    	 order.setStatus(openOrders.getOpenOrders().get(j).getStatus().toString());
									     }
										
											order.setId(openOrders.getOpenOrders().get(j).getId());
											order.setCurrencyPair(openOrders.getOpenOrders().get(j).getCurrencyPair().toString());
											order.setLimitPrice(openOrders.getOpenOrders().get(j).getLimitPrice().toString());
											order.setTimestamp(openOrders.getOpenOrders().get(j).getTimestamp().toString());
											order.setTradableAmount(openOrders.getOpenOrders().get(j).getTradableAmount().toString());
											order.setType(openOrders.getOpenOrders().get(j).getType().toString());
											lists.add(order);
									}
										allPlatformOrderMap.put(platform, lists);
							  }
						}
						}
				}
					else{

						ExchangeSpecification spec = new ExchangeSpecification(
								class4Name(platform));
						spec.setUserName(userId);
						spec.setApiKey(apikey);
						spec.setSecretKey(apisecret);
						Exchange bitstamp = ExchangeFactory.INSTANCE
								.createExchange(spec);
						OpenOrders uts;
						try {
							uts = bitstamp.getTradeService().getOpenOrders();
						} catch (IOException io) {
							uts= new OpenOrders(new ArrayList<LimitOrder>(0));
							} catch (Exception e) {
								/*e.printStackTrace();*/
							uts=new OpenOrders(new ArrayList<LimitOrder>(0));
						} 
					if(!uts.getOpenOrders().isEmpty()){
						List<com.bdh.db.entry.Order>list= new ArrayList<com.bdh.db.entry.Order>();
					for (int i = 0; i < uts.getOpenOrders().size(); i++) {
						com.bdh.db.entry.Order order= new com.bdh.db.entry.Order();
					     if(uts.getOpenOrders().get(i).getAveragePrice()==null
					    		 || uts.getOpenOrders().get(i).getStatus()==null){
					    	 order.setAveragePrice("0");
					    	 order.setStatus("0");
					     }else{
					    	 order.setAveragePrice(uts.getOpenOrders().get(i).getAveragePrice().toString());
					    	 order.setStatus(uts.getOpenOrders().get(i).getStatus().toString());
					     }
							order.setId(uts.getOpenOrders().get(i).getId());
							order.setCurrencyPair(uts.getOpenOrders().get(i).getCurrencyPair().toString());
							order.setLimitPrice(uts.getOpenOrders().get(i).getLimitPrice().toString());
							order.setTimestamp(uts.getOpenOrders().get(i).getTimestamp().toString());
							
							order.setTradableAmount(uts.getOpenOrders().get(i).getTradableAmount().toString());
							order.setType(uts.getOpenOrders().get(i).getType().toString());
							list.add(order);
					}
						allPlatformOrderMap.put(platform, list);	
					}else{
						allPlatformOrderMap.put(platform, new ArrayList<Order>(0));	
					}
					}
			return allPlatformOrderMap;
		}
		

		
}
