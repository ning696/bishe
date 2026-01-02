# 学生服务接口文档

## 文档说明

- **服务名称**: hiring-student
- **服务端口**: 8082
- **网关路径前缀**: `/student`
- **基础URL**: `http://localhost:8080/student` (通过网关访问)
- **文档版本**: v1.0
- **最后更新**: 2025-01-XX

---

## 一、通用说明

### 1.1 统一响应格式

#### 成功响应（R<T>）
```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": {}
}
```

#### 分页响应（TableDataInfo）
```json
{
  "code": 1000,
  "msg": "操作成功",
  "total": 100,
  "rows": []
}
```

### 1.2 请求头说明

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| Authorization | String | 是 | JWT Token，格式：`Bearer {token}` |

### 1.3 分页参数说明

| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| pageNum | Integer | 是 | 1 | 页码 |
| pageSize | Integer | 是 | 10 | 每页数量 |

### 1.4 错误码说明

| 错误码 | 说明 |
|--------|------|
| 1000 | 操作成功 |
| 2000 | 服务繁忙请稍后重试 |
| 3000 | 操作失败 |
| 3001 | 未授权 |
| 3002 | 参数校验失败 |
| 3003 | 资源不存在 |
| 3101 | 用户已存在 |
| 3102 | 用户不存在 |
| 3103 | 账号或密码错误 |
| 3104 | 您已被列入黑名单, 请联系管理员 |
| 3201 | 职位不存在 |
| 3301 | 简历不存在 |
| 3303 | 已投递过该职位 |

---   

## 二、用户管理接口

### 2.1 学生注册

**接口地址**: `/student/student/register`  
**请求方式**: `POST`  
**接口说明**: 学生用户注册

#### 请求参数

```json
{
  "username": "student001",
  "password": "123456"
}
```

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| username | String | 是 | 用户名（唯一） |
| password | String | 是 | 密码 |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": null
}
```

#### 错误响应

- `3101`: 用户已存在

---

### 2.2 发送验证码

**接口地址**: `/student/student/sendCode`  
**请求方式**: `POST`  
**接口说明**: 发送手机验证码

#### 请求参数

```json
{
  "phone": "13800138000"
}
```

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| phone | String | 是 | 手机号 |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": null
}
```

#### 错误响应

- `3105`: 你输入的手机号有误
- `3106`: 操作频繁，请稍后重试
- `3107`: 当天请求次数已达到上限
- `3108`: 验证码发送错误

---

### 2.3 学生登录

**接口地址**: `/student/student/login`  
**请求方式**: `POST`  
**接口说明**: 学生登录，生成 JWT Token。只需要用户名和密码即可登录。

#### 请求参数

```json
{
  "username": "student001",
  "password": "123456"
}
```

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| username | String | 是 | 用户名 |
| password | String | 是 | 密码 |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

| 字段名 | 类型 | 说明 |
|--------|------|------|
| data | String | JWT Token |

#### 错误响应

- `3102`: 用户不存在
- `3103`: 账号或密码错误
- `3104`: 您已被列入黑名单, 请联系管理员

---

### 2.4 获取学生信息

**接口地址**: `/student/student/info`  
**请求方式**: `GET`  
**接口说明**: 获取当前登录学生信息

#### 请求头

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| Authorization | String | 是 | JWT Token |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": {
    "nickName": "张三",
    "headImage": "http://localhost:9000/campus-hiring/avatar/student001.jpg"
  }
}
```

---

### 2.5 学生详情查询

**接口地址**: `/student/student/detail`  
**请求方式**: `GET`  
**接口说明**: 查看学生详细信息

#### 请求头

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| Authorization | String | 是 | JWT Token |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": {
    "id": 1,
    "username": "student001",
    "nickName": "张三",
    "realName": "张三",
    "phone": "13800138000",
    "email": "student001@example.com",
    "headImage": "http://localhost:9000/campus-hiring/avatar/student001.jpg",
    "gender": 1,
    "genderName": "男",
    "birthday": "2000-01-01",
    "campusId": 1,
    "campusName": "XX大学",
    "major": "计算机科学与技术",
    "education": "本科",
    "educationName": "本科",
    "grade": "2024届",
    "skills": "Java,Spring,MySQL",
    "experience": 6,
    "expectedSalary": 8000.00,
    "expectedLocation": "北京",
    "status": 1,
    "statusName": "正常",
    "createTime": "2025-01-01 10:00:00"
  }
}
```

#### 关联查询说明

- 查询学生详情时，关联 `campus` 表获取校园名称

#### 枚举值说明

| 字段 | 值 | 说明 |
|------|-----|------|
| status | 0 | 已拉黑 |
| status | 1 | 正常 |
| status | 2 | 已禁用 |
| status | 3 | 待审核 |
| gender | 0 | 女 |
| gender | 1 | 男 |

---

### 2.6 更新学生信息

**接口地址**: `/student/student/edit`  
**请求方式**: `PUT`  
**接口说明**: 编辑学生个人信息

#### 请求参数

```json
{
  "nickName": "张三",
  "realName": "张三",
  "email": "student001@example.com",
  "gender": 1,
  "birthday": "2000-01-01",
  "campusId": 1,
  "major": "计算机科学与技术",
  "education": "本科",
  "grade": "2024届",
  "skills": "Java,Spring,MySQL",
  "experience": 6,
  "expectedSalary": 8000.00,
  "expectedLocation": "北京"
}
```

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| nickName | String | 否 | 昵称 |
| realName | String | 否 | 真实姓名 |
| email | String | 否 | 邮箱 |
| gender | Integer | 否 | 性别：0-女，1-男 |
| birthday | String | 否 | 生日（格式：yyyy-MM-dd） |
| campusId | Long | 否 | 所属校园ID |
| major | String | 否 | 专业 |
| education | String | 否 | 学历：专科、本科、硕士、博士 |
| grade | String | 否 | 年级 |
| skills | String | 否 | 技能（多个用逗号分隔） |
| experience | Integer | 否 | 工作经验（月） |
| expectedSalary | BigDecimal | 否 | 期望薪资 |
| expectedLocation | String | 否 | 期望工作地点 |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": null
}
```

---

### 2.7 修改密码

**接口地址**: `/student/student/changePassword`  
**请求方式**: `PUT`  
**接口说明**: 修改登录密码

#### 请求参数

```json
{
  "oldPassword": "123456",
  "newPassword": "654321"
}
```

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| oldPassword | String | 是 | 原密码 |
| newPassword | String | 是 | 新密码 |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": null
}
```

