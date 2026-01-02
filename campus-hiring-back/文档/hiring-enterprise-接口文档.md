# 企业服务接口文档

## 文档说明

- **服务名称**: hiring-enterprise
- **服务端口**: 8083
- **网关路径前缀**: `/enterprise`
- **基础URL**: `http://localhost:8080/enterprise` (通过网关访问)
- **文档版本**: v1.0
- **最后更新**: 2025-11-10

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
| 3202 | 职位已存在 |
| 3203 | 职位已过期 |
| 3204 | 职位未发布 |
| 3301 | 简历不存在 |

---

## 二、用户管理接口

### 2.1 企业注册

**接口地址**: `/enterprise/enterprise/register`  
**请求方式**: `POST`  
**接口说明**: 企业用户注册

#### 请求参数

```json
{
  "username": "enterprise001",
  "password": "123456",
  "enterpriseName": "XX科技有限公司"
}
```

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| username | String | 是 | 用户名（唯一） |
| password | String | 是 | 密码 |
| enterpriseName | String | 是 | 企业名称 |

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

**接口地址**: `/enterprise/enterprise/sendCode`  
**请求方式**: `POST`  
**接口说明**: 发送手机验证码

#### 请求参数

```json
{
  "phone": "13800138001"
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

### 2.3 企业登录

**接口地址**: `/enterprise/enterprise/login`  
**请求方式**: `POST`  
**接口说明**: 企业登录，生成 JWT Token。只需要用户名和密码即可登录。

#### 请求参数

```json
{
  "username": "enterprise001",
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

### 2.4 获取企业信息

**接口地址**: `/enterprise/enterprise/info`  
**请求方式**: `GET`  
**接口说明**: 获取当前登录企业信息

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
    "enterpriseName": "XX科技有限公司",
    "logo": "http://localhost:9000/campus-hiring/logo/enterprise001.jpg"
  }
}
```

---

### 2.5 企业详情查询

**接口地址**: `/enterprise/enterprise/detail`  
**请求方式**: `GET`  
**接口说明**: 查看企业详细信息

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
    "username": "enterprise001",
    "enterpriseName": "XX科技有限公司",
    "legalPerson": "李四",
    "phone": "13800138001",
    "email": "enterprise001@example.com",
    "logo": "http://localhost:9000/campus-hiring/logo/enterprise001.jpg",
    "address": "北京市朝阳区XX路XX号",
    "industry": "互联网",
    "scale": "201-500人",
    "description": "公司简介...",
    "website": "https://www.example.com",
    "certificationStatus": 1,
    "certificationStatusName": "已认证",
    "certificationFile": "http://localhost:9000/campus-hiring/certificate/enterprise001.pdf",
    "status": 1,
    "statusName": "正常",
    "createTime": "2025-01-01 10:00:00"
  }
}
```

#### 关联查询说明

- 查询企业详情时，从 `enterprise` 表获取企业基本信息
- 企业认证信息从 `enterprise` 表的 `certification_status` 和 `certification_file` 字段获取

#### 枚举值说明

| 字段 | 值 | 说明 |
|------|-----|------|
| status | 0 | 已拉黑 |
| status | 1 | 正常 |
| status | 2 | 已禁用 |
| status | 3 | 待审核 |
| certificationStatus | 0 | 未认证 |
| certificationStatus | 1 | 已认证 |
| certificationStatus | 2 | 认证中 |
| certificationStatus | 3 | 认证失败 |

---

### 2.6 更新企业信息

**接口地址**: `/enterprise/enterprise/edit`  
**请求方式**: `PUT`  
**接口说明**: 管理企业招聘信息及企业资料

#### 请求参数

```json
{
  "enterpriseName": "XX科技有限公司",
  "legalPerson": "李四",
  "phone": "13800138001",
  "email": "enterprise001@example.com",
  "address": "北京市朝阳区XX路XX号",
  "industry": "互联网",
  "scale": "201-500人",
  "description": "公司简介...",
  "website": "https://www.example.com"
}
```

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| enterpriseName | String | 否 | 企业名称 |
| legalPerson | String | 否 | 法人代表 |
| phone | String | 否 | 联系电话 |
| email | String | 否 | 邮箱 |
| address | String | 否 | 企业地址 |
| industry | String | 否 | 所属行业 |
| scale | String | 否 | 企业规模：1-50人、51-200人、201-500人、500人以上 |
| description | String | 否 | 企业简介 |
| website | String | 否 | 企业官网 |

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

**接口地址**: `/enterprise/enterprise/changePassword`  
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

### 2.8 更新企业Logo

