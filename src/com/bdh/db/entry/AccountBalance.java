package com.bdh.db.entry;

import java.math.BigDecimal;

public class AccountBalance {
	private String currency;
	private BigDecimal available;
	private BigDecimal borrowed;
	private BigDecimal depositing;
	private BigDecimal frozen;
	private BigDecimal total;
	private BigDecimal withdrawing;
	private BigDecimal loaned;
	private BigDecimal last;
	
	
	public BigDecimal getLast() {
		return last;
	}
	public void setLast(BigDecimal last) {
		this.last = last;
	}
	public BigDecimal getAvailable() {
		return available;
	}
	public void setAvailable(BigDecimal available) {
		this.available = available;
	}
	public BigDecimal getBorrowed() {
		return borrowed;
	}
	public void setBorrowed(BigDecimal borrowed) {
		this.borrowed = borrowed;
	}
	public BigDecimal getDepositing() {
		return depositing;
	}
	public void setDepositing(BigDecimal depositing) {
		this.depositing = depositing;
	}
	public BigDecimal getFrozen() {
		return frozen;
	}
	public void setFrozen(BigDecimal frozen) {
		this.frozen = frozen;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public BigDecimal getWithdrawing() {
		return withdrawing;
	}
	public void setWithdrawing(BigDecimal withdrawing) {
		this.withdrawing = withdrawing;
	}
	public BigDecimal getLoaned() {
		return loaned;
	}
	public void setLoaned(BigDecimal loaned) {
		this.loaned = loaned;
	}

	
	
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public AccountBalance(BigDecimal available, BigDecimal borrowed,
			BigDecimal depositing, BigDecimal frozen, BigDecimal total,
			BigDecimal withdrawing, BigDecimal loaned, String currency) {
		super();
		this.available = available;
		this.borrowed = borrowed;
		this.depositing = depositing;
		this.frozen = frozen;
		this.total = total;
		this.withdrawing = withdrawing;
		this.loaned = loaned;
		this.currency = currency;
	}
	public AccountBalance() {
		super();
	}
	@Override
	public String toString() {
		return "AccountBalance [available=" + available + ", borrowed="
				+ borrowed + ", depositing=" + depositing + ", frozen="
				+ frozen + ", total=" + total + ", withdrawing=" + withdrawing
				+ ", loaned=" + loaned + ", currency=" + currency + "]";
	}

	
	
}
