package com.bdh.db.mob;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;


public class SmsSender {

    public static boolean sendMsgAfterCreated(String userPhone, String providerName, String title) {
        String msg = "您好！您已成功预约�??" + providerName + "”的�?" + title + "�?" 
                + "。请您尽快完成支付，您也可以给对方留�?，协商细节！【易绿网�?";
        return sendMessageBy(userPhone, msg);
    }
    
    public static boolean sendMsgAfterConfirmed(String userPhone, String providerName, String title) {
        String msg = "您好，对方�??" + providerName + "”已接受您�??" + title + "’的预订"
                + "。您可进入�?�会员中心�?��?�我的订单�?�栏目，关注对方给您的留�?。�?�易绿网�?";
        return sendMessageBy(userPhone, msg);
    }
    
    public static boolean sendMsgAfterRejected(String userPhone, String providerName, String title) {
        String msg = "抱歉，您预订的�??" + title + "�?" 
                + "，因对方未能接受您的预订。建议您重新选择其他公司！�?�易绿网�?";
        return sendMessageBy(userPhone, msg);
    }
    
    
    public static boolean sendMsgAfterFinished(String userPhone, String providerName, String title) {
        String msg = "您好，�??" + providerName + "�?"
                + "已确认完成您预订的业务�?��?�易绿网�?";
        
        return sendMessageBy(userPhone, msg);
    }
    
    public static boolean sendMsg2MentorAfterPaid(String providerPhone, String userName, String title) {
        String msg = "您好！会员�??" + userName + "’已预订您的�?" + title + "�?"
                + "。请进入“会员中�?-我的订单”查看详细信息，尽快给会员回复确认�?��?�易绿网�?";
        
        return sendMessageBy(providerPhone, msg);
    }
    
    public static boolean sendMsg2MentorAfterFinished(String providerPhone, String userName, String title) {
        String msg = "您好，您已确认�??" + userName + "”的订单已完成�??" + title + "�?"+
                 "。�?�易绿网�?";
        
        return sendMessageBy(providerPhone, msg);
    }


    public static boolean sendphone(String phone, String code) {

        String msg = StaticConstants.SMS_MSG.replaceAll("CODE", code);
        msg = msg + StaticConstants.SMS_NAME.replaceAll("<br/>", " ");

        return sendMessageBy(phone, msg);
    }

    public static boolean sendMessageBy(String phone, String msg) {
        URL url;
        HttpURLConnection conn;
        BufferedReader br;
        StringBuilder sub = new StringBuilder();
        try {
            msg = URLEncoder.encode(msg, "UTF-8");

            url = new URL("http://sh2.ipyy.com/sms.aspx?action=send&userid=&account=" + StaticConstants.SMS_ACCOUNT
                    + "&password=" + StaticConstants.SMS_PWD + "&mobile=" + phone + "&content=" + msg + "&sendTime=");
            conn = (HttpURLConnection) url.openConnection();

            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            String line;
            while ((line = br.readLine()) != null) {
                sub.append(line + "");
            }
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return sub.indexOf("Success") > 0 && sub.indexOf("成功") > 0;
    }

    public static void main(String[] args) {
    	 Random r=new Random();
	       int tag[]={0,0,0,0,0,0,0,0,0,0};
	       String four="";
	       int temp=0;
	       while(four.length()!=4){
	               temp=r.nextInt(10);//随机获取0~9的数�?
	               if(tag[temp]==0){
	                     four+=temp;
	                    tag[temp]=1;
	               }
	       }
	       System.out.println(four);
        System.out.println(sendphone("15171981855",four));

    }
}