#### 错误响应

- `3111`: 原密码错误

---

### 2.8 更新头像

**接口地址**: `/student/student/headImage/update`  
**请求方式**: `PUT`  
**接口说明**: 更新学生头像

#### 请求参数

```json
{
  "headImage": "http://localhost:9000/campus-hiring/avatar/student001.jpg"
}
```

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| headImage | String | 是 | 头像URL |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": null
}
```

---

### 2.9 学生文件上传

**接口地址**: `/student/student/upload`  
**请求方式**: `POST`  
**接口说明**: 上传学生相关文件（如头像、简历附件等），返回 MinIO 文件访问地址

#### 请求参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| file | MultipartFile | 是 | 需要上传的文件，表单字段名称为 `file` |

#### 请求头

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| Authorization | String | 是 | JWT Token |
| Content-Type | String | 是 | `multipart/form-data` |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": {
    "fileName": "student/1/1731225476123_avatar.png",
    "url": "http://localhost:9000/campus-hiring/student/1/1731225476123_avatar.png"
  }
}
```

| 字段名 | 类型 | 说明 |
|--------|------|------|
| fileName | String | MinIO 中的对象名称 |
| url | String | 文件访问地址 |

#### 注意事项

- 系统会自动根据学生ID和时间戳生成文件存储路径，确保不同学生文件隔离
- 上传成功后请在相关业务接口中使用返回的 `url` 字段
- 文件大小及类型限制遵循 MinIO 服务端配置

---

### 2.10 退出登录

**接口地址**: `/student/student/logout`  
**请求方式**: `DELETE`  
**接口说明**: 退出登录，清除 Redis 中的登录状态

#### 请求头

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| Authorization | String | 是 | JWT Token |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": null
}
```

---

## 三、职位浏览与搜索接口

### 3.1 职位列表查询

**接口地址**: `/student/job/list`  
**请求方式**: `GET`  
**接口说明**: 分页查询职位列表，关联查询企业信息、职位类别信息，并判断当前学生是否已收藏、已申请。如果传入了 `studentId` 参数（从JWT Token获取），会计算 `isFavorite` 和 `isApplied` 字段

#### 请求参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 是 | 页码 |
| pageSize | Integer | 是 | 每页数量 |
| categoryId | Long | 否 | 职位类别ID |
| workLocation | String | 否 | 工作地点 |
| salaryMin | BigDecimal | 否 | 最低薪资 |
| salaryMax | BigDecimal | 否 | 最高薪资 |
| jobType | String | 否 | 工作类型：全职、兼职、实习 |
| requiredEducation | String | 否 | 要求学历：专科、本科、硕士、博士 |
| keyword | String | 否 | 搜索关键词（职位名称、企业名称） |
| sortField | String | 否 | 排序字段：publishTime-发布时间，salary-薪资，viewCount-浏览量 |
| sortOrder | String | 否 | 排序方式：asc-升序，desc-降序 |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "total": 100,
  "rows": [
    {
      "id": 1,
      "enterpriseId": 1,
      "enterpriseName": "XX科技有限公司",
      "enterpriseLogo": "http://localhost:9000/campus-hiring/logo/enterprise001.jpg",
      "jobName": "Java开发工程师",
      "workLocation": "北京",
      "salaryMin": 8000.00,
      "salaryMax": 15000.00,
      "salaryType": "月薪",
      "jobType": "全职",
      "requiredEducation": "本科",
      "requiredExperience": 6,
      "recruitCount": 5,
      "viewCount": 100,
      "applyCount": 20,
      "publishTime": "2025-01-01 10:00:00",
      "isFavorite": false,
      "isApplied": false
    }
  ]
}
```

#### 关联查询说明

- 关联 `enterprise` 表获取企业名称、Logo等信息
- 关联 `job_category` 表获取职位类别名称
- 关联 `job_favorite` 表判断是否收藏（需要传入studentId）
- 关联 `resume_delivery` 表判断是否已申请（需要传入studentId）

**注意**: 如果未传入studentId参数（未登录或JWT Token无效），isFavorite和isApplied将返回false

#### 计算字段说明

- `isFavorite`: 通过查询 `job_favorite` 表判断职位是否被当前学生收藏（student_id + job_id）
- `isApplied`: 通过查询 `resume_delivery` 或 `job_application` 表判断职位是否被当前学生申请（student_id + job_id）

---

### 3.2 职位详情查询

**接口地址**: `/student/job/detail`  
**请求方式**: `GET`  
**接口说明**: 查看职位详细信息（通过 Feign 调用企业服务），关联查询企业信息、职位类别信息，并判断当前学生是否已收藏、已申请

