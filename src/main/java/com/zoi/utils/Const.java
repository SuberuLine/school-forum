package com.zoi.utils;

public class Const {
    // Redis中存入无效JWT Token的键名
    public static final String JWT_BLACK_LIST = "jwt:blacklist:";

    // Redis中存入邮箱验证码限制时长
    public static final String VERIFY_EMAIL_LIMIT = "verify:email:limit";

    // Redis中存入发送右键数据
    public static final String VERIFY_EMAIL_DATA = "verify:email:data";

    // 跨域等级，需小于-100（Security默认跨域等级为-100）
    public static final int ORDER_CORS = -103;

    // 限流等级
    public static final int ORDER_LIMIT = -101;
    // Redis中记录ip访问次数
    public static final String FLOW_LIMIT_COUNTER = "flow:counter:";
    // Redis中记录封禁ip
    public static final String FLOW_LIMIT_BLOCK = "flow:block:";

    //请求自定义属性
    public final static String ATTR_USER_ID = "userId";
    //消息队列
    public final static String MQ_MAIL = "mail";
    //用户角色
    public final static String ROLE_DEFAULT = "user";
    // 论坛相关
    public final static String FORUM_WEATHER_CACHE = "weather:cache:";
    public final static String FORUM_IMAGE_COUNTER = "forum:image:";
    public final static String FORUM_TOPIC_CREATE_COUNTER = "forum:topic:create";
}
