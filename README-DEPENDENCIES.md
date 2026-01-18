# 模块依赖关系说明

本文档详细说明了 Weighty 项目各模块之间的依赖关系，严格遵循 DDD（领域驱动设计）架构规范。

## 依赖关系图

```
┌─────────────────────────────────────────────────────────────────┐
│                         Weight-start                            │
│                      (启动模块 - Bootstrap)                       │
└────────────┬────────────────────────────────────────────────────┘
             │
             ├─── Weight-adapter ─────┐
             │    (适配器层)            │
             │                         │
             │                         └─── Weight-app ───┐
             │                             (应用层)        │
             │                                              │
             └─── Weight-infrastructure ──┐                │
                  (基础设施层)              │                │
                                           │                │
                                           └─── Weight-domain ──── Weight-common
                                                (领域层)           (通用模块)
                                                      │
                                                      │
                                           Weight-client
                                           (客户端层)
```

## 详细依赖说明

### 1. Weight-common（通用模块）

**职责**：存放通用常量、枚举、异常码等共享代码

**依赖关系**：
- ✅ `lombok` - 代码简化
- ✅ `common-tools-core` - 通用工具类

**设计原则**：
- 不依赖任何业务模块
- 只包含常量、枚举、异常码等静态定义
- 可被所有业务模块依赖

---

### 2. Weight-domain（领域层）

**职责**：核心业务逻辑、领域模型、领域服务接口、仓储接口

**依赖关系**：
- ✅ `Weight-common` - 通用常量、枚举
- ✅ `lombok` - 代码简化
- ✅ `hutool-all` - 工具类
- ✅ `spring-boot-starter` - Spring 容器（用于 Command Handler 注入）
- ✅ `javax.annotation-api` - Java 注解
- ✅ `common-tools-core` - 通用工具类

**设计原则**：
- **不依赖** `Weight-client`、`Weight-app`、`Weight-infrastructure`、`Weight-adapter`
- 只依赖基础设施层的接口定义（如 `SmsService`、`TokenService`、`Repository`）
- 不包含具体的技术实现

---

### 3. Weight-client（客户端层）

**职责**：接口定义、DTO 数据传输对象

**依赖关系**：
- ✅ `lombok` - 代码简化
- ✅ `knife4j-spring-boot-starter` - API 文档
- ✅ `spring-cloud-starter-openfeign` - Feign 客户端
- ✅ `spring-web` - Web 注解
- ✅ `validation-api` - 参数校验
- ✅ `common-tools-core` - 通用工具类

**设计原则**：
- **不依赖**任何业务模块
- 只包含接口定义和 DTO
- 可被 `Weight-app`、`Weight-adapter`、`Weight-infrastructure` 依赖

---

### 4. Weight-app（应用层）

**职责**：应用服务编排、用例协调、DTO 转换

**依赖关系**：
- ✅ `Weight-client` - DTO 和接口定义
- ✅ `Weight-domain` - 领域服务、仓储接口
- ✅ `mapstruct` - DTO 转换
- ✅ `mapstruct-jdk8` - MapStruct JDK8 支持
- ✅ `fastjson` - JSON 处理
- ✅ `pagehelper` - 分页插件
- ✅ `javax.annotation-api` - Java 注解
- ✅ `spring-boot-starter` - Spring 容器

**设计原则**：
- **不依赖** `Weight-infrastructure`（通过领域层接口使用）
- **不依赖** `Weight-adapter`
- 只依赖领域层的接口，不依赖具体实现

---

### 5. Weight-infrastructure（基础设施层）

**职责**：技术实现、数据持久化、外部服务集成

**依赖关系**：
- ✅ `Weight-domain` - 实现领域层接口
- ✅ `Weight-client` - DTO 定义
- ✅ `Weight-common` - 通用常量
- ✅ `common-tools-mybatis` - MyBatis 工具
- ✅ `common-tools-core` - 通用工具
- ✅ `mapstruct` - DO 转换
- ✅ `mapstruct-jdk8` - MapStruct JDK8 支持
- ✅ `mybatis-plus-boot-starter` - MyBatis Plus
- ✅ `lombok` - 代码简化
- ✅ `jjwt-api` - JWT API
- ✅ `jjwt-impl` - JWT 实现
- ✅ `jjwt-jackson` - JWT Jackson
- ✅ `dypnsapi20170525` - 阿里云号码认证 SDK
- ✅ `dysmsapi20170525` - 阿里云短信 SDK
- ✅ `tea-openapi` - 阿里云 OpenAPI

**设计原则**：
- **不依赖** `Weight-app`、`Weight-adapter`
- 实现领域层定义的接口（如 `SmsService`、`TokenService`、`Repository`）
- 包含所有技术细节（数据库、外部 API、消息队列等）

