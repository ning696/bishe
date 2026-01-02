package cn.zc.common.core.domain;

import cn.zc.common.core.enums.ResultCode;
import lombok.Data;

import java.io.Serializable;

/**
 * 后端统一返回结果
 * @param <T>
 */
@Data
public class R<T> implements Serializable {

    private Integer code; //编码：1成功，0和其它数字为失败
    private String msg; //错误信息
    private T data; //数据

    public static <T> R<T> ok() {
        return assembleResult(null, ResultCode.SUCCESS);
    }

    public static <T> R<T> ok(T data) {
        return assembleResult(data, ResultCode.SUCCESS);
    }

    public static <T> R<T> fail() {
        return assembleResult(null, ResultCode.FAILED);
    }

    public static <T> R<T> fail(ResultCode resultCode) {
        return assembleResult(null, resultCode);
    }

    private static <T> R<T> assembleResult(T data, ResultCode resultCode) {
        R<T> r = new R();
        r.msg = resultCode.getMsg();
        r.code = resultCode.getCode();
        r.data = data;
        return r;
    }
    public static <T> R<T> fail(int code, String msg) {
        return assembleResult(code, msg, null);
    }
    public static <T> R<T> fail(String msg, ResultCode resultCode) {
        return assembleResult(null, resultCode.getCode(), msg);
    }
    private static <T> R<T> assembleResult(int code, String msg, T data) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setData(data);
        r.setMsg(msg);
        return r;
    }
    private static <T> R<T> assembleResult(T data, int code, String msg) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setData(data);
        r.setMsg(msg);
        return r;
    }

}