**接口地址**: `/enterprise/enterprise/logo/update`  
**请求方式**: `PUT`  
**接口说明**: 更新企业Logo

#### 请求参数

```json
{
  "logo": "http://localhost:9000/campus-hiring/logo/enterprise001.jpg"
}
```

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| logo | String | 是 | Logo URL |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": null
}
```

---

### 2.9 企业文件上传

**接口地址**: `/enterprise/enterprise/upload`  
**请求方式**: `POST`  
**接口说明**: 上传企业相关文件（如 Logo、认证材料等），返回 MinIO 文件访问地址

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
    "fileName": "enterprise/1/1731225476123_business_license.pdf",
    "url": "http://localhost:9000/campus-hiring/enterprise/1/1731225476123_business_license.pdf"
  }
}
```

| 字段名 | 类型 | 说明 |
|--------|------|------|
| fileName | String | MinIO 中的对象名称 |
| url | String | 文件访问地址 |

#### 注意事项

- 系统会自动根据企业ID和时间戳生成文件存储路径，确保不同企业文件隔离
- 上传成功后请在相关业务接口中使用返回的 `url` 字段
- 文件大小及类型限制遵循 MinIO 服务端配置

---

### 2.10 企业认证申请

**接口地址**: `/enterprise/enterprise/certification/apply`  
**请求方式**: `POST`  
**接口说明**: 提交企业认证申请

#### 请求参数

```json
{
  "certificationFile": "http://localhost:9000/campus-hiring/certificate/enterprise001.pdf"
}
```

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| certificationFile | String | 是 | 认证文件URL（营业执照等） |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": null
}
```

---

### 2.11 退出登录

**接口地址**: `/enterprise/enterprise/logout`  
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

## 三、职位管理接口

### 3.1 职位列表查询

**接口地址**: `/enterprise/job/list`  
**请求方式**: `GET`  
**接口说明**: 查看企业发布的所有职位

#### 请求参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 是 | 页码 |
| pageSize | Integer | 是 | 每页数量 |
| status | Integer | 否 | 状态：0-待审核，1-已通过，2-已拒绝，3-已下线 |
| jobName | String | 否 | 职位名称（模糊查询） |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "total": 20,
  "rows": [
    {
      "id": 1,
      "jobName": "Java开发工程师",
      "workLocation": "北京",
      "salaryMin": 8000.00,
      "salaryMax": 15000.00,
      "salaryType": "月薪",
      "recruitCount": 5,
      "viewCount": 100,
      "applyCount": 20,
      "status": 1,
      "statusName": "已通过",
      "publishTime": "2025-01-01 10:00:00",
      "createTime": "2025-01-01 09:00:00"
    }
  ]
}
```

---

### 3.2 职位详情查询

