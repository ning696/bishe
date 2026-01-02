package cn.zc.student.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 职位收藏DTO
 * 
 * @author campus-hiring-system
 */
@Data
public class JobFavoriteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long jobId;
}

