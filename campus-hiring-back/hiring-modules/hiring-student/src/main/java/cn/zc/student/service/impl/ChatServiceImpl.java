package cn.zc.student.service.impl;

import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;
import cn.zc.common.core.enums.ResultCode;
import cn.zc.student.domain.dto.*;
import cn.zc.student.domain.entity.ChatSession;
import cn.zc.student.domain.entity.Resume;
import cn.zc.student.domain.vo.*;
import cn.zc.student.mapper.ChatMessageMapper;
import cn.zc.student.mapper.ChatSessionMapper;
import cn.zc.student.mapper.ResumeMapper;
import cn.zc.student.rabbit.ChatMessageProducer;
import cn.zc.student.service.IChatService;
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
    private ResumeMapper resumeMapper;

    @Autowired
    private ChatMessageProducer chatMessageProducer;

    @Override
    @Transactional
    public R<ChatSessionDetailVO> createOrGetSession(ChatSessionCreateDTO dto, Long studentId) {
        // 查询是否已存在会话
        ChatSession existSession = chatSessionMapper.selectSessionByStudentAndEnterpriseAndJob(
                studentId, dto.getEnterpriseId(), dto.getJobId());
        
        if (existSession != null) {
            // 返回现有会话
            ChatSessionDetailVO vo = chatSessionMapper.selectSessionDetail(existSession.getId(), studentId);
            return R.ok(vo);
        }

        // 创建新会话
        ChatSession session = new ChatSession();
        session.setStudentId(studentId);
        session.setEnterpriseId(dto.getEnterpriseId());
        session.setJobId(dto.getJobId());
        session.setStudentUnreadCount(0);
        session.setEnterpriseUnreadCount(0);
        session.setStatus(1); // 正常状态

        int rows = chatSessionMapper.insert(session);
        if (rows <= 0) {
            return R.fail(ResultCode.FAILED);
        }

        ChatSessionDetailVO vo = chatSessionMapper.selectSessionDetail(session.getId(), studentId);
        return R.ok(vo);
    }

    @Override
    public R<TableDataInfo> getSessionList(Long studentId, Integer pageNum, Integer pageSize, String keyword) {
        PageHelper.startPage(pageNum, pageSize);
        List<ChatSessionListVO> list = chatSessionMapper.selectSessionList(studentId, keyword);
        PageInfo<ChatSessionListVO> pageInfo = new PageInfo<>(list);
        TableDataInfo dataInfo = TableDataInfo.success(list, pageInfo.getTotal());
        return R.ok(dataInfo);
    }

    @Override
    public R<ChatSessionDetailVO> getSessionDetail(Long sessionId, Long studentId) {
        ChatSessionDetailVO vo = chatSessionMapper.selectSessionDetail(sessionId, studentId);
        if (vo == null) {
            return R.fail(ResultCode.FAILED_NOT_EXISTS);
        }
        return R.ok(vo);
    }

    @Override
    public R<ChatMessageSendVO> sendMessage(ChatMessageSendDTO dto, Long studentId) {
        // 验证会话是否存在且属于当前学生
        ChatSession session = chatSessionMapper.selectById(dto.getSessionId());
        if (session == null || !session.getStudentId().equals(studentId)) {
            return R.fail(ResultCode.FAILED_NOT_EXISTS);
        }

        // 生成临时消息ID（实际ID由数据库生成）
        Long tempMessageId = System.currentTimeMillis();

        // 构建消息存储DTO
        ChatMessageStoreDTO storeDTO = new ChatMessageStoreDTO();
        storeDTO.setSessionId(dto.getSessionId());
        storeDTO.setSenderId(studentId);
        storeDTO.setSenderType(1); // 学生
        storeDTO.setReceiverId(session.getEnterpriseId());
        storeDTO.setReceiverType(2); // 企业
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
        vo.setSenderId(studentId);
        vo.setSenderType(1);
        vo.setReceiverId(session.getEnterpriseId());
        vo.setReceiverType(2);
        vo.setMessageType(dto.getMessageType());
        vo.setContent(dto.getContent());
        vo.setRelatedJobId(dto.getRelatedJobId());
        vo.setIsRead(Boolean.TRUE);
        vo.setCreateTime(LocalDateTime.now());
        return R.ok(vo);
    }

    @Override
    public R<TableDataInfo> getMessageList(Long sessionId, Long studentId, Integer pageNum, Integer pageSize) {
        // 验证会话是否存在且属于当前学生
        ChatSession session = chatSessionMapper.selectById(sessionId);
        if (session == null || !session.getStudentId().equals(studentId)) {
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
    public R<Void> markRead(ChatMessageMarkReadDTO dto, Long studentId) {
        // 验证会话是否存在且属于当前学生
        ChatSession session = chatSessionMapper.selectById(dto.getSessionId());
        if (session == null || !session.getStudentId().equals(studentId)) {
            return R.fail(ResultCode.FAILED_NOT_EXISTS);
        }

        // 更新消息已读状态
        if (dto.getMessageIds() != null && !dto.getMessageIds().isEmpty()) {
            int rows = chatMessageMapper.updateMessageRead(dto.getMessageIds(), studentId);
            
            // 更新会话未读消息数
            if (rows > 0) {
                LambdaUpdateWrapper<ChatSession> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.eq(ChatSession::getId, dto.getSessionId())
                        .setSql("student_unread_count = GREATEST(0, student_unread_count - " + rows + ")");
                chatSessionMapper.update(null, updateWrapper);
            }
        }

        return R.ok();
    }

    @Override
    @Transactional
    public R<Void> markReadBySessionId(Long sessionId, Long studentId) {
        // 验证会话是否存在且属于当前学生
        ChatSession session = chatSessionMapper.selectById(sessionId);
        if (session == null || !session.getStudentId().equals(studentId)) {
            return R.fail(ResultCode.FAILED_NOT_EXISTS);
        }

        // 标记会话所有未读消息为已读
        int rows = chatMessageMapper.updateSessionMessageRead(sessionId, studentId);
        
        // 更新会话未读消息数
        if (rows > 0) {
            LambdaUpdateWrapper<ChatSession> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(ChatSession::getId, sessionId)
                    .setSql("student_unread_count = GREATEST(0, student_unread_count - " + rows + ")");
            chatSessionMapper.update(null, updateWrapper);
        }

        return R.ok();
    }

    @Override
    public R<ChatMessageSendVO> sendResume(ChatResumeSendDTO dto, Long studentId) {
        // 验证会话是否存在且属于当前学生
        ChatSession session = chatSessionMapper.selectById(dto.getSessionId());
        if (session == null || !session.getStudentId().equals(studentId)) {
            return R.fail(ResultCode.FAILED_NOT_EXISTS);
        }

        // 验证简历是否存在且属于当前学生
        Resume resume = resumeMapper.selectById(dto.getResumeId());
        if (resume == null || !resume.getStudentId().equals(studentId)) {
            return R.fail(ResultCode.FAILED_RESUME_NOT_EXISTS);
        }

        // 构建简历消息内容（JSON格式）
        String resumeContent = String.format("{\"resumeId\":%d,\"resumeName\":\"%s\"}", 
                resume.getId(), resume.getResumeName());

        // 构建消息存储DTO
        ChatMessageStoreDTO storeDTO = new ChatMessageStoreDTO();
        storeDTO.setSessionId(dto.getSessionId());
        storeDTO.setSenderId(studentId);
        storeDTO.setSenderType(1); // 学生
        storeDTO.setReceiverId(session.getEnterpriseId());
        storeDTO.setReceiverType(2); // 企业
        storeDTO.setMessageType("resume");
        storeDTO.setContent(resumeContent);
        storeDTO.setRelatedJobId(null);

        // 发送到RabbitMQ异步存储
        try {
            chatMessageProducer.sendMessage(storeDTO);
        } catch (Exception e) {
            // 记录日志，但不影响接口返回
            // log.error("发送简历消息到RabbitMQ失败", e);
        }

        // 立即返回消息基本信息
        ChatMessageSendVO vo = new ChatMessageSendVO();
        vo.setMessageId(System.currentTimeMillis());
        vo.setSessionId(dto.getSessionId());
        vo.setSenderId(studentId);
        vo.setSenderType(1);
        vo.setReceiverId(session.getEnterpriseId());
        vo.setReceiverType(2);
        vo.setMessageType("resume");
        vo.setContent(resumeContent);
        vo.setRelatedJobId(null);
        vo.setIsRead(Boolean.TRUE);
        vo.setCreateTime(LocalDateTime.now());
        return R.ok(vo);
    }

    @Override
    public R<ChatUnreadCountVO> getUnreadCount(Long studentId) {
        Integer count = chatMessageMapper.selectUnreadCount(studentId);
        ChatUnreadCountVO vo = new ChatUnreadCountVO();
        vo.setTotalUnreadCount(count != null ? count : 0);
        return R.ok(vo);
    }
}

