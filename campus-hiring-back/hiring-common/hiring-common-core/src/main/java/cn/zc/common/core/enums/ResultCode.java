package cn.zc.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应结果码枚举
 * 定义统一的响应码和消息
 * 
 * @author campus-hiring-system
 */
@AllArgsConstructor
@Getter
public enum ResultCode {

    /** 操作成功 */
    SUCCESS(1000, "操作成功"),

    /** 服务器内部错误，友好提示 */
    ERROR(2000, "服务繁忙请稍后重试"),

    /** 操作失败，但是服务器不存在异常 */
    FAILED(3000, "操作失败"),
    FAILED_UNAUTHORIZED(3001, "未授权"),
    FAILED_PARAMS_VALIDATE(3002, "参数校验失败"),
    FAILED_NOT_EXISTS(3003, "资源不存在"),
    FAILED_ALREADY_EXISTS(3004, "资源已存在"),

    /** 用户相关错误 */
    FAILED_USER_EXISTS(3101, "用户已存在"),
    FAILED_USER_NOT_EXISTS(3102, "用户不存在"),
    FAILED_LOGIN(3103, "账号或密码错误"),
    FAILED_USER_BANNED(3104, "您已被列入黑名单, 请联系管理员"),
    FAILED_USER_PHONE(3105, "你输入的手机号有误"),
    FAILED_FREQUENT(3106, "操作频繁，请稍后重试"),
    FAILED_TIME_LIMIT(3107, "当天请求次数已达到上限"),
    FAILED_SEND_CODE(3108, "验证码发送错误"),
    FAILED_INVALID_CODE(3109, "验证码无效"),
    FAILED_ERROR_CODE(3110, "账号或密码错误"),
    FAILED_ERROR_PASSWORD(3111, "原密码错误"),

    /** 职位相关错误 */
    FAILED_JOB_NOT_EXISTS(3201, "职位不存在"),
    FAILED_JOB_ALREADY_EXISTS(3202, "职位已存在"),
    FAILED_JOB_EXPIRED(3203, "职位已过期"),
    FAILED_JOB_NOT_PUBLISHED(3204, "职位未发布"),

    /** 简历相关错误 */
    FAILED_RESUME_NOT_EXISTS(3301, "简历不存在"),
    FAILED_RESUME_ALREADY_EXISTS(3302, "简历已存在"),
    FAILED_RESUME_ALREADY_APPLIED(3303, "已投递过该职位"),

    /** 文件上传相关错误 */
    FAILED_FILE_UPLOAD(3401, "文件上传失败"),
    FAILED_FILE_UPLOAD_TIME_LIMIT(3402, "当天上传文件数量超过上限"),
    FAILED_FILE_TYPE_NOT_SUPPORT(3403, "不支持的文件类型"),
    FAILED_FILE_SIZE_EXCEED(3404, "文件大小超过限制"),

    /** 面试相关错误 */
    FAILED_INTERVIEW_NOT_EXISTS(3501, "面试不存在"),
    FAILED_INTERVIEW_ALREADY_EXISTS(3502, "面试已存在"),
    FAILED_INTERVIEW_TIME_CONFLICT(3503, "面试时间冲突"),

    /** 消息队列相关错误 */
    FAILED_RABBIT_PRODUCE(3701, "mq生产消息异常");

    private final int code;
    private final String msg;
}
















































































