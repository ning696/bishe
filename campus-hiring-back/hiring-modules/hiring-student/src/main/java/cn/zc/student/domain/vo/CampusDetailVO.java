package cn.zc.student.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 校园详情VO（学生端）
 *
 * @author
 */
@Data
public class CampusDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String campusName;
    private String campusCode;
    private String address;
    private String contactPerson;
    private String contactPhone;
    private Integer status;
    private String statusName;
    private String remark;
    private String createBy;
    private LocalDateTime createTime;
    private String updateBy;
    private LocalDateTime updateTime;
}

