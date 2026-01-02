package cn.zc.student.domain.dto;

import cn.zc.common.core.domain.PageQueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 投诉查询DTO
 *
 * @author campus-hiring
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ComplaintQueryDTO extends PageQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 处理状态：0-待处理，1-处理中，2-已处理，3-已关闭
     */
    private Integer handleStatus;
}

