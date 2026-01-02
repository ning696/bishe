package cn.zc.enterprise.service.impl;

import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;
import cn.zc.common.core.enums.ResultCode;
import cn.zc.enterprise.domain.dto.*;
import cn.zc.enterprise.domain.entity.ChatSession;
import cn.zc.enterprise.domain.entity.Job;
import cn.zc.enterprise.domain.vo.*;
import cn.zc.enterprise.mapper.ChatMessageMapper;
import cn.zc.enterprise.mapper.ChatSessionMapper;
import cn.zc.enterprise.mapper.JobMapper;
import cn.zc.enterprise.rabbit.ChatMessageProducer;
import cn.zc.enterprise.service.IChatService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 聊天服务实现类
 * 
 * @author campus-hiring-system
 */
@Service
public class ChatServiceImpl implements IChatService {

    @Autowired
    private ChatSessionMapper chatSessionMapper;

    @Autowired
    private ChatMessageMapper chatMessageMapper;

    @Autowired
    private JobMapper jobMapper;

    @Autowired
    private ChatMessageProducer chatMessageProducer;

    @Override
    public R<TableDataInfo> getSessionList(Long enterpriseId, Integer pageNum, Integer pageSize, String keyword) {
        PageHelper.startPage(pageNum, pageSize);
        List<ChatSessionListVO> list = chatSessionMapper.selectSessionList(enterpriseId, keyword);
        PageInfo<ChatSessionListVO> pageInfo = new PageInfo<>(list);
        TableDataInfo dataInfo = TableDataInfo.success(list, pageInfo.getTotal());
        return R.ok(dataInfo);
    }

    @Override
    public R<ChatSessionDetailVO> getSessionDetail(Long sessionId, Long enterpriseId) {
        ChatSessionDetailVO vo = chatSessionMapper.selectSessionDetail(sessionId, enterpriseId);
        if (vo == null) {
            return R.fail(ResultCode.FAILED_NOT_EXISTS);
        }
        return R.ok(vo);
    }

    @Override
    public R<ChatMessageSendVO> sendMessage(ChatMessageSendDTO dto, Long enterpriseId) {
        // 验证会话是否存在且属于当前企业
        ChatSession session = chatSessionMapper.selectById(dto.getSessionId());
        if (session == null || !session.getEnterpriseId().equals(enterpriseId)) {
            return R.fail(ResultCode.FAILED_NOT_EXISTS);
        }

        // 生成临时消息ID（实际ID由数据库生成）
        Long tempMessageId = System.currentTimeMillis();

        // 构建消息存储DTO
        ChatMessageStoreDTO storeDTO = new ChatMessageStoreDTO();
        storeDTO.setSessionId(dto.getSessionId());
        storeDTO.setSenderId(enterpriseId);
        storeDTO.setSenderType(2); // 企业
        storeDTO.setReceiverId(session.getStudentId());
        storeDTO.setReceiverType(1); // 学生
        storeDTO.setMessageType(dto.getMessageType());
        storeDTO.setContent(dto.getContent());
        storeDTO.setRelatedJobId(dto.getRelatedJobId());

        // 发送到RabbitMQ异步存储
        try {
            chatMessageProducer.sendMessage(storeDTO);
        } catch (Exception e) {
            // 记录日志，但不影响接口返回
            // log.error("发送消息到RabbitMQ失败", e);
        }

        // 立即返回消息基本信息
        ChatMessageSendVO vo = new ChatMessageSendVO();
        vo.setMessageId(tempMessageId);
        vo.setSessionId(dto.getSessionId());
        vo.setSenderId(enterpriseId);
        vo.setSenderType(2);
        vo.setReceiverId(session.getStudentId());
        vo.setReceiverType(1);
        vo.setMessageType(dto.getMessageType());
        vo.setContent(dto.getContent());
        vo.setRelatedJobId(dto.getRelatedJobId());
        vo.setIsRead(Boolean.TRUE);
        vo.setCreateTime(LocalDateTime.now());
        return R.ok(vo);
    }

    @Override
    public R<TableDataInfo> getMessageList(Long sessionId, Long enterpriseId, Integer pageNum, Integer pageSize) {
        // 验证会话是否存在且属于当前企业
        ChatSession session = chatSessionMapper.selectById(sessionId);
        if (session == null || !session.getEnterpriseId().equals(enterpriseId)) {
            return R.fail(ResultCode.FAILED_NOT_EXISTS);
        }

        PageHelper.startPage(pageNum, pageSize);
        List<ChatMessageListVO> list = chatMessageMapper.selectMessageList(sessionId);
        PageInfo<ChatMessageListVO> pageInfo = new PageInfo<>(list);
        TableDataInfo dataInfo = TableDataInfo.success(list, pageInfo.getTotal());
        return R.ok(dataInfo);
    }

