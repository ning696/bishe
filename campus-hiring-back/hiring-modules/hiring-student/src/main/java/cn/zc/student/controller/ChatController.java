package cn.zc.student.controller;

import cn.zc.common.core.controller.BaseController;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.domain.TableDataInfo;
import cn.zc.common.core.enums.ResultCode;
import cn.zc.security.service.TokenService;
import cn.zc.student.domain.dto.*;
import cn.zc.student.domain.vo.*;
import cn.zc.student.service.IChatService;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * 聊天控制器
 * 
 * @author campus-hiring-system
 */
@Tag(name = "即时聊天（学生端）", description = "学生端即时聊天相关接口")
@RestController
@RequestMapping("/student/chat")
public class ChatController extends BaseController {

    @Autowired
    private IChatService chatService;

    @Autowired
    private TokenService tokenService;

    @Value("${jwt.secret}")
    private String secret;

    /**
     * 创建/获取会话
     */
    @Operation(summary = "创建/获取会话", description = "创建新会话或获取已有会话")
    @PostMapping("/session/create")
    public R<ChatSessionDetailVO> createOrGetSession(@RequestBody ChatSessionCreateDTO dto,
                                                      @RequestHeader("Authorization") String authorization) {
        Long studentId = getStudentId(authorization);
        if (studentId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return chatService.createOrGetSession(dto, studentId);
    }

    /**
     * 会话列表查询
     */
    @Operation(summary = "会话列表查询", description = "查询学生的会话列表，支持搜索")
    @GetMapping("/session/list")
    public R<TableDataInfo> getSessionList(@RequestParam(required = false) String keyword,
                                            @RequestParam(defaultValue = "1") Integer pageNum,
                                            @RequestParam(defaultValue = "10") Integer pageSize,
                                            @RequestHeader("Authorization") String authorization) {
        Long studentId = getStudentId(authorization);
        if (studentId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return chatService.getSessionList(studentId, pageNum, pageSize, keyword);
    }

    /**
     * 会话详情查询
     */
    @Operation(summary = "会话详情查询", description = "查询会话详细信息")
    @GetMapping("/session/detail")
    public R<ChatSessionDetailVO> getSessionDetail(@RequestParam Long sessionId,
                                                    @RequestHeader("Authorization") String authorization) {
        Long studentId = getStudentId(authorization);
        if (studentId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return chatService.getSessionDetail(sessionId, studentId);
    }

    /**
     * 发送消息
     */
    @Operation(summary = "发送消息", description = "发送聊天消息，支持文本、图片、文件等类型")
    @PostMapping("/message/send")
    public R<ChatMessageSendVO> sendMessage(@RequestBody ChatMessageSendDTO dto,
                                             @RequestHeader("Authorization") String authorization) {
        Long studentId = getStudentId(authorization);
        if (studentId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return chatService.sendMessage(dto, studentId);
    }

    /**
     * 消息列表查询
     */
    @Operation(summary = "消息列表查询", description = "分页查询会话中的消息列表")
    @GetMapping("/message/list")
    public R<TableDataInfo> getMessageList(@RequestParam Long sessionId,
                                            @RequestParam(defaultValue = "1") Integer pageNum,
                                            @RequestParam(defaultValue = "20") Integer pageSize,
                                            @RequestHeader("Authorization") String authorization) {
        Long studentId = getStudentId(authorization);
        if (studentId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return chatService.getMessageList(sessionId, studentId, pageNum, pageSize);
    }

    /**
     * 标记已读
     */
    @Operation(summary = "标记已读", description = "标记消息为已读状态")
    @PostMapping("/message/mark-read")
    public R<Void> markRead(@RequestBody ChatMessageMarkReadDTO dto,
                             @RequestHeader("Authorization") String authorization) {
        Long studentId = getStudentId(authorization);
        if (studentId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return chatService.markRead(dto, studentId);
    }

    /**
     * 标记会话所有消息为已读（路径参数方式）
     */
    @Operation(summary = "标记会话所有消息为已读", description = "标记指定会话的所有未读消息为已读状态")
    @PutMapping("/message/read/{sessionId}")
    public R<Void> markReadBySessionId(@PathVariable("sessionId") Long sessionId,
                                      @RequestHeader("Authorization") String authorization) {
        Long studentId = getStudentId(authorization);
        if (studentId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return chatService.markReadBySessionId(sessionId, studentId);
    }

    /**
     * 发送简历
     */
    @Operation(summary = "发送简历", description = "在聊天中发送简历")
    @PostMapping("/message/send-resume")
    public R<ChatMessageSendVO> sendResume(@RequestBody ChatResumeSendDTO dto,
                                            @RequestHeader("Authorization") String authorization) {
        Long studentId = getStudentId(authorization);
        if (studentId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return chatService.sendResume(dto, studentId);
    }

    /**
     * 查询未读消息总数
     */
    @Operation(summary = "查询未读消息总数", description = "查询学生的未读消息总数")
    @GetMapping("/message/unread-count")
    public R<ChatUnreadCountVO> getUnreadCount(@RequestHeader("Authorization") String authorization) {
        Long studentId = getStudentId(authorization);
        if (studentId == null) {
            return R.fail(ResultCode.FAILED_UNAUTHORIZED);
        }
        return chatService.getUnreadCount(studentId);
    }

    /**
     * 从Token中获取学生ID
     */
    private Long getStudentId(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return null;
        }
        try {
            String token = authorization.replace("Bearer ", "");
            Claims claims = tokenService.getClaims(token, secret);
            if (claims == null) {
                return null;
            }
            return tokenService.getUserId(claims);
        } catch (Exception e) {
            return null;
        }
    }
}

