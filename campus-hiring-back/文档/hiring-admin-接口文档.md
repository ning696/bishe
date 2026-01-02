# 管理员服务接口文档

## 文档说明

- **服务名称**: hiring-admin
- **服务端口**: 8081
- **网关路径前缀**: `/admin`
- **基础URL**: `http://localhost:8080/admin` (通过网关访问)
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
| 3102 | 用户不存在 |
| 3103 | 账号或密码错误 |
| 3104 | 您已被列入黑名单, 请联系管理员 |

---

## 二、管理员管理接口

### 2.1 管理员登录

**接口地址**: `/admin/admin/login`  
**请求方式**: `POST`  
**接口说明**: 管理员登录，生成 JWT Token。只需要用户名和密码即可登录。

#### 请求参数

```json
{
  "username": "admin",
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

### 2.2 获取管理员信息

**接口地址**: `/admin/admin/info`  
**请求方式**: `GET`  
**接口说明**: 获取当前登录管理员信息

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
    "username": "admin",
    "nickName": "管理员",
    "realName": "管理员",
    "phone": "13800138000",
    "email": "admin@example.com",
    "headImage": "http://localhost:9000/campus-hiring/avatar/admin.jpg",
    "status": 1,
    "statusName": "正常"
  }
}
```

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | Long | 管理员ID |
| username | String | 用户名 |
| nickName | String | 昵称 |
| realName | String | 真实姓名 |
| phone | String | 手机号 |
| email | String | 邮箱 |
| headImage | String | 头像URL |
| status | Integer | 状态：0-已拉黑，1-正常，2-已禁用 |
| statusName | String | 状态名称 |

#### 枚举值说明

| 字段 | 值 | 说明 |
|------|-----|------|
| status | 0 | 已拉黑 |
| status | 1 | 正常 |
| status | 2 | 已禁用 |

---

### 2.3 退出登录

**接口地址**: `/admin/admin/logout`  
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

## 三、用户管理接口

### 3.1 用户列表查询

**接口地址**: `/admin/user/list`  
**请求方式**: `GET`  
**接口说明**: 分页查询所有用户列表（包括学生和企业用户），关联查询用户基本信息

#### 请求参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 是 | 页码 |
| pageSize | Integer | 是 | 每页数量 |
| userType | Integer | 否 | 用户类型：1-学生，2-企业，不传查询全部 |
| status | Integer | 否 | 状态：0-已拉黑，1-正常，2-已禁用 |
| keyword | String | 否 | 搜索关键词（用户名、昵称、手机号） |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "total": 100,
  "rows": [
    {
      "id": 1,
      "username": "student001",
      "nickName": "张三",
      "phone": "13800138000",
      "email": "student001@example.com",
      "userType": 1,
      "userTypeName": "学生",
      "status": 1,
      "statusName": "正常",
      "createTime": "2025-01-01 10:00:00"
    }
  ]
}
```

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | Long | 用户ID |
| username | String | 用户名 |
| nickName | String | 昵称 |
| phone | String | 手机号 |
| email | String | 邮箱 |
| userType | Integer | 用户类型：1-学生，2-企业 |
| userTypeName | String | 用户类型名称 |
| status | Integer | 状态：0-已拉黑，1-正常，2-已禁用，3-待审核（仅学生和企业） |
| statusName | String | 状态名称 |
| createTime | String | 创建时间 |

#### 关联查询说明

- 查询学生用户时，关联 `student` 表获取学生信息
- 查询企业用户时，关联 `enterprise` 表获取企业信息
- 根据 `userType` 参数区分查询学生表或企业表

#### 枚举值说明

| 字段 | 值 | 说明 |
|------|-----|------|
| userType | 1 | 学生 |
| userType | 2 | 企业 |
| status | 0 | 已拉黑 |
| status | 1 | 正常 |
| status | 2 | 已禁用 |
| status | 3 | 待审核（仅学生和企业） |

---

### 3.2 用户详情查询

**接口地址**: `/admin/user/detail`  
**请求方式**: `GET`  
**接口说明**: 查看用户详细信息

#### 请求参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |
| userType | Integer | 是 | 用户类型：1-学生，2-企业 |

#### 响应数据

**学生用户响应**:
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
    "birthday": "2000-01-01",
    "campusId": 1,
    "campusName": "XX大学",
    "major": "计算机科学与技术",
    "education": "本科",
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

**企业用户响应**:
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
    "status": 1,
    "statusName": "正常",
    "createTime": "2025-01-01 10:00:00"
  }
}
```