**接口地址**: `/enterprise/job/detail`  
**请求方式**: `GET`  
**接口说明**: 查看职位详细信息

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
    "viewCount": 100,
    "applyCount": 20,
    "status": 1,
    "statusName": "已通过",
    "auditRemark": "审核通过",
    "publishTime": "2025-01-01 10:00:00",
    "expireTime": "2025-06-01 10:00:00",
    "createTime": "2025-01-01 09:00:00"
  }
}
```

---

### 3.3 发布职位

**接口地址**: `/enterprise/job/add`  
**请求方式**: `POST`  
**接口说明**: 发布职位信息（状态：待审核）。如果传入了 `campusIds` 数组，需要将职位与校园的关联关系插入到 `campus_job` 表中

#### 请求参数

```json
{
  "categoryId": 1,
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
  "expireTime": "2025-06-01 10:00:00",
  "campusIds": [1, 2, 3]
}
```

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| categoryId | Long | 是 | 职位类别ID |
| jobName | String | 是 | 职位名称 |
| jobDescription | String | 是 | 职位描述 |
| requiredMajor | String | 否 | 要求专业 |
| requiredSkills | String | 否 | 要求技能（多个用逗号分隔） |
| requiredEducation | String | 否 | 要求学历：专科、本科、硕士、博士 |
| requiredExperience | Integer | 否 | 要求工作经验（月） |
| workLocation | String | 是 | 工作地点 |
| salaryMin | BigDecimal | 否 | 最低薪资 |
| salaryMax | BigDecimal | 否 | 最高薪资 |
| salaryType | String | 否 | 薪资类型：面议、月薪、年薪 |
| jobType | String | 否 | 工作类型：全职、兼职、实习 |
| recruitCount | Integer | 否 | 招聘人数 |
| expireTime | String | 否 | 过期时间（格式：yyyy-MM-dd HH:mm:ss） |
| campusIds | List<Long> | 否 | 校园ID列表（用于校园管理） |

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
| data | Long | 职位ID |

#### 数组参数处理说明

- `campusIds` 参数为数组类型，表示职位要发布到的校园ID列表
- 如果传入了 `campusIds`，需要执行以下操作：
  1. 先插入职位记录到 `job` 表
  2. 然后遍历 `campusIds` 数组，为每个校园ID插入一条记录到 `campus_job` 表
  3. `campus_job` 表的字段：`campus_id`（校园ID）、`job_id`（职位ID）、`create_by`（从JWT Token获取）、`create_time`（当前时间）

---

### 3.4 编辑职位

**接口地址**: `/enterprise/job/edit`  
**请求方式**: `PUT`  
**接口说明**: 编辑职位信息

#### 请求参数

```json
{
  "jobId": 1,
  "categoryId": 1,
  "jobName": "Java开发工程师（更新）",
  "jobDescription": "职位描述（更新）...",
  "requiredMajor": "计算机科学与技术",
  "requiredSkills": "Java,Spring,MySQL,Redis",
  "requiredEducation": "本科",
  "requiredExperience": 6,
  "workLocation": "北京",
  "salaryMin": 10000.00,
  "salaryMax": 18000.00,
  "salaryType": "月薪",
  "jobType": "全职",
  "recruitCount": 8,
  "expireTime": "2025-06-01 10:00:00",
  "campusIds": [1, 2, 3, 4]
}
```

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| jobId | Long | 是 | 职位ID |
| categoryId | Long | 否 | 职位类别ID |
| jobName | String | 否 | 职位名称 |
| jobDescription | String | 否 | 职位描述 |
| requiredMajor | String | 否 | 要求专业 |
| requiredSkills | String | 否 | 要求技能（多个用逗号分隔） |
| requiredEducation | String | 否 | 要求学历 |
| requiredExperience | Integer | 否 | 要求工作经验（月） |
| workLocation | String | 否 | 工作地点 |
| salaryMin | BigDecimal | 否 | 最低薪资 |
| salaryMax | BigDecimal | 否 | 最高薪资 |
| salaryType | String | 否 | 薪资类型 |
| jobType | String | 否 | 工作类型 |
| recruitCount | Integer | 否 | 招聘人数 |
| expireTime | String | 否 | 过期时间 |
| campusIds | List<Long> | 否 | 校园ID列表 |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": null
}
```

---

### 3.5 删除职位

**接口地址**: `/enterprise/job/delete`  
**请求方式**: `DELETE`  
**接口说明**: 删除职位信息

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

### 3.6 下线职位

**接口地址**: `/enterprise/job/offline`  
**请求方式**: `PUT`  
**接口说明**: 下线职位（状态改为已下线）

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

## 四、校园管理接口

### 4.1 校园列表查询

**接口地址**: `/enterprise/campus/list`  
**请求方式**: `GET`  
**接口说明**: 查看合作的校园列表

#### 请求参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 是 | 页码 |
| pageSize | Integer | 是 | 每页数量 |
| campusName | String | 否 | 校园名称（模糊查询） |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "total": 10,
  "rows": [
    {
      "id": 1,
      "campusName": "XX大学",
      "campusCode": "CAMPUS001",
      "address": "北京市XX区XX路XX号",
      "contactPerson": "张老师",
      "contactPhone": "010-12345678",
      "status": 1,
      "statusName": "启用"
    }
  ]
}
```

---

### 4.2 职位校园关联查询

**接口地址**: `/enterprise/campus/job/list`  
**请求方式**: `GET`  
**接口说明**: 根据学生来自的不同校园进行管理，查看职位关联的校园

#### 请求参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| jobId | Long | 是 | 职位ID |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": [
    {
      "id": 1,
      "campusId": 1,
      "campusName": "XX大学",
      "jobId": 1,
      "jobName": "Java开发工程师"
    }
  ]
}
```

---

## 五、人才推荐接口

### 5.1 人才推荐

**接口地址**: `/enterprise/talent/recommend`  
**请求方式**: `GET`  
**接口说明**: 根据企业招聘条件（专业、技能、经验等）查询匹配的学生（同步查询数据库，通过 Feign 调用学生服务）

