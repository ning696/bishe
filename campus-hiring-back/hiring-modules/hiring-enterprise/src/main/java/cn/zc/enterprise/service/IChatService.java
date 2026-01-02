package cn.zc.enterprise.service;

import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;
import cn.zc.enterprise.domain.dto.*;
import cn.zc.enterprise.domain.vo.*;

/**
 * 聊天服务接口
 * 
 * @author campus-hiring-system
 */
public interface IChatService {

    /**
     * 会话列表查询
     */
    R<TableDataInfo> getSessionList(Long enterpriseId, Integer pageNum, Integer pageSize, String keyword);

    /**
     * 会话详情查询
     */
    R<ChatSessionDetailVO> getSessionDetail(Long sessionId, Long enterpriseId);

    /**
     * 发送消息
     */
    R<ChatMessageSendVO> sendMessage(ChatMessageSendDTO dto, Long enterpriseId);

    /**
     * 消息列表查询
     */
    R<TableDataInfo> getMessageList(Long sessionId, Long enterpriseId, Integer pageNum, Integer pageSize);

    /**
     * 标记已读
     */
    R<Void> markRead(ChatMessageMarkReadDTO dto, Long enterpriseId);

    /**
     * 标记会话所有消息为已读（路径参数方式）
     */
    R<Void> markReadBySessionId(Long sessionId, Long enterpriseId);

    /**
     * 发送职位信息
     */
    R<ChatMessageSendVO> sendJob(ChatJobSendDTO dto, Long enterpriseId);

    /**
     * 查询未读消息总数
     */
    R<ChatUnreadCountVO> getUnreadCount(Long enterpriseId);
}

