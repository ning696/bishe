package cn.zc.common.core.constants;

/**
 * Redis 缓存常量
 * 
 * @author campus-hiring-system
 */
public class CacheConstants {

    /** 登录Token key前缀 */
    public static final String LOGIN_TOKEN_KEY = "logintoken:";

    /** Token过期时间（单位：分钟） */
    public static final long EXP = 720;

    /** Token刷新时间阈值（单位：分钟），当剩余时间小于此值时刷新Token */
    public static final long REFRESH_TIME = 3;

    /** 手机验证码key前缀 */
    public static final String PHONE_CODE_KEY = "p:c:";

    /** 验证码时间key前缀 */
    public static final String CODE_TIME_KEY = "c:t:";

    /** 用户详情信息key前缀 */
    public static final String USER_DETAIL = "u:d:";

    /** 用户缓存过期时间（单位：分钟） */
    public static final long USER_EXP = 10;

    /** 用户上传次数key */
    public static final String USER_UPLOAD_TIMES_KEY = "u:u:t";

    /** 职位列表key */
    public static final String JOB_LIST = "j:l";

    /** 职位详情key前缀 */
    public static final String JOB_DETAIL = "j:d:";

    /** 简历列表key前缀 */
    public static final String RESUME_LIST = "r:l:";

    /** 面试列表key前缀 */
    public static final String INTERVIEW_LIST = "i:l:";

    /** 默认起始位置 */
    public static final long DEFAULT_START = 0;

    /** 默认结束位置（-1表示到末尾） */
    public static final long DEFAULT_END = -1;
}

















