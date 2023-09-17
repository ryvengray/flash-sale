# 开始

### 安装软件
* mysql
```shell
# 设置时区、设置root用户密码、设置root用户只能本地访问
docker run --name mysql-container -p 3306:3306 -e MYSQL_TZ=Asia/Shanghai -e MYSQL_ROOT_PASSWORD=your_password -e MYSQL_ROOT_HOST=localhost -d mysql:tag

# 进入容器
docker exec -it 容器ID /bin/bash

# 登录mysql root 用户
mysql -uroot -p 

# 创建数据库并创建用户用于远程访问
create database if not exists db_abc;

create USER 'username'@'%' identified by 'password';
grant all privileges on `db_abc`.* to 'username'@'%';
```

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)

