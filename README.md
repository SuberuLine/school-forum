前端代码： https://github.com/SuberuLine/school-forum-frontend

### 后端技术栈

- SpringBoot3.2.3
- MySQL
- Redis
- RabbitMQ
- Minio

### 后端功能与技术点

用户注册、用户登录、重置密码等基础功能以及对应接口

- 对接Github-OAuth2，实现快速登陆
- 采用Mybatis-Plus作为持久层框架，使用更便捷
- 采用Redis存储注册/重置操作验证码，带过期时间控制
- 采用RabbitMQ积压短信发送任务，再由监听器统一处理
- 采用SpringSecurity作为权限校验框架，手动整合Jwt校验方案
- 采用Redis进行IP地址限流处理，防刷接口
- 视图层对象和数据层对象分离，编写工具方法利用反射快速互相转换
- 错误和异常页面统一采用JSON格式返回，前端处理响应更统一
- 手动处理跨域，采用过滤器实现
- 使用Swagger作为接口文档自动生成，已自动配置登录相关接口
- 采用过滤器实现对所有请求自动生成雪花ID方便线上定位问题
- 针对于多环境进行处理，开发环境和生产环境采用不同的配置
- 日志中包含单次请求完整信息以及对应的雪花ID，支持文件记录
- 项目整体结构清晰，职责明确，注释全面，开箱即用

### 部署准备

以Ubuntu22.04部署为例

```shell
sudo apt-get update
sudo apt-get upgrade
sudo apt-get install openjdk-17-jdk
sudo apt-get install mysql-server
sudo mysql_secure_installation
```

通过APT安装Redis：

```shell
sudo apt-get install redis-server
```

开启Redis服务：

```shell
sudo systemctl enable redis-server.service
```

导入RabbitMQ仓库签名密钥：

```shell
sudo wget -O- https://www.rabbitmq.com/rabbitmq-release-signing-key.asc | sudo apt-key add -
```

添加RabbitMQ APT仓库到您的APT源：

```shell
echo "deb http://dl.bintray.com/rabbitmq-erlang/debian bionic erlang" | sudo tee /etc/apt/sources.list.d/bintray.rabbitmq.list
```

安装RabbitMQ

```shell
 sudo apt-get update
 sudo apt-get install rabbitmq-server
```

启用和启动RabbitMQ服务：

```shell
 sudo systemctl enable rabbitmq-server
 sudo systemctl start rabbitmq-server
```

下载Minio：

```shell
wget https://dl.minio.org.cn/server/minio/release/linux-amd64/minio
chmod +x minio
sudo mv minio /usr/local/bin/
```

运行Minio

```shell
mkdir ~/minio
minio server ~/minio --console-address :9090
```



### 运行前配置

- 导入项目中的的sql文件

- 在RabbitMQ中创建一个名为mail的队列

  登录mq后台 -> Queues and Streams -> Add a new queue

- 在Minio中创建一个储存桶

  登录Minio后台 -> Buckets -> Create Bucket -> 创建一个名为“Forum”的Bucket

  