    @Override
    @Transactional
    public R<Void> markRead(ChatMessageMarkReadDTO dto, Long enterpriseId) {
        // 验证会话是否存在且属于当前企业
        ChatSession session = chatSessionMapper.selectById(dto.getSessionId());
        if (session == null || !session.getEnterpriseId().equals(enterpriseId)) {
            return R.fail(ResultCode.FAILED_NOT_EXISTS);
        }

        // 更新消息已读状态
        if (dto.getMessageIds() != null && !dto.getMessageIds().isEmpty()) {
            int rows = chatMessageMapper.updateMessageRead(dto.getMessageIds(), enterpriseId);
            
            // 更新会话未读消息数
            if (rows > 0) {
                LambdaUpdateWrapper<ChatSession> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.eq(ChatSession::getId, dto.getSessionId())
                        .setSql("enterprise_unread_count = GREATEST(0, enterprise_unread_count - " + rows + ")");
                chatSessionMapper.update(null, updateWrapper);
            }
        }

        return R.ok();
    }

    @Override
    @Transactional
    public R<Void> markReadBySessionId(Long sessionId, Long enterpriseId) {
        // 验证会话是否存在且属于当前企业
        ChatSession session = chatSessionMapper.selectById(sessionId);
        if (session == null || !session.getEnterpriseId().equals(enterpriseId)) {
            return R.fail(ResultCode.FAILED_NOT_EXISTS);
        }

        // 标记会话所有未读消息为已读
        int rows = chatMessageMapper.updateSessionMessageRead(sessionId, enterpriseId);
        
        // 更新会话未读消息数
        if (rows > 0) {
            LambdaUpdateWrapper<ChatSession> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(ChatSession::getId, sessionId)
                    .setSql("enterprise_unread_count = GREATEST(0, enterprise_unread_count - " + rows + ")");
            chatSessionMapper.update(null, updateWrapper);
        }

        return R.ok();
    }

    @Override
    public R<ChatMessageSendVO> sendJob(ChatJobSendDTO dto, Long enterpriseId) {
        // 验证会话是否存在且属于当前企业
        ChatSession session = chatSessionMapper.selectById(dto.getSessionId());
        if (session == null || !session.getEnterpriseId().equals(enterpriseId)) {
            return R.fail(ResultCode.FAILED_NOT_EXISTS);
        }

        // 验证职位是否存在且属于当前企业
        Job job = jobMapper.selectById(dto.getJobId());
        if (job == null || !job.getEnterpriseId().equals(enterpriseId)) {
            return R.fail(ResultCode.FAILED_JOB_NOT_EXISTS);
        }

        // 构建职位消息内容（JSON格式）
        String jobContent = String.format("{\"jobId\":%d,\"jobName\":\"%s\"}", 
                job.getId(), job.getJobName());

        // 构建消息存储DTO
        ChatMessageStoreDTO storeDTO = new ChatMessageStoreDTO();
        storeDTO.setSessionId(dto.getSessionId());
        storeDTO.setSenderId(enterpriseId);
        storeDTO.setSenderType(2); // 企业
        storeDTO.setReceiverId(session.getStudentId());
        storeDTO.setReceiverType(1); // 学生
        storeDTO.setMessageType("job");
        storeDTO.setContent(jobContent);
        storeDTO.setRelatedJobId(dto.getJobId());

        // 发送到RabbitMQ异步存储
        try {
            chatMessageProducer.sendMessage(storeDTO);
        } catch (Exception e) {
            // 记录日志，但不影响接口返回
            // log.error("发送职位消息到RabbitMQ失败", e);
        }

        // 立即返回消息基本信息
        ChatMessageSendVO vo = new ChatMessageSendVO();
        vo.setMessageId(System.currentTimeMillis());
        vo.setSessionId(dto.getSessionId());
        vo.setSenderId(enterpriseId);
        vo.setSenderType(2);
        vo.setReceiverId(session.getStudentId());
        vo.setReceiverType(1);
        vo.setMessageType("job");
        vo.setContent(jobContent);
        vo.setRelatedJobId(dto.getJobId());
        vo.setIsRead(Boolean.TRUE);
        vo.setCreateTime(LocalDateTime.now());
        return R.ok(vo);
    }

    @Override
    public R<ChatUnreadCountVO> getUnreadCount(Long enterpriseId) {
        Integer count = chatMessageMapper.selectUnreadCount(enterpriseId);
        ChatUnreadCountVO vo = new ChatUnreadCountVO();
        vo.setTotalUnreadCount(count != null ? count : 0);
        return R.ok(vo);
    }
}