#### 关联查询说明

- 查询学生详情时，关联 `campus` 表获取校园名称
- 查询企业详情时，关联相关表获取企业认证信息

#### 枚举值说明

| 字段 | 值 | 说明 |
|------|-----|------|
| status | 0 | 已拉黑 |
| status | 1 | 正常 |
| status | 2 | 已禁用 |
| status | 3 | 待审核（仅学生和企业） |
| certificationStatus | 0 | 未认证 |
| certificationStatus | 1 | 已认证 |
| certificationStatus | 2 | 认证中 |
| certificationStatus | 3 | 认证失败 |

---

### 3.3 更新用户状态

**接口地址**: `/admin/user/updateStatus`  
**请求方式**: `PUT`  
**接口说明**: 更新用户状态（正常/拉黑/禁用）

#### 请求参数

```json
{
  "userId": 1,
  "userType": 1,
  "status": 0
}
```

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |
| userType | Integer | 是 | 用户类型：1-学生，2-企业 |
| status | Integer | 是 | 状态：0-已拉黑，1-正常，2-已禁用 |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": null
}
```

---

## 四、职位审核接口

### 4.1 职位列表查询

**接口地址**: `/admin/job/list`  
**请求方式**: `GET`  
**接口说明**: 查看所有企业发布的职位

#### 请求参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 是 | 页码 |
| pageSize | Integer | 是 | 每页数量 |
| status | Integer | 否 | 审核状态：0-待审核，1-已通过，2-已拒绝，3-已下线 |
| enterpriseId | Long | 否 | 企业ID |
| jobName | String | 否 | 职位名称（模糊查询） |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "total": 50,
  "rows": [
    {
      "id": 1,
      "enterpriseId": 1,
      "enterpriseName": "XX科技有限公司",
      "jobName": "Java开发工程师",
      "workLocation": "北京",
      "salaryMin": 8000.00,
      "salaryMax": 15000.00,
      "salaryType": "月薪",
      "recruitCount": 5,
      "viewCount": 100,
      "applyCount": 20,
      "status": 0,
      "statusName": "待审核",
      "publishTime": "2025-01-01 10:00:00",
      "createTime": "2025-01-01 09:00:00"
    }
  ]
}
```

---

### 4.2 职位详情查询

**接口地址**: `/admin/job/detail`  
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
    "enterpriseId": 1,
    "enterpriseName": "XX科技有限公司",
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
    "status": 0,
    "statusName": "待审核",
    "auditRemark": null,
    "auditTime": null,
    "auditBy": null,
    "publishTime": "2025-01-01 10:00:00",
    "expireTime": "2025-06-01 10:00:00",
    "createTime": "2025-01-01 09:00:00"
  }
}
```

---

### 4.3 职位审核

**接口地址**: `/admin/job/audit`  
**请求方式**: `PUT`  
**接口说明**: 审核职位信息，确保真实性和合法性

#### 请求参数

```json
{
  "jobId": 1,
  "status": 1,
  "auditRemark": "审核通过"
}
```

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| jobId | Long | 是 | 职位ID |
| status | Integer | 是 | 审核状态：1-已通过，2-已拒绝 |
| auditRemark | String | 否 | 审核备注 |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": null
}
```

---

## 五、企业认证接口

### 5.1 企业认证列表查询

**接口地址**: `/admin/enterprise/certification/list`  
**请求方式**: `GET`  
**接口说明**: 查看企业提交的认证申请

