package com.bdh.db.entry;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


public class Account {
	private String assatsCount; //���������ܶ�
	private String openOrdercount;//�ҵ���
	private String btcAount;//BTC������
	private String totelCNY;//���ʲ�
	private String availableAmount;//�����ʲ�
	private String bdhAmount;//BDH(��ʦ��)
	private Map<String, List<Following>> flist;

	private Map<String,List<AccountBalance>> list;
	private List<Following> master;
	private PagableData<Bookmark> bookmark;
	private BigDecimal totlecny;
	private BigDecimal Frozencny;
	
	
	public BigDecimal getTotlecny() {
		return totlecny;
	}
	public void setTotlecny(BigDecimal totlecny) {
		this.totlecny = totlecny;
	}
	public BigDecimal getFrozencny() {
		return Frozencny;
	}
	public void setFrozencny(BigDecimal frozencny) {
		Frozencny = frozencny;
	}
	public Map<String, List<Following>> getFlist() {
		return flist;
	}
	public void setFlist(Map<String, List<Following>> flist) {
		this.flist = flist;
	}
	public String getAssatsCount() {
		return assatsCount;
	}
	public void setAssatsCount(String assatsCount) {
		this.assatsCount = assatsCount;
	}
	public String getOpenOrdercount() {
		return openOrdercount;
	}
	public void setOpenOrdercount(String openOrdercount) {
		this.openOrdercount = openOrdercount;
	}
	public String getBtcAount() {
		return btcAount;
	}
	public void setBtcAount(String btcAount) {
		this.btcAount = btcAount;
	}
	public String getTotelCNY() {
		return totelCNY;
	}
	public void setTotelCNY(String totelCNY) {
		this.totelCNY = totelCNY;
	}
	public String getAvailableAmount() {
		return availableAmount;
	}
	public void setAvailableAmount(String availableAmount) {
		this.availableAmount = availableAmount;
	}
	public String getBdhAmount() {
		return bdhAmount;
	}
	public void setBdhAmount(String bdhAmount) {
		this.bdhAmount = bdhAmount;
	}
	public Map<String, List<AccountBalance>> getList() {
		return list;
	}
	public void setList(Map<String, List<AccountBalance>> list) {
		this.list = list;
	}
	public List<Following> getMaster() {
		return master;
	}
	public void setMaster(List<Following> master) {
		this.master = master;
	}
	public PagableData<Bookmark> getBookmark() {
		return bookmark;
	}
	public void setBookmark(PagableData<Bookmark> bookmarklist) {
		this.bookmark = bookmarklist;
	}
	public Account(String assatsCount, String openOrdercount, String btcAount,
			String totelCNY, String availableAmount, String bdhAmount,
			Map<String, List<AccountBalance>> list, List<Following> master,
			PagableData<Bookmark> bookmark) {
		super();
		this.assatsCount = assatsCount;
		this.openOrdercount = openOrdercount;
		this.btcAount = btcAount;
		this.totelCNY = totelCNY;
		this.availableAmount = availableAmount;
		this.bdhAmount = bdhAmount;
		this.list = list;
		this.master = master;
		this.bookmark = bookmark;
	}
	public Account() {
		super();
	}
	@Override
	public String toString() {
		return "Account [assatsCount=" + assatsCount + ", openOrdercount="
				+ openOrdercount + ", btcAount=" + btcAount + ", totelCNY="
				+ totelCNY + ", availableAmount=" + availableAmount
				+ ", bdhAmount=" + bdhAmount + ", list=" + list + ", master="
				+ master + ", bookmark=" + bookmark + "]";
	}
}
