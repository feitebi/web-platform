package com.bdh.db.entry;

import java.util.List;

public class Master extends Balance{
	
	private String userid;
	private int caincont=0;   //币资�??
	private int followcount=0; //关注�??
	private int viewcant=0;   //查看�??
	private double balanceCNY;  //RMBֵ
	private double balanceBDH;  //BDHֵ
	private int fellowing=0;        //
	private setting setting;
	private Following following;
	
	private List<setting> slist;
	
	
	public Following getFollowing() {
		return following;
	}

	public void setFollowing(Following following) {
		this.following = following;
	}

	public List<setting> getSlist() {
		return slist;
	}

	public void setSlist(List<setting> slist) {
		this.slist = slist;
	}

	public setting getSetting() {
		return setting;
	}

	public void setSetting(setting setting) {
		this.setting = setting;
	}

	public Master() {
		super();
	}

	public Master(int caincont, int followcount, int viewcant,
			double balanceCNY, double balanceBDH, int fellowing) {
		super();
		this.caincont = caincont;
		this.followcount = followcount;
		this.viewcant = viewcant;
		this.balanceCNY = balanceCNY;
		this.balanceBDH = balanceBDH;
		this.fellowing = fellowing;
	}

	public Master(String userid, int caincont, int followcount, int viewcant,
			double balanceCNY, double balanceBDH,int fellowing,setting setting) {
		super();
		this.userid = userid;
		this.caincont = caincont;
		this.followcount = followcount;
		this.viewcant = viewcant;
		this.balanceCNY = balanceCNY;
		this.balanceBDH = balanceBDH;
		this.fellowing=fellowing;
		this.setting=setting;
	}
	
	
	
	public int getFellowing() {
		return fellowing;
	}

	public void setFellowing(int fellowing) {
		this.fellowing = fellowing;
	}

	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public int getCaincont() {
		return caincont;
	}
	public void setCaincont(int caincont) {
		this.caincont = caincont;
	}
	public int getFollowcount() {
		return followcount;
	}
	public void setFollowcount(int followcount) {
		this.followcount = followcount;
	}
	public int getViewcant() {
		return viewcant;
	}
	public void setViewcant(int viewcant) {
		this.viewcant = viewcant;
	}
	public double getBalanceCNY() {
		return balanceCNY;
	}
	public void setBalanceCNY(double balanceCNY) {
		this.balanceCNY = balanceCNY;
	}
	public double getBalanceBDH() {
		return balanceBDH;
	}
	public void setBalanceBDH(double balanceBDH) {
		this.balanceBDH = balanceBDH;
	}

/*	
	
	@Override
	public String toString() {
		return "Master [caincont=" + caincont + ", followcount=" + followcount
				+ ", viewcant=" + viewcant + ", balanceCNY=" + balanceCNY
				+ ", balanceBDH=" + balanceBDH + ", fellowing=" + fellowing
				+ "]";
	}

	@Override
	public int compareTo(Master o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	public static void main(String[] args) {
		
		Master menuN1 = new Master(1,4,11,0.1,1.1,14);  
		Master menuN2 = new Master(2,3,12,0.2,1.2,13);
		Master menuN3 = new Master(3,2,13,0.3,1.3,12);
		Master menuN4 = new Master(4,1,14,0.4,1.4,11);
	      
		 List<Master> list = new ArrayList<>(); 
		 list.add(menuN2);  
		 list.add(menuN1); 
		 list.add(menuN4);  
		 list.add(menuN3); 
		  for (Master menu : list) {  
	            System.out.println(menu);  
	        } 
		  long start = new Date().getTime();  
		  Collections.sort(list);  
	        System.out.println(new Date().getTime() - start);  
	  	  System.out.println("排序�??");
		  for (Master master : list) {
		
			System.out.println(master);
		}
	}
	*/

}