#### 请求参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 是 | 页码 |
| pageSize | Integer | 是 | 每页数量 |
| jobId | Long | 否 | 职位ID（根据职位要求推荐） |
| requiredMajor | String | 否 | 要求专业 |
| requiredSkills | String | 否 | 要求技能（多个用逗号分隔） |
| requiredEducation | String | 否 | 要求学历 |
| requiredExperience | Integer | 否 | 要求工作经验（月） |
| campusId | Long | 否 | 校园ID（根据校园筛选） |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "total": 20,
  "rows": [
    {
      "id": 1,
      "nickName": "张三",
      "realName": "张三",
      "headImage": "http://localhost:9000/campus-hiring/avatar/student001.jpg",
      "campusId": 1,
      "campusName": "XX大学",
      "major": "计算机科学与技术",
      "education": "本科",
      "grade": "2024届",
      "skills": "Java,Spring,MySQL",
      "experience": 6,
      "expectedSalary": 8000.00,
      "expectedLocation": "北京",
      "matchScore": 85,
      "matchReason": "专业匹配、技能匹配、经验匹配",
      "createTime": "2025-01-01 10:00:00"
    }
  ]
}
```

---

## 六、简历管理接口

### 6.1 简历列表查询

**接口地址**: `/enterprise/resume/list`  
**请求方式**: `GET`  
**接口说明**: 查看学生用户投递的简历（通过 RabbitMQ 消息接收），关联查询学生、职位、简历信息

#### 请求参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 是 | 页码 |
| pageSize | Integer | 是 | 每页数量 |
| jobId | Long | 否 | 职位ID |
| deliveryStatus | Integer | 否 | 投递状态：0-待查看，1-已查看，2-已通过，3-已拒绝 |
| keyword | String | 否 | 搜索关键词（学生姓名、专业、技能） |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "total": 50,
  "rows": [
    {
      "id": 1,
      "studentId": 1,
      "studentName": "张三",
      "studentPhone": "13800138000",
      "jobId": 1,
      "jobName": "Java开发工程师",
      "resumeId": 1,
      "resumeName": "我的简历",
      "resumeFile": "http://localhost:9000/campus-hiring/resume/resume001.pdf",
      "deliveryStatus": 0,
      "deliveryStatusName": "待查看",
      "deliveryTime": "2025-01-01 10:00:00"
    }
  ]
}
```

#### 关联查询说明

- 关联 `student` 表获取学生姓名、手机号等信息
- 关联 `job` 表获取职位名称等信息
- 关联 `resume` 表获取简历名称、简历文件等信息

#### 枚举值说明

| 字段 | 值 | 说明 |
|------|-----|------|
| deliveryStatus | 0 | 待查看 |
| deliveryStatus | 1 | 已查看 |
| deliveryStatus | 2 | 已通过 |
| deliveryStatus | 3 | 已拒绝 |

---

### 6.2 简历详情查询

**接口地址**: `/enterprise/resume/detail`  
**请求方式**: `GET`  
**接口说明**: 查看学生简历详细信息（通过 Feign 调用学生服务），关联查询学生、简历信息，并更新简历查看状态

#### 请求参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| resumeId | Long | 是 | 简历ID |
| deliveryId | Long | 否 | 投递ID（用于更新查看状态） |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": {
    "id": 1,
    "studentId": 1,
    "studentName": "张三",
    "studentPhone": "13800138000",
    "studentEmail": "student001@example.com",
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
    "deliveryStatus": 1,
    "deliveryStatusName": "已查看",
    "viewTime": "2025-01-02 10:00:00"
  }
}
```

#### 关联查询说明

- 关联 `student` 表获取学生姓名、手机号、邮箱等信息
- 关联 `resume` 表获取简历详细信息
- 如果传入了 `deliveryId` 参数，需要更新 `resume_delivery` 表的 `view_time` 和 `delivery_status`（更新为已查看）

#### 枚举值说明

| 字段 | 值 | 说明 |
|------|-----|------|
| deliveryStatus | 0 | 待查看 |
| deliveryStatus | 1 | 已查看 |
| deliveryStatus | 2 | 已通过 |
| deliveryStatus | 3 | 已拒绝 |

---

### 6.3 更新简历投递状态

**接口地址**: `/enterprise/resume/updateStatus`  
**请求方式**: `PUT`  
**接口说明**: 更新简历投递状态（已查看、已通过、已拒绝）

#### 请求参数

```json
{
  "deliveryId": 1,
  "deliveryStatus": 2,
  "handleRemark": "简历符合要求，安排面试"
}
```

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| deliveryId | Long | 是 | 投递ID |
| deliveryStatus | Integer | 是 | 投递状态：1-已查看，2-已通过，3-已拒绝 |
| handleRemark | String | 否 | 处理备注 |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": null
}
```

---

## 七、面试管理接口

### 7.1 面试申请查看

