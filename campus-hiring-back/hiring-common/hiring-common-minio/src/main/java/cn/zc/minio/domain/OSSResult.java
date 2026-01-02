package cn.zc.minio.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 文件上传结果封装类
 * 
 * @author campus-hiring-system
 */
@Getter
@Setter
public class OSSResult {

    /** 文件URL或名称 */
    private String name;

    /** 对象状态：true成功，false失败 */
    private boolean success;
}

