#### 请求参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| jobId | Long | 是 | 职位ID |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": {
    "id": 1,
    "enterpriseId": 1,
    "enterpriseName": "XX科技有限公司",
    "enterpriseLogo": "http://localhost:9000/campus-hiring/logo/enterprise001.jpg",
    "enterpriseDescription": "公司简介...",
    "categoryId": 1,
    "categoryName": "技术类",
    "jobName": "Java开发工程师",
    "jobDescription": "职位描述...",
    "requiredMajor": "计算机科学与技术",
    "requiredSkills": "Java,Spring,MySQL",
    "requiredEducation": "本科",
    "requiredExperience": 6,
    "workLocation": "北京",
    "salaryMin": 8000.00,
    "salaryMax": 15000.00,
    "salaryType": "月薪",
    "jobType": "全职",
    "recruitCount": 5,
    "viewCount": 101,
    "applyCount": 20,
    "publishTime": "2025-01-01 10:00:00",
    "expireTime": "2025-06-01 10:00:00",
    "isFavorite": false,
    "isApplied": false
  }
}
```

#### 关联查询说明

- 关联 `enterprise` 表获取企业名称、Logo、企业简介等信息
- 关联 `job_category` 表获取职位类别名称
- 关联 `job_favorite` 表判断是否收藏（需要传入studentId）
- 关联 `resume_delivery` 表判断是否已申请（需要传入studentId）

**注意**: 如果未传入studentId参数（未登录或JWT Token无效），isFavorite和isApplied将返回false

#### 计算字段说明

- `isFavorite`: 通过查询 `job_favorite` 表判断职位是否被当前学生收藏（student_id + job_id）
- `isApplied`: 通过查询 `resume_delivery` 或 `job_application` 表判断职位是否被当前学生申请（student_id + job_id）

---

### 3.3 职位收藏

**接口地址**: `/student/job/favorite`  
**请求方式**: `POST`  
**接口说明**: 收藏职位

#### 请求参数

```json
{
  "jobId": 1
}
```

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| jobId | Long | 是 | 职位ID |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": null
}
```

---

### 3.4 取消收藏

**接口地址**: `/student/job/unfavorite`  
**请求方式**: `DELETE`  
**接口说明**: 取消收藏职位

#### 请求参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| jobId | Long | 是 | 职位ID |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": null
}
```

---

### 3.5 收藏职位列表

**接口地址**: `/student/job/favorite/list`  
**请求方式**: `GET`  
**接口说明**: 查看收藏的职位列表

#### 请求参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 是 | 页码 |
| pageSize | Integer | 是 | 每页数量 |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "total": 10,
  "rows": [
    {
      "id": 1,
      "jobId": 1,
      "jobName": "Java开发工程师",
      "enterpriseName": "XX科技有限公司",
      "workLocation": "北京",
      "salaryMin": 8000.00,
      "salaryMax": 15000.00,
      "salaryType": "月薪",
      "publishTime": "2025-01-01 10:00:00",
      "favoriteTime": "2025-01-02 10:00:00"
    }
  ]
}
```

#### 关联查询说明

- 关联 `job` 表获取职位详细信息
- 关联 `enterprise` 表获取企业名称等信息
- `favoriteTime` 字段来自 `job_favorite` 表的 `create_time` 字段

#### 字段映射说明

- 数据库字段 `job_favorite.create_time` 映射为接口字段 `favoriteTime`（收藏时间）

---

## 四、职位推荐接口

### 4.1 职位推荐

**接口地址**: `/student/job/recommend`  
**请求方式**: `GET`  
**接口说明**: 根据学生条件（专业、技能、经验等）查询匹配的职位（同步查询数据库）

#### 请求参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 是 | 页码 |
| pageSize | Integer | 是 | 每页数量 |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "total": 20,
  "rows": [
    {
      "id": 1,
      "enterpriseId": 1,
      "enterpriseName": "XX科技有限公司",
      "enterpriseLogo": "http://localhost:9000/campus-hiring/logo/enterprise001.jpg",
      "jobName": "Java开发工程师",
      "workLocation": "北京",
      "salaryMin": 8000.00,
      "salaryMax": 15000.00,
      "salaryType": "月薪",
      "requiredMajor": "计算机科学与技术",
      "requiredSkills": "Java,Spring,MySQL",
      "matchScore": 85,
      "matchReason": "专业匹配、技能匹配、薪资匹配",
      "publishTime": "2025-01-01 10:00:00"
    }
  ]
}
```

---

## 五、简历管理接口

### 5.1 简历列表查询

**接口地址**: `/student/resume/list`  
**请求方式**: `GET`  
**接口说明**: 查看学生的简历列表

#### 请求参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 是 | 页码 |
| pageSize | Integer | 是 | 每页数量 |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "total": 3,
  "rows": [
    {
      "id": 1,
      "resumeName": "我的简历",
      "resumeFile": "http://localhost:9000/campus-hiring/resume/resume001.pdf",
      "isDefault": 1,
      "createTime": "2025-01-01 10:00:00"
    }
  ]
}
```

---

### 5.2 简历详情查询

**接口地址**: `/student/resume/detail`  
**请求方式**: `GET`  
**接口说明**: 查看简历详细信息，从 `resume` 表查询简历信息

#### 请求参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| resumeId | Long | 是 | 简历ID |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": {
    "id": 1,
    "resumeName": "我的简历",
    "resumeFile": "http://localhost:9000/campus-hiring/resume/resume001.pdf",
    "personalInfo": {
      "name": "张三",
      "phone": "13800138000",
      "email": "student001@example.com",
      "gender": "男",
      "birthday": "2000-01-01"
    },
    "educationBackground": [
      {
        "school": "XX大学",
        "major": "计算机科学与技术",
        "education": "本科",
        "startTime": "2020-09",
        "endTime": "2024-06"
      }
    ],
    "workExperience": [],
    "projectExperience": [],
    "skills": "Java,Spring,MySQL",
    "selfIntroduction": "自我介绍...",
    "isDefault": 1,
    "createTime": "2025-01-01 10:00:00"
  }
}
```

---

### 5.3 创建简历

**接口地址**: `/student/resume/add`  
**请求方式**: `POST`  
**接口说明**: 创建个人简历

#### 请求参数

```json
{
  "resumeName": "我的简历",
  "resumeFile": "http://localhost:9000/campus-hiring/resume/resume001.pdf",
  "personalInfo": "{\"name\":\"张三\",\"phone\":\"13800138000\"}",
  "educationBackground": "[{\"school\":\"XX大学\",\"major\":\"计算机科学与技术\"}]",
  "workExperience": "[]",
  "projectExperience": "[]",
  "skills": "Java,Spring,MySQL",
  "selfIntroduction": "自我介绍...",
  "isDefault": 0
}
```

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| resumeName | String | 是 | 简历名称 |
| resumeFile | String | 否 | 简历文件URL |
| personalInfo | String | 否 | 个人信息（JSON格式） |
| educationBackground | String | 否 | 教育背景（JSON格式） |
| workExperience | String | 否 | 工作经历（JSON格式） |
| projectExperience | String | 否 | 项目经历（JSON格式） |
| skills | String | 否 | 技能描述 |
| selfIntroduction | String | 否 | 自我介绍 |
| isDefault | Integer | 否 | 是否默认简历：0-否，1-是 |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": 1
}
```

