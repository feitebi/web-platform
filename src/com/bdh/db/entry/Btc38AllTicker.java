/**
 * 
 */
package com.bdh.db.entry;

import java.io.IOException;
import java.util.Map;

import org.apache.log4j.chainsaw.Main;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.btc38.dto.marketdata.Btc38TickerReturn;
import org.knowm.xchange.btc38.service.Btc38MarketDataServiceRaw;

/**
 * @author mac
 *
 */
public class Btc38AllTicker extends Btc38MarketDataServiceRaw {

	public Btc38AllTicker(Exchange exchange) {
		super(exchange);
	}

	public Map<String, Btc38TickerReturn> getBtc38AllTickers(String targetCurrency) throws IOException {

		return this.btc38.getMarketTicker(targetCurrency);

	}
	
}
