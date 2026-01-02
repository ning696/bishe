-- ============================================
-- 校园招聘系统数据库设计
-- 数据库名称: campus_hiring_system
-- 字符集: utf8mb4
-- 排序规则: utf8mb4_unicode_ci
-- ============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `campus_hiring_system` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `campus_hiring_system`;

-- ============================================
-- 1. 用户相关表
-- ============================================

-- 1.1 管理员表
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码（加密）',
    `nick_name` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    `real_name` VARCHAR(50) DEFAULT NULL COMMENT '真实姓名',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `head_image` VARCHAR(500) DEFAULT NULL COMMENT '头像URL',
    `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态：0-已拉黑，1-正常，2-已禁用（管理员只有0、1、2三种状态）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` BIGINT(20) DEFAULT NULL COMMENT '创建人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` BIGINT(20) DEFAULT NULL COMMENT '更新人',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='管理员表';

-- 1.2 学生表
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '学生ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码（加密）',
    `nick_name` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    `real_name` VARCHAR(50) DEFAULT NULL COMMENT '真实姓名',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `head_image` VARCHAR(500) DEFAULT NULL COMMENT '头像URL',
    `gender` TINYINT(4) DEFAULT NULL COMMENT '性别：0-女，1-男',
    `birthday` DATE DEFAULT NULL COMMENT '生日',
    `campus_id` BIGINT(20) DEFAULT NULL COMMENT '所属校园ID',
    `major` VARCHAR(100) DEFAULT NULL COMMENT '专业',
    `education` VARCHAR(50) DEFAULT NULL COMMENT '学历：专科、本科、硕士、博士',
    `grade` VARCHAR(50) DEFAULT NULL COMMENT '年级',
    `skills` VARCHAR(500) DEFAULT NULL COMMENT '技能（多个用逗号分隔）',
    `experience` INT(11) DEFAULT 0 COMMENT '工作经验（月）',
    `expected_salary` DECIMAL(10,2) DEFAULT NULL COMMENT '期望薪资',
    `expected_location` VARCHAR(200) DEFAULT NULL COMMENT '期望工作地点',
    `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态：0-已拉黑，1-正常，2-已禁用，3-待审核',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` BIGINT(20) DEFAULT NULL COMMENT '创建人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` BIGINT(20) DEFAULT NULL COMMENT '更新人',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_campus_id` (`campus_id`),
    KEY `idx_status` (`status`),
    KEY `idx_major` (`major`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学生表';

-- 1.3 企业表
DROP TABLE IF EXISTS `enterprise`;
CREATE TABLE `enterprise` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '企业ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码（加密）',
    `enterprise_name` VARCHAR(200) NOT NULL COMMENT '企业名称',
    `legal_person` VARCHAR(50) DEFAULT NULL COMMENT '法人代表',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `logo` VARCHAR(500) DEFAULT NULL COMMENT '企业Logo URL',
    `address` VARCHAR(500) DEFAULT NULL COMMENT '企业地址',
    `industry` VARCHAR(100) DEFAULT NULL COMMENT '所属行业',
    `scale` VARCHAR(50) DEFAULT NULL COMMENT '企业规模：1-50人、51-200人、201-500人、500人以上',
    `description` TEXT COMMENT '企业简介',
    `website` VARCHAR(200) DEFAULT NULL COMMENT '企业官网',
    `certification_status` TINYINT(4) NOT NULL DEFAULT 0 COMMENT '认证状态：0-未认证，1-已认证，2-认证中，3-认证失败',
    `certification_file` VARCHAR(500) DEFAULT NULL COMMENT '认证文件URL（营业执照等）',
    `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态：0-已拉黑，1-正常，2-已禁用，3-待审核',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` BIGINT(20) DEFAULT NULL COMMENT '创建人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` BIGINT(20) DEFAULT NULL COMMENT '更新人',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_certification_status` (`certification_status`),
    KEY `idx_status` (`status`),
    KEY `idx_industry` (`industry`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='企业表';

-- ============================================
-- 2. 校园相关表
-- ============================================

-- 2.1 校园表
DROP TABLE IF EXISTS `campus`;
CREATE TABLE `campus` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '校园ID',
    `campus_name` VARCHAR(100) NOT NULL COMMENT '校园名称',
    `campus_code` VARCHAR(50) DEFAULT NULL COMMENT '校园编码',
    `address` VARCHAR(500) DEFAULT NULL COMMENT '校园地址',
    `contact_person` VARCHAR(50) DEFAULT NULL COMMENT '联系人',
    `contact_phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` BIGINT(20) DEFAULT NULL COMMENT '创建人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` BIGINT(20) DEFAULT NULL COMMENT '更新人',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_campus_name` (`campus_name`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='校园表';

-- ============================================
-- 3. 职位相关表
-- ============================================

-- 3.1 职位类别表
DROP TABLE IF EXISTS `job_category`;
CREATE TABLE `job_category` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '类别ID',
    `category_name` VARCHAR(100) NOT NULL COMMENT '类别名称',
    `parent_id` BIGINT(20) DEFAULT 0 COMMENT '父类别ID（0表示顶级类别）',
    `sort_order` INT(11) DEFAULT 0 COMMENT '排序顺序',
    `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` BIGINT(20) DEFAULT NULL COMMENT '创建人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` BIGINT(20) DEFAULT NULL COMMENT '更新人',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='职位类别表';

-- 3.2 职位表
DROP TABLE IF EXISTS `job`;
CREATE TABLE `job` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '职位ID',
    `enterprise_id` BIGINT(20) NOT NULL COMMENT '企业ID',
    `category_id` BIGINT(20) DEFAULT NULL COMMENT '职位类别ID',
    `job_name` VARCHAR(200) NOT NULL COMMENT '职位名称',
    `job_description` TEXT COMMENT '职位描述',
    `required_major` VARCHAR(100) DEFAULT NULL COMMENT '要求专业',
    `required_skills` VARCHAR(500) DEFAULT NULL COMMENT '要求技能（多个用逗号分隔）',
    `required_education` VARCHAR(50) DEFAULT NULL COMMENT '要求学历：专科、本科、硕士、博士',
    `required_experience` INT(11) DEFAULT 0 COMMENT '要求工作经验（月）',
    `work_location` VARCHAR(200) NOT NULL COMMENT '工作地点',
    `salary_min` DECIMAL(10,2) DEFAULT NULL COMMENT '最低薪资',
    `salary_max` DECIMAL(10,2) DEFAULT NULL COMMENT '最高薪资',
    `salary_type` VARCHAR(20) DEFAULT NULL COMMENT '薪资类型：面议、月薪、年薪',
    `job_type` VARCHAR(50) DEFAULT NULL COMMENT '工作类型：全职、兼职、实习',
    `recruit_count` INT(11) DEFAULT 1 COMMENT '招聘人数',
    `view_count` INT(11) DEFAULT 0 COMMENT '浏览次数',
    `apply_count` INT(11) DEFAULT 0 COMMENT '申请次数',
    `status` TINYINT(4) NOT NULL DEFAULT 0 COMMENT '状态：0-待审核，1-已通过，2-已拒绝，3-已下线',
    `audit_remark` VARCHAR(500) DEFAULT NULL COMMENT '审核备注',
    `audit_time` DATETIME DEFAULT NULL COMMENT '审核时间',
    `audit_by` BIGINT(20) DEFAULT NULL COMMENT '审核人（管理员ID）',
    `publish_time` DATETIME DEFAULT NULL COMMENT '发布时间',
    `expire_time` DATETIME DEFAULT NULL COMMENT '过期时间',
    `create_by` BIGINT(20) DEFAULT NULL COMMENT '创建人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` BIGINT(20) DEFAULT NULL COMMENT '更新人',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_enterprise_id` (`enterprise_id`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_status` (`status`),
    KEY `idx_enterprise_status` (`enterprise_id`, `status`),
    KEY `idx_work_location` (`work_location`),
    KEY `idx_publish_time` (`publish_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='职位表';

-- 3.3 校园职位关联表
DROP TABLE IF EXISTS `campus_job`;
CREATE TABLE `campus_job` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '关联ID',
    `campus_id` BIGINT(20) NOT NULL COMMENT '校园ID',
    `job_id` BIGINT(20) NOT NULL COMMENT '职位ID',
    `create_by` BIGINT(20) DEFAULT NULL COMMENT '创建人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` BIGINT(20) DEFAULT NULL COMMENT '更新人',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_campus_job` (`campus_id`, `job_id`),
    KEY `idx_campus_id` (`campus_id`),
    KEY `idx_job_id` (`job_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='校园职位关联表';

-- 3.4 职位收藏表
DROP TABLE IF EXISTS `job_favorite`;
CREATE TABLE `job_favorite` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
    `student_id` BIGINT(20) NOT NULL COMMENT '学生ID',
    `job_id` BIGINT(20) NOT NULL COMMENT '职位ID',
    `create_by` BIGINT(20) DEFAULT NULL COMMENT '创建人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` BIGINT(20) DEFAULT NULL COMMENT '更新人',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_student_job` (`student_id`, `job_id`),
    KEY `idx_student_id` (`student_id`),
    KEY `idx_job_id` (`job_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='职位收藏表';

-- 3.5 职位申请表
DROP TABLE IF EXISTS `job_application`;
CREATE TABLE `job_application` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '申请ID',
    `student_id` BIGINT(20) NOT NULL COMMENT '学生ID',
    `job_id` BIGINT(20) NOT NULL COMMENT '职位ID',
    `resume_id` BIGINT(20) DEFAULT NULL COMMENT '简历ID',
    `application_status` TINYINT(4) NOT NULL DEFAULT 0 COMMENT '申请状态：0-待处理，1-已通过，2-已拒绝，3-已取消',
    `application_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
    `view_time` DATETIME DEFAULT NULL COMMENT '查看时间',
    `handle_time` DATETIME DEFAULT NULL COMMENT '处理时间',
    `handle_remark` VARCHAR(500) DEFAULT NULL COMMENT '处理备注',
    `create_by` BIGINT(20) DEFAULT NULL COMMENT '创建人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` BIGINT(20) DEFAULT NULL COMMENT '更新人',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_student_id` (`student_id`),
    KEY `idx_job_id` (`job_id`),
    KEY `idx_application_status` (`application_status`),
    KEY `idx_application_time` (`application_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='职位申请表';

-- ============================================
-- 4. 简历相关表
-- ============================================

-- 4.1 简历表
DROP TABLE IF EXISTS `resume`;
CREATE TABLE `resume` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '简历ID',
    `student_id` BIGINT(20) NOT NULL COMMENT '学生ID',
    `resume_name` VARCHAR(200) DEFAULT NULL COMMENT '简历名称',
    `resume_file` VARCHAR(500) DEFAULT NULL COMMENT '简历文件URL（PDF、Word等）',
    `personal_info` TEXT COMMENT '个人信息（JSON格式存储）',
    `education_background` TEXT COMMENT '教育背景（JSON格式存储）',
    `work_experience` TEXT COMMENT '工作经历（JSON格式存储）',
    `project_experience` TEXT COMMENT '项目经历（JSON格式存储）',
    `skills` TEXT COMMENT '技能描述',
    `self_introduction` TEXT COMMENT '自我介绍',
    `is_default` TINYINT(4) DEFAULT 0 COMMENT '是否默认简历：0-否，1-是',
    `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态：0-已删除，1-正常',
    `create_by` BIGINT(20) DEFAULT NULL COMMENT '创建人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` BIGINT(20) DEFAULT NULL COMMENT '更新人',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_student_id` (`student_id`),
    KEY `idx_status` (`status`),
    KEY `idx_is_default` (`is_default`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='简历表';


-- ============================================
-- 5. 面试相关表
-- ============================================

-- 5.1 面试表
DROP TABLE IF EXISTS `interview`;
CREATE TABLE `interview` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '面试ID',
    `student_id` BIGINT(20) NOT NULL COMMENT '学生ID',
    `enterprise_id` BIGINT(20) NOT NULL COMMENT '企业ID',
    `job_id` BIGINT(20) NOT NULL COMMENT '职位ID',
    `application_id` BIGINT(20) DEFAULT NULL COMMENT '申请ID',
    `interview_time` DATETIME DEFAULT NULL COMMENT '面试时间',
    `interview_location` VARCHAR(500) DEFAULT NULL COMMENT '面试地点',
    `interview_type` VARCHAR(50) DEFAULT NULL COMMENT '面试类型：现场面试、视频面试、电话面试',
    `interview_status` TINYINT(4) NOT NULL DEFAULT 0 COMMENT '面试状态：0-待安排，1-已安排，2-已完成，3-已取消',
    `contact_person` VARCHAR(50) DEFAULT NULL COMMENT '联系人',
    `contact_phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` BIGINT(20) DEFAULT NULL COMMENT '创建人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` BIGINT(20) DEFAULT NULL COMMENT '更新人',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_student_id` (`student_id`),
    KEY `idx_enterprise_id` (`enterprise_id`),
    KEY `idx_job_id` (`job_id`),
    KEY `idx_enterprise_status` (`enterprise_id`, `interview_status`),
    KEY `idx_interview_status` (`interview_status`),
    KEY `idx_interview_time` (`interview_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='面试表';

-- 5.2 面试评价表
DROP TABLE IF EXISTS `interview_evaluation`;
CREATE TABLE `interview_evaluation` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '评价ID',
    `interview_id` BIGINT(20) NOT NULL COMMENT '面试ID',
    `student_id` BIGINT(20) NOT NULL COMMENT '学生ID',
    `enterprise_id` BIGINT(20) NOT NULL COMMENT '企业ID',
    `job_id` BIGINT(20) NOT NULL COMMENT '职位ID',
    `professional_ability` INT(11) DEFAULT NULL COMMENT '专业能力评分（1-10分）',
    `communication_ability` INT(11) DEFAULT NULL COMMENT '沟通能力评分（1-10分）',
    `team_cooperation` INT(11) DEFAULT NULL COMMENT '团队合作评分（1-10分）',
    `overall_score` DECIMAL(3,1) DEFAULT NULL COMMENT '综合评分（1-10分）',
    `evaluation_content` TEXT COMMENT '评价内容',
    `strengths` VARCHAR(500) DEFAULT NULL COMMENT '优点',
    `weaknesses` VARCHAR(500) DEFAULT NULL COMMENT '不足',
    `recommendation` VARCHAR(500) DEFAULT NULL COMMENT '推荐建议',
    `create_by` BIGINT(20) DEFAULT NULL COMMENT '创建人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` BIGINT(20) DEFAULT NULL COMMENT '更新人',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_interview_id` (`interview_id`),
    KEY `idx_student_id` (`student_id`),
    KEY `idx_enterprise_id` (`enterprise_id`),
    KEY `idx_job_id` (`job_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='面试评价表';

-- ============================================
-- 6. 线上咨询相关表
-- ============================================

-- 6.1 咨询表（不再使用，学生咨询使用chat_session和chat_message）
DROP TABLE IF EXISTS `consultation`;
CREATE TABLE `consultation` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '咨询ID',
    `student_id` BIGINT(20) NOT NULL COMMENT '学生ID',
    `enterprise_id` BIGINT(20) NOT NULL COMMENT '企业ID',
    `job_id` BIGINT(20) DEFAULT NULL COMMENT '职位ID（可选，针对某个职位的咨询）',
    `consultation_type` VARCHAR(50) DEFAULT NULL COMMENT '咨询类型：职位咨询、面试咨询、其他',
    `title` VARCHAR(200) DEFAULT NULL COMMENT '咨询标题',
    `content` TEXT COMMENT '咨询内容',
    `reply_content` TEXT COMMENT '回复内容',
    `reply_time` DATETIME DEFAULT NULL COMMENT '回复时间',
    `status` TINYINT(4) NOT NULL DEFAULT 0 COMMENT '状态：0-待回复，1-已回复，2-已关闭',
    `create_by` BIGINT(20) DEFAULT NULL COMMENT '创建人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` BIGINT(20) DEFAULT NULL COMMENT '更新人',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_student_id` (`student_id`),
    KEY `idx_enterprise_id` (`enterprise_id`),
    KEY `idx_job_id` (`job_id`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='线上咨询表';


-- ============================================
-- 聊天会话表
-- ============================================
DROP TABLE IF EXISTS `chat_session`;
CREATE TABLE `chat_session` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '会话ID',
    `student_id` BIGINT(20) NOT NULL COMMENT '学生ID',
    `enterprise_id` BIGINT(20) NOT NULL COMMENT '企业ID',
    `job_id` BIGINT(20) DEFAULT NULL COMMENT '关联职位ID（可选）',
    `last_message_id` BIGINT(20) DEFAULT NULL COMMENT '最后一条消息ID',
    `last_message_time` DATETIME DEFAULT NULL COMMENT '最后消息时间',
    `last_message_content` VARCHAR(500) DEFAULT NULL COMMENT '最后消息内容（用于会话列表展示）',
    `student_unread_count` INT(11) DEFAULT 0 COMMENT '学生未读消息数',
    `enterprise_unread_count` INT(11) DEFAULT 0 COMMENT '企业未读消息数',
    `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态：0-已关闭，1-正常',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        `update_by` BIGINT(20) DEFAULT NULL COMMENT '更新人',
            `create_by` BIGINT(20) DEFAULT NULL COMMENT '创建人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_student_enterprise_job` (`student_id`, `enterprise_id`, `job_id`),
    KEY `idx_student_id` (`student_id`),
    KEY `idx_enterprise_id` (`enterprise_id`),
    KEY `idx_last_message_time` (`last_message_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='聊天会话表';


-- ============================================
-- 聊天消息表
-- ============================================
DROP TABLE IF EXISTS `chat_message`;
CREATE TABLE `chat_message` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '消息ID',
    `session_id` BIGINT(20) NOT NULL COMMENT '会话ID',
    `sender_id` BIGINT(20) NOT NULL COMMENT '发送者ID',
    `sender_type` TINYINT(4) NOT NULL COMMENT '发送者类型：1-学生，2-企业',
    `receiver_id` BIGINT(20) NOT NULL COMMENT '接收者ID',
    `receiver_type` TINYINT(4) NOT NULL COMMENT '接收者类型：1-学生，2-企业',
    `message_type` VARCHAR(50) NOT NULL DEFAULT 'text' COMMENT '消息类型：text-文本，job-职位信息，image-图片，file-文件',
    `content` TEXT COMMENT '消息内容（文本消息或JSON格式的职位信息）',
    `related_job_id` BIGINT(20) DEFAULT NULL COMMENT '关联职位ID（当message_type为job时）',
    `is_read` TINYINT(4) NOT NULL DEFAULT 0 COMMENT '是否已读：0-未读，1-已读',
    `read_time` DATETIME DEFAULT NULL COMMENT '阅读时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
            `update_by` BIGINT(20) DEFAULT NULL COMMENT '更新人',
            `create_by` BIGINT(20) DEFAULT NULL COMMENT '创建人',
    PRIMARY KEY (`id`),
    KEY `idx_session_id` (`session_id`),
    KEY `idx_session_time` (`session_id`, `create_time`),
    KEY `idx_sender_id` (`sender_id`),
    KEY `idx_receiver_id` (`receiver_id`),
    KEY `idx_is_read` (`is_read`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='聊天消息表';
-- ============================================
-- 7. 投诉与反馈相关表
-- ============================================

-- 7.1 投诉表
DROP TABLE IF EXISTS `complaint`;
CREATE TABLE `complaint` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '投诉ID',
    `complaint_type` TINYINT(4) NOT NULL COMMENT '投诉类型：1-学生投诉企业，2-企业投诉学生',
    `complainer_id` BIGINT(20) NOT NULL COMMENT '投诉人ID（学生ID或企业ID）',
    `complainer_type` TINYINT(4) NOT NULL COMMENT '投诉人类型：1-学生，2-企业',
    `complained_id` BIGINT(20) NOT NULL COMMENT '被投诉人ID（企业ID或学生ID）',
    `complained_type` TINYINT(4) NOT NULL COMMENT '被投诉人类型：1-学生，2-企业',
    `job_id` BIGINT(20) DEFAULT NULL COMMENT '关联职位ID（可选）',
    `title` VARCHAR(200) NOT NULL COMMENT '投诉标题',
    `content` TEXT NOT NULL COMMENT '投诉内容',
    `attachment` VARCHAR(500) DEFAULT NULL COMMENT '附件URL（可选）',
    `handle_status` TINYINT(4) NOT NULL DEFAULT 0 COMMENT '处理状态：0-待处理，1-处理中，2-已处理，3-已关闭',
    `handle_result` VARCHAR(500) DEFAULT NULL COMMENT '处理结果',
    `handle_remark` VARCHAR(500) DEFAULT NULL COMMENT '处理备注',
    `handle_time` DATETIME DEFAULT NULL COMMENT '处理时间',
    `handle_by` BIGINT(20) DEFAULT NULL COMMENT '处理人（管理员ID）',
    `create_by` BIGINT(20) DEFAULT NULL COMMENT '创建人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` BIGINT(20) DEFAULT NULL COMMENT '更新人',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_complainer_id` (`complainer_id`),
    KEY `idx_complained_id` (`complained_id`),
    KEY `idx_job_id` (`job_id`),
    KEY `idx_handle_status` (`handle_status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='投诉表';

-- 7.2 反馈表
DROP TABLE IF EXISTS `feedback`;
CREATE TABLE `feedback` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '反馈ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `user_type` TINYINT(4) NOT NULL COMMENT '用户类型：1-学生，2-企业，3-管理员',
    `feedback_type` VARCHAR(50) DEFAULT NULL COMMENT '反馈类型：功能建议、问题反馈、其他',
    `title` VARCHAR(200) DEFAULT NULL COMMENT '反馈标题',
    `content` TEXT NOT NULL COMMENT '反馈内容',
    `contact_info` VARCHAR(200) DEFAULT NULL COMMENT '联系方式',
    `handle_status` TINYINT(4) NOT NULL DEFAULT 0 COMMENT '处理状态：0-待处理，1-已处理，2-已关闭',
    `handle_result` VARCHAR(500) DEFAULT NULL COMMENT '处理结果',
    `handle_remark` VARCHAR(500) DEFAULT NULL COMMENT '处理备注',
    `handle_time` DATETIME DEFAULT NULL COMMENT '处理时间',
    `handle_by` BIGINT(20) DEFAULT NULL COMMENT '处理人（管理员ID）',
    `create_by` BIGINT(20) DEFAULT NULL COMMENT '创建人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` BIGINT(20) DEFAULT NULL COMMENT '更新人',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_handle_status` (`handle_status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='反馈表';

-- ============================================
-- 8. 消息通知相关表
-- ============================================

-- 8.1 消息表
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '消息ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `user_type` TINYINT(4) NOT NULL COMMENT '用户类型：1-学生，2-企业，3-管理员',
    `message_type` VARCHAR(50) NOT NULL COMMENT '消息类型：面试通知、申请状态变更、咨询回复、系统通知等',
    `title` VARCHAR(200) NOT NULL COMMENT '消息标题',
    `content` TEXT NOT NULL COMMENT '消息内容',
    `related_id` BIGINT(20) DEFAULT NULL COMMENT '关联ID（职位ID、申请ID、咨询ID等）',
    `related_type` VARCHAR(50) DEFAULT NULL COMMENT '关联类型：job、application、consultation等',
    `is_read` TINYINT(4) NOT NULL DEFAULT 0 COMMENT '是否已读：0-未读，1-已读',
    `read_time` DATETIME DEFAULT NULL COMMENT '阅读时间',
    `create_by` BIGINT(20) DEFAULT NULL COMMENT '创建人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` BIGINT(20) DEFAULT NULL COMMENT '更新人',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_user_type` (`user_type`),
    KEY `idx_message_type` (`message_type`),
    KEY `idx_is_read` (`is_read`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息表';

-- ============================================
-- 9. 数据统计相关表（可选，用于统计数据缓存）
-- ============================================

-- 9.1 统计数据表
DROP TABLE IF EXISTS `statistics`;
CREATE TABLE `statistics` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '统计ID',
    `statistics_type` VARCHAR(50) NOT NULL COMMENT '统计类型：user_count、job_count、application_count等',
    `statistics_date` DATE NOT NULL COMMENT '统计日期',
    `statistics_value` BIGINT(20) DEFAULT 0 COMMENT '统计值',
    `extra_data` TEXT COMMENT '额外数据（JSON格式）',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_type_date` (`statistics_type`, `statistics_date`),
    KEY `idx_statistics_type` (`statistics_type`),
    KEY `idx_statistics_date` (`statistics_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='统计数据表';

-- ============================================
-- 数据库设计完成
-- ============================================