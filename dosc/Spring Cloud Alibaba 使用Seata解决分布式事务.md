#### 为什么会产生分布式事务?
随着业务的快速发展，网站系统往往由单体架构逐渐演变为分布式、微服务架构，而对于数据库则由单机数据库架构向分布式数据库架构转变。此时，我们会将一个大的应用系统拆分为多个可以独立部署的应用服务，需要各个服务之间进行远程协作才能完成事务操作。在微服务项目中通常一个大项目会被拆分为N个子项目,例如用户中心服务,会员中心服务,支付中心服务等一系列微服务，在面临各种业务需求时难免会产生用户中心服务中需要调用会员中心服务,支付中心服务而产生调用链路;服务与服务之间通讯采用RPC远程调用技术，但是每个服务中都有自己独立的数据源，即自己独立的本地事务；两个服务相互进行通讯的时候，两个本地事务互不影响，从而出现分布式事务产生的原因。

#### Seata简介

Seata 是一款开源的分布式事务解决方案，致力于提供高性能和简单易用的分布式事务服务。Seata 将为用户提供了 AT、TCC、SAGA 和 XA 事务模式，为用户打造一站式的分布式解决方案。

##### Seata核心术语

TC (Transaction Coordinator) - 事务协调者：维护全局和分支事务的状态，驱动全局事务提交或回滚。

TM (Transaction Manager) - 事务管理器：定义全局事务的范围：开始全局事务、提交或回滚全局事务。

RM (Resource Manager) - 资源管理器：管理分支事务处理的资源，与TC交谈以注册分支事务和报告分支事务的状态，并驱动分支事务提交或回滚。

##### AT模式工作机制

根据官方说明当前：通过JDBC访问支持本地 ACID 事务的关系型数据库的Java应用程序才支持AT模式。

两阶段提交协议的演变：

- 一阶段：业务数据和回滚日志记录在同一个本地事务中提交，释放本地锁和连接资源。
- 二阶段：
  - 提交异步化，非常快速地完成。
  - 回滚通过一阶段的回滚日志进行反向补偿。

更详细可参考官方文档: http://seata.io/zh-cn/docs/dev/mode/at-mode.html

#### Seata Server 部署
```
官方Seata配置中心信息：https://github.com/seata/seata/blob/develop/script/config-center/config.txt
官方Seata Nacos配置部署脚本：https://github.com/seata/seata/blob/develop/script/config-center/config.txt
版本信息与Seata Server下载地址可参考首页介绍文档：https://gitee.com/SimpleWu/spring-cloud-alibaba-example
```

Seata目录结构说明：

- bin：运行脚本
- conf：配置文件
- lib：依赖包

当前部署方式采用Nacos作为注册中心与配置中心。

