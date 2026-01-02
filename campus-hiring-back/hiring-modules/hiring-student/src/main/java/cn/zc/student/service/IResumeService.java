package cn.zc.student.service;

import cn.zc.student.domain.dto.ResumeAddDTO;
import cn.zc.student.domain.dto.ResumeDeliveryDTO;
import cn.zc.student.domain.dto.ResumeUpdateDTO;
import cn.zc.student.domain.vo.ResumeDetailVO;
import cn.zc.student.domain.vo.ResumeListVO;
import cn.zc.common.core.domain.R;

import java.util.List;

/**
 * 简历服务接口
 * 
 * @author campus-hiring-system
 */
public interface IResumeService {

    /**
     * 简历列表查询
     */
    R<List<ResumeListVO>> list(Long studentId);

    /**
     * 简历详情查询
     */
    R<ResumeDetailVO> getDetail(Long resumeId, Long studentId);

    /**
     * 创建简历
     */
    R<Void> add(ResumeAddDTO dto, Long studentId);

    /**
     * 更新简历
     */
    R<Void> update(ResumeUpdateDTO dto, Long studentId);

    /**
     * 删除简历
     */
    R<Void> delete(Long resumeId, Long studentId);

    /**
     * 投递简历
     */
    R<Void> delivery(ResumeDeliveryDTO dto, Long studentId);

    /**
     * 简历详情查询（不验证学生身份，供服务间调用）
     */
    R<ResumeDetailVO> getDetailWithoutAuth(Long resumeId);
}