| 字段名 | 类型 | 说明 |
|--------|------|------|
| data | Long | 简历ID |

---

### 5.4 编辑简历

**接口地址**: `/student/resume/edit`  
**请求方式**: `PUT`  
**接口说明**: 编辑简历信息

#### 请求参数

```json
{
  "resumeId": 1,
  "resumeName": "我的简历（更新）",
  "resumeFile": "http://localhost:9000/campus-hiring/resume/resume001.pdf",
  "personalInfo": "{\"name\":\"张三\",\"phone\":\"13800138000\"}",
  "educationBackground": "[{\"school\":\"XX大学\",\"major\":\"计算机科学与技术\"}]",
  "workExperience": "[]",
  "projectExperience": "[]",
  "skills": "Java,Spring,MySQL,Redis",
  "selfIntroduction": "自我介绍（更新）...",
  "isDefault": 1
}
```

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| resumeId | Long | 是 | 简历ID |
| resumeName | String | 否 | 简历名称 |
| resumeFile | String | 否 | 简历文件URL |
| personalInfo | String | 否 | 个人信息（JSON格式） |
| educationBackground | String | 否 | 教育背景（JSON格式） |
| workExperience | String | 否 | 工作经历（JSON格式） |
| projectExperience | String | 否 | 项目经历（JSON格式） |
| skills | String | 否 | 技能描述 |
| selfIntroduction | String | 否 | 自我介绍 |
| isDefault | Integer | 否 | 是否默认简历：0-否，1-是 |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": null
}
```

---

### 5.5 删除简历

**接口地址**: `/student/resume/delete`  
**请求方式**: `DELETE`  
**接口说明**: 删除简历

#### 请求参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| resumeId | Long | 是 | 简历ID |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": null
}
```

---

### 5.6 投递简历

**接口地址**: `/student/resume/delivery`  
**请求方式**: `POST`  
**接口说明**: 向企业投递简历（发送消息到 RabbitMQ）

#### 请求参数

```json
{
  "jobId": 1,
  "resumeId": 1
}
```

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| jobId | Long | 是 | 职位ID |
| resumeId | Long | 是 | 简历ID |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": null
}
```

#### 错误响应

- `3301`: 简历不存在
- `3303`: 已投递过该职位

---

## 六、面试管理接口

### 6.1 面试申请

**接口地址**: `/student/interview/apply`  
**请求方式**: `POST`  
**接口说明**: 对心仪的职位进行面试申请

#### 请求参数

```json
{
  "jobId": 1,
  "resumeId": 1
}
```

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| jobId | Long | 是 | 职位ID |
| resumeId | Long | 否 | 简历ID |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": null
}
```

---

### 6.2 申请记录查询（列表）

**接口地址**: `/student/interview/application/list`  
**请求方式**: `GET`  
**接口说明**: 查看学生的面试申请记录列表（不分页），关联职位、企业及已安排的面试信息

#### 请求参数

无（从 JWT Token 获取学生ID）

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": [
    {
      "id": 1,
      "jobId": 1,
      "jobName": "Java开发工程师",
      "enterpriseId": 1,
      "enterpriseName": "XX科技有限公司",
      "interviewTime": "2025-01-05 14:00:00",
      "interviewLocation": "北京市朝阳区XX路XX号",
      "interviewType": "现场面试",
      "interviewStatus": 1,
      "interviewStatusName": "已安排",
      "remark": "请携带简历和身份证",
      "createTime": "2025-01-02 10:00:00"
    }
  ]
}
```

---

### 6.2.1 申请记录分页查询

**接口地址**: `/student/interview/application/page`  
**请求方式**: `GET`  
**接口说明**: 分页查询学生的面试申请记录，支持按面试状态筛选

#### 请求参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 是 | 页码 |
| pageSize | Integer | 是 | 每页数量 |
| status | Integer | 否 | 面试状态：0-待安排，1-已安排，2-已完成，3-已取消 |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "total": 10,
  "rows": [
    {
      "id": 1,
      "jobId": 1,
      "jobName": "Java开发工程师",
      "enterpriseId": 1,
      "enterpriseName": "XX科技有限公司",
      "interviewTime": "2025-01-05 14:00:00",
      "interviewLocation": "北京市朝阳区XX路XX号",
      "interviewType": "现场面试",
      "interviewStatus": 1,
      "interviewStatusName": "已安排",
      "remark": "请携带简历和身份证",
      "createTime": "2025-01-02 10:00:00"
    }
  ]
}
```

---

### 6.3 面试安排查询

**接口地址**: `/student/interview/list`  
**请求方式**: `GET`  
**接口说明**: 查询学生的面试安排列表（不分页），包含面试时间、地点、联系人等

#### 请求参数

