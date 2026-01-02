package cn.zc.student.service;

import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;
import cn.zc.student.domain.dto.*;
import cn.zc.student.domain.vo.*;

import java.util.List;

/**
 * 聊天服务接口
 * 
 * @author campus-hiring-system
 */
public interface IChatService {

    /**
     * 创建/获取会话
     */
    R<ChatSessionDetailVO> createOrGetSession(ChatSessionCreateDTO dto, Long studentId);

    /**
     * 会话列表查询
     */
    R<TableDataInfo> getSessionList(Long studentId, Integer pageNum, Integer pageSize, String keyword);

    /**
     * 会话详情查询
     */
    R<ChatSessionDetailVO> getSessionDetail(Long sessionId, Long studentId);

    /**
     * 发送消息
     */
    R<ChatMessageSendVO> sendMessage(ChatMessageSendDTO dto, Long studentId);

    /**
     * 消息列表查询
     */
    R<TableDataInfo> getMessageList(Long sessionId, Long studentId, Integer pageNum, Integer pageSize);

    /**
     * 标记已读
     */
    R<Void> markRead(ChatMessageMarkReadDTO dto, Long studentId);

    /**
     * 标记会话所有消息为已读（路径参数方式）
     */
    R<Void> markReadBySessionId(Long sessionId, Long studentId);

    /**
     * 发送简历
     */
    R<ChatMessageSendVO> sendResume(ChatResumeSendDTO dto, Long studentId);

    /**
     * 查询未读消息总数
     */
    R<ChatUnreadCountVO> getUnreadCount(Long studentId);
}

