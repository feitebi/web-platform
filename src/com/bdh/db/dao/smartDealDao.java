/*package com.bdh.db.dao;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.bdh.db.entry.PagableData;
import com.bdh.db.entry.dealHistory;
import com.bdh.db.entry.smartDeal;
import com.bdh.db.entry.smartOrder;

public class smartDealDao {

	public PagableData<smartOrder> getsmartorder(String userid){
		PagableData<smartOrder> smarto=new PagableData<smartOrder>();
		List<smartOrder> smartorderlist=new ArrayList<smartOrder>();
		smartOrder order=new smartOrder();
		order.setUserid("2012");
		List<smartDeal>smartdeallist=new ArrayList<smartDeal>();
		smartDeal smartdeal=new smartDeal();
		smartdeal.setId("1");
		smartdeal.setPlatform("poloniex");
		smartdeal.setCoinName("BTC");
		smartdeal.setNowprice(BigDecimal.valueOf(28000.00));
		smartdeal.setInprice(BigDecimal.valueOf(13000.00));
		smartdeal.setNowprofit(BigDecimal.valueOf(15000.000));
		smartdeal.setSmartdeal("7����ͼ�");
		smartdeal.setDatatime("2017-8-8 12:12:12");
		smartdeal.setType("��");
		
		smartDeal smartdeal2=new smartDeal();
		smartdeal2.setId("2");
		smartdeal2.setPlatform("Jubi");
		smartdeal2.setCoinName("ETH");
		smartdeal2.setNowprice(BigDecimal.valueOf(28000.00));
		smartdeal2.setInprice(BigDecimal.valueOf(13000.00));
		smartdeal2.setNowprofit(BigDecimal.valueOf(15000.000));
		smartdeal2.setSmartdeal("7����߼�");
		smartdeal2.setDatatime("2017-8-8 12:12:12");
		smartdeal2.setType("��");
		smartdeallist.add(smartdeal);
		smartdeallist.add(smartdeal2);
		List<dealHistory>dealhistorylist=new ArrayList<dealHistory>();
		dealHistory dealHistory=new dealHistory();
		dealHistory.setId("1");
		dealHistory.setPlatform("poloniex");
		dealHistory.setName("BTC");
		dealHistory.setDealType("��");
		dealHistory.setAvgPrice(BigDecimal.valueOf(25000.00));
		dealHistory.setDealamount(BigDecimal.valueOf(25000.00));
		dealHistory.setDealnum(BigDecimal.valueOf(1.00));
		dealHistory.setDealtime("2017-8-8 12:12:12");
		dealhistorylist.add(dealHistory);	
		order.setSmartdeal(smartdeallist);
		order.setDealhistory(dealhistorylist);
		smartorderlist.add(order);
		smarto.setDataList(smartorderlist);
		return smarto;
	}
	
	
}
*/