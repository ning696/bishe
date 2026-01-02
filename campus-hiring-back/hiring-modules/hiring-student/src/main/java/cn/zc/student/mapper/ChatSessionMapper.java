package cn.zc.student.mapper;

import cn.zc.student.domain.entity.ChatSession;
import cn.zc.student.domain.vo.ChatSessionDetailVO;
import cn.zc.student.domain.vo.ChatSessionListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 聊天会话Mapper接口
 * 
 * @author campus-hiring-system
 */
@Mapper
public interface ChatSessionMapper extends BaseMapper<ChatSession> {

    /**
     * 查询会话（根据学生ID、企业ID、职位ID）
     */
    ChatSession selectSessionByStudentAndEnterpriseAndJob(@Param("studentId") Long studentId,
                                                          @Param("enterpriseId") Long enterpriseId,
                                                          @Param("jobId") Long jobId);

    /**
     * 查询会话列表（学生端）
     */
    List<ChatSessionListVO> selectSessionList(@Param("studentId") Long studentId,
                                               @Param("keyword") String keyword);

    /**
     * 查询会话详情
     */
    ChatSessionDetailVO selectSessionDetail(@Param("sessionId") Long sessionId,
                                             @Param("studentId") Long studentId);
}

