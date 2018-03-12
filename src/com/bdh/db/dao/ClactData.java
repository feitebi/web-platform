package com.bdh.db.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONException;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.bter.service.BTERMarketDataServiceRaw;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.hitbtc.HitbtcExchange;
import org.knowm.xchange.hitbtc.dto.marketdata.HitbtcSymbol;
import org.knowm.xchange.hitbtc.dto.marketdata.HitbtcSymbols;
import org.knowm.xchange.hitbtc.dto.meta.HitbtcMetaData;
import org.knowm.xchange.hitbtc.service.HitbtcMarketDataService;
import org.knowm.xchange.hitbtc.service.HitbtcMarketDataServiceRaw;
import org.knowm.xchange.huobi.service.HuobiMarketDataServiceRaw;
import org.knowm.xchange.jubi.JubiExchange;
import org.knowm.xchange.jubi.dto.marketdata.JubiTicker;
import org.knowm.xchange.poloniex.dto.marketdata.PoloniexChartData;
import org.knowm.xchange.poloniex.service.PoloniexChartDataPeriodType;
import org.knowm.xchange.poloniex.service.PoloniexMarketDataServiceRaw;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.jubi.service.JubiMarketDataServiceRaw;

import com.alibaba.fastjson.JSON;
import com.bdh.db.entry.ChactDataEntry;
import com.bdh.db.entry.Exchang;
import com.bdh.db.util.DBUtil;
import com.bdh.db.util.FunctionSet;
import com.mysql.fabric.xmlrpc.base.Data;
import com.sun.jersey.api.json.JSONWithPadding;

public class ClactData {
	/**
	 * https://bittrex.com/Api/v2.0/pub/market/GetTicks?marketName=BTC-SC&
	 * tickInterval=thirtyMin&_=1500713275429
	 * 
	 * 
	 * dayLine TimeLine=hour
	 * http://www.btc38.com/trade/getTrade5minLine.php?coinname
	 * =BTC&mk_type=CNY&n=0.8403421349216622
	 * 
	 * 
	 * https://data.bter.com/json_svr/query/?u=10&c=380225&type=kline&symbol=
	 * btc_cny&group_sec=1800&range_hour=48
	 * 
	 * 
	 * 
	 * 
	 * https://www.livecoin.net/tradeapi/mainGraphData?period=day&currencyPair=
	 * BTC%2FUSD
	 * 
	 * https://c-cex.com/t/api_pub.html?a=getmarketsummaries
	 * 
	 * 
	 * https://www.jubi.com/api/v1/allticker/
	 */

