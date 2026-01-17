# Weighty - 体重管理系统

基于 DDD（领域驱动设计）架构的体重记录管理系统。

## 项目简介

**Weighty**（胖了么）是一个轻量级的体重记录管理应用，用户可以记录自己每天的体重变化（胖了/瘦了），系统会智能提醒用户保持记录习惯。

### 应用信息
- **应用名称**：Weighty
- **中文名称**：胖了么
- **应用标识**：weighty
- **项目版本**：1.0-SNAPSHOT

## 项目架构

本项目采用 **DDD（领域驱动设计）** 分层架构，严格按照领域驱动设计规范组织代码结构。

### DDD 分层架构说明

```
Weight/
├── Weight-domain/          # 领域层（Domain Layer）
│   └── 核心业务逻辑，不依赖任何外部框架
├── Weight-app/             # 应用层（Application Layer）
│   └── 应用服务编排，领域服务调用
├── Weight-client/          # 客户端层（Client Layer）
│   └── 接口定义，DTO 数据传输对象
├── Weight-adapter/         # 适配器层（Adapter Layer）
│   └── 外部接口适配，Web Controller
├── Weight-infrastructure/  # 基础设施层（Infrastructure Layer）
│   └── 技术实现，数据持久化，外部服务集成
└── Weight-start/           # 启动模块（Bootstrap Module）
    └── 应用启动入口，配置文件
```

### 模块说明

#### 1. Weight-domain（领域层）
**职责**：领域模型、领域服务、仓储接口定义

**目录结构**：
```
com.cy.domain.weight/
├── user/                    # 用户聚合
│   ├── User.java           # 用户实体（聚合根）
│   ├── UserRepository.java # 用户仓储接口
│   ├── login/              # 登录用例
│   │   ├── LoginCmd.java
│   │   └── LoginCmdHandler.java
│   └── sendcode/           # 发送验证码用例
│       ├── SendCodeCmd.java
│       └── SendCodeCmdHandler.java
├── verificationcode/        # 验证码值对象
│   ├── VerificationCode.java
│   └── VerificationCodeRepository.java
├── weightrecord/           # 体重记录聚合
│   ├── WeightRecord.java   # 体重记录实体（聚合根）
│   ├── WeightRecordRepository.java
│   ├── WeightRecordFactory.java
│   ├── WeightType.java
│   └── create/            # 创建记录用例
│       ├── WeightRecordCreateCmd.java
│       └── WeightRecordCreateCmdHandler.java
└── sms/                    # 短信服务接口
    └── SmsService.java     # 领域服务接口定义
```

**设计原则**：
- 领域层不依赖任何技术框架
- 只包含业务逻辑，不包含技术实现
- 通过接口定义依赖外部能力（如 SmsService）
- 遵循聚合根、值对象、领域服务等 DDD 概念

#### 2. Weight-app（应用层）
**职责**：应用服务编排、用例协调、DTO 转换

**目录结构**：
```
com.cy.app.weight/
├── WeightRpcServiceImpl.java      # RPC 服务实现
├── auth/                          # 认证服务
│   └── AuthService.java
├── assembler/                     # DTO 转换器
│   ├── LoginDTO2LoginCmdConvert.java
│   ├── SendCodeDTO2SendCodeCmdConvert.java
│   └── WeightRecordCreateDTO2WeightRecordCreateCmdConvert.java
└── task/                          # 定时任务
    ├── SmsReminderTask.java       # 短信提醒任务
    └── TokenCleanupTask.java      # Token 清理任务
```

**设计原则**：
- 调用领域服务完成业务用例
- 处理事务边界
- DTO 与领域对象之间的转换
- 不包含业务逻辑

#### 3. Weight-client（客户端层）
**职责**：接口定义、DTO 定义

**目录结构**：
```
com.cy.client.weight/
├── WeightRpcService.java          # RPC 接口定义
├── dto/                           # 数据传输对象
│   ├── LoginResponseDTO.java
│   └── WeightRecordDTO.java
└── query/                         # 查询对象
    ├── LoginDTO.java
    ├── SendCodeDTO.java
    ├── RefreshTokenDTO.java
    ├── CheckTodayRecordDTO.java
    └── WeightRecordCreateDTO.java
```

**设计原则**：
- 定义对外的接口契约
- DTO 只包含数据传输，不包含业务逻辑
- 使用 JSR-303 注解进行参数校验

#### 4. Weight-adapter（适配器层）
**职责**：Web 接口适配、异常处理、请求拦截

