package cn.zc.api.feign;

import cn.zc.api.domain.dto.ConsultationDTO;
import cn.zc.api.domain.dto.InterviewEvaluationDTO;
import cn.zc.api.domain.vo.ResumeDetailVO;
import cn.zc.api.domain.vo.StudentInfoVO;
import cn.zc.common.core.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 学生服务调用接口
 * 
 * @author campus-hiring-system
 */
@FeignClient(contextId = "RemoteStudentService", value = "hiring-student", url = "http://localhost:8082")
public interface RemoteStudentService {

    /**
     * 获取学生信息
     * 
     * @param studentId 学生ID
     * @return 学生信息
     */
    @GetMapping("/student/student/info/{studentId}")
    R<StudentInfoVO> getStudentInfo(@PathVariable("studentId") Long studentId);

    /**
     * 获取简历详情
     * 
     * @param resumeId 简历ID
     * @return 简历详情
     */
    @GetMapping("/student/resume/detail/{resumeId}")
    R<ResumeDetailVO> getResumeDetail(@PathVariable("resumeId") Long resumeId);

    /**
     * 提交面试评价
     * 
     * @param dto 面试评价DTO
     * @return 操作结果
     */
    @PostMapping("/student/interview/evaluation")
    R<Void> submitInterviewEvaluation(@RequestBody InterviewEvaluationDTO dto);

    /**
     * 更新咨询记录
     * 
     * @param dto 咨询DTO
     * @return 操作结果
     */
    @PostMapping("/student/consultation/update")
    R<Void> updateConsultation(@RequestBody ConsultationDTO dto);

    /**
     * 获取学生信息（用于聊天）
     * 
     * @param studentId 学生ID
     * @return 学生信息
     */
    @GetMapping("/student/student/info/{studentId}")
    R<StudentInfoVO> getStudentInfoForChat(@PathVariable("studentId") Long studentId);
}

