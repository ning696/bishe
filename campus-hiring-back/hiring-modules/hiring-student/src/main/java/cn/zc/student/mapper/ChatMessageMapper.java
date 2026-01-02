package cn.zc.student.mapper;

import cn.zc.student.domain.entity.ChatMessage;
import cn.zc.student.domain.vo.ChatMessageListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 聊天消息Mapper接口
 * 
 * @author campus-hiring-system
 */
@Mapper
public interface ChatMessageMapper extends BaseMapper<ChatMessage> {

    /**
     * 查询消息列表（分页）
     */
    List<ChatMessageListVO> selectMessageList(@Param("sessionId") Long sessionId);

    /**
     * 更新消息已读状态
     */
    int updateMessageRead(@Param("messageIds") List<Long> messageIds,
                          @Param("receiverId") Long receiverId);

    /**
     * 标记会话所有未读消息为已读
     */
    int updateSessionMessageRead(@Param("sessionId") Long sessionId,
                                 @Param("receiverId") Long receiverId);

    /**
     * 查询未读消息总数（学生端）
     */
    Integer selectUnreadCount(@Param("studentId") Long studentId);
}

