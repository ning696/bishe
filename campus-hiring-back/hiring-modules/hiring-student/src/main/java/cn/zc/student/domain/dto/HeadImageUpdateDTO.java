package cn.zc.student.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 头像更新DTO
 * 
 * @author campus-hiring-system
 */
@Data
public class HeadImageUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String headImage;
}

