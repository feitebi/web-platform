package com.ex.db.dao;

import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


import org.codehaus.jettison.json.JSONObject;

import com.bdh.db.dao.UserAccountDao;

public class Test {
	
	public static void main(String[] args) {
		  String url="https://bittrex.com/Api/v2.0/pub/market/GetTicks?marketName=BTC-SC&tickInterval=thirtyMin&_=1500713275429";
		  doGet(url);
	}
	public static JSONObject doGet(String url) {
		  try {
		   String result = null;
		   DefaultHttpClient httpClient = new DefaultHttpClient();

		   HttpGet request = new HttpGet(url);
		   HttpResponse response = httpClient.execute(request);
		   result = EntityUtils.toString(response.getEntity());
		   JSONObject object = new JSONObject(result);
		   System.out.println(object);
		   return object;
		               
		  } catch (Exception e) {
		   // TODO: handle exception
		  }
		  return null;

		 }
	
	
	/*
	public static void main(String[] args) {
		Runnable r = new Runnable() {
			final long start = System.currentTimeMillis();

			public void run() {
				try {
					while (true) {
						Thread.sleep(1000);
						long now = System.currentTimeMillis();
						if (now - start >= 10000) {
							System.out.println("打字时间结束了");
							System.exit(0);
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		System.out.println("可以开始打字了,10秒后就不能打了");
		Scanner s = new Scanner(System.in);
		UserAccountDao dao= new UserAccountDao();
		dao.sendCode("103417c8d47843aca10b2ed18ae39d4d", "18337816380");
		System.out.println("已发送");
		dao.updatePhone("103417c8d47843aca10b2ed18ae39d4d", "18337816380","");
		System.out.println("已修改");
		// 操作的方法
		Thread t = new Thread(r);
		t.start();
		
		 * while(s.hasNext()) { System.err.println("da");
		 * System.out.println(s.nextLine()); }
		 
	}*/
}
