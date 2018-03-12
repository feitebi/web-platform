/**
 * 
 */
package com.bdh.db.mob;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;



/**
 * @author mac
 * 
 */
public class SendSMSValidCode {

    public static final String APP_ID = "277668010000031749";
    public static final String APP_SECRET = "7d6d059b5aa35b543bb0332dc963916d";

    public static final String AT = "e8193e3dd1800fcccfcd31c592c133ff1373340406980";

    public static final String REDIRECT_URI = "http://sufuche.fitbee.cn/sms/rand_code_call_back";

    public static final Map<String, String> idn2PhoneMap = new ConcurrentHashMap<String, String>(10);

    public static void main(String[] args) {
    
        System.out.println(sendSMS("18337816380","尊敬的客户，您的注册号码位：98232"));
    }

    public static boolean sendSMS(String phone, String msg) {
        URL url;

        try {
            String a = "http://utf8.sms.webchinese.cn/?Uid=shanghai2k10&key=c2d9c62971eb94d57e55&smsMob=" + phone
                    + "&smsText=";
            a += URLEncoder.encode(msg, "UTF-8");

            url = new URL(a);
            URLConnection conn = url.openConnection();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            int i = 0;
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                i = Integer.parseInt(inputLine);
            }
            br.close();
            if (i > 0) {
                return true;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static byte[] HmacSHA1Encrypt(String encryptText, String encryptKey) throws Exception {
        String MAC_NAME = "HmacSHA1";
        String ENCODING = "UTF-8";
        byte[] data = encryptKey.getBytes(ENCODING);
        SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);
        Mac mac = Mac.getInstance(MAC_NAME);
        mac.init(secretKey);
        byte[] text = encryptText.getBytes(ENCODING);
        return mac.doFinal(text);
    }

  /*  public static String encryptBASE64(byte[] key) throws Exception {
        return new String(Base64.encodeBase64(key));
    }*/

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static String signsort(String sign_before_sort) {
        TreeMap map = new TreeMap();
        String[] sign = sign_before_sort.split("&");
        for (int i = 0; i < sign.length; i++) {
            String[] aign_a = sign[i].split("=");
            map.put(aign_a[0], aign_a[1]);
        }
        String sign_after_sort = "";
        Iterator iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            if ((map.get(key) != null) && (map.get(key) != "")) {
                sign_after_sort = sign_after_sort + "&" + key + "=" + map.get(key);
            }
        }
        return sign_after_sort.substring(1);
    }

    public static String excutePost(String targetURL, String urlParameters) {
        URL url;
        HttpURLConnection connection = null;
        try {
            // Create connection
            url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            // Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            // Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();

        } catch (Exception e) {

            e.printStackTrace();
            return null;

        } finally {

            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