##### registry.conf
该配置位于conf目录,按下以下注释区域进行修改
```conf
registry {
  # file 、nacos 、eureka、redis、zk、consul、etcd3、sofa
  # 使用nacos作为注册中心
  type = "nacos"

  nacos {
    # 注册到nacos应用名称
    application = "seata-server"
    # nacos ip
    serverAddr = "127.0.0.1:8848"
    # 所在分组
    group = "EXAMPLE-GROUP"
    # 所在命名空间
    namespace = "7e3699fa-09eb-4d47-8967-60f6c98da94a"
    # 所在集群
    #cluster = "default"
    username = "nacos"
    password = "nacos"
  }
  eureka {
    serviceUrl = "http://localhost:8761/eureka"
    application = "default"
    weight = "1"
  }
  redis {
    serverAddr = "localhost:6379"
    db = 0
    password = ""
    cluster = "default"
    timeout = 0
  }
  zk {
    cluster = "default"
    serverAddr = "127.0.0.1:2181"
    sessionTimeout = 6000
    connectTimeout = 2000
    username = ""
    password = ""
  }
  consul {
    cluster = "default"
    serverAddr = "127.0.0.1:8500"
  }
  etcd3 {
    cluster = "default"
    serverAddr = "http://localhost:2379"
  }
  sofa {
    serverAddr = "127.0.0.1:9603"
    application = "default"
    region = "DEFAULT_ZONE"
    datacenter = "DefaultDataCenter"
    cluster = "default"
    group = "SEATA_GROUP"
    addressWaitTime = "3000"
  }
  file {
    name = "file.conf"
  }
}

config {
  # file、nacos 、apollo、zk、consul、etcd3
  # 使用nacos管理配置
  type = "nacos"

  nacos {
    # nacos ip
    serverAddr = "127.0.0.1:8848"
    # 所在命名空间
    namespace = "7e3699fa-09eb-4d47-8967-60f6c98da94a"
    # 所在分组
    group = "EXAMPLE-GROUP"
    username = "nacos"
    password = "nacos"
  }
  consul {
    serverAddr = "127.0.0.1:8500"
  }
  apollo {
    appId = "seata-server"
    apolloMeta = "http://192.168.1.204:8801"
    namespace = "application"
  }
  zk {
    serverAddr = "127.0.0.1:2181"
    sessionTimeout = 6000
    connectTimeout = 2000
    username = ""
    password = ""
  }
  etcd3 {
    serverAddr = "http://localhost:2379"
  }
  file {
    name = "file.conf"
  }
}
```
以上内容主要修改了注册中心与配置中心为Nacos并且修改了Nacos地址与登录账号/登录密码,命名空间,分组;
##### 配置部署到Nacos
这里简化了下Nacos官网下载的config.txt内容,从官网下载的配置文本以下内容标记需要修改的需要关注
```properties
#事务组 重点关注
service.vgroupMapping.my_test_tx_group=default
#服务段分组地址
service.default.grouplist=127.0.0.1:8091
#保持默认
service.enableDegrade=false
#保持默认
service.disableGlobalTransaction=false
#存储方式选择 db模式则数据库
store.mode=db
#需修改
store.lock.mode=db
#需修改
store.session.mode=db
store.publicKey=
#需修改
store.db.datasource=druid
#需修改
store.db.dbType=mysql
#需修改
store.db.driverClassName=com.mysql.jdbc.Driver
#需修改
store.db.url=jdbc:mysql://127.0.0.1:3306/seata?useUnicode=true&rewriteBatchedStatements=true
#需修改
store.db.user=root
#需修改
store.db.password=123456
store.db.minConn=5
store.db.maxConn=30
store.db.globalTable=global_table
store.db.branchTable=branch_table
store.db.distributedLockTable=distributed_lock
store.db.queryLimit=100
store.db.lockTable=lock_table
store.db.maxWait=5000
client.undo.dataValidation=true
#需修改
#jackson改为kryo 解决数据库Datetime类型问题
client.undo.logSerialization=kryo
client.undo.onlyCareUpdateColumns=true
server.undo.logSaveDays=7
server.undo.logDeletePeriod=86400000
client.undo.logTable=undo_log
client.undo.compress.enable=true
client.undo.compress.type=zip
client.undo.compress.threshold=64k
log.exceptionRate=100
transport.serialization=seata
transport.compressor=none
```
其中该配置需要重点关注`service.vgroupMapping.my_test_tx_group=default`这里的配置与微服务应用中的配置必须要一致后面会描述到。

由于有时间类型是Seata回滚反序列化Date类型无法成功反序列化，需要修改序列化方式解决该问题: `client.undo.logSerialization=kryo`

修改完所有配置运行从官网下载的nacos-config.sh文件将文本内容上次到nacos配置中心中:

```shell script
# -h ip -p 端口 -t 命名空间 -g 分组
sh nacos-config.sh -h localhost -p 8848 -t 7e3699fa-09eb-4d47-8967-60f6c98da94a -g EXAMPLE-GROUP
```
部署好配置文件之后在Nacos命名空间为7e3699fa-09eb-4d47-8967-60f6c98da94a(dev)的配置管理界面可以看到文本中的内容。
![](https://gitee.com/SimpleWu/spring-cloud-alibaba-example/raw/master/cnblogs/images/seata/20211104145103.png)

##### Seata数据库
按照config.txt中对应的数据库连接信息创建Seata数据库并且创建以下几张表
```sql
CREATE TABLE IF NOT EXISTS `global_table`
(
    `xid`                       VARCHAR(128) NOT NULL,
    `transaction_id`            BIGINT,
    `status`                    TINYINT      NOT NULL,
    `application_id`            VARCHAR(32),
    `transaction_service_group` VARCHAR(32),
    `transaction_name`          VARCHAR(128),
    `timeout`                   INT,
    `begin_time`                BIGINT,
    `application_data`          VARCHAR(2000),
    `gmt_create`                DATETIME,
    `gmt_modified`              DATETIME,
    PRIMARY KEY (`xid`),
    KEY `idx_gmt_modified_status` (`gmt_modified`, `status`),
    KEY `idx_transaction_id` (`transaction_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- the table to store BranchSession data
CREATE TABLE IF NOT EXISTS `branch_table`
(
    `branch_id`         BIGINT       NOT NULL,
    `xid`               VARCHAR(128) NOT NULL,
    `transaction_id`    BIGINT,
    `resource_group_id` VARCHAR(32),
    `resource_id`       VARCHAR(256),
    `branch_type`       VARCHAR(8),
    `status`            TINYINT,
    `client_id`         VARCHAR(64),
    `application_data`  VARCHAR(2000),
    `gmt_create`        DATETIME(6),
    `gmt_modified`      DATETIME(6),
    PRIMARY KEY (`branch_id`),
    KEY `idx_xid` (`xid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- the table to store lock data
CREATE TABLE IF NOT EXISTS `lock_table`
(
    `row_key`        VARCHAR(128) NOT NULL,
    `xid`            VARCHAR(96),
    `transaction_id` BIGINT,
    `branch_id`      BIGINT       NOT NULL,
    `resource_id`    VARCHAR(256),
    `table_name`     VARCHAR(32),
    `pk`             VARCHAR(36),
    `gmt_create`     DATETIME,
    `gmt_modified`   DATETIME,
    PRIMARY KEY (`row_key`),
    KEY `idx_branch_id` (`branch_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
```
##### 部署Seata Server
以上工作准备就绪,进入bin目录运行seata-server.bat(windows用户)/seata-server.sh(linux用户)即可。
#### Seata应用场景模拟

这里做一个用户服务用户登录成功后调用会员服务增加会员积分场景案例。

##### 父工程改造

工程名称：spring-cloud-alibaba-version-parent，增加mybatis，seata序列化等依赖版本管理。

```xml
<!-- properties 增加版本号 -->
<!-- mybatis -->
<mybatis.plus.version>3.4.2</mybatis.plus.version>
<mybatis.plus.ds.version>2.5.4</mybatis.plus.ds.version>
<seata.serializer.kryo.version>1.3.0</seata.serializer.kryo.version>

<!-- dependencyManagement 增加以下依赖 -->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>${mybatis.plus.version}</version>
</dependency>
<dependency>
    <groupId>io.seata</groupId>
    <artifactId>seata-serializer-kryo</artifactId>
    <version>${seata.serializer.kryo.version}</version>
</dependency>
```

##### 会员服务工程改造

工程名称:spring-cloud-alibaba-service-member,增加数据库与Seata依赖，增加用户会员积分接口。

**pom.xml**

```xml
 <!-- Seata  & mybatis-plus -->
<dependency>
	<groupId>com.alibaba.cloud</groupId>
	<artifactId>spring-cloud-starter-alibaba-seata</artifactId>
</dependency>
<dependency>
	<groupId>io.seata</groupId>
	<artifactId>seata-serializer-kryo</artifactId>
</dependency>
<dependency>
	<groupId>com.baomidou</groupId>
	<artifactId>mybatis-plus-boot-starter</artifactId>
</dependency>
<dependency>
	<groupId>mysql</groupId>
	<artifactId>mysql-connector-java</artifactId>
</dependency>
```

**bootstrap.yaml**

```yaml
#注意,此处省略之前配置的信息....
#注意,此处省略之前配置的信息....
#注意,此处省略之前配置的信息....
#注意,此处省略之前配置的信息....
#数据库信息配置
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/member_db?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true
    username: root
    password: 123456
#Seata配置
seata:
  enabled: true
  application-id: ${spring.application.name}
  #对应nacos配置 service.vgroupMapping.my_test_tx_group
  tx-service-group: 'my_test_tx_group'
  service:
    vgroup-mapping:
      #对应nacos配置 service.vgroupMapping.my_test_tx_group 的值 default
      my_test_tx_group: 'default'
  registry:
    type: nacos
    nacos:
      server-addr: ${spring.cloud.nacos.discovery.server-addr}
      namespace: ${spring.cloud.nacos.discovery.namespace}
      group: ${spring.cloud.nacos.discovery.group}
      #cluster: ${spring.cloud.nacos.discovery.cluster}
  config:
    type: nacos
    nacos:
      server-addr: ${spring.cloud.nacos.discovery.server-addr}
      namespace: ${spring.cloud.nacos.discovery.namespace}
      group: ${spring.cloud.nacos.discovery.group}
```

注意事项：

1. bootstrap.yaml中seata.tx-service-group 配置项一定要配置nacos配置中心中service.vgroupMapping对应的my_test_tx_group。也就是说一定要保持一致。
2. bootstrap.yaml中seata.service.vgroup-mapping.my_test_tx_group配置项一定要配置nacos配置中心对应service.vgroupMapping.my_test_tx_group配置祥的值。

如果没有注意上方两点将会导致启动时报:no available service 'default' found, please make sure registry config correct。

**创建member_db数据库**

其中undo_log表为Seata回滚日志表，需要在每个使用到Seata的业务服务数据库中都需要创建。

```sql
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_member_integral
-- ----------------------------
DROP TABLE IF EXISTS `t_member_integral`;
CREATE TABLE `t_member_integral`  (
  `ID` bigint(20) NOT NULL COMMENT '主键',
  `USERNAME` varchar(55) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户名称',
  `INTEGRAL` int(11) DEFAULT NULL COMMENT '积分',
  `CREDATE` datetime(0) DEFAULT NULL COMMENT '时间',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `context` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime(0) NOT NULL,
  `log_modified` datetime(0) NOT NULL,
  `ext` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ux_undo_log`(`xid`, `branch_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
```

**新增会员积分CRUD**

我这里新增以下类,具体内容大家都比较熟悉。

```
MemberIntegralController.java
IMemberIntegralBiz.java
IMemberIntegralBizImpl.java
MemberIntegralMapper.java
MemberIntegral.xml
```

在这里所有增加会员积分的逻辑都写在同一个类中 MemberIntegralController.java

```java
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.gitee.eample.member.service.biz.IMemberIntegralBiz;
import com.gitee.eample.member.service.domain.MemberIntegral;
import com.gtiee.example.common.exception.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 用户积分
 *
 * @author wentao.wu
 */
@RestController
@RequestMapping("/member/integral")
public class MemberIntegralController {
    @Autowired
    private IMemberIntegralBiz memberIntegralBiz;

    @PostMapping("/login/{username}")
    public Response<Boolean> login(@PathVariable("username") String username) {
        // 每天第一次登录则增加积分，我这里就不判断了，每次调用都新增一条积分记录了
        MemberIntegral memberIntegral = new MemberIntegral();
        memberIntegral.setId(IdWorker.getId());
        memberIntegral.setIntegral(10);//固定10积分
        memberIntegral.setUsername(username);
        memberIntegral.setCredate(new Date());
        memberIntegralBiz.save(memberIntegral);
        return Response.createOk("登录新增会员积分成功!", true);
    }
}
```

运行MemberServiceApplication.java启动服务，如果想知道有没有注册成功：

第一可以看Seata Server端有没有日志输出，该日志内容主要为注册的业务服务的数据库信息。

第二可以看业务服务有没有输出以下日志，有输出以下日志则Seata Server端注册成功

```
2021-11-05 09:56:30.962  INFO 16420 --- [           main] i.s.c.r.netty.NettyClientChannelManager  : will connect to 2.0.4.58:8091
2021-11-05 09:56:30.962  INFO 16420 --- [           main] i.s.c.rpc.netty.RmNettyRemotingClient    : RM will register :jdbc:mysql://localhost:3306/member_db
2021-11-05 09:56:30.967  INFO 16420 --- [           main] i.s.core.rpc.netty.NettyPoolableFactory  : NettyPool create channel to transactionRole:RMROLE,address:2.0.4.58:8091,msg:< RegisterRMRequest{resourceIds='jdbc:mysql://localhost:3306/member_db', applicationId='service-member', transactionServiceGroup='my_test_tx_group'} >
```

##### 用户服务工程改造

工程名称:spring-cloud-alibaba-service-member,增加数据库与Seata依赖，增加用户登录接口，增加调用会员服务积分接口feign。

`由于内容一致此处省略pom.xml,bootstrap.xml(里面注意数据库要修改为用户服务的数据库)。`

**创建user_db数据库**

其中undo_log表为Seata回滚日志表，需要在每个使用到Seata的业务服务数据库中都需要创建。

```sql

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `ID` bigint(20) NOT NULL COMMENT '主键',
  `USERNAME` varchar(55) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户名',
  `PWD` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '密码',
  `ADDR` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '地址',
  `LAST_LOGIN_DATE` datetime(0) DEFAULT NULL COMMENT '最后登录时间',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (1, 'test1', '123456', '123', NULL);

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `context` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime(0) NOT NULL,
  `log_modified` datetime(0) NOT NULL,
  `ext` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ux_undo_log`(`xid`, `branch_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
```

**新增用户登录CRUD**

我这里新增以下类,具体内容大家都比较熟悉。

```
UserController.java
IUserBiz.java
IUserBizImpl.java
UserMapper.java
UserMapper.xml
MemberInfoControllerClient.java
```

MemberInfoControllerClient.java

```
/**
 * service-member服务远程调用接口
 *
 * @author wentao.wu
 */
@FeignClient(name = "service-member")
public interface MemberInfoControllerClient {
    /**
     * 登录送积分
     *
     * @param username
     * @return
     */
    @PostMapping("/member/integral/login/{username}")
    Response<Boolean> login(@PathVariable("username")String username);
}
```

IUserBiz.java

```java
public interface IUserBiz extends IService<User> {
    /**
     * 用户登录并且赠送第一次登录积分
     *
     * @param command
     * @return
     */
    boolean login(UserLoginCommand command);

}
```

IUserBizImpl.java

```java
package com.gitee.eample.user.service.biz;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.eample.user.service.controller.command.UserLoginCommand;
import com.gitee.eample.user.service.dao.UserMapper;
import com.gitee.eample.user.service.domain.User;
import com.gitee.eample.user.service.feign.MemberInfoControllerClient;
import com.gtiee.example.common.exception.Response;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;

@Service
public class IUserBizImpl extends ServiceImpl<UserMapper, User> implements IUserBiz {

    @Autowired
    private MemberInfoControllerClient client;

    @GlobalTransactional(name = "login_add_member_intergral",rollbackFor = Exception.class)//开启分布式事务
    @Override
    public boolean login(UserLoginCommand command) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, command.getUsername())
                .eq(User::getPwd, command.getPwd());
        User loginUser = getOne(wrapper);
        if (ObjectUtils.isEmpty(loginUser)) {
            return false;
        }
        //调用会员登录接口增加积分
        Response<Boolean> response = client.login(command.getUsername());
        if (response.isOk()) {//增加积分成功，或已增加积分
            //调用积分接口成功，修改当前用户登录时间
            loginUser.setLastLoginDate(new Date());
            updateById(loginUser);
            //假设此处发生异常，不但修改当前用户登录时间需要回滚并且新增的会员积分信息也回滚才算正常
            int i = 0 / 0;
            return true;
        } else {
            //增加积分失败
            return false;
        }
    }
}
```

UserController.java

```
import com.gitee.eample.user.service.biz.IUserBiz;
import com.gitee.eample.user.service.controller.command.UserLoginCommand;
import com.gtiee.example.common.exception.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * User Business Controller
 *
 * @author wentao.wu
 */
@RestController
@RequestMapping("/users/")
public class UserController {
    private Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private IUserBiz userBiz;

    @PostMapping("/login")
    public Response<Boolean> login(UserLoginCommand command) {
        try {
            boolean result = userBiz.login(command);
            if (result) {
                return Response.createOk("登录并赠送积分成功!", result);
            }else{
                return Response.createError("账号或密码不存在!", result);
            }
        } catch (Exception e) {
            logger.error("登录失败!", e);
            return Response.createError("服务器繁忙请稍后再试!", false);
        }
    }

}
```

运行启动类UserServiceApplication.java。

服务改造成功后主要模拟有三个场景：

1. 有分布式事务处理发生异常场景：调用积分接口成功，修改当前用户登录时间之后发生异常，用户表的修改操作进行回滚，同时会员服务新增的用户对应的积分数据同样发生回滚
2. 无分布式事务处理发生异常场景:  调用积分接口成功，修改当前用户登录时间之后发生异常，用户表的修改操作进行回滚，用户会员新增的数据并没有发生回滚，此处造成数据异常。
3. 正常执行场景: 调用积分接口成功，修改当前用户登录时间之后未发生异常，所有操作生效。

##### 有分布式事务处理发生异常场景

IUserBizImpl.java中login方法增加分布式事务注解 @GlobalTransactional(name = "login_add_member_intergral",rollbackFor = Exception.class)//开启分布式事务，name为属性名称，rollbackFor 为指定回滚异常。

首先在用户服务表中插入一条用户数据，作为登录用户：

```sql
INSERT INTO `user_db`.`t_user`(`ID`, `USERNAME`, `PWD`, `ADDR`, `LAST_LOGIN_DATE`) VALUES (1, 'test1', '123456', '123', NULL);
```

![](https://gitee.com/SimpleWu/spring-cloud-alibaba-example/raw/master/cnblogs/images/seata/20211105102345.png)

并且当前会员服务t_member_integral表中还没有数据还没初始化过数据，当前场景操作会修改t_user.LAST_LOGIN_DATE，并且向t_member_integral表中插入数据；但是最后发生异常导致操作失败，并且存在分布式事务注解，此时会回滚所有服务DML操作。

请求用户登录接口:

![](https://gitee.com/SimpleWu/spring-cloud-alibaba-example/raw/master/cnblogs/images/seata/20211105102923.png)

请求成功后查看t_user与t_member_integral依旧没有发生任何改变：

![](https://gitee.com/SimpleWu/spring-cloud-alibaba-example/raw/master/cnblogs/images/seata/20211105102934.png)

![](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20211105103353597.png)

表示分布式事务处理成功无任何异常。

##### 无分布式事务处理发生异常场景

IUserBizImpl.java中login方法注释掉全局事务(分布式事务),并且修改为本地事务：

```java
//@GlobalTransactional(name = "login_add_member_intergral",rollbackFor = Exception.class)//开启分布式事务
@Transactional
```

请求用户登录接口:

![](https://gitee.com/SimpleWu/spring-cloud-alibaba-example/raw/master/cnblogs/images/seata/20211105102923.png)

此时发生的异常导致了用户服务中修改LAST_LOGIN_DATE操作被回滚成功，但是t_member_integral表中依然插入了积分数据并未被回滚：

![](https://gitee.com/SimpleWu/spring-cloud-alibaba-example/raw/master/cnblogs/images/seata/20211105104046.png)

![](https://gitee.com/SimpleWu/spring-cloud-alibaba-example/raw/master/cnblogs/images/seata/20211105104056.png)

表示在跨服务调用下没有分布式事务将会导致数据不一致，事务异常。

##### 正常执行场景

IUserBizImpl.java中login方法注释掉本地事务,并且修改为全局事务（分布式事务），这里改不改无所谓，事务都是成功的，无论使用本地事务与全局事务都不会有问题，此处改成全局事务主要是验证全局事务不会影响什么：

```java
@GlobalTransactional(name = "login_add_member_intergral",rollbackFor = Exception.class)//开启分布式事务
//@Transactional
```

同时将login方法中的异常处理去除掉:

```java
//假设此处发生异常，不但修改当前用户登录时间需要回滚并且新增的会员积分信息也回滚才算正常
int i = 0 / 0;
```

请求用户登录接口，此时所有操作全部成功，用户服务修改LAST_LOGIN_DATE成功，并且t_member_integral表中数据新增成功；这里就不贴图了，浪费大家流量。

#### 总结

- 每个业务服务对应的数据库中都需要包含undo_log表，这个表主要是记录全局事务操作的日志，后续发生异常Seata会通过该日志进行事务回滚补偿;
- Seata回滚反序列化时Date类型无法反序列化，所以要修改Seata的序列化为：kryo;(此问题将在1.5版本 Seata发布后彻底解决)

#### 源码代码存放地址

gitee: https://gitee.com/SimpleWu/spring-cloud-alibaba-example.git
cnblogs: https://www.cnblogs.com/SimpleWu
持续更新目录:https://www.cnblogs.com/SimpleWu/p/15476427.html