#### 请求参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 是 | 页码 |
| pageSize | Integer | 是 | 每页数量 |
| certificationStatus | Integer | 否 | 认证状态：0-未认证，1-已认证，2-认证中，3-认证失败 |
| enterpriseName | String | 否 | 企业名称（模糊查询） |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "total": 20,
  "rows": [
    {
      "id": 1,
      "enterpriseName": "XX科技有限公司",
      "legalPerson": "李四",
      "phone": "13800138001",
      "certificationStatus": 2,
      "certificationStatusName": "认证中",
      "certificationFile": "http://localhost:9000/campus-hiring/certificate/enterprise001.pdf",
      "createTime": "2025-01-01 10:00:00"
    }
  ]
}
```

---

### 5.2 企业认证审核

**接口地址**: `/admin/enterprise/certification/audit`  
**请求方式**: `PUT`  
**接口说明**: 审核企业认证信息

#### 请求参数

```json
{
  "enterpriseId": 1,
  "certificationStatus": 1,
  "auditRemark": "认证通过"
}
```

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| enterpriseId | Long | 是 | 企业ID |
| certificationStatus | Integer | 是 | 认证状态：1-已认证，3-认证失败 |
| auditRemark | String | 否 | 审核备注 |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": null
}
```

---

## 六、数据统计接口

### 6.1 用户统计

**接口地址**: `/admin/statistics/user`  
**请求方式**: `GET`  
**接口说明**: 查看用户数量、新增用户趋势（同步查询数据库）

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
    "totalUsers": 1000,
    "totalStudents": 800,
    "totalEnterprises": 200,
    "newUsersToday": 10,
    "newUsersThisMonth": 100,
    "activeUsers": 500,
    "trendData": [
      {
        "date": "2025-01-01",
        "studentCount": 5,
        "enterpriseCount": 2
      }
    ]
  }
}
```

---

### 6.2 职位统计

**接口地址**: `/admin/statistics/job`  
**请求方式**: `GET`  
**接口说明**: 查看职位发布数量、申请数量（同步查询数据库）

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
    "totalJobs": 500,
    "publishedJobs": 450,
    "pendingJobs": 30,
    "rejectedJobs": 20,
    "totalApplications": 2000,
    "averageApplicationsPerJob": 4.4,
    "trendData": [
      {
        "date": "2025-01-01",
        "jobCount": 10,
        "applicationCount": 50
      }
    ]
  }
}
```

---

### 6.3 运营数据统计

**接口地址**: `/admin/statistics/overview`  
**请求方式**: `GET`  
**接口说明**: 查看网站访问量、活跃度等运营数据（同步查询数据库）

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": {
    "totalUsers": 1000,
    "totalJobs": 500,
    "totalApplications": 2000,
    "totalInterviews": 300,
    "totalConsultations": 500,
    "totalComplaints": 20,
    "todayActiveUsers": 100,
    "todayNewUsers": 10,
    "todayNewJobs": 5,
    "todayNewApplications": 50
  }
}
```

---

## 七、投诉与反馈管理接口

### 7.1 投诉列表查询

**接口地址**: `/admin/complaint/list`  
**请求方式**: `GET`  
**接口说明**: 查看学生和企业的投诉

#### 请求参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 是 | 页码 |
| pageSize | Integer | 是 | 每页数量 |
| handleStatus | Integer | 否 | 处理状态：0-待处理，1-处理中，2-已处理，3-已关闭 |
| complaintType | Integer | 否 | 投诉类型：1-学生投诉企业，2-企业投诉学生 |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "total": 20,
  "rows": [
    {
      "id": 1,
      "complaintType": 1,
      "complaintTypeName": "学生投诉企业",
      "complainerId": 1,
      "complainerName": "张三",
      "complainedId": 1,
      "complainedName": "XX科技有限公司",
      "title": "虚假招聘信息",
      "content": "投诉内容...",
      "handleStatus": 0,
      "handleStatusName": "待处理",
      "createTime": "2025-01-01 10:00:00"
    }
  ]
}
```

---

### 7.2 投诉详情查询

**接口地址**: `/admin/complaint/detail`  
**请求方式**: `GET`  
**接口说明**: 查看投诉详细信息