无（从 JWT Token 获取学生ID）

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": [
    {
      "id": 1,
      "jobId": 1,
      "jobName": "Java开发工程师",
      "enterpriseId": 1,
      "enterpriseName": "XX科技有限公司",
      "interviewTime": "2025-01-05 14:00:00",
      "interviewLocation": "北京市朝阳区XX路XX号",
      "interviewType": "现场面试",
      "interviewStatus": 1,
      "interviewStatusName": "已安排",
      "contactPerson": "HR张",
      "contactPhone": "13800138001",
      "remark": "请携带简历和身份证",
      "createTime": "2025-01-02 10:00:00"
    }
  ]
}
```

---

### 6.4 面试详情查询

**接口地址**: `/student/interview/detail/{interviewId}`  
**请求方式**: `GET`  
**接口说明**: 查看指定面试的详细信息（路径参数）

#### 路径参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| interviewId | Long | 是 | 面试ID |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": {
    "id": 1,
    "jobId": 1,
    "jobName": "Java开发工程师",
    "enterpriseId": 1,
    "enterpriseName": "XX科技有限公司",
    "interviewTime": "2025-01-05 14:00:00",
    "interviewLocation": "北京市朝阳区XX路XX号",
    "interviewType": "现场面试",
    "interviewStatus": 1,
    "interviewStatusName": "已安排",
    "contactPerson": "HR张",
    "contactPhone": "13800138001",
    "remark": "请携带简历和身份证",
    "createTime": "2025-01-02 10:00:00"
  }
}
```

---

### 6.5 面试评价查看

**接口地址**: `/student/interview/evaluation/{interviewId}`  
**请求方式**: `GET`  
**接口说明**: 查看企业对自己的面试评价（路径参数）

#### 路径参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| interviewId | Long | 是 | 面试ID |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": {
    "id": 1,
    "interviewId": 1,
    "jobId": 1,
    "jobName": "Java开发工程师",
    "enterpriseId": 1,
    "enterpriseName": "XX科技有限公司",
    "professionalAbility": 8,
    "communicationAbility": 7,
    "teamCooperation": 8,
    "overallScore": 7.7,
    "evaluationContent": "评价内容...",
    "strengths": "技术基础扎实，学习能力强",
    "weaknesses": "项目经验较少",
    "recommendation": "建议录用",
    "createTime": "2025-01-06 10:00:00"
  }
}
```

---

## 七、线上咨询接口

### 7.1 咨询列表查询

**接口地址**: `/student/consultation/list`  
**请求方式**: `GET`  
**接口说明**: 查看与企业HR的咨询记录

#### 请求参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 是 | 页码 |
| pageSize | Integer | 是 | 每页数量 |
| status | Integer | 否 | 状态：0-待回复，1-已回复，2-已关闭 |
| enterpriseId | Long | 否 | 企业ID |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "total": 10,
  "rows": [
    {
      "id": 1,
      "enterpriseId": 1,
      "enterpriseName": "XX科技有限公司",
      "jobId": 1,
      "jobName": "Java开发工程师",
      "consultationType": "职位咨询",
      "title": "关于职位要求",
      "content": "咨询内容...",
      "replyContent": "回复内容...",
      "replyTime": "2025-01-02 10:00:00",
      "status": 1,
      "statusName": "已回复",
      "createTime": "2025-01-01 10:00:00"
    }
  ]
}
```

---

### 7.2 发起咨询

**接口地址**: `/student/consultation/add`  
**请求方式**: `POST`  
**接口说明**: 通过系统与企业HR进行在线咨询（通过 Feign 调用企业服务）

#### 请求参数

```json
{
  "enterpriseId": 1,
  "jobId": 1,
  "consultationType": "职位咨询",
  "title": "关于职位要求",
  "content": "咨询内容..."
}
```

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| enterpriseId | Long | 是 | 企业ID |
| jobId | Long | 否 | 职位ID（可选，针对某个职位的咨询） |
| consultationType | String | 否 | 咨询类型：职位咨询、面试咨询、其他 |
| title | String | 否 | 咨询标题 |
| content | String | 是 | 咨询内容 |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": 1
}
```

| 字段名 | 类型 | 说明 |
|--------|------|------|
| data | Long | 咨询ID |

---

## 八、投诉与反馈接口

### 8.1 提交投诉

**接口地址**: `/student/complaint/add`  
**请求方式**: `POST`  
**接口说明**: 对于违反规定的企业进行投诉

#### 请求参数

```json
{
  "enterpriseId": 1,
  "jobId": 1,
  "title": "虚假招聘信息",
  "content": "投诉内容...",
  "attachment": "http://localhost:9000/campus-hiring/attachment/complaint001.pdf"
}
```

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| enterpriseId | Long | 是 | 被投诉企业ID |
| jobId | Long | 否 | 关联职位ID（可选） |
| title | String | 是 | 投诉标题 |
| content | String | 是 | 投诉内容 |
| attachment | String | 否 | 附件URL（可选） |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": 1
}
```

| 字段名 | 类型 | 说明 |
|--------|------|------|
| data | Long | 投诉ID |

---

### 8.2 投诉查询

**接口地址**: `/student/complaint/list`  
**请求方式**: `GET`  
**接口说明**: 查看投诉处理状态

