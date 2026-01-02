package cn.zc.security.exception;

import cn.zc.common.core.enums.ResultCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 自定义业务异常
 * 用于业务逻辑中的异常处理，携带 ResultCode
 * 
 * @author campus-hiring-system
 */
@Getter
@Setter
public class ServiceException extends RuntimeException {
    
    private ResultCode resultCode;

    public ServiceException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.resultCode = resultCode;
    }

    public ServiceException(ResultCode resultCode, String message) {
        super(message);
        this.resultCode = resultCode;
    }

    public ServiceException(ResultCode resultCode, Throwable cause) {
        super(resultCode.getMsg(), cause);
        this.resultCode = resultCode;
    }
}

