**接口地址**: `/enterprise/interview/application/list`  
**请求方式**: `GET`  
**接口说明**: 查看学生的面试申请（通过 RabbitMQ 消息接收），关联查询学生、职位信息

#### 请求参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 是 | 页码 |
| pageSize | Integer | 是 | 每页数量 |
| jobId | Long | 否 | 职位ID |
| applicationStatus | Integer | 否 | 申请状态：0-待处理，1-已通过，2-已拒绝，3-已取消 |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "total": 20,
  "rows": [
    {
      "id": 1,
      "studentId": 1,
      "studentName": "张三",
      "studentPhone": "13800138000",
      "jobId": 1,
      "jobName": "Java开发工程师",
      "resumeId": 1,
      "applicationStatus": 0,
      "applicationStatusName": "待处理",
      "applicationTime": "2025-01-01 10:00:00"
    }
  ]
}
```

#### 关联查询说明

- 关联 `student` 表获取学生姓名、手机号等信息
- 关联 `job` 表获取职位名称等信息

#### 枚举值说明

| 字段 | 值 | 说明 |
|------|-----|------|
| applicationStatus | 0 | 待处理 |
| applicationStatus | 1 | 已通过 |
| applicationStatus | 2 | 已拒绝 |
| applicationStatus | 3 | 已取消 |

---

### 7.2 处理面试申请

**接口地址**: `/enterprise/interview/application/handle`  
**请求方式**: `PUT`  
**接口说明**: 处理学生的面试申请（通过/拒绝）

#### 请求参数

```json
{
  "applicationId": 1,
  "applicationStatus": 1,
  "handleRemark": "通过，安排面试"
}
```

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| applicationId | Long | 是 | 申请ID |
| applicationStatus | Integer | 是 | 申请状态：1-已通过，2-已拒绝 |
| handleRemark | String | 否 | 处理备注 |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": null
}
```

---

### 7.3 安排面试

**接口地址**: `/enterprise/interview/arrange`  
**请求方式**: `POST`  
**接口说明**: 在线联系面试时间和地点（通过 Feign 调用学生服务，发送消息到 RabbitMQ）

#### 请求参数

```json
{
  "applicationId": 1,
  "studentId": 1,
  "jobId": 1,
  "interviewTime": "2025-01-05 14:00:00",
  "interviewLocation": "北京市朝阳区XX路XX号",
  "interviewType": "现场面试",
  "contactPerson": "HR张",
  "contactPhone": "13800138001",
  "remark": "请携带简历和身份证"
}
```

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| applicationId | Long | 是 | 申请ID |
| studentId | Long | 是 | 学生ID |
| jobId | Long | 是 | 职位ID |
| interviewTime | String | 是 | 面试时间（格式：yyyy-MM-dd HH:mm:ss） |
| interviewLocation | String | 是 | 面试地点 |
| interviewType | String | 否 | 面试类型：现场面试、视频面试、电话面试 |
| contactPerson | String | 否 | 联系人 |
| contactPhone | String | 否 | 联系电话 |
| remark | String | 否 | 备注 |

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
| data | Long | 面试ID |

---

### 7.4 面试列表查询

**接口地址**: `/enterprise/interview/list`  
**请求方式**: `GET`  
**接口说明**: 查看面试历史记录

