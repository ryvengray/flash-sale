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

* pg
```shell
docker run --name pgsql-sale -e POSTGRES_PASSWORD='afcjdnei&78a7&CHddjnacg7D' -p 5432:5432 -d postgres
```

* redis
```shell
# 设置密码
docker run -itd --name redis-sale -p 6379:6379 redis --requirepass kdCen83291
```

### Guides
* 打包镜像
```shell
cd aio
docker build --build-arg JAR_FILE='target/*.jar' -t ryvengray/k8s-sale:1.0 .
# 在mac的M1机器上需要添加platform
docker build --platform linux/amd64 --build-arg JAR_FILE='target/*.jar' -t ryvengray/k8s-sale:1.1 . 
# 测试
docker run --name java-app -p 8080:8080 -d ryvengray/k8s-sale:1.0
curl localhost:8080

# 推送到远程仓库
docker push ryvengray/k8s-sale:1.0

#
```