package cn.zc.enterprise.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Logo更新DTO
 * 
 * @author campus-hiring-system
 */
@Data
public class LogoUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String logo;
}