#### 请求参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 是 | 页码 |
| pageSize | Integer | 是 | 每页数量 |
| jobId | Long | 否 | 职位ID |
| interviewStatus | Integer | 否 | 面试状态：0-待安排，1-已安排，2-已完成，3-已取消 |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "total": 10,
  "rows": [
    {
      "id": 1,
      "studentId": 1,
      "studentName": "张三",
      "jobId": 1,
      "jobName": "Java开发工程师",
      "interviewTime": "2025-01-05 14:00:00",
      "interviewLocation": "北京市朝阳区XX路XX号",
      "interviewType": "现场面试",
      "interviewStatus": 1,
      "interviewStatusName": "已安排",
      "contactPerson": "HR张",
      "contactPhone": "13800138001",
      "createTime": "2025-01-02 10:00:00"
    }
  ]
}
```

---

### 7.5 面试评价

**接口地址**: `/enterprise/interview/evaluation`  
**请求方式**: `POST`  
**接口说明**: 对已完成面试的学生进行评价（通过 Feign 调用学生服务）

#### 请求参数

```json
{
  "interviewId": 1,
  "studentId": 1,
  "jobId": 1,
  "professionalAbility": 8,
  "communicationAbility": 7,
  "teamCooperation": 8,
  "evaluationContent": "评价内容...",
  "strengths": "技术基础扎实，学习能力强",
  "weaknesses": "项目经验较少",
  "recommendation": "建议录用"
}
```

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| interviewId | Long | 是 | 面试ID |
| studentId | Long | 是 | 学生ID |
| jobId | Long | 是 | 职位ID |
| professionalAbility | Integer | 否 | 专业能力评分（1-10分） |
| communicationAbility | Integer | 否 | 沟通能力评分（1-10分） |
| teamCooperation | Integer | 否 | 团队合作评分（1-10分） |
| evaluationContent | String | 是 | 评价内容 |
| strengths | String | 否 | 优点 |
| weaknesses | String | 否 | 不足 |
| recommendation | String | 否 | 推荐建议 |

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
| data | Long | 评价ID |

---

## 八、线上咨询接口

### 8.1 咨询列表查询

**接口地址**: `/enterprise/consultation/list`  
**请求方式**: `GET`  
**接口说明**: 查看与学生的咨询记录（通过 Feign 调用学生服务）

#### 请求参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 是 | 页码 |
| pageSize | Integer | 是 | 每页数量 |
| status | Integer | 否 | 状态：0-待回复，1-已回复，2-已关闭 |
| studentId | Long | 否 | 学生ID |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "total": 20,
  "rows": [
    {
      "id": 1,
      "studentId": 1,
      "studentName": "张三",
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

### 8.2 回复咨询

**接口地址**: `/enterprise/consultation/reply`  
**请求方式**: `PUT`  
**接口说明**: 通过系统与学生进行在线咨询（通过 Feign 调用学生服务）

#### 请求参数

```json
{
  "consultationId": 1,
  "replyContent": "回复内容..."
}
```

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| consultationId | Long | 是 | 咨询ID |
| replyContent | String | 是 | 回复内容 |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": null
}
```

---

## 九、投诉与反馈接口

### 9.1 提交投诉

**接口地址**: `/enterprise/complaint/add`  
**请求方式**: `POST`  
**接口说明**: 对于违反规定的学生进行投诉

#### 请求参数

```json
{
  "studentId": 1,
  "jobId": 1,
  "title": "虚假简历信息",
  "content": "投诉内容...",
  "attachment": "http://localhost:9000/campus-hiring/attachment/complaint001.pdf"
}
```

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| studentId | Long | 是 | 被投诉学生ID |
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

### 9.2 投诉查询

**接口地址**: `/enterprise/complaint/list`  
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
      "studentId": 1,
      "studentName": "张三",
      "jobId": 1,
      "jobName": "Java开发工程师",
      "title": "虚假简历信息",
      "content": "投诉内容...",
      "handleStatus": 2,
      "handleStatusName": "已处理",
      "handleResult": "已核实，已对相关学生进行警告处理",
      "handleTime": "2025-01-03 10:00:00",
      "createTime": "2025-01-01 10:00:00"
    }
  ]
}
```

---

## 十、数据分析接口

### 10.1 招聘效果统计

**接口地址**: `/enterprise/statistics/recruitment`  
**请求方式**: `GET`  
**接口说明**: 查看职位浏览量、申请量等数据（同步查询数据库）

#### 请求参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| startDate | String | 否 | 开始日期（格式：yyyy-MM-dd） |
| endDate | String | 否 | 结束日期（格式：yyyy-MM-dd） |
| jobId | Long | 否 | 职位ID（不传查询所有职位） |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": {
    "totalJobs": 20,
    "publishedJobs": 18,
    "totalViews": 1000,
    "totalApplications": 200,
    "totalResumes": 150,
    "totalInterviews": 50,
    "averageViewsPerJob": 55.6,
    "averageApplicationsPerJob": 11.1,
    "conversionRate": 20.0,
    "jobStatistics": [
      {
        "jobId": 1,
        "jobName": "Java开发工程师",
        "viewCount": 100,
        "applyCount": 20,
        "resumeCount": 15,
        "interviewCount": 5
      }
    ],
    "trendData": [
      {
        "date": "2025-01-01",
        "viewCount": 50,
        "applyCount": 10
      }
    ]
  }
}
```

---

### 10.2 数据分析

**接口地址**: `/enterprise/statistics/analysis`  
**请求方式**: `GET`  
**接口说明**: 为企业提供招聘效果评估，帮助优化招聘策略（同步查询数据库）

