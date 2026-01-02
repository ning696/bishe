package cn.zc.enterprise.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 职位校园关联VO
 * 
 * @author campus-hiring-system
 */
@Data
public class CampusJobVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long campusId;
    private String campusName;
    private Long jobId;
    private String jobName;
}

