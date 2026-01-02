package cn.zc.admin.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 学生状态更新 DTO
 *
 * @author
 */
@Data
public class StudentStatusUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 学生 ID
     */
    private Long studentId;

    /**
     * 状态：0-已拉黑，1-正常，2-已禁用，3-待审核
     */
    private Integer status;
}