#### 请求参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| startDate | String | 否 | 开始日期（格式：yyyy-MM-dd） |
| endDate | String | 否 | 结束日期（格式：yyyy-MM-dd） |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": {
    "totalJobs": 20,
    "totalViews": 1000,
    "totalApplications": 200,
    "totalInterviews": 50,
    "totalHires": 10,
    "viewToApplicationRate": 20.0,
    "applicationToInterviewRate": 25.0,
    "interviewToHireRate": 20.0,
    "averageTimeToFill": 15,
    "topPerformingJobs": [
      {
        "jobId": 1,
        "jobName": "Java开发工程师",
        "viewCount": 100,
        "applyCount": 20,
        "performanceScore": 85.5
      }
    ],
    "recommendations": [
      "建议优化职位描述，提高吸引力",
      "建议调整薪资范围，提高竞争力"
    ]
  }
}
```

---

## 十一、即时聊天接口

### 11.1 会话列表查询

**接口地址**: `/enterprise/chat/session/list`  
**请求方式**: `GET`  
**接口说明**: 查询企业的会话列表，按最后消息时间倒序排列，支持搜索

#### 请求参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 是 | 页码 |
| pageSize | Integer | 是 | 每页数量 |
| keyword | String | 否 | 搜索关键词（学生姓名、职位名称） |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "total": 10,
  "rows": [
    {
      "sessionId": 1,
      "studentId": 1,
      "studentName": "张三",
      "studentAvatar": "http://localhost:9000/campus-hiring/avatar/student001.jpg",
      "jobId": 1,
      "jobName": "Java开发工程师",
      "unreadCount": 2,
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
| studentId | Long | 学生ID |
| studentName | String | 学生姓名 |
| studentAvatar | String | 学生头像URL |
| jobId | Long | 职位ID |
| jobName | String | 职位名称 |
| unreadCount | Integer | 企业未读消息数 |
| lastMessageTime | String | 最后消息时间 |
| lastMessageContent | String | 最后消息内容（最多50字符，超出显示省略号） |
| status | Integer | 状态：0-已关闭，1-正常 |
| createTime | String | 创建时间 |

---

### 11.2 会话详情查询

**接口地址**: `/enterprise/chat/session/detail`  
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
    "studentId": 1,
    "studentName": "张三",
    "studentAvatar": "http://localhost:9000/campus-hiring/avatar/student001.jpg",
    "studentPhone": "13800138000",
    "jobId": 1,
    "jobName": "Java开发工程师",
    "unreadCount": 2,
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
| studentId | Long | 学生ID |
| studentName | String | 学生姓名 |
| studentAvatar | String | 学生头像URL |
| studentPhone | String | 学生手机号 |
| jobId | Long | 职位ID |
| jobName | String | 职位名称 |
| unreadCount | Integer | 企业未读消息数 |
| lastMessageTime | String | 最后消息时间 |
| lastMessageContent | String | 最后消息内容 |
| status | Integer | 状态：0-已关闭，1-正常 |
| isOnline | Boolean | 学生是否在线 |
| createTime | String | 创建时间 |

---

### 11.3 发送消息

**接口地址**: `/enterprise/chat/message/send`  
**请求方式**: `POST`  
**接口说明**: 发送聊天消息，支持文本、图片、文件、职位信息等类型。消息通过RabbitMQ异步存储。

#### 请求参数

```json
{
  "sessionId": 1,
  "messageType": "text",
  "content": "您好，欢迎咨询",
  "relatedJobId": null
}
```

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| sessionId | Long | 是 | 会话ID |
| messageType | String | 是 | 消息类型：text-文本，image-图片，file-文件，job-职位信息 |
| content | String | 是 | 消息内容（文本消息直接传文本，图片/文件传URL，职位传职位ID或JSON） |
| relatedJobId | Long | 否 | 关联职位ID（当message_type为job时必填） |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": {
    "messageId": 1,
    "sessionId": 1,
    "senderId": 2,
    "senderType": 2,
    "receiverId": 1,
    "receiverType": 1,
    "messageType": "text",
    "content": "您好，欢迎咨询",
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
| job | 职位信息 | 职位ID或职位信息JSON |

#### 注意事项

- 消息发送后立即返回，实际存储通过RabbitMQ异步处理
- 如果接收者在线，会通过WebSocket实时推送
- 如果接收者不在线，会增加其未读消息数

---

### 11.4 消息列表查询

**接口地址**: `/enterprise/chat/message/list`  
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
      "senderType": 2,
      "senderName": "XX科技有限公司",
      "senderAvatar": "http://localhost:9000/campus-hiring/logo/enterprise001.jpg",
      "receiverId": 1,
      "receiverType": 1,
      "messageType": "text",
      "content": "您好，欢迎咨询",
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

### 11.5 标记消息为已读

**接口地址**: `/enterprise/chat/message/mark-read`  
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
- 同时更新 `chat_session` 表的 `enterprise_unread_count`（减对应数量）

---

### 11.5.1 标记会话所有消息为已读（路径参数方式）

**接口地址**: `/enterprise/chat/message/read/{sessionId}`  
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
- 同时更新 `chat_session` 表的 `enterprise_unread_count`（减对应数量）
- 此接口会标记该会话中所有未读消息为已读

---

### 11.6 发送职位信息

**接口地址**: `/enterprise/chat/message/send-job`  
**请求方式**: `POST`  
**接口说明**: 在聊天中发送职位信息卡片，创建一条职位类型的消息

#### 请求参数

```json
{
  "sessionId": 1,
  "jobId": 1
}
```

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| sessionId | Long | 是 | 会话ID |
| jobId | Long | 是 | 职位ID |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": {
    "messageId": 1,
    "sessionId": 1,
    "senderId": 2,
    "senderType": 2,
    "receiverId": 1,
    "receiverType": 1,
    "messageType": "job",
    "content": "{\"jobId\":1,\"jobName\":\"Java开发工程师\"}",
    "relatedJobId": 1,
    "isRead": true,
    "createTime": "2025-01-02 10:00:00"
  }
}
```