#### 请求参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| complaintId | Long | 是 | 投诉ID |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "data": {
    "id": 1,
    "complaintType": 1,
    "complaintTypeName": "学生投诉企业",
    "complainerId": 1,
    "complainerName": "张三",
    "complainedId": 1,
    "complainedName": "XX科技有限公司",
    "jobId": 1,
    "jobName": "Java开发工程师",
    "title": "虚假招聘信息",
    "content": "投诉内容...",
    "attachment": "http://localhost:9000/campus-hiring/attachment/complaint001.pdf",
    "handleStatus": 0,
    "handleStatusName": "待处理",
    "handleResult": null,
    "handleRemark": null,
    "handleTime": null,
    "handleBy": null,
    "createTime": "2025-01-01 10:00:00"
  }
}
```

---

### 7.3 处理投诉

**接口地址**: `/admin/complaint/handle`  
**请求方式**: `PUT`  
**接口说明**: 处理投诉，记录处理结果

#### 请求参数

```json
{
  "complaintId": 1,
  "handleStatus": 2,
  "handleResult": "已核实，已对相关企业进行警告处理",
  "handleRemark": "处理备注"
}
```

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| complaintId | Long | 是 | 投诉ID |
| handleStatus | Integer | 是 | 处理状态：1-处理中，2-已处理，3-已关闭 |
| handleResult | String | 是 | 处理结果 |
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

### 7.4 反馈列表查询

**接口地址**: `/admin/feedback/list`  
**请求方式**: `GET`  
**接口说明**: 查看和处理用户反馈

#### 请求参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 是 | 页码 |
| pageSize | Integer | 是 | 每页数量 |
| handleStatus | Integer | 否 | 处理状态：0-待处理，1-已处理，2-已关闭 |
| feedbackType | String | 否 | 反馈类型：功能建议、问题反馈、其他 |

#### 响应数据

```json
{
  "code": 1000,
  "msg": "操作成功",
  "total": 10,
  "rows": [
    {
      "id": 1,
      "userId": 1,
      "userType": 1,
      "userName": "张三",
      "feedbackType": "功能建议",
      "title": "建议增加职位筛选功能",
      "content": "反馈内容...",
      "handleStatus": 0,
      "handleStatusName": "待处理",
      "createTime": "2025-01-01 10:00:00"
    }
  ]
}
```

---

### 7.5 处理反馈

**接口地址**: `/admin/feedback/handle`  
**请求方式**: `PUT`  
**接口说明**: 处理用户反馈

#### 请求参数

```json
{
  "feedbackId": 1,
  "handleStatus": 1,
  "handleResult": "已采纳，将在下个版本中实现",
  "handleRemark": "处理备注"
}
```

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| feedbackId | Long | 是 | 反馈ID |
| handleStatus | Integer | 是 | 处理状态：1-已处理，2-已关闭 |
| handleResult | String | 是 | 处理结果 |
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

## 八、接口调用示例

### 8.1 管理员登录示例

```bash
curl -X POST "http://localhost:8080/admin/admin/login" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "123456"
  }'
```

### 8.2 查询用户列表示例

```bash
curl -X GET "http://localhost:8080/admin/user/list?pageNum=1&pageSize=10&userType=1" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

### 8.3 职位审核示例

```bash
curl -X PUT "http://localhost:8080/admin/job/audit" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
  -H "Content-Type: application/json" \
  -d '{
    "jobId": 1,
    "status": 1,
    "auditRemark": "审核通过"
  }'
```

---

## 九、注意事项

1. **身份认证**: 除登录接口外，所有接口都需要在请求头中携带 JWT Token
2. **权限控制**: 管理员服务接口仅管理员身份可访问，网关会进行权限验证
3. **分页查询**: 所有列表查询接口都支持分页，必须传递 `pageNum` 和 `pageSize` 参数
4. **数据同步**: 统计数据接口采用同步查询数据库方式，不依赖缓存
5. **时间格式**: 所有时间字段统一使用 `yyyy-MM-dd HH:mm:ss` 格式
6. **文件URL**: 文件相关字段返回的是 MinIO 存储的文件访问URL

---

**文档维护**: 开发团队  
**联系方式**: 如有问题请联系开发团队

