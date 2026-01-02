package cn.zc.student.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 学生个人中心-职位申请 列表项
 */
@Getter
@Setter
public class ApplicationItemVO {

	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	@JsonSerialize(using = ToStringSerializer.class)
	private Long jobId;

	private String jobName;

	@JsonSerialize(using = ToStringSerializer.class)
	private Long enterpriseId;

	private String enterpriseName;

	private Integer applicationStatus;

	private String applicationStatusName;

	private LocalDateTime applicationTime;
}


