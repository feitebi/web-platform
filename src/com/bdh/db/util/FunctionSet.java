/**
 * 
 */
package com.bdh.db.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.ocpsoft.pretty.time.PrettyTime;

/**
 * @author
 * 
 */
public class FunctionSet {

    protected static FunctionSet instance;

    public static PrettyTime prettyTime = new PrettyTime(Locale.CHINESE);

    public static String formatTimePretty(Long time) {
        if (time != null) {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(time);
            return prettyTime.format(c.getTime()).replaceAll(" ", "");
        } else {
            return "";
        }
    }

    public static boolean isCarNo(String carNo) {
        if (carNo == null) {
            return false;
        }
        if (carNo.length()>=7) {
            return true;
        }
        return false;
    }

    /**
     * getInstance()
     */
    public static FunctionSet getInstance() {
        if (instance == null) {
            instance = new FunctionSet();
        }
        return instance;
    }

    public static int getDaysAfter(String date) {
        long t = 0;
        try {
            t = new SimpleDateFormat("yyyy-MM-dd").parse(date).getTime();
        } catch (ParseException e) {
        }
        Long ct = System.currentTimeMillis();

        long diff = ct - t;

        float dayCount = (float) diff / (24 * 60 * 60 * 1000);

        return (int) dayCount;
    }

