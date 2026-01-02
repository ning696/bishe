package cn.zc.admin.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 学生列表 VO
 *
 * @author
 */
@Data
public class StudentListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String nickName;

    private String phone;

    private String email;

    private Integer status;

    private String statusName;

    private Date createTime;
}