> 字段说明同“发送消息”接口。

---

### 11.7 查询未读消息总数

**接口地址**: `/enterprise/chat/message/unread-count`  
**请求方式**: `GET`  
**接口说明**: 查询企业的总未读消息数（用于导航栏角标显示）

#### 请求参数

无（从JWT Token获取企业ID）

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": {
    "totalUnreadCount": 15
  }
}
```

| 字段名 | 类型 | 说明 |
|--------|------|------|
| totalUnreadCount | Integer | 总未读消息数 |

---

### 11.8 提交反馈

**接口地址**: `/enterprise/feedback/add`  
**请求方式**: `POST`  
**接口说明**: 提交系统反馈和建议

#### 请求参数

```json
{
  "feedbackType": "功能建议",
  "title": "建议优化职位发布流程",
  "content": "反馈内容...",
  "contactInfo": "13800138001"
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

## 十二、接口调用示例

### 12.1 企业登录示例

```bash
curl -X POST "http://localhost:8080/enterprise/enterprise/login" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "enterprise001",
    "password": "123456"
  }'
```

### 12.2 发布职位示例

```bash
curl -X POST "http://localhost:8080/enterprise/job/add" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
  -H "Content-Type: application/json" \
  -d '{
    "categoryId": 1,
    "jobName": "Java开发工程师",
    "jobDescription": "职位描述...",
    "workLocation": "北京",
    "salaryMin": 8000.00,
    "salaryMax": 15000.00,
    "salaryType": "月薪",
    "jobType": "全职",
    "recruitCount": 5,
    "campusIds": [1, 2, 3]
  }'
```

### 12.3 安排面试示例

```bash
curl -X POST "http://localhost:8080/enterprise/interview/arrange" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
  -H "Content-Type: application/json" \
  -d '{
    "applicationId": 1,
    "studentId": 1,
    "jobId": 1,
    "interviewTime": "2025-01-05 14:00:00",
    "interviewLocation": "北京市朝阳区XX路XX号",
    "interviewType": "现场面试",
    "contactPerson": "HR张",
    "contactPhone": "13800138001"
  }'
```

---

## 十三、注意事项

1. **身份认证**: 除注册、登录、发送验证码接口外，所有接口都需要在请求头中携带 JWT Token
2. **权限控制**: 企业服务接口仅企业身份可访问，网关会进行权限验证
3. **分页查询**: 所有列表查询接口都支持分页，必须传递 `pageNum` 和 `pageSize` 参数
4. **服务间调用**: 人才推荐、简历详情查询、回复咨询等接口通过 Feign 调用学生服务
5. **消息队列**: 简历投递、面试申请等操作通过 RabbitMQ 消息接收，实现异步处理
6. **即时聊天**: 聊天消息通过RabbitMQ异步存储，通过WebSocket实时推送
7. **职位审核**: 发布的职位需要管理员审核，审核通过后才能上线
8. **企业认证**: 企业需要提交认证申请，管理员审核通过后才能发布职位
9. **时间格式**: 所有时间字段统一使用 `yyyy-MM-dd HH:mm:ss` 格式
10. **文件URL**: 文件相关字段返回的是 MinIO 存储的文件访问URL
11. **数据同步**: 统计数据接口采用同步查询数据库方式，不依赖缓存

---

**文档维护**: 开发团队  
**联系方式**: 如有问题请联系开发团队


