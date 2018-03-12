package com.ex.db.dao;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
public class time {

	


	    public static void main(String[] args) {
/*	    	System.out.println(System.currentTimeMillis());
	 System.out.println(System.currentTimeMillis()+(1000*60));
*/
	        Timer timer = new Timer();
	        timer.schedule(new TimerTask() {
	            @Override
	            public void run() {
	                System.out.println(1);

	            }

	        }, 0, 60000);

	    }
	
}