**目录结构**：
```
com.cy.adapter.weight.web/
├── WeightWebController.java       # Web Controller
├── GlobalExceptionHandler.java    # 全局异常处理
└── AuthInterceptor.java           # 认证拦截器
```

**设计原则**：
- 适配 HTTP 协议到应用服务
- 处理请求参数提取（如 IP 地址）
- 统一异常处理和响应格式

#### 5. Weight-infrastructure（基础设施层）
**职责**：技术实现、数据持久化、外部服务集成

**目录结构**：
```
com.cy.infrastructure.weight/
├── repository/                    # 仓储实现
│   ├── UserRepositoryImpl.java
│   ├── WeightRecordRepositoryImpl.java
│   ├── VerificationCodeRepositoryImpl.java
│   ├── UserTokenRepositoryImpl.java
│   ├── mapper/                   # MyBatis Mapper
│   │   ├── UserMapper.java
│   │   ├── WeightRecordMapper.java
│   │   └── VerificationCodeMapper.java
│   ├── model/                    # 数据对象（DO）
│   │   ├── UserDO.java
│   │   ├── WeightRecordDO.java
│   │   └── VerificationCodeDO.java
│   └── assembler/                # DO 转换器
│       ├── User2UserDOConvert.java
│       └── WeightRecord2WeightRecordDOConvert.java
├── auth/                          # 认证技术实现
│   └── TokenService.java          # JWT Token 服务
└── sms/                           # 短信服务实现
    └── AliyunSmsService.java      # 阿里云短信服务实现
```

**设计原则**：
- 实现领域层定义的接口（如 SmsService、Repository）
- 包含所有技术细节（数据库访问、外部 API 调用）
- 数据对象（DO）与领域对象（Entity）分离

#### 6. Weight-start（启动模块）
**职责**：应用启动、配置管理

**目录结构**：
```
com.cy.start/
├── WeightApplication.java         # Spring Boot 启动类
└── resources/
    └── application.yml            # 应用配置文件
```

## 功能特性

### 1. 用户登录认证
- **手机号 + 验证码登录**：支持手机号验证码登录
- **自动创建账号**：首次登录自动创建用户账号
- **Token 认证**：基于 JWT 的双 Token 机制（AccessToken + RefreshToken）
- **Token 自动刷新**：支持 Token 过期自动刷新
- **登录状态保持**：7 天未使用自动清除登录状态

### 2. 验证码功能
- **随机验证码生成**：6 位数字随机验证码
- **短信发送集成**：支持 UCloud 云短信服务
- **开发模式支持**：配置开关支持固定验证码"111111"（用于开发测试）
- **验证码有效期**：5 分钟有效期

### 3. 防刷机制
- **手机号限流**：同一手机号 1 小时内最多发送 5 次验证码
- **IP 限流**：同一 IP 1 小时内最多发送 10 次验证码
- **发送间隔限制**：60 秒内不能重复发送验证码
- **友好错误提示**：返回剩余等待时间

### 4. 体重记录功能
- **记录类型**：支持"胖了"（类型 1）和"没胖"（类型 2）两种记录
- **日唯一性约束**：用户每个自然日只能记录一次
- **记录查询**：支持查询今天是否已记录及记录详情

### 5. 定时任务
- **短信提醒任务**：每天凌晨 2 点执行，提醒超过 2 天未记录的用户
- **Token 清理任务**：定期清理 7 天未使用的 Token

## 数据库设计

### user 表（用户表）
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 用户ID（主键，自增） |
| phone | VARCHAR(11) | 手机号（唯一索引） |
| create_time | DATETIME | 创建时间 |
| update_time | DATETIME | 更新时间 |

**索引**：
- PRIMARY KEY (`id`)
- UNIQUE KEY `uk_phone` (`phone`)

### verification_code 表（验证码表）
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | ID（主键，自增） |
| phone | VARCHAR(11) | 手机号 |
| code | VARCHAR(6) | 验证码 |
| expire_time | DATETIME | 过期时间 |
| send_time | DATETIME | 发送时间 |
| ip | VARCHAR(50) | IP 地址 |
| used | TINYINT | 是否已使用（0-未使用，1-已使用） |

**索引**：
- PRIMARY KEY (`id`)
- KEY `idx_phone_code` (`phone`, `code`)
- KEY `idx_phone_send_time` (`phone`, `send_time`)

