# Weighty

基于 DDD（领域驱动设计）架构的体重记录管理系统。

## 项目简介

**Weighty** 是一个轻量级的体重记录管理应用，用户可以记录自己每天的体重变化（胖了/瘦了），系统会智能提醒用户保持记录习惯。

### 应用名称
- **应用名**：Weighty
- **中文名**：胖了么
- **应用标识**：weighty

## 项目结构

```
Weight/
├── Weight-domain/          # 领域层
├── Weight-app/             # 应用层
├── Weight-client/          # 客户端层
├── Weight-adapter/         # 适配器层
├── Weight-infrastructure/  # 基础设施层
└── db/                     # 数据库脚本
```

## 功能特性

1. **用户登录**
   - 手机号+验证码登录
   - 手机号不存在自动创建账号
   - 发送验证码（TODO：实际发送短信）

2. **防刷机制**
   - 同一手机号1小时内最多发送5次验证码
   - 同一IP1小时内最多发送10次验证码

3. **体重记录**
   - 用户点击按钮记录"胖了"或"瘦了"
   - 用户自然日只能点击一次
   - 记录存储到MySQL数据库

4. **短信提醒**
   - 定时任务每天凌晨2点执行
   - 如果用户两天没有点击，发送短信提醒（TODO：实际发送短信）

## 数据库设计

### user 表
- id: 用户ID（主键）
- phone: 手机号（唯一索引）
- create_time: 创建时间
- update_time: 更新时间

### verification_code 表
- id: ID（主键）
- phone: 手机号
- code: 验证码
- expire_time: 过期时间
- send_time: 发送时间
- ip: IP地址
- used: 是否已使用（0-未使用，1-已使用）

### weight_record 表
- id: 记录ID（主键）
- user_id: 用户ID
- weight_type: 体重类型（1-胖了，2-瘦了）
- record_date: 记录日期（唯一索引：user_id + record_date）
- create_time: 创建时间

### sms_log 表
- id: ID（主键）
- user_id: 用户ID
- phone: 手机号
- sms_type: 短信类型（1-验证码，2-提醒）
- send_time: 发送时间
- content: 短信内容

## API 接口

### 1. 发送验证码
POST /web/weight/send-code
```json
{
  "phone": "13800138000"
}
```

### 2. 登录
POST /web/weight/login
```json
{
  "phone": "13800138000",
  "code": "123456"
}
```

### 3. 创建体重记录
POST /web/weight/create-record
```json
{
  "userId": 1,
  "weightType": 1
}
```
weightType: 1-胖了，2-瘦了

## 技术栈

- Spring Boot
- MyBatis Plus
- MapStruct
- MySQL

## 运行说明

1. 执行数据库脚本 `db/schema.sql` 创建表结构
2. 配置数据库连接
3. 启动应用
4. 访问接口进行测试

## TODO

- [ ] 实现实际短信发送功能（验证码和提醒）
- [ ] 完善短信发送日志记录
- [ ] 添加更多业务校验和异常处理