#### 请求参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 是 | 页码 |
| pageSize | Integer | 是 | 每页数量 |
| handleStatus | Integer | 否 | 处理状态：0-待处理，1-处理中，2-已处理，3-已关闭 |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "total": 5,
  "rows": [
    {
      "id": 1,
      "enterpriseId": 1,
      "enterpriseName": "XX科技有限公司",
      "jobId": 1,
      "jobName": "Java开发工程师",
      "title": "虚假招聘信息",
      "content": "投诉内容...",
      "handleStatus": 2,
      "handleStatusName": "已处理",
      "handleResult": "已核实，已对相关企业进行警告处理",
      "handleTime": "2025-01-03 10:00:00",
      "createTime": "2025-01-01 10:00:00"
    }
  ]
}
```

---

## 九、个人中心接口

### 9.1 申请记录查询

**接口地址**: `/student/personal/application/list`  
**请求方式**: `GET`  
**接口说明**: 查看学生职位申请记录，支持按申请状态筛选（来源表：`job_application`）

#### 请求参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 是 | 页码 |
| pageSize | Integer | 是 | 每页数量 |
| status | Integer | 否 | 申请状态：1-已通过，2-已拒绝，3-已取消；不传为全部 |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "total": 20,
  "rows": [
    {
      "id": 1,
      "jobId": 1,
      "jobName": "Java开发工程师",
      "enterpriseId": 1,
      "enterpriseName": "XX科技有限公司",
      "applicationStatus": 1,
      "applicationStatusName": "已通过",
      "applicationTime": "2025-01-01 10:00:00"
    }
  ]
}
```

#### 状态枚举说明

| 字段 | 值 | 说明 |
|------|----|------|
| applicationStatus | 1 | 已通过 |
| applicationStatus | 2 | 已拒绝 |
| applicationStatus | 3 | 已取消 |

---

### 9.2 面试记录查询

**接口地址**: `/student/personal/interview/list`  
**请求方式**: `GET`  
**接口说明**: 查看面试历史记录

#### 请求参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 是 | 页码 |
| pageSize | Integer | 是 | 每页数量 |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "total": 10,
  "rows": [
    {
      "id": 1,
      "jobId": 1,
      "jobName": "Java开发工程师",
      "enterpriseId": 1,
      "enterpriseName": "XX科技有限公司",
      "interviewTime": "2025-01-05 14:00:00",
      "interviewLocation": "北京市朝阳区XX路XX号",
      "interviewStatus": 2,
      "interviewStatusName": "已完成",
      "overallScore": 7.7,
      "createTime": "2025-01-02 10:00:00"
    }
  ]
}
```

---

## 十、数据统计接口

### 10.1 统计概览

**接口地址**: `/student/statistics/overview`  
**请求方式**: `GET`  
**接口说明**: 获取学生的统计数据概览，包括投递数、面试数、收藏数、简历数等

#### 请求参数

无（从JWT Token中获取学生ID）

#### 请求头

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| Authorization | String | 是 | JWT Token，格式：`Bearer {token}` |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": {
    "totalApplications": 15,
    "pendingApplications": 5,
    "passedApplications": 8,
    "totalInterviews": 10,
    "scheduledInterviews": 3,
    "completedInterviews": 6,
    "totalFavorites": 20,
    "totalResumes": 3,
    "defaultResumeId": 1,
    "resumeCompleteness": 80
  }
}
```

#### 响应字段说明

| 字段名 | 类型 | 说明 |
|--------|------|------|
| totalApplications | Integer | 总投递数 |
| pendingApplications | Integer | 待处理投递数（状态为0） |
| passedApplications | Integer | 已通过投递数（状态为1） |
| totalInterviews | Integer | 总面试数 |
| scheduledInterviews | Integer | 已安排面试数（状态为1） |
| completedInterviews | Integer | 已完成面试数（状态为2） |
| totalFavorites | Integer | 总收藏数 |
| totalResumes | Integer | 总简历数 |
| defaultResumeId | Long | 默认简历ID（可为null） |
| resumeCompleteness | Integer | 简历完整度（0-100的百分比） |

---

## 十、即时聊天接口

### 10.1 创建/获取会话

**接口地址**: `/student/chat/session/create`  
**请求方式**: `POST`  
**接口说明**: 从职位页面发起咨询时，创建或获取会话。如果该学生与该企业针对该职位已存在会话，则返回现有会话；否则创建新会话。

#### 请求参数

```json
{
  "enterpriseId": 1,
  "jobId": 1
}
```

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| enterpriseId | Long | 是 | 企业ID |
| jobId | Long | 是 | 职位ID |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": {
    "sessionId": 1,
    "enterpriseId": 1,
    "enterpriseName": "XX科技有限公司",
    "enterpriseLogo": "http://localhost:9000/campus-hiring/logo/enterprise001.jpg",
    "jobId": 1,
    "jobName": "Java开发工程师",
    "unreadCount": 0,
    "lastMessageTime": null,
    "lastMessageContent": null,
    "status": 1,
    "createTime": "2025-01-01 10:00:00"
  }
}
```

| 字段名 | 类型 | 说明 |
|--------|------|------|
| sessionId | Long | 会话ID |
| enterpriseId | Long | 企业ID |
| enterpriseName | String | 企业名称 |
| enterpriseLogo | String | 企业Logo URL |
| jobId | Long | 职位ID |
| jobName | String | 职位名称 |
| unreadCount | Integer | 学生未读消息数 |
| lastMessageTime | String | 最后消息时间 |
| lastMessageContent | String | 最后消息内容（最多50字符） |
| status | Integer | 状态：0-已关闭，1-正常 |
| createTime | String | 创建时间 |

---

### 10.2 会话列表查询

**接口地址**: `/student/chat/session/list`  
**请求方式**: `GET`  
**接口说明**: 查询学生的会话列表，按最后消息时间倒序排列，支持搜索

#### 请求参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 是 | 页码 |
| pageSize | Integer | 是 | 每页数量 |
| keyword | String | 否 | 搜索关键词（企业名称、职位名称） |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "total": 10,
  "rows": [
    {
      "sessionId": 1,
      "enterpriseId": 1,
      "enterpriseName": "XX科技有限公司",
      "enterpriseLogo": "http://localhost:9000/campus-hiring/logo/enterprise001.jpg",
      "jobId": 1,
      "jobName": "Java开发工程师",
      "unreadCount": 3,
      "lastMessageTime": "2025-01-02 10:00:00",
      "lastMessageContent": "您好，我想了解一下这个职位...",
      "status": 1,
      "createTime": "2025-01-01 10:00:00"
    }
  ]
}
```

