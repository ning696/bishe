package cn.zc.student.service.impl;

import cn.zc.student.domain.dto.*;
import cn.zc.student.domain.entity.Student;
import cn.zc.student.domain.vo.StudentDetailVO;
import cn.zc.student.domain.vo.StudentInfoVO;
import cn.zc.student.mapper.StudentMapper;
import cn.zc.student.service.IStudentService;
import cn.zc.minio.domain.OSSResult;
import cn.zc.minio.service.MinioService;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.enums.ResultCode;
import cn.zc.common.core.enums.UserIdentity;
import cn.zc.common.core.util.PasswordUtils;
import cn.zc.security.service.TokenService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 学生服务实现类
 * 
 * @author campus-hiring-system
 */
@Service
public class StudentServiceImpl implements IStudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private MinioService minioService;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Override
    public R<Void> register(StudentRegisterDTO dto) {
        // 检查用户名是否已存在
        LambdaQueryWrapper<Student> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Student::getUsername, dto.getUsername());
        Student existStudent = studentMapper.selectOne(queryWrapper);
        if (existStudent != null) {
            return R.fail(ResultCode.FAILED_USER_EXISTS);
        }

        // 创建学生
        Student student = new Student();
        student.setUsername(dto.getUsername());
        student.setPassword(PasswordUtils.encode(dto.getPassword()));
        student.setStatus(1); // 正常状态

        int rows = studentMapper.insert(student);
        return rows > 0 ? R.ok() : R.fail();
    }

    @Override
    public R<Void> sendCode(SendCodeDTO dto) {
        // TODO: 发送手机验证码（这里简化处理，实际应该调用短信服务）
        // 验证码应该存储到Redis中，设置过期时间
        return R.ok();
    }

    @Override
    public R<String> login(StudentLoginDTO dto) {
        // 参数校验
        if (dto.getUsername() == null || dto.getUsername().trim().isEmpty()) {
            return R.fail(ResultCode.FAILED_PARAMS_VALIDATE);
        }
        if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
            return R.fail(ResultCode.FAILED_PARAMS_VALIDATE);
        }

        // 根据用户名查询学生
        LambdaQueryWrapper<Student> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Student::getUsername, dto.getUsername());
        Student student = studentMapper.selectOne(queryWrapper);

        if (student == null) {
            return R.fail(ResultCode.FAILED_USER_NOT_EXISTS);
        }

        // 验证密码
        if (!PasswordUtils.matches(dto.getPassword(), student.getPassword())) {
            return R.fail(ResultCode.FAILED_LOGIN);
        }

        // 检查状态
        if (student.getStatus() == 0) {
            return R.fail(ResultCode.FAILED_USER_BANNED);
        }

        // 生成Token
        String token = tokenService.createToken(
                student.getId(),
                secret,
                UserIdentity.STUDENT.getValue(),
                student.getNickName(),
                student.getHeadImage()
        );

        return R.ok(token);
    }

    @Override
    public R<StudentInfoVO> getInfo(Long studentId) {
        Student student = studentMapper.selectById(studentId);
        if (student == null) {
            return R.fail(ResultCode.FAILED_USER_NOT_EXISTS);
        }

        StudentInfoVO vo = new StudentInfoVO();
        vo.setNickName(student.getNickName());
        vo.setHeadImage(student.getHeadImage());
        return R.ok(vo);
    }

    @Override
    public R<StudentDetailVO> getDetail(Long studentId) {
        StudentDetailVO detail = studentMapper.selectStudentDetail(studentId);
        if (detail == null) {
            return R.fail(ResultCode.FAILED_USER_NOT_EXISTS);
        }
        return R.ok(detail);
    }

    @Override
    public R<Void> updateInfo(StudentUpdateDTO dto, Long studentId) {
        LambdaUpdateWrapper<Student> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Student::getId, studentId);

        if (dto.getNickName() != null) {
            updateWrapper.set(Student::getNickName, dto.getNickName());
        }
        if (dto.getRealName() != null) {
            updateWrapper.set(Student::getRealName, dto.getRealName());
        }
        if (dto.getPhone() != null) {
            updateWrapper.set(Student::getPhone, dto.getPhone());
        }
        if (dto.getEmail() != null) {
            updateWrapper.set(Student::getEmail, dto.getEmail());
        }
        if (dto.getGender() != null) {
            updateWrapper.set(Student::getGender, dto.getGender());
        }
        if (dto.getBirthday() != null) {
            updateWrapper.set(Student::getBirthday, dto.getBirthday());
        }
        if (dto.getCampusId() != null) {
            updateWrapper.set(Student::getCampusId, dto.getCampusId());
        }
        if (dto.getMajor() != null) {
            updateWrapper.set(Student::getMajor, dto.getMajor());
        }
        if (dto.getEducation() != null) {
            updateWrapper.set(Student::getEducation, dto.getEducation());
        }
        if (dto.getGrade() != null) {
            updateWrapper.set(Student::getGrade, dto.getGrade());
        }
        if (dto.getSkills() != null) {
            updateWrapper.set(Student::getSkills, dto.getSkills());
        }
        if (dto.getExperience() != null) {
            updateWrapper.set(Student::getExperience, dto.getExperience());
        }
        if (dto.getExpectedSalary() != null) {
            updateWrapper.set(Student::getExpectedSalary, dto.getExpectedSalary());
        }
        if (dto.getExpectedLocation() != null) {
            updateWrapper.set(Student::getExpectedLocation, dto.getExpectedLocation());
        }

        int rows = studentMapper.update(null, updateWrapper);
        return rows > 0 ? R.ok() : R.fail();
    }

    @Override
    public R<Void> changePassword(PasswordChangeDTO dto, Long studentId) {
        Student student = studentMapper.selectById(studentId);
        if (student == null) {
            return R.fail(ResultCode.FAILED_USER_NOT_EXISTS);
        }

        if (!PasswordUtils.matches(dto.getOldPassword(), student.getPassword())) {
            return R.fail(ResultCode.FAILED_ERROR_PASSWORD);
        }

        LambdaUpdateWrapper<Student> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Student::getId, studentId)
                .set(Student::getPassword, PasswordUtils.encode(dto.getNewPassword()));

        int rows = studentMapper.update(null, updateWrapper);
        return rows > 0 ? R.ok() : R.fail();
    }

    @Override
    public R<Void> updateHeadImage(HeadImageUpdateDTO dto, Long studentId) {
        LambdaUpdateWrapper<Student> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Student::getId, studentId)
                .set(Student::getHeadImage, dto.getHeadImage());

        int rows = studentMapper.update(null, updateWrapper);
        return rows > 0 ? R.ok() : R.fail();
    }

    @Override
    public R<OSSResult> uploadFile(MultipartFile file, Long studentId) {
        if (file == null || file.isEmpty()) {
            return R.fail(ResultCode.FAILED_PARAMS_VALIDATE);
        }

        String originalFilename = file.getOriginalFilename();
        String objectName = "student/" + studentId + "/" + System.currentTimeMillis();
        if (StringUtils.hasText(originalFilename)) {
            objectName = objectName + "_" + originalFilename;
        }

        OSSResult ossResult = minioService.uploadFile(file, bucketName, objectName);
        return R.ok(ossResult);
    }

    @Override
    public R<Void> logout(String token) {
        boolean result = tokenService.deleteLoginUser(token, secret);
        return result ? R.ok() : R.fail();
    }

    @Override
    public R<cn.zc.api.domain.vo.StudentInfoVO> getInfoForService(Long studentId) {
        StudentDetailVO detail = studentMapper.selectStudentDetail(studentId);
        if (detail == null) {
            return R.fail(ResultCode.FAILED_USER_NOT_EXISTS);
        }

        // 转换为服务间传输的 StudentInfoVO
        cn.zc.api.domain.vo.StudentInfoVO vo = new cn.zc.api.domain.vo.StudentInfoVO();
        vo.setId(detail.getId());
        vo.setUsername(detail.getUsername());
        vo.setNickName(detail.getNickName());
        vo.setRealName(detail.getRealName());
        vo.setPhone(detail.getPhone());
        vo.setEmail(detail.getEmail());
        vo.setHeadImage(detail.getHeadImage());
        vo.setGender(detail.getGender());
        vo.setBirthday(detail.getBirthday());
        vo.setCampusId(detail.getCampusId());
        vo.setCampusName(detail.getCampusName());
        vo.setMajor(detail.getMajor());
        vo.setEducation(detail.getEducation());
        vo.setGrade(detail.getGrade());

        return R.ok(vo);
    }
}

