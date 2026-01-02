package cn.zc.enterprise.mapper;

import cn.zc.enterprise.domain.entity.ChatSession;
import cn.zc.enterprise.domain.vo.ChatSessionDetailVO;
import cn.zc.enterprise.domain.vo.ChatSessionListVO;
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
     * 查询会话列表（企业端）
     */
    List<ChatSessionListVO> selectSessionList(@Param("enterpriseId") Long enterpriseId,
                                               @Param("keyword") String keyword);

    /**
     * 查询会话详情
     */
    ChatSessionDetailVO selectSessionDetail(@Param("sessionId") Long sessionId,
                                             @Param("enterpriseId") Long enterpriseId);
}

