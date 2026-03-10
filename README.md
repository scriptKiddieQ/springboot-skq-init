## SpringBoot 项目初始化模板
### 1.技术选型
- jdk21 + SpringBoot 3.5.11
- MyBatis Plus 3.5.15
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