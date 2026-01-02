package cn.zc.enterprise.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 人才推荐VO
 * 
 * @author campus-hiring-system
 */
@Data
public class TalentRecommendVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String nickName;
    private String realName;
    private String headImage;
    private Long campusId;
    private String campusName;
    private String major;
    private String education;
    private String grade;
    private String skills;
    private Integer experience;
    private BigDecimal expectedSalary;
    private String expectedLocation;
    private Integer matchScore;
    private String matchReason;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