### weight_record 表（体重记录表）
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 记录ID（主键，自增） |
| user_id | BIGINT | 用户ID |
| weight_type | TINYINT | 体重类型（1-胖了，2-瘦了） |
| record_date | DATE | 记录日期 |
| create_time | DATETIME | 创建时间 |
| update_time | DATETIME | 更新时间 |

**索引**：
- PRIMARY KEY (`id`)
- UNIQUE KEY `uk_user_date` (`user_id`, `record_date`)
- KEY `idx_user_id` (`user_id`)
- KEY `idx_record_date` (`record_date`)

### user_token 表（用户令牌表）
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | ID（主键，自增） |
| user_id | BIGINT | 用户ID |
| access_token | VARCHAR(500) | 访问令牌 |
| refresh_token | VARCHAR(500) | 刷新令牌 |
| expire_time | DATETIME | 过期时间 |
| last_used_time | DATETIME | 最后使用时间 |
| create_time | DATETIME | 创建时间 |
| update_time | DATETIME | 更新时间 |

**索引**：
- PRIMARY KEY (`id`)
- UNIQUE KEY `uk_user_id` (`user_id`)
- KEY `idx_access_token` (`access_token`)
- KEY `idx_refresh_token` (`refresh_token`)
- KEY `idx_last_used_time` (`last_used_time`)

## API 接口

### 1. 发送验证码
**接口**：`POST /web/weight/send-code`

**请求参数**：
```json
{
  "phone": "13800138000"
}
```

**响应示例**：
```json
{
  "code": 0,
  "msg": "success",
  "success": true,
  "data": "111111"
}
```

### 2. 登录
**接口**：`POST /web/weight/login`

**请求参数**：
```json
{
  "phone": "13800138000",
  "code": "123456"
}
```

**响应示例**：
```json
{
  "code": 0,
  "msg": "success",
  "success": true,
  "data": {
    "userId": 1,
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "expireTime": 1704067200000
  }
}
```

### 3. 刷新 Token
**接口**：`POST /web/weight/refresh-token`

**请求参数**：
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### 4. 检查今天是否已记录
**接口**：`POST /web/weight/check-today-record`

**请求头**：
```
Authorization: Bearer {accessToken}
```

**响应示例**：
```json
{
  "code": 0,
  "msg": "success",
  "success": true,
  "data": true
}
```

### 5. 获取今天的记录详情
**接口**：`POST /web/weight/get-today-record`

**请求头**：
```
Authorization: Bearer {accessToken}
```

**响应示例**：
```json
{
  "code": 0,
  "msg": "success",
  "success": true,
  "data": {
    "id": 1,
    "userId": 1,
    "weightType": 1,
    "recordDate": "2024-01-01",
    "createTime": "2024-01-01T10:00:00"
  }
}
```

### 6. 创建体重记录
**接口**：`POST /web/weight/create-record`

**请求头**：
```
Authorization: Bearer {accessToken}
```

**请求参数**：
```json
{
  "userId": 1,
  "weightType": 1
}
```

**参数说明**：
- `weightType`: 1-胖了，2-瘦了（虽然 userId 在请求体中，但后端会从 Token 中获取真实 userId）

**响应示例**：
```json
{
  "code": 0,
  "msg": "success",
  "success": true,
  "data": 1
}
```

## 技术栈

### 核心框架
- **Spring Boot 2.3.2.RELEASE**：应用框架
- **Spring Cloud OpenFeign**：RPC 调用
- **MyBatis Plus**：数据持久化框架

### 工具库
- **Lombok**：简化 Java 代码
- **MapStruct**：对象转换
- **Hutool**：Java 工具库
- **JWT (JJWT)**：Token 生成和验证

### 数据库
- **MySQL 5.7+**：关系型数据库
- **Druid**：数据库连接池

### 第三方服务
- **阿里云短信服务（Dysmsapi）**：短信发送服务

### 开发工具
- **Maven**：项目构建工具
- **Swagger**：API 文档（通过注解）

## 配置说明

### 应用配置（application.yml）

#### 数据库配置
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/weight?useUnicode=true&characterEncoding=utf-8
    username: root
    password: 123456
```

#### JWT 配置
```yaml
jwt:
  secret: your-secret-key
  expiration: 604800000      # AccessToken 过期时间（7天，单位：毫秒）
  refresh-expiration: 1209600000  # RefreshToken 过期时间（14天，单位：毫秒）
```

#### 短信服务配置
```yaml
# 短信开关（false 时使用固定验证码"111111"用于开发测试）
sms:
  enabled: false