| 字段名 | 类型 | 说明 |
|--------|------|------|
| sessionId | Long | 会话ID |
| enterpriseId | Long | 企业ID |
| enterpriseName | String | 企业名称 |
| enterpriseLogo | String | 企业Logo URL |
| jobId | Long | 职位ID |
| jobName | String | 职位名称 |
| unreadCount | Integer | 学生未读消息数 |
| lastMessageTime | String | 最后消息时间 |
| lastMessageContent | String | 最后消息内容（最多50字符，超出显示省略号） |
| status | Integer | 状态：0-已关闭，1-正常 |
| createTime | String | 创建时间 |

---

### 10.3 会话详情查询

**接口地址**: `/student/chat/session/detail`  
**请求方式**: `GET`  
**接口说明**: 查询会话详细信息，包括对方信息、在线状态等

#### 请求参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| sessionId | Long | 是 | 会话ID |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": {
    "sessionId": 1,
    "enterpriseId": 1,
    "enterpriseName": "XX科技有限公司",
    "enterpriseLogo": "http://localhost:9000/campus-hiring/logo/enterprise001.jpg",
    "enterpriseDescription": "公司简介...",
    "jobId": 1,
    "jobName": "Java开发工程师",
    "unreadCount": 3,
    "lastMessageTime": "2025-01-02 10:00:00",
    "lastMessageContent": "您好，我想了解一下这个职位...",
    "status": 1,
    "isOnline": false,
    "createTime": "2025-01-01 10:00:00"
  }
}
```

| 字段名 | 类型 | 说明 |
|--------|------|------|
| sessionId | Long | 会话ID |
| enterpriseId | Long | 企业ID |
| enterpriseName | String | 企业名称 |
| enterpriseLogo | String | 企业Logo URL |
| enterpriseDescription | String | 企业简介 |
| jobId | Long | 职位ID |
| jobName | String | 职位名称 |
| unreadCount | Integer | 学生未读消息数 |
| lastMessageTime | String | 最后消息时间 |
| lastMessageContent | String | 最后消息内容 |
| status | Integer | 状态：0-已关闭，1-正常 |
| isOnline | Boolean | 企业是否在线 |
| createTime | String | 创建时间 |

---

### 10.4 发送消息

**接口地址**: `/student/chat/message/send`  
**请求方式**: `POST`  
**接口说明**: 发送聊天消息，支持文本、图片、文件等类型。消息通过RabbitMQ异步存储。

#### 请求参数

```json
{
  "sessionId": 1,
  "messageType": "text",
  "content": "您好，我想了解一下这个职位",
  "relatedJobId": null
}
```

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| sessionId | Long | 是 | 会话ID |
| messageType | String | 是 | 消息类型：text-文本，image-图片，file-文件，resume-简历 |
| content | String | 是 | 消息内容（文本消息直接传文本，图片/文件传URL，简历传简历ID或JSON） |
| relatedJobId | Long | 否 | 关联职位ID（当message_type为job时） |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": {
    "messageId": 1,
    "sessionId": 1,
    "senderId": 1,
    "senderType": 1,
    "receiverId": 2,
    "receiverType": 2,
    "messageType": "text",
    "content": "您好，我想了解一下这个职位",
    "relatedJobId": null,
    "isRead": true,
    "createTime": "2025-01-02 10:00:00"
  }
}
```

| 字段名 | 类型 | 说明 |
|--------|------|------|
| messageId | Long | 消息ID |
| sessionId | Long | 会话ID |
| senderId | Long | 发送者ID |
| senderType | Integer | 发送者类型：1-学生，2-企业 |
| receiverId | Long | 接收者ID |
| receiverType | Integer | 接收者类型：1-学生，2-企业 |
| messageType | String | 消息类型 |
| content | String | 消息内容 |
| relatedJobId | Long | 关联职位ID，可能为null |
| isRead | Boolean | 是否已读（对发送者而言默认true） |
| createTime | String | 创建时间 |
| senderName | String | 发送者名称，可能为空 |
| senderAvatar | String | 发送者头像URL，可能为空 |
| receiverName | String | 接收者名称，可能为空 |
| receiverAvatar | String | 接收者头像URL，可能为空 |

| 字段名 | 类型 | 说明 |
|--------|------|------|
| messageId | Long | 消息ID |
| sessionId | Long | 会话ID |
| messageType | String | 消息类型 |
| content | String | 消息内容 |
| createTime | String | 创建时间 |

#### 消息类型说明

| messageType | 说明 | content格式 |
|------------|------|-------------|
| text | 文本消息 | 直接传文本内容 |
| image | 图片消息 | 图片URL（MinIO地址） |
| file | 文件消息 | JSON格式：`{"url":"文件URL","fileName":"文件名"}` |
| resume | 简历消息 | 简历ID或简历信息JSON |

#### 注意事项

- 消息发送后立即返回，实际存储通过RabbitMQ异步处理
- 如果接收者在线，会通过WebSocket实时推送
- 如果接收者不在线，会增加其未读消息数

---

### 10.5 消息列表查询

**接口地址**: `/student/chat/message/list`  
**请求方式**: `GET`  
**接口说明**: 分页查询会话中的消息列表，按创建时间正序排列（最早的消息在上方）