---

### 6. Weight-adapter（适配器层）

**职责**：Web 接口适配、异常处理、请求拦截

**依赖关系**：
- ✅ `Weight-app` - 应用服务
- ✅ `spring-boot-starter-web` - Web 支持
- ✅ `validation-api` - 参数校验
- ✅ `knife4j-spring-boot-starter` - API 文档
- ✅ `javax.annotation-api` - Java 注解

**设计原则**：
- **不依赖** `Weight-infrastructure`、`Weight-domain`（通过 `Weight-app` 间接使用）
- 只依赖应用层，负责 HTTP 协议适配

---

### 7. Weight-start（启动模块）

**职责**：应用启动入口、配置管理

**依赖关系**：
- ✅ `Weight-adapter` - Web 接口
- ✅ `Weight-infrastructure` - 基础设施实现（需要扫描所有 Bean）
- ✅ `spring-boot-starter` - Spring Boot 核心
- ✅ `spring-boot-starter-web` - Web 支持
- ✅ `druid-spring-boot-starter` - 数据库连接池
- ✅ `mysql-connector-java` - MySQL 驱动
- ✅ `mybatis-plus-boot-starter` - MyBatis Plus
- ✅ `spring-cloud-starter-openfeign` - Feign 客户端
- ✅ `common-tools-core` - 通用工具
- ✅ `common-tools-mybatis` - MyBatis 工具
- ✅ `spring-boot-starter-validation` - 参数校验

**设计原则**：
- 依赖 `Weight-adapter` 和 `Weight-infrastructure`（需要加载所有 Bean）
- 包含启动类和配置文件
- 统一管理第三方依赖

---

## DDD 依赖规范总结

### ✅ 符合规范的依赖方向

1. **领域层（Domain）** → 通用模块（Common）
2. **应用层（App）** → 客户端层（Client）+ 领域层（Domain）+ 通用模块（Common）
3. **基础设施层（Infrastructure）** → 领域层（Domain）+ 客户端层（Client）+ 通用模块（Common）
4. **适配器层（Adapter）** → 应用层（App）
5. **启动模块（Start）** → 适配器层（Adapter）+ 基础设施层（Infrastructure）

### ❌ 禁止的依赖方向

1. ❌ **领域层** 不应依赖 `Weight-client`、`Weight-app`、`Weight-infrastructure`、`Weight-adapter`
2. ❌ **应用层** 不应直接依赖 `Weight-infrastructure`（通过领域层接口使用）
3. ❌ **适配器层** 不应直接依赖 `Weight-infrastructure`、`Weight-domain`（通过 `Weight-app` 使用）
4. ❌ **客户端层** 不应依赖任何业务模块

### 依赖管理原则

1. **版本统一管理**：所有第三方依赖版本在父 POM 的 `dependencyManagement` 中统一管理
2. **模块版本管理**：子模块版本在父 POM 中统一管理，子模块无需指定版本号
3. **避免版本冲突**：使用 `dependencyManagement` 统一版本，避免传递依赖冲突

---

## 版本管理

所有依赖版本在 `pom.xml` 的 `dependencyManagement` 中统一管理：

```xml
<properties>
  <!-- 项目模块版本 -->
  <project.version>1.0-SNAPSHOT</project.version>
  
  <!-- 第三方依赖版本 -->
  <jjwt.version>0.11.5</jjwt.version>
  <aliyun.dypnsapi.version>2.0.0</aliyun.dypnsapi.version>
  <aliyun.dysmsapi.version>2.0.24</aliyun.dysmsapi.version>
  <aliyun.tea-openapi.version>0.2.8</aliyun.tea-openapi.version>
  <!-- ... 其他版本 -->
</properties>
```

子模块引用时无需指定版本号：

```xml
<dependency>
  <groupId>io.jsonwebtoken</groupId>
  <artifactId>jjwt-api</artifactId>
  <!-- 无需指定 version，从父 POM 继承 -->
</dependency>
```

---

## 注意事项

1. **新增依赖时**：
   - 优先在父 POM 的 `dependencyManagement` 中声明版本
   - 子模块引用时不要指定版本号

2. **依赖冲突排查**：
   - 使用 `mvn dependency:tree` 查看依赖树
   - 使用 `mvn dependency:analyze` 分析未使用的依赖

3. **模块间依赖**：
   - 严格遵循 DDD 依赖方向
   - 禁止跨层直接依赖（必须通过接口）

4. **测试依赖**：
   - 测试相关的依赖（如 `junit`、`mockito`）应使用 `<scope>test</scope>`