	public String class4Name(String name) {
		// bitflyer cryptopia localbitcoins litebit bitlish exmo usecryptos
		// bitport.net cryptomate
		
		if(name==null){
			return "";
		}
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
		if (name.equalsIgnoreCase("therocktrading")) {
			return "org.knowm.xchange." + name.substring(0, 7) + "."
					+ name.substring(0, 1).toUpperCase() + name.substring(1, 3)
					+ name.substring(3, 4).toUpperCase() + name.substring(4, 7)
					+ "Exchange";
		}

		return "org.knowm.xchange." + name.toLowerCase() + "."
				+ name.substring(0, 1).toUpperCase() + name.substring(1)
				+ "Exchange";
	}

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
			e.printStackTrace();
		} finally {
			DBUtil.getInstance().close(stmt);
			DBUtil.getInstance().close(conn);
		}
		return new ArrayList<Exchang>(0);
	}

	private Exchange getExchangeByName(String userId, String platform) {

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
			return exchange;
		}
		exchange = ExchangeFactory.INSTANCE.createExchange(spec);
		return exchange;
	}
	private Exchange getExchangeTickers(String platform) {
	
		Exchange exchange = null;
		ExchangeSpecification spec = null;
			spec = new ExchangeSpecification(class4Name(platform.toLowerCase()));
			exchange = ExchangeFactory.INSTANCE.createExchange(spec);
		return exchange;
	}
	/***
	 * 
	 * 
	 * hitbtc 
	 * https://hitbtc.com/save-hit-chart-state:{"zoom":10,"period":"D1","symbol":"BCCETH"}
	 * state:{zoom:10,period:D1,symbol:BCCETH}
	 * 
	 * 
	 * https://hitbtc.com/save-hit-chart-{zoom:10,period:D1,symbol:BCCBTC}
	 * 
	 * */

	/**
	 * poloniex
	 * 
	 * if BTC currencyPair=BTC_***
	 * 
	 * if ETH currencyPair=ETH_*** if XMR currencyPair=XMR_***
	 *  if USDT  currencyPair=USDT_***
	 * 
	 * https://poloniex.com/public?command=returnChartData&currencyPair
	 * =BTC_ETH&start=1499904000&end=9999999999&period=14400 完成度
	 * 100%**************************************
	 * */

	
	
	
	public List getPoloniexChartData(String exchangeName,
			String currency,String Pair, String period){
		List<PoloniexChartData> cdList = new ArrayList<PoloniexChartData>(0);
		if(exchangeName==null || currency ==null || Pair==null || period==null){
			return cdList;
		}
		String pcpt = null;
		  long time=1439010600;
		  long timeStart = 0;
		if ("60".equals(period) || "300".equals(period)) {
			pcpt = "300";
			  timeStart	=time+300000;
		} else if ("900".equals(period)) {
			pcpt = "900";
		    	  timeStart	=time+900000;
		} else if ("1800".equals(period) || "3600".equals(period)) {
			pcpt = "1800";
			  timeStart	=time+1800000;
		} else if ("7200".equals(period) || "10800".equals(period)) {
			pcpt = "7200";
			  timeStart	=time+7200000;
		} else if ("14400".equals(period) || "43200".equals(period)) {
			pcpt = "14400";
			  timeStart	=time+14400000;
		} else if ("86400".equals(period)) {
			pcpt = "86400";
			  timeStart	=time+86400000;
		}
		String url = "https://poloniex.com/public?command=returnChartData&currencyPair="
				+ currency+"_"+Pair
				+ "&start="+time+"&end="+timeStart+"&period="
				+ pcpt + "";
		URL chata;
		try {
			chata = new URL(url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			chata=null;
		}
		StringBuffer strBuf;
		strBuf = new StringBuffer();
		HttpURLConnection connection;
		try {
			connection = (HttpURLConnection) chata
					.openConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			connection=null;
		}
		connection.setRequestProperty("User-Agent",
				"Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			reader=null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			reader=null;
		}// 转码。
		try {
			strBuf.append(reader.readLine());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			strBuf.append("");
		}
		String json = strBuf.toString();// 判断获取的数据
		JSONArray chartList;
		try {
			chartList = new JSONArray(json);
		} catch (org.codehaus.jettison.json.JSONException e) {
			// TODO Auto-generated catch block
			chartList=new JSONArray();
		}
		for (int i = 0; i < chartList.length(); i++) {
		
			try {
				JSONObject k = chartList.getJSONObject(i);
				long l = Long.parseLong(k.getString("date"));
				Date d = new Date(l);
				PoloniexChartData pcd = new PoloniexChartData(d, new BigDecimal(
						k.getDouble("high")).setScale(8, BigDecimal.ROUND_HALF_UP),
						new BigDecimal(k.getDouble("low")).setScale(8,
								BigDecimal.ROUND_HALF_UP), new BigDecimal(
								k.getDouble("open")).setScale(8,
								BigDecimal.ROUND_HALF_UP), new BigDecimal(
								k.getDouble("close")).setScale(8,
								BigDecimal.ROUND_HALF_UP), new BigDecimal(
								k.getDouble("volume")).setScale(8,
								BigDecimal.ROUND_HALF_UP), new BigDecimal(
								k.getDouble("quoteVolume")).setScale(8,
								BigDecimal.ROUND_HALF_UP), new BigDecimal(
								k.getDouble("weightedAverage")).setScale(8,
								BigDecimal.ROUND_HALF_UP));
			
				cdList.add(pcd);
			} catch (org.codehaus.jettison.json.JSONException e) {
				// TODO Auto-generated catch block
				cdList.add(null);
			}
			
		}
		return cdList;

	}

	/***
	 * bittrex BTC-SC
	 * 
	 * 
	 * if   BTC BTC-***
	 * if   ETH ETH-***
	 * if USDT  USDT-***
	 * 完成度 100%**********************************************************
	 * 
	 * */

/*public static void main(String[] args) {
	ClactData dao= new ClactData();
	dao.getBittrexChartData( "bittrex", "BTC", "LMC", "1800");
}*/
	public List getBittrexChartData(String exchangeName,
			String currency, String Pair, String period){
		
		List<PoloniexChartData> cdList = new ArrayList<PoloniexChartData>(0);
		if( exchangeName==null || currency ==null || Pair==null || period==null){
			return cdList;
		}
		String pcpt = null;
		if ("60".equals(period)) {
			pcpt = "oneMin";
		} else if ("300".equals(period) || "900".equals(period)) {
			pcpt = "fiveMin";
		} else if ("1800".equals(period)) {
			pcpt = "thirtyMin";
		} else if ("3600".equals(period) 
				|| "7200".equals(period) 
				|| "10800".equals(period) 
				|| "14400".equals(period) 
				|| "43200".equals(period)) {
			pcpt = "hour";
		} else if ("86400".equals(period) || "259200".equals(period)
				|| "604800".equals(period) || "2678400".equals(period)
				|| "2592000".equals(period) || "2419200".equals(period)
				|| "2505600".equals(period)) {
			pcpt = "day";
		}
		JSONObject json;
		try {
			json = new JSONObject(
					readJsonFromUrl("https://bittrex.com/Api/v2.0/pub/market/GetTicks?marketName="
							+ currency
							+ "-"
							+ Pair
							+ "&tickInterval="
							+ pcpt
							+ "&_=" + System.currentTimeMillis()));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			json=new JSONObject();
		} catch (org.codehaus.jettison.json.JSONException e) {
			// TODO Auto-generated catch block
			json=new JSONObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			json=new JSONObject();
		}
		JSONArray chartList;
		try {
			chartList = json.getJSONArray("result");
		} catch (org.codehaus.jettison.json.JSONException e) {
			// TODO Auto-generated catch block
			chartList=new JSONArray();
		}
		for (int i = 0; i < chartList.length(); i++) {
			try {
				JSONObject k = chartList.getJSONObject(i);
				Date d = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(k
							.getString("T"));
				PoloniexChartData pcd = new PoloniexChartData(d, new BigDecimal(
						k.getDouble("H")).setScale(8, BigDecimal.ROUND_HALF_UP),
						new BigDecimal(k.getDouble("L")).setScale(8,
								BigDecimal.ROUND_HALF_UP), new BigDecimal(
								k.getDouble("O")).setScale(8,
								BigDecimal.ROUND_HALF_UP), new BigDecimal(
								k.getDouble("C")).setScale(8,
								BigDecimal.ROUND_HALF_UP), new BigDecimal(
								k.getDouble("V")).setScale(8,
								BigDecimal.ROUND_HALF_UP), new BigDecimal(
								k.getDouble("BV")).setScale(8,
								BigDecimal.ROUND_HALF_UP), new BigDecimal("0"));
			
				cdList.add(pcd);
			}catch (ParseException e) {
				// TODO Auto-generated catch block
				cdList.add(null);
			} catch (org.codehaus.jettison.json.JSONException e) {
				// TODO Auto-generated catch block
				cdList.add(null);
			}
			
		}
		System.out.println(cdList);
		return cdList;
	}

	/**
	 * 
	 * btc38
	 * 
	 * 完成度 100%**********************************************************
	 * http://www.btc38.com/trade/getTrade5minLine.php?coinname=BTC&mk_type=CNY&n=0.7928042956000709
	 * http://www.btc38.com/trade/getTrade5minLine.php?coinname=BTC&mk_type=CNY&n=0.08688116086952191
	 * http://www.btc38.com/trade/getTradeTimeLine.php?coinname=BTC&mk_type=CNY&n=0.05874548283864689
	 * http://www.btc38.com/trade/getTradeTimeLine.php?coinname=BTC&mk_type=CNY&n=0.6899549888643872
	 * http://www.btc38.com/trade/getTradeDayLine.php?coinname=BTC&mk_type=CNY&n=0.1800261947194466
	 * http://www.btc38.com/trade/getTradeDayLine.php?coinname=BTC&mk_type=CNY&n=0.5620155209933024
	 * dayLine TimeLine=hour
	 * http://www.btc38.com/trade/getTrade5minLine.php?coinname
	 * =BTC&mk_type=CNY&n=0.8403421349216622
	 * **/
	
	public List getBtc38ChartData(String exchangeName,
			String currency,String Pair, String period){
		List<PoloniexChartData> cdList = new ArrayList<PoloniexChartData>(0);
		if(exchangeName==null || currency ==null || Pair==null || period==null){
			return cdList;
		}
		String pcpt = null;
		Pair="CNY";
		if ("60".equals(period) || "300".equals(period)) {
			pcpt = "5minLine";
		} else if ("900".equals(period) || "1800".equals(period) || "3600".equals(period)) {
			pcpt = "TimeLine";
		} else if ("7200".equals(period) || "10800".equals(period) || "14400".equals(period) || "43200".equals(period) || "86400".equals(period)) {
			pcpt = "DayLine";
		}
		String url = "http://www.btc38.com/trade/getTrade" + pcpt
				+ ".php?coinname=" + currency + "&mk_type="+Pair+"";
		JSONArray chartList;
		try {
			URL chata = new URL(url);
			StringBuffer strBuf;
			strBuf = new StringBuffer();
			HttpURLConnection connection = (HttpURLConnection) chata
					.openConnection();
			connection.setRequestProperty("User-Agent",
					"Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), "utf-8"));// 转码。
			strBuf.append(reader.readLine());
			String json = strBuf.toString();// 判断获取的数据
			chartList = new JSONArray(json);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			chartList = new JSONArray();
		}
		
		for (int i = 0; i < chartList.length(); i++) {
			 
			try {
				long time = (long) chartList.getJSONArray(i).get(0);
				Date d = new Date(time);
				PoloniexChartData pcd = new PoloniexChartData(d,
						new BigDecimal(chartList.getJSONArray(i).get(2).toString()),
						new BigDecimal(chartList.getJSONArray(i).get(5).toString()),
						new BigDecimal(chartList.getJSONArray(i).get(4).toString()),
						new BigDecimal(chartList.getJSONArray(i).get(3).toString()),
						new BigDecimal(chartList.getJSONArray(i).get(1).toString()),
						new BigDecimal(0), new BigDecimal(0));
			
				cdList.add(pcd);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				cdList.add(null);
			}
		
		}
		return cdList;
	}

	/****
	 * 
	 * 
	 * bleutrade ETH_BTC 币种_BTC
	 * https://bleutrade.com/api/v2/public/getcandles?market=ETH_BTC
	 * &period=30m&lasthours=30
	 * 
	 * 完成度
	 * **********************************************************************
	 * *100
	 * 
	 * */

	public List getBleutradeChartData(String exchangeName,
			String currency,String Pair, String period)  {
		List<PoloniexChartData> cdList = new ArrayList<PoloniexChartData>(0);
		if(exchangeName==null || currency ==null || Pair==null || period==null){
			return cdList;
		}
		String pcpt = null;
		Pair="_BTC";
		if ("60".equals(period) || "300".equals(period)) {
			pcpt = "5m&lasthours=5";
		} else if ("900".equals(period)) {
			pcpt = "15m&lasthours=15";
		} else if ("1800".equals(period)) {
			pcpt = "30m&lasthours=30";
		} else if ("3600".equals(period)) {
			pcpt = "1h&lasthours=24";
		} else if ("7200".equals(period) || "10800".equals(period)) {
			pcpt = "3h&lasthours=72";
		} else if ("14400".equals(period) || "43200".equals(period)) {
			pcpt = "12h&lasthours=720";
		} else if ("86400".equals(period)) {
			pcpt = "1d&lasthours=720";
		}
		String url = "https://bleutrade.com/api/v2/public/getcandles?market="
				+ currency + Pair + "&period=" + pcpt;
		URL chata;
		try {
			chata = new URL(url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			chata=null;
		}
		StringBuffer strBuf;
		strBuf = new StringBuffer();
		HttpURLConnection connection;
		try {
			connection = (HttpURLConnection) chata
					.openConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			connection=null;
		}
		connection.setRequestProperty("User-Agent",
				"Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
		JSONArray chartList;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), "utf-8"));
			strBuf.append(reader.readLine());
			String json = strBuf.toString();// 判断获取的数据
			JSONObject jsons = new JSONObject(json);
		    chartList = jsons.getJSONArray("result");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			chartList=new JSONArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			chartList=new JSONArray();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			chartList=new JSONArray();
		}// 转码。
		
		
		for (int i = 0; i < chartList.length(); i++) {
			try {
				JSONObject	k = chartList.getJSONObject(i);
				Date d;
				try {
					d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(k
							.getString("TimeStamp"));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					d=null;
				}
				PoloniexChartData pcd = new PoloniexChartData(d, new BigDecimal(
						k.getDouble("High")).setScale(8, BigDecimal.ROUND_HALF_UP),
						new BigDecimal(k.getDouble("Low")).setScale(8,
								BigDecimal.ROUND_HALF_UP), new BigDecimal(
								k.getDouble("Open")).setScale(8,
								BigDecimal.ROUND_HALF_UP), new BigDecimal(
								k.getDouble("Close")).setScale(8,
								BigDecimal.ROUND_HALF_UP), new BigDecimal(
								k.getDouble("Volume")).setScale(8,
								BigDecimal.ROUND_HALF_UP), new BigDecimal(
								k.getDouble("BaseVolume")).setScale(8,
								BigDecimal.ROUND_HALF_UP), new BigDecimal("0"));
			
				cdList.add(pcd);
			} catch (org.codehaus.jettison.json.JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return cdList;
	}

	/****
	 * 
	 * 
	 * bter eth_cny
	 * https://bter.com/json_svr/query/?u=10&c=380225&type=kline&symbol
	 * =eth_cny&group_sec=300&range_hour=12
	 * 
	 * */

	public  String readTxt(String exchangeName,
			String currency,String Pair, String period){
		PrintWriter out = null;  
        BufferedReader in = null;  
        StringBuffer result = null;
    	String pcpt = null;
    	if(exchangeName==null || currency ==null || Pair==null || period==null){
			return result.toString();
		}
    	try {
		if ("300".equals(period) || "60".equals(period)) {
			pcpt = "300&range_hour=12";// 5min
		} else if ("900".equals(period)) {
			pcpt = "900&range_hour=24";// 15min
		} else if ("1800".equals(period)) {
			pcpt = "1800&range_hour=48";// 30min
		} else if ("7200".equals(period) || "3600".equals(period)) {
			pcpt = "3600&range_hour=96";// 1hour
		} else if ("14400".equals(period) || "10800".equals(period)) {
			pcpt = "14400&range_hour=768";// 8hour
		} else if ("86400".equals(period) || "43200".equals(period)) {
			pcpt = "86400&range_hour=2304";// 1day
		}
		String url = "https://bter.com/json_svr/query/?u=10&c=380225&type=kline&symbol="
				+ currency+"_"+Pair + "&group_sec=" + pcpt;
        try {  
            URL realUrl = new URL(url);  
            // 打开和URL之间的连接  
            URLConnection conn = realUrl.openConnection();  
            // 设置通用的请求属性  
            conn.setRequestProperty("accept", "*/*");  
            conn.setRequestProperty("connection", "Keep-Alive");  
            conn.setRequestProperty("user-agent",  
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");  
            // 发送POST请求必须设置如下两行  
            conn.setDoOutput(true);  
            conn.setDoInput(true);  
           //  获取URLConnection对象对应的输出流  
            out = new PrintWriter(conn.getOutputStream());  
           //  flush输出流的缓冲  
            out.flush();  
          //   定义BufferedReader输入流来读取URL的响应  
            in = new BufferedReader(  
                new InputStreamReader(conn.getInputStream()));  
            String line;  
           // ChactDataEntry entry= new ChactDataEntry();
            result=new StringBuffer();
            while ((line = in.readLine()) != null) {  
          
            	result.append(line);
            	result.append(";");
            	/*result.append("\n");*///=line;
            }  
          } catch (Exception e) {  
            e.printStackTrace();  
          }  
         // 使用finally块来关闭输出流、输入流  
          finally{  
            try{  
              if(out!=null){  
                out.close();  
              }  
              if(in!=null){  
                in.close();  
              }  
            }  
            catch(IOException ex){  
              ex.printStackTrace();  
            }  
          } 
     return result.toString();
    	} catch (NullPointerException e) {
			// TODO: handle exception
    		return result.toString();
		}
	}
	public List getBterChartData(String exchangeName,
			String currency,String Pair, String period){
		List<PoloniexChartData> cdList = new ArrayList<PoloniexChartData>(0);
		if(exchangeName==null || currency ==null || Pair==null || period==null){
			return cdList;
		}
		
		String result=readTxt(exchangeName, currency,Pair, period);
	String [] dataBig = result.substring(32).toString().split(";");
	 List<ChactDataEntry> datas = new ArrayList<ChactDataEntry>();
	for(String da: dataBig){
		String [] dataSmart= da.split(",");
		ChactDataEntry data2 =new ChactDataEntry();
			data2.setDate(dataSmart[0]);
			data2.setOpen(BigDecimal.valueOf(Double.valueOf(dataSmart[1])));
			data2.setHigh(BigDecimal.valueOf(Double.valueOf(dataSmart[2])));
			data2.setLow(BigDecimal.valueOf(Double.valueOf(dataSmart[3])));
			data2.setClose(BigDecimal.valueOf(Double.valueOf(dataSmart[4])));
			data2.setVolume(BigDecimal.valueOf(Double.valueOf(dataSmart[5])));
		datas.add(data2);
	}
	for (ChactDataEntry dataEntry : datas) {
		Date d = new Date(Long.valueOf(dataEntry.getDate()));
		PoloniexChartData pcd = new PoloniexChartData(d,
				new BigDecimal(dataEntry.getHigh().toString()),//high
				new BigDecimal(dataEntry.getLow().toString()),//low
				new BigDecimal(dataEntry.getOpen().toString()),//open
				new BigDecimal(dataEntry.getClose().toString()),//close
				new BigDecimal(dataEntry.getVolume().toString()),//volume
				new BigDecimal(0),//quoteVolume
				new BigDecimal(0));//weightedAverage
		cdList.add(pcd);
	}
		return cdList;
		
	}



	/**
	 * livecoin
	 * https://www.livecoin.net/tradeapi/mainGraphData?period=day&currencyPair
	 * =BTC%2FUSD
	 *
	 *
	 * 完成度 100%*******************************************
	 * **/
	
	public List getLivecoinChartData(String exchangeName,
			String currency, String Pair, String period){
		List<PoloniexChartData> cdList = new ArrayList<PoloniexChartData>(0);
		if(exchangeName==null || currency ==null || Pair==null || period==null){
			return cdList;
		}
		String pcpt = null;
		if ("60".equals(period) || "300".equals(period) || "900".equals(period)) {
			pcpt = "m15";
		} else if ("1800".equals(period)) {
			pcpt = "m30";
		} else if ("3600".equals(period)) {
			pcpt = "hour";
		} else if ("7200".equals(period) || "10800".equals(period) || "14400".equals(period)) {
			pcpt = "hour4";
		} else if ("43200".equals(period) || "86400".equals(period)) {
			pcpt = "day";
		}
		JSONObject json;
		try {
			json = new JSONObject(
					readJsonFromUrl("https://www.livecoin.net/tradeapi/mainGraphData?period="
							+ pcpt
							+ "&currencyPair="
							+ currency
							+ "%2F"
							+ Pair
							+ ""));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			json=new JSONObject();
		} catch (org.codehaus.jettison.json.JSONException e) {
			// TODO Auto-generated catch block
			json=new JSONObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			json=new JSONObject();
		}
		JSONArray chartList;
		try {
			chartList = json.getJSONArray("ohlc");
		} catch (org.codehaus.jettison.json.JSONException e) {
			// TODO Auto-generated catch block
			chartList=new JSONArray();
		}
		JSONArray chartLists;
		try {
			chartLists = json.getJSONArray("volume");
		} catch (org.codehaus.jettison.json.JSONException e) {
			// TODO Auto-generated catch block
			chartLists=new JSONArray();
		}
		for (int i = 0; i < chartList.length(); i++) {
			for (int j = 0; j < chartLists.length(); j++) {
				try {
					if (chartList.getJSONArray(i).get(0)
							.equals(chartLists.getJSONArray(j).get(0))) {
						BigDecimal volume = new BigDecimal(chartLists
								.getJSONArray(j).get(1).toString());
						long time = (long) chartList.getJSONArray(i).get(0);
						Date d = new Date(time);
						PoloniexChartData pcd = new PoloniexChartData(d,
								new BigDecimal(chartList.getJSONArray(i).get(2)
										.toString()),
								new BigDecimal(chartList
										.getJSONArray(i).get(3).toString()),
								new BigDecimal(chartList.getJSONArray(i).get(1)
										.toString()), 
								new BigDecimal(chartList
										.getJSONArray(i).get(4).toString()),
								new BigDecimal(volume.toString()),
								new BigDecimal(0), new BigDecimal(0));
					
						cdList.add(pcd);
					}
				} catch (org.codehaus.jettison.json.JSONException e) {
					// TODO Auto-generated catch block
					cdList.add(null);
				}

			}
		}
		return cdList;
	}
	public List ChartDataKine(String exchangeName,
			String currency, String Pair, String period){
		  if(exchangeName==null || currency==null || Pair==null || period==null ){
			  return new ArrayList(0);
		  }
		try {
		
		if(exchangeName.equals("Livecoin")){
			return getLivecoinChartData(exchangeName, currency, Pair, period);
		} else if(exchangeName.equals("Bleutrade")){
			return getBleutradeChartData( exchangeName, currency, Pair, period);
		} else if(exchangeName.equals("Btc38")){
			return getBtc38ChartData(exchangeName, currency, Pair, period);
		} else if(exchangeName.equals("Bittrex")){
			return getBittrexChartData( exchangeName, currency, Pair, period);
		} else if(exchangeName.equals("Poloniex")){
			return getPoloniexChartData(exchangeName, currency, Pair, period);
		} else if(exchangeName.equals("BTER")){
			return getBterChartData(exchangeName, currency, Pair,period);
		}
		
		}
		catch (IllegalStateException e) {
			return new ArrayList<>();
			// TODO: handle exception
		}catch (Exception e) {
			// TODO: handle exception
			return new ArrayList<>();
		}
		return new ArrayList<>();
	}
	 
	public static void main(String[] args) {
		ClactData dao= new ClactData();
		dao.gethitbtc();
		
		//HitbtcMarketDataServiceRaw pmds=new HitbtcMarketDataServiceRaw(exchange);
		
		/*String[][] chartData = bwdsr.getBitVcKline(
				currencyPair.toLowerCase(), "030");
		for (int i = 0; i < chartData.length; i++) {
			String[] k = chartData[i];

			Date d = new SimpleDateFormat("yyyyMMddHH:mm00000").parse(k[0]);

			PoloniexChartData pcd = new PoloniexChartData(d,
					new BigDecimal(k[2]), new BigDecimal(k[3]),
					new BigDecimal(k[1]), new BigDecimal(k[4]),
					new BigDecimal(k[5]), new BigDecimal(k[5]),
					new BigDecimal("0"));
			cdList.add(pcd);
		}*/
	}
	 
	public List gethitbtc(){
		Exchange exchange = getExchangeTickers("Hitbtc");
		HitbtcMarketDataServiceRaw pmds=new HitbtcMarketDataService(exchange);
		try {
			System.out.println(pmds.getHitbtcTime());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public List getChartData(String userId, String exchangeName,
			String currencyPair, String Pair, String startTime, String endTime,
			String period) throws Exception {
		Exchange exchange = getExchangeByName(userId, exchangeName);
		if (exchange == null) {
			return new ArrayList<>();
		}
		CurrencyPair cp = new CurrencyPair(currencyPair, Pair);
		PoloniexChartDataPeriodType pcpt = PoloniexChartDataPeriodType.PERIOD_300;
		if ("900".equals(period)) {
			pcpt = PoloniexChartDataPeriodType.PERIOD_900;
		} else if ("7200".equals(period)) {
			pcpt = PoloniexChartDataPeriodType.PERIOD_7200;
		} else if ("1800".equals(period)) {
			pcpt = PoloniexChartDataPeriodType.PERIOD_1800;
		} else if ("14400".equals(period)) {
			pcpt = PoloniexChartDataPeriodType.PERIOD_14400;
		} else if ("86400".equals(period)) {
			pcpt = PoloniexChartDataPeriodType.PERIOD_86400;
		}
		Long st = Long.parseLong(startTime);
		Long et = Long.parseLong(endTime);

		if (exchangeName.equals("poloniex")) {
			PoloniexMarketDataServiceRaw pmds = new PoloniexMarketDataServiceRaw(
					exchange);

			return Arrays.asList(pmds.getPoloniexChartData(cp, st, et, pcpt));
		} else if (exchangeName.equals("bittrex")) {
			List<PoloniexChartData> cdList = new ArrayList<PoloniexChartData>(0);

			JSONObject json = new JSONObject(
					readJsonFromUrl("https://bittrex.com/Api/v2.0/pub/market/GetTicks?marketName="
							+ currencyPair
							+ "-"
							+ Pair
							+ "&tickInterval=day&_="
							+ System.currentTimeMillis()));

			JSONArray chartList = json.getJSONArray("result");

			for (int i = 0; i < chartList.length(); i++) {
				JSONObject k = chartList.getJSONObject(i);
				Date d = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(k
						.getString("T"));
				PoloniexChartData pcd = new PoloniexChartData(new Date(),
						new BigDecimal(k.getDouble("H")), new BigDecimal(
								k.getDouble("L")), new BigDecimal(
								k.getDouble("O")), new BigDecimal(
								k.getDouble("C")), new BigDecimal(
								k.getDouble("V")), new BigDecimal(
								k.getDouble("BV")), new BigDecimal("0"));

				cdList.add(pcd);

			}
			return cdList;

		} else if (exchangeName.equals("Huobi")) {
			List<PoloniexChartData> cdList = new ArrayList<PoloniexChartData>(0);
			HuobiMarketDataServiceRaw bwdsr = new HuobiMarketDataServiceRaw(
					exchange);
			String[][] chartData = bwdsr.getBitVcKline(
					currencyPair.toLowerCase(), "030");
			for (int i = 0; i < chartData.length; i++) {
				String[] k = chartData[i];

				Date d = new SimpleDateFormat("yyyyMMddHH:mm00000").parse(k[0]);

				PoloniexChartData pcd = new PoloniexChartData(d,
						new BigDecimal(k[2]), new BigDecimal(k[3]),
						new BigDecimal(k[1]), new BigDecimal(k[4]),
						new BigDecimal(k[5]), new BigDecimal(k[5]),
						new BigDecimal("0"));
				cdList.add(pcd);
			}
			return cdList;
		}
		return new ArrayList<>();
	}

	public static String readJsonFromUrl(String url) throws IOException,
			JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is,
					Charset.forName("UTF-8")));
			return readAll(rd);
		} finally {
			is.close();
		}
	}

	private static String readAll(BufferedReader rd) throws IOException {
		StringBuilder sb = new StringBuilder();

		String line = null;
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		rd.close();
		return sb.toString();
	}

}