    public static int getDayOfWeek() {
        return GregorianCalendar.getInstance().get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 取今天的日期
     * 
     * @return
     */
    public static String getToday() {
        return FunctionSet.formatDate(System.currentTimeMillis());
    }

    /**
     * 取今天以前的某天的日期�??
     * 
     * @param days 几天�?
     * @return
     */
    public static String getDayBefore(int days) {
        Long ct = System.currentTimeMillis();
        Calendar c = GregorianCalendar.getInstance();
        c.setTimeInMillis(ct);
        c.add(Calendar.DAY_OF_YEAR, -days);
        return FunctionSet.formatDate(c.getTimeInMillis());
    }

    public static void main(String[] args) {
//        System.out.println(getDayBefore(7, "2013-01-01"));
//        
//        String today = FunctionSet.getToday();
//
//        String tomorrow = FunctionSet.getDayAfter(1, today);
//
//        Long t = FunctionSet.parseDateTime(today + " 00:00:01");
//
//        Long tr = FunctionSet.parseDateTime(tomorrow + " 00:00:01");
//        
//        System.out.println(t+">>>"+tr);
        
        System.out.println((getTodayLeftSeconds()/60/60));
    }

    public static String getDayBefore(int days, String date) {
        long t = 0;
        try {
            t = new SimpleDateFormat("yyyy-MM-dd").parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = GregorianCalendar.getInstance();
        c.setTimeInMillis(t);
        c.add(Calendar.DAY_OF_YEAR, -days);
        return FunctionSet.formatDate(c.getTimeInMillis());
    }

    public static String getDayAfter(int days, String date) {
        long t = 0;
        try {
            t = new SimpleDateFormat("yyyy-MM-dd").parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = GregorianCalendar.getInstance();
        c.setTimeInMillis(t);
        c.add(Calendar.DAY_OF_YEAR, days);
        return FunctionSet.formatDate(c.getTimeInMillis());
    }

    /**
     * formatUsaCurrenty()
     */
    public String formatUsaCurrenty(double amount) {
        return NumberFormat.getCurrencyInstance(Locale.US).format(amount);
    }

    /**
     * formatNumber()
     */
    public String formatNumber(int number) {
        return NumberFormat.getNumberInstance(Locale.US).format(number);
    }

    /**
     * parseDateTime()
     */
    public static long parseDateTime(String datetime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = dateFormat.parse(datetime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date != null ? date.getTime() : 0;
    }

    public int daysBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }

    /**
     * formatDateTime()
     */
    public String formatDateTime(long datetime) {
        if (datetime < 100000000) {
            return "";
        }
        Date date = new Date(datetime);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sDateTime = simpleDateFormat.format(date);
        return sDateTime;
    }

    /**
     * formatDateTime()
     */
    public static String formatDate(long datetime) {
        Date date = new Date(datetime);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String sDateTime = simpleDateFormat.format(date);
        return sDateTime;
    }

    /**
     * getDateTime()
     */
    public String formatDateTime(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sDateTime = simpleDateFormat.format(date);
        return sDateTime;
    }

    /**
     * getCurrentTime().
     */
    public long getCurrentTime() {
        Date date = new Date();
        Random random = new Random();
        return date.getTime() + random.nextInt();
    }

    /**
     * getCurrentTimeMillis().
     */
    public long getCurrentTimeInMillis() {
        Random random = new Random();
        return Calendar.getInstance().getTimeInMillis() + random.nextInt();
    }

    /**
     * Filter()
     */
    public static String filt(String in) {
        return filt(in, true);
    }

    public static String filt(String in, boolean tag) {
        if (in == null) {
            return "";
        }
        try {
            in = in.trim();
            in = in.replaceAll("'", "''");
            in = in.replaceAll("\\\\", "\\\\\\\\");
            if (tag) {
                in = filterTag(in);
            }
            try {
                in = URLDecoder.decode(in, "UTF-8");
            } catch (Exception e) {
            }
            // in = DFA.getInstance().filt(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return in.trim();
    }

    public static String keepHtmlButFilterScript(String in) {
        try {
            return doFilter(filt(in, false));
        } catch (Exception e) {
        }
        return in;
    }

    public static String doFilter(String str) {
        str = Pattern.compile("<script.*?>.*?</script>", Pattern.CASE_INSENSITIVE).matcher(str).replaceAll("****");
        return str;
    }

    /**
     * filterTag()
     */
    public static String filterTag(String in) {
        if (in == null) {
            return "";
        }
        in = in.replaceAll("<", "&lt;");
        in = in.replaceAll(">", "&gt;");
        return in.trim();
    }

    /**
     * filtDoubleQuotation()
     */
    public String filtDoubleQuotation(String sIn) {
        String sOut = null;
        if (sIn != null && !sIn.equals("")) {
            sOut = sIn.trim().replaceAll("\"", "'");
        } else {
            sOut = "";
        }
        return sOut;
    }

    /**
     * filtQuotation()
     */
    public String filtQuotation(String sIn) {
        String sOut = null;
        if (sIn != null && !sIn.equals("")) {
            sOut = sIn.replaceAll("'", "''");
        } else {
            sOut = "";
        }
        return sOut;
    }

    /**
     * round()
     */
    public double round(double dValue, int iScale) {
        BigDecimal bigDecimal = new BigDecimal(dValue);
        return bigDecimal.setScale(iScale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * round()
     */
    public float round(float fValue, int iScale) {
        BigDecimal bigDecimal = new BigDecimal(fValue);
        return bigDecimal.setScale(iScale, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     * format()
     */
    public String format(double value, int scale) {
        StringBuffer buffer = new StringBuffer("#0");
        if (scale > 0)
            buffer.append(".");
        for (int i = 0; i < scale; i++) {
            buffer.append("0");
        }
        DecimalFormat df = new DecimalFormat(buffer.toString());
        return df.format(value);
    }

    /**
     * isValidEmail()
     */
    public boolean isValidEmail(String email) {
        if (email == null || email.equals("")) {
            return false;
        }
        try {
            String str = "^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$";// email
            Pattern pattern = Pattern.compile(str);
            Matcher matcher = pattern.matcher(email);
            if (matcher.matches()) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * isValidUserName()
     */
    public boolean isValidUserName(String name) {
        if (name == null || name.equals("")) {
            return false;
        }
        // String str = "^[a-zA-Z]{1}([a-zA-Z0-9]|[._]){4,19}$";
        String str = "^([a-zA-Z0-9]|[-]){4,20}$";
        Pattern pattern = Pattern.compile(str);
        Matcher matcher = pattern.matcher(name);
        if (!matcher.matches()) {
            return false;
        }
        return true;
    }

    /**
     * substring()
     */
    public String substring(String string, int length) {
        if (length >= string.length()) {
            return string;
        }

        int iIndex = length;
        while (iIndex < string.length()) {
            if (string.charAt(length - 1) == ' ') {
                break;
            }
            iIndex++;
            length++;
        }

        return string.substring(0, length);
    }

    /**
     * replaceAll().
     */
    public StringBuffer replaceAll(StringBuffer buffer, String oldString, String newString) {
        int start = buffer.indexOf(oldString);
        int end = start + oldString.length();
        while (start != -1) {
            buffer.replace(start, end, newString);
            start = buffer.indexOf(oldString);
            end = start + oldString.length();
        }
        return buffer;
    }

    /**
     * getGUID().
     */
    public synchronized long getGUID() {
        long guid = System.currentTimeMillis();
        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
        }
        return guid;
    }

    /**
     * getVerifyCode().
     * @param j 
     */
    public static String getVerifyCode(int j) {
        String[] alphabets = { "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h", "i",
                "j", "k", "m", "n", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z" };
        Random random = new Random();
        StringBuffer buffer = new StringBuffer();
        int index = 0;
        for (int i = 0; i < 4; i++) {
            index = random.nextInt(alphabets.length);
            if (index < 0)
                index = 0;
            if (index >= alphabets.length)
                index = alphabets.length - 1;
            buffer.append(alphabets[index]);
        }

        return buffer.toString();
    }

   public static int getTodayLeftSeconds () {
       
       Calendar cal = Calendar.getInstance();
       cal.setTimeInMillis(System.currentTimeMillis());
     
       
       Calendar cal2 = Calendar.getInstance();
       cal2.setTimeInMillis(System.currentTimeMillis());
       cal2.set(Calendar.HOUR_OF_DAY, 23);
       cal2.set(Calendar.MINUTE, 59);
       cal2.set(Calendar.SECOND, 59);
       
       return  (int)(cal2.getTimeInMillis()-cal.getTimeInMillis())/1000;
   }
    
    /**
     * workTimeInMillis().
     */
    public long workTimeInMillis(long startTime, long endTime) {
        long dayMillisTime = 86400000L;
        long factStartDayTime = 0L;
        long factStartLeavingTime = 0L;
        long factEndDayTime = 0L;
        long factEndLeavingTime = 0L;
        long factWorkTime = 0L;

        // System.currentTimeMillis() method return milliseconds since
        // 1970-01-01 08:00:00
        // 8 hours equal to 28800000 millisecond
        startTime = startTime + 28800000;
        endTime = endTime + 28800000;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startTime);

        if (calendar.get(Calendar.DAY_OF_WEEK) == 1) {
            factStartDayTime = (startTime / dayMillisTime) * dayMillisTime + dayMillisTime;
            factStartLeavingTime = 0L;
        } else if (calendar.get(Calendar.DAY_OF_WEEK) == 7) {
            factStartDayTime = (startTime / dayMillisTime) * dayMillisTime + dayMillisTime * 2;
            factStartLeavingTime = 0L;
        } else {
            factStartDayTime = (startTime / dayMillisTime) * dayMillisTime;
            factStartLeavingTime = startTime % dayMillisTime;
        }

        calendar.setTimeInMillis(endTime);
        if (calendar.get(Calendar.DAY_OF_WEEK) == 1) {
            factEndDayTime = (endTime / dayMillisTime) * dayMillisTime + dayMillisTime;
            factEndLeavingTime = 0L;
        } else if (calendar.get(Calendar.DAY_OF_WEEK) == 7) {
            factEndDayTime = (endTime / dayMillisTime) * dayMillisTime + dayMillisTime * 2;
            factEndLeavingTime = 0L;
        } else {
            factEndDayTime = (endTime / dayMillisTime) * dayMillisTime;
            factEndLeavingTime = endTime % dayMillisTime;
        }

        if ((factStartDayTime + factStartLeavingTime) >= factEndDayTime + factEndLeavingTime) {
            factWorkTime = 0L;
        } else {
            int days = (int) ((factEndDayTime - factStartDayTime) / dayMillisTime);
            if (days == 0) {
                factWorkTime = factEndLeavingTime - factStartLeavingTime;
            } else {
                for (long i = 1; i <= days; i++) {
                    calendar.setTimeInMillis(factStartDayTime + i * dayMillisTime);
                    if (calendar.get(Calendar.DAY_OF_WEEK) != 1 && calendar.get(Calendar.DAY_OF_WEEK) != 7) {
                        factWorkTime = factWorkTime + dayMillisTime;
                    }
                }
                factWorkTime = factWorkTime - factStartLeavingTime + factEndLeavingTime;
            }
        }
        return factWorkTime;
    }

    /**
     * formatDayEn()
     */
    public String formatDayEn(long millis) {
        String day = "";
        if (millis == 0) {
            return day;
        }
        day = "" + millis / (24 * 3600000) + " Days " + (millis % (24 * 3600000)) / 3600000 + " Hours "
                + ((millis % (24 * 3600000)) % 3600000) / 60000 + " Minutes";
        return day;
    }

    /**
     * formatDayCn()
     */
    public String formatDayCn(long millis) {
        String day = "";
        if (millis == 0) {
            return day;
        }
        day = "" + millis / (24 * 3600000) + "�?" + (millis % (24 * 3600000)) / 3600000 + "�?"
                + ((millis % (24 * 3600000)) % 3600000) / 60000 + "�?";
        return day;
    }

    /**
     * isWindows()
     */
    public boolean isWindows() {
        String osName = System.getProperty("os.name");
        System.out.println("OS Name: " + osName);
        return osName != null && osName.startsWith("Windows");
    }

    /**
     * getRandString()
     */
    public static String getRandString(int length) {
        byte[] charList = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
                'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b',
                'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
                'w', 'x', 'y', 'z' };
        byte[] rev = new byte[length];
        Random f = new Random();
        for (int i = 0; i < length; i++) {
            rev[i] = charList[Math.abs(f.nextInt()) % charList.length];
        }
        return new String(rev);
    }

    /**
     * getHourOfDay()
     */
    public int getHourOfDay() {
        return new GregorianCalendar().get(Calendar.HOUR_OF_DAY);
    }

    /**
     * escape()
     */
    public String escape(String src) {
        int i;
        char j;
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length() * 6);
        for (i = 0; i < src.length(); i++) {
            j = src.charAt(i);
            if (Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j))
                tmp.append(j);
            else if (j < 256) {
                tmp.append("%");
                if (j < 16)
                    tmp.append("0");
                tmp.append(Integer.toString(j, 16));
            } else {
                tmp.append("%u");
                tmp.append(Integer.toString(j, 16));
            }
        }
        return tmp.toString();
    }

    /**
     * unescape()
     */
    public String unescape(String src) {
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length());
        int lastPos = 0, pos = 0;
        char ch;
        while (lastPos < src.length()) {
            pos = src.indexOf("%", lastPos);
            if (pos == lastPos) {
                if (src.charAt(pos + 1) == 'u') {
                    ch = (char) Integer.parseInt(src.substring(pos + 2, pos + 6), 16);
                    tmp.append(ch);
                    lastPos = pos + 6;
                } else {
                    ch = (char) Integer.parseInt(src.substring(pos + 1, pos + 3), 16);
                    tmp.append(ch);
                    lastPos = pos + 3;
                }
            } else {
                if (pos == -1) {
                    tmp.append(src.substring(lastPos));
                    lastPos = src.length();
                } else {
                    tmp.append(src.substring(lastPos, pos));
                    lastPos = pos;
                }
            }
        }
        return tmp.toString();
    }

    /**
     * @param catalog setCatalog
     */
    public String filtMessage(String message) {
        String sResult = message;
        // String s =
        // "(!*|@*|#*|\\$*|%*|\\^*|&*|\\**|\\+*|\\-*|;*|:*|\\\\*|/*|'*|\"*|\\.*|,*|<*|>*|\\(*|\\)*|\\[*|\\]*|\\{*|\\}*|\\?*)";
        // String s =
        // "(!{0,2}|@{0,2}|#{0,2}|\\${0,2}|%{0,2}|\\^{0,2}|&{0,2}|\\*{0,2}|\\+{0,2}|\\-{0,2}|\\.{0,2}|\\,{0,2}|\\?{0,2})";
        String[] regexpArray = { "[D|d][H|h][G|g][A|a][T|t][E|e]", "[T|t][R|r][A|a][D|d][E|e][T|t][A|a][N|n][G|g]",
                "[M|m][Y|y][E|e][G|g][L|l][O|o][B|b][A|a][L|l]", "[Y|y][A|a][H|h][O|o][O|o]", "[M|m][S|s][N|n]",
                "[G|g][M|m][A|a][I|i][L|l]", "[A|a][O|o][L|l]", "[S|s][I|i][N|n][A|a]", "[S|s][O|o][H|h][U|u]",
                "[H|h][O|o][T|t][M|m][A|a][I|i][L|l]", "126.com", "163.com", "263.com" };
        // Replace
        for (int i = 0; i < regexpArray.length; i++) {
            sResult = sResult.replaceAll(regexpArray[i], "");
        }
        return sResult;
    }

    /*
     * ip2Long
     */
    public long ip2Long(String ip) {
        if (ip == null || ip.equals("")) {
            return -1;
        }
        String[] ipArray = ip.split("[.]");
        if (ipArray == null || ipArray.length != 4) {
            return -2;
        }
        long ip0 = Long.parseLong(ipArray[0]) * 256 * 256 * 256;
        long ip1 = Long.parseLong(ipArray[1]) * 256 * 256;
        long ip2 = Long.parseLong(ipArray[2]) * 256;
        long ip3 = Long.parseLong(ipArray[3]);
        long ips = ip0 + ip1 + ip2 + ip3;
        return ips;
    }

    /*
     * long2Ip
     */
    public String long2Ip(long ipLong) {
        long mask[] = { 0x000000FF, 0x0000FF00, 0x00FF0000, 0xFF000000 };
        long num = 0;
        StringBuffer ipInfo = new StringBuffer();
        for (int i = 0; i < 4; i++) {
            num = (ipLong & mask[i]) >> (i * 8);
            if (i > 0)
                ipInfo.insert(0, ".");
            ipInfo.insert(0, Long.toString(num, 10));
        }
        return ipInfo.toString();
    }

    public static String encrypt(String str) {
        byte[] b = str.getBytes();
        try {
            MessageDigest md5 = MessageDigest.getInstance("SHA");
            b = md5.digest(b);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new String(b);
    }
    
    public static String getCookieValue(String name, HttpServletRequest req) {
        Cookie value = null;
        Cookie[] cookies = req.getCookies();
        if (cookies == null) {
            return "";
        }
        for (int i = 0; i < cookies.length; i++) {
            if (name.equals(cookies[i].getName())) {
                value = cookies[i];
                break;
            }
        }

        if (value != null && StringUtils.isNotBlank(value.getValue())) {
            String v = new String(value.getValue().getBytes());
            try {
                v = URLDecoder.decode(v, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return v;
        }
        return "";
    }
    
    public static void deleteCookie(String name, HttpServletResponse rep) {
        if (StringUtils.isNotBlank(name)) {
            try {
                Cookie userCookie = new Cookie(name, "");
                userCookie.setPath("/");
                // userCookie.setDomain(DOMAIN);
                userCookie.setMaxAge(0);
                addCookie(userCookie, rep);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void addCookie(Cookie cookie, HttpServletResponse rep) {
        if (rep != null) {
            rep.addCookie(cookie);
        }
    }
    
    public static void addCookie(String name,String value, HttpServletResponse rep) {
        addCookie(name, value, 5*365*24*60*60, rep);
    }
    
    public static void addCookie(String name,String value,int maxAge, HttpServletResponse rep) {
        if (rep != null) {
            Cookie userCookie = new Cookie(name, value);
            userCookie.setPath("/");
            userCookie.setMaxAge(maxAge);
            rep.addCookie(userCookie);
        }
    }
}
