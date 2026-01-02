package cn.zc.student.service;

import cn.zc.student.domain.dto.*;
import cn.zc.student.domain.vo.StudentDetailVO;
import cn.zc.student.domain.vo.StudentInfoVO;
import cn.zc.minio.domain.OSSResult;
import cn.zc.common.core.domain.R;
import org.springframework.web.multipart.MultipartFile;

/**
 * 学生服务接口
 * 
 * @author campus-hiring-system
 */
public interface IStudentService {

    /**
     * 学生注册
     */
    R<Void> register(StudentRegisterDTO dto);

    /**
     * 发送验证码
     */
    R<Void> sendCode(SendCodeDTO dto);

    /**
     * 学生登录
     */
    R<String> login(StudentLoginDTO dto);

    /**
     * 获取学生信息
     */
    R<StudentInfoVO> getInfo(Long studentId);

    /**
     * 学生详情查询
     */
    R<StudentDetailVO> getDetail(Long studentId);

    /**
     * 更新学生信息
     */
    R<Void> updateInfo(StudentUpdateDTO dto, Long studentId);

    /**
     * 修改密码
     */
    R<Void> changePassword(PasswordChangeDTO dto, Long studentId);

    /**
     * 更新头像
     */
    R<Void> updateHeadImage(HeadImageUpdateDTO dto, Long studentId);

    /**
     * 学生文件上传
     */
    R<OSSResult> uploadFile(MultipartFile file, Long studentId);

    /**
     * 退出登录
     */
    R<Void> logout(String token);

    /**
     * 获取学生信息（服务间调用）
     */
    R<cn.zc.api.domain.vo.StudentInfoVO> getInfoForService(Long studentId);
}