# 阿里云短信配置（仅在 sms.enabled=true 时生效）
aliyun:
  sms:
    access-key-id: YOUR_ACCESS_KEY_ID
    access-key-secret: YOUR_ACCESS_KEY_SECRET
    sign-name: 胖了么  # 短信签名名称（需要在阿里云控制台申请）
    template-code: SMS_123456789  # 短信模板Code（需要在阿里云控制台申请）
    endpoint: dysmsapi.aliyuncs.com
```

## 运行说明

### 1. 环境要求
- JDK 1.8+
- Maven 3.6+
- MySQL 5.7+
- Redis（可选，用于缓存）

### 2. 数据库初始化
执行数据库脚本创建表结构：
```bash
mysql -u root -p weight < db/schema.sql
```

### 3. 配置修改
修改 `Weight-start/src/main/resources/application.yml` 中的配置：
- 数据库连接信息
- JWT 密钥（生产环境必须修改）
- 短信服务配置（如需要）

### 4. 编译打包
```bash
mvn clean package
```

### 5. 启动应用
```bash
cd Weight-start
java -jar target/Weight-start-1.0-SNAPSHOT.jar
```

或者使用 IDE 直接运行 `WeightApplication.java`

### 6. 验证启动
访问 `http://localhost:8888` 验证服务是否启动成功

## 开发规范

### DDD 架构规范
1. **领域层（Domain）**：
   - 不依赖任何技术框架
   - 只包含业务逻辑
   - 通过接口定义依赖外部能力

2. **应用层（Application）**：
   - 编排领域服务完成用例
   - 处理事务边界
   - DTO 转换

3. **基础设施层（Infrastructure）**：
   - 实现领域层定义的接口
   - 包含所有技术实现细节

4. **适配器层（Adapter）**：
   - 适配外部协议（如 HTTP）
   - 统一异常处理

### 代码规范（阿里巴巴 Java 开发手册）
1. **命名规范**：
   - 类名：大驼峰（PascalCase），如 `SendCodeCmdHandler`
   - 方法名/变量名：小驼峰（camelCase），如 `sendCode`
   - 常量：全大写+下划线，如 `MAX_SEND_COUNT_PER_HOUR`
   - 包名：全小写，如 `com.cy.domain.weight`

2. **注释规范**：
   - 类注释：说明类的职责
   - 方法注释：使用 JavaDoc 格式
   - 关键业务逻辑：必须有注释说明

3. **异常处理**：
   - 业务异常使用 `RuntimeException` 抛出，由全局异常处理器统一处理
   - 异常信息要清晰明确

4. **常量定义**：
   - 避免魔法值，使用常量定义
   - 常量统一放在类顶部

## 项目特色

### 1. 严格的 DDD 分层
- 领域层完全独立，不依赖任何技术框架
- 通过依赖倒置实现层间解耦

### 2. 完善的防刷机制
- 多维度限流（手机号、IP、时间间隔）
- 友好的错误提示

### 3. 灵活的配置管理
- 短信服务开关，方便开发测试
- 环境隔离配置

### 4. 安全的认证机制
- 双 Token 机制
- 自动过期清理
- Token 刷新机制

## 目录结构

```
Weight/
├── Weight-domain/              # 领域层
│   └── src/main/java/com/cy/domain/weight/
├── Weight-app/                 # 应用层
│   └── src/main/java/com/cy/app/weight/
├── Weight-client/              # 客户端层
│   └── src/main/java/com/cy/client/weight/
├── Weight-adapter/             # 适配器层
│   └── src/main/java/com/cy/adapter/weight/
├── Weight-infrastructure/      # 基础设施层
│   └── src/main/java/com/cy/infrastructure/weight/
├── Weight-start/               # 启动模块
│   └── src/main/java/com/cy/start/
├── db/                         # 数据库脚本
│   └── schema.sql
├── pom.xml                     # 父 POM
└── README.md                   # 项目文档
```

## 版本历史

### v1.0-SNAPSHOT（当前版本）
- ✅ 用户登录认证（手机号+验证码）
- ✅ 验证码发送（支持 UCloud 云短信）
- ✅ 体重记录功能（日唯一性约束）
- ✅ Token 认证机制
- ✅ 防刷机制
- ✅ 定时任务（短信提醒、Token 清理）

## 贡献指南

欢迎提交 Issue 和 Pull Request！

## 许可证

本项目采用 MIT 许可证。
