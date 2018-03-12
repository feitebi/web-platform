package com.bdh.db.mob;

public class StaticConstants {

    // 排序定义
    // 综合
    public static final String ORDER_BY_SYSTEM = "1";
    // 点评数量
    public static final String ORDER_BY_REVIEW_COUNT = "2";
    // 发布时间
    public static final String ORDER_BY_CREATE_TIME = "3";

    public static final String TALK_TYPE_PHONE_CALL = "电话";
    public static final String TALK_TYPE_FACE_2_FACE = "面谈";

    // 0:deleted 1:created 2:paid 3:accept 4:cancelled 5:refunded 6:finished 7:settled

    // 订单状�??
    public static final String ORDER_DELETED = "0";
    public static final String ORDER_CREATED = "1";
    public static final String ORDER_PAID = "2";
    public static final String ORDER_ACCEPT = "3";
    public static final String ORDER_CANCELLED = "4";
    public static final String ORDER_REFUNDED = "5";
    public static final String ORDER_FINISHED = "6";
    public static final String ORDER_SETTLED = "7";

    public static final String ICON_PATH = "/opt/cw8_wx/web/icons/";

    public static final String SMS_ACCOUNT = "jksc130";
    public static final String SMS_PWD = "123123";
    public static final String SMS_SIGN = "【创问咨询平台�??";
    public static final String SMS_NAME= "【币大师�?";
    public static final String SMS_MSG = "您的验证码是CODE，请尽快输入 ^_^�? ";
    
    public static final String MESSAGE_SENDER_TYPE_USER = "1";
    
    public static final String MESSAGE_SENDER_TYPE_MENTOR = "2";
    
}
