package com.bdh.db.entry;

import java.math.BigDecimal;
import java.util.Date;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order.OrderType;

public class UserTrade {

	/**
	 * Did this trade result from the execution of a bid or a ask?
	 */
	protected OrderType type;

	/**
	 * Amount that was traded
	 */
	protected BigDecimal tradableAmount;

	/**
	 * The currency pair
	 */
	protected String currencyPair;

	/**
	 * The price
	 */
	protected BigDecimal price;

	/**
	 * The timestamp of the trade according to the exchange's server, null if
	 * not provided
	 */
	protected Date timestamp;

	/**
	 * The trade id
	 */
	protected String id;

	public OrderType getType() {
		return type;
	}

	public void setType(OrderType type) {
		this.type = type;
	}

	public BigDecimal getTradableAmount() {
		return tradableAmount;
	}

	public void setTradableAmount(BigDecimal tradableAmount) {
		this.tradableAmount = tradableAmount;
	}

	public String getCurrencyPair() {
		return currencyPair;
	}

	public void setCurrencyPair(String currencyPair) {
		this.currencyPair = currencyPair;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