#### 请求参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| sessionId | Long | 是 | 会话ID |
| pageNum | Integer | 是 | 页码 |
| pageSize | Integer | 是 | 每页数量（建议20条） |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "total": 50,
  "rows": [
    {
      "messageId": 1,
      "sessionId": 1,
      "senderId": 1,
      "senderType": 1,
      "senderName": "张三",
      "senderAvatar": "http://localhost:9000/campus-hiring/avatar/student001.jpg",
      "receiverId": 1,
      "receiverType": 2,
      "messageType": "text",
      "content": "您好，我想了解一下这个职位",
      "relatedJobId": null,
      "isRead": 1,
      "readTime": "2025-01-02 10:05:00",
      "createTime": "2025-01-02 10:00:00"
    }
  ]
}
```

| 字段名 | 类型 | 说明 |
|--------|------|------|
| messageId | Long | 消息ID |
| sessionId | Long | 会话ID |
| senderId | Long | 发送者ID |
| senderType | Integer | 发送者类型：1-学生，2-企业 |
| senderName | String | 发送者名称 |
| senderAvatar | String | 发送者头像 |
| receiverId | Long | 接收者ID |
| receiverType | Integer | 接收者类型：1-学生，2-企业 |
| messageType | String | 消息类型：text-文本，image-图片，file-文件，job-职位，resume-简历 |
| content | String | 消息内容 |
| relatedJobId | Long | 关联职位ID |
| isRead | Integer | 是否已读：0-未读，1-已读 |
| readTime | String | 阅读时间 |
| createTime | String | 创建时间 |

---

### 10.6 标记消息为已读

**接口地址**: `/student/chat/message/mark-read`  
**请求方式**: `POST`  
**接口说明**: 标记会话中的指定未读消息为已读，更新未读消息数

#### 请求参数

```json
{
  "sessionId": 1,
  "messageIds": [1, 2, 3]
}
```

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| sessionId | Long | 是 | 会话ID |
| messageIds | List<Long> | 否 | 消息ID列表（不传则标记该会话所有未读消息为已读） |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": null
}
```

#### 注意事项

- 标记已读后，会更新 `chat_message` 表的 `is_read` 和 `read_time` 字段
- 同时更新 `chat_session` 表的 `student_unread_count`（减对应数量）

---

### 10.6.1 标记会话所有消息为已读（路径参数方式）

**接口地址**: `/student/chat/message/read/{sessionId}`  
**请求方式**: `PUT`  
**接口说明**: 标记指定会话的所有未读消息为已读，更新未读消息数

#### 路径参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| sessionId | Long | 是 | 会话ID |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": null
}
```

#### 注意事项

- 标记已读后，会更新 `chat_message` 表的 `is_read` 和 `read_time` 字段
- 同时更新 `chat_session` 表的 `student_unread_count`（减对应数量）
- 此接口会标记该会话中所有未读消息为已读

---

### 10.7 发送简历（在聊天中）

**接口地址**: `/student/chat/message/send-resume`  
**请求方式**: `POST`  
**接口说明**: 在聊天中发送简历，创建一条简历类型的消息

#### 请求参数

```json
{
  "sessionId": 1,
  "resumeId": 1
}
```

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| sessionId | Long | 是 | 会话ID |
| resumeId | Long | 是 | 简历ID |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": {
    "messageId": 1,
    "sessionId": 1,
    "senderId": 1,
    "senderType": 1,
    "receiverId": 2,
    "receiverType": 2,
    "messageType": "resume",
    "content": "{\"resumeId\":1,\"resumeName\":\"我的简历\"}",
    "relatedJobId": null,
    "isRead": true,
    "createTime": "2025-01-02 10:00:00"
  }
}
```

> 字段说明同“发送消息”接口。

---

### 10.8 查询未读消息总数

**接口地址**: `/student/chat/message/unread-count`  
**请求方式**: `GET`  
**接口说明**: 查询学生的总未读消息数（用于导航栏角标显示）

#### 请求参数

无（从JWT Token获取学生ID）

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": {
    "totalUnreadCount": 10
  }
}
```

| 字段名 | 类型 | 说明 |
|--------|------|------|
| totalUnreadCount | Integer | 总未读消息数 |

---

### 10.9 提交反馈

**接口地址**: `/student/feedback/add`  
**请求方式**: `POST`  
**接口说明**: 提交系统反馈和建议

#### 请求参数

```json
{
  "feedbackType": "功能建议",
  "title": "建议增加职位筛选功能",
  "content": "反馈内容...",
  "contactInfo": "13800138000"
}
```

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| feedbackType | String | 是 | 反馈类型：功能建议、问题反馈、其他 |
| title | String | 是 | 反馈标题 |
| content | String | 是 | 反馈内容 |
| contactInfo | String | 否 | 联系方式（用于管理员回复反馈） |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": 1
}
```

| 字段名 | 类型 | 说明 |
|--------|------|------|
| data | Long | 反馈ID |

---

## 十一、接口调用示例

### 11.1 学生登录示例

```bash
curl -X POST "http://localhost:8080/student/student/login" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "student001",
    "password": "123456"
  }'
```

### 11.2 查询职位列表示例

```bash
curl -X GET "http://localhost:8080/student/job/list?pageNum=1&pageSize=10&workLocation=北京" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

### 11.3 投递简历示例

```bash
curl -X POST "http://localhost:8080/student/resume/delivery" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
  -H "Content-Type: application/json" \
  -d '{
    "jobId": 1,
    "resumeId": 1
  }'
```

---

## 十二、注意事项

1. **身份认证**: 除注册、登录、发送验证码接口外，所有接口都需要在请求头中携带 JWT Token
2. **权限控制**: 学生服务接口仅学生身份可访问，网关会进行权限验证
3. **分页查询**: 所有列表查询接口都支持分页，必须传递 `pageNum` 和 `pageSize` 参数
4. **服务间调用**: 职位详情查询、发起咨询等接口通过 Feign 调用企业服务
5. **消息队列**: 简历投递、面试申请等操作会发送消息到 RabbitMQ，实现异步通知
6. **即时聊天**: 聊天消息通过RabbitMQ异步存储，通过WebSocket实时推送
7. **时间格式**: 所有时间字段统一使用 `yyyy-MM-dd HH:mm:ss` 格式
8. **文件URL**: 文件相关字段返回的是 MinIO 存储的文件访问URL

---

**文档维护**: 开发团队  
**联系方式**: 如有问题请联系开发团队


