# SpringBoot 项目初始化模板
## 1.技术选型
### 主体技术框架 & 特性

- jdk21 + SpringBoot 3.5.11
- MyBatis + MyBatis Plus 数据访问（开启分页） 3.5.15
- Spring AOP 切面编程 3.5.11

### 数据存储

- MySQL 数据库 8.0.41
- Redis 内存数据库

### 工具类

- Lombok 注解
- Hutool 工具库

### 业务特性

- 全局请求响应拦截器（记录日志）
- 全局异常处理器
- 自定义错误码
- 封装通用响应类
- Swagger + Knife4j 接口文档
- 自定义权限注解 + 全局校验
- 全局跨域处理
- 长整数丢失精度解决
- Spring Session Redis 分布式登录

## 业务功能

- 用户登录、注册、注销、更新、检索、权限管理
## 快速上手

### MySQL 数据库

1）修改 `application.yml` 的数据库配置为你自己的：
```yml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/default_db
    username: root
    password: 123123
```
2）执行 `sql/create_table.sql` 中的数据库语句，自动创建库表

3）启动项目，访问 `http://localhost:8123/api/doc.html` 即可打开 knife4j 接口文档

### Redis 分布式登录

1）修改 `application.yml` 的 Redis 配置为你自己的：

```yml
  data:
    redis:
      database: 0
      host: localhost
      port: 6379
      timeout: 5000
      password:     # 可选
```

2）修改 `application.yml` 中的 session 存储方式：

```yml
spring:
  session:
    store-type: redis
```

3）移除 `MainApplication` 类开头 `@SpringBootApplication` 注解内的 exclude 参数

修改前：

```java
@SpringBootApplication(exclude = {RedisAutoConfiguration.class})
```

修改后：


```java
@SpringBootApplication
```