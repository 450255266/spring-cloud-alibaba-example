#### 简介

SpringCloud Alibaba是阿里巴巴集团开源的一套微服务架构解决方案。

微服务架构是为了更好的分布式系统开发，将一个应用拆分成多个子应用，每一个服务都是可以独立运行的子工程。其中涵盖了非常多的内容，包括：服务治理、配置管理、限流降级以及对阿里开源生态（Dubbo、RocketMQ等）支持的N多组件。

**SpringCloud Alibaba相关组件**

- **Sentinel**：阿里巴巴开源产品，把流量作为切入点，从流量控制、熔断降级、系统负载保护等多个维度保护服务的稳定性。
- **Nacos**：阿里巴巴开源产品，一个更易于构建云原生应用的动态服务发现、配置管理和服务管理平台。
- **RocketMQ**：Apache RocketMQ™ 基于 Java 的高性能、高吞吐量的分布式消息和流计算平台。
- **Dubbo**：Apache Dubbo™ 是一款高性能 Java RPC 框架。
- **Seata**：阿里巴巴开源产品，一个易于使用的高性能微服务分布式事务解决方案。
- **Alibaba Cloud ACM**：一款在分布式架构环境中对应用配置进行集中管理和推送的应用配置中心产品。
- **Alibaba Cloud OSS**: 阿里云对象存储服务（Object Storage Service，简称 OSS），是阿里云提供的海量、安全、低成本、高可靠的云存储服务。您可以在任何应用、任何时间、任何地点存储和访问任意类型的数据。
- **Alibaba Cloud SchedulerX**: 阿里中间件团队开发的一款分布式任务调度产品，提供秒级、精准、高可靠、高可用的定时（基于 Cron 表达式）任务调度服务。
- **Alibaba Cloud SMS**: 覆盖全球的短信服务，友好、高效、智能的互联化通讯能力，帮助企业迅速搭建客户触达通道。

**主要功能**

- **服务限流降级**：默认支持 WebServlet、WebFlux, OpenFeign、RestTemplate、Spring Cloud Gateway, Zuul, Dubbo 和 RocketMQ 限流降级功能的接入，可以在运行时通过控制台实时修改限流降级规则，还支持查看限流降级 Metrics 监控。
- **服务注册与发现**：适配 Spring Cloud 服务注册与发现标准，默认集成了 Ribbon 的支持。
- **分布式配置管理**：支持分布式系统中的外部化配置，配置更改时自动刷新。
- **消息驱动能力**：基于 Spring Cloud Stream 为微服务应用构建消息驱动能力。
- **分布式事务**：使用 @GlobalTransactional 注解， 高效并且对业务零侵入地解决分布式事务问题。。
- **阿里云对象存储**：阿里云提供的海量、安全、低成本、高可靠的云存储服务。支持在任何应用、任何时间、任何地点存储和访问任意类型的数据。
- **分布式任务调度**：提供秒级、精准、高可靠、高可用的定时（基于 Cron 表达式）任务调度服务。同时提供分布式的任务执行模型，如网格任务。网格任务支持海量子任务均匀分配到所有 Worker（schedulerx-client）上执行。
- **阿里云短信服务**：覆盖全球的短信服务，友好、高效、智能的互联化通讯能力，帮助企业迅速搭建客户触达通道。

#### 组件版本关系

| Spring Cloud Alibaba Version                              | Sentinel Version | Nacos Version | RocketMQ Version | Dubbo Version | Seata Version |
| --------------------------------------------------------- | ---------------- | ------------- | ---------------- | ------------- | ------------- |
| 2.2.6.RELEASE                                             | 1.8.1            | 1.4.2         | 4.4.0            | 2.7.8         | 1.3.0         |
| 2021.1 or 2.2.5.RELEASE or 2.1.4.RELEASE or 2.0.4.RELEASE | 1.8.0            | 1.4.1         | 4.4.0            | 2.7.8         | 1.3.0         |
| 2.2.3.RELEASE or 2.1.3.RELEASE or 2.0.3.RELEASE           | 1.8.0            | 1.3.3         | 4.4.0            | 2.7.8         | 1.3.0         |
| 2.2.1.RELEASE or 2.1.2.RELEASE or 2.0.2.RELEASE           | 1.7.1            | 1.2.1         | 4.4.0            | 2.7.6         | 1.2.0         |
| 2.2.0.RELEASE                                             | 1.7.1            | 1.1.4         | 4.4.0            | 2.7.4.1       | 1.0.0         |
| 2.1.1.RELEASE or 2.0.1.RELEASE or 1.5.1.RELEASE           | 1.7.0            | 1.1.4         | 4.4.0            | 2.7.3         | 0.9.0         |
| 2.1.0.RELEASE or 2.0.0.RELEASE or 1.5.0.RELEASE           | 1.6.3            | 1.1.1         | 4.4.0            | 2.7.3         | 0.7.1         |

#### 毕业版本依赖关系

| Spring Cloud Version        | Spring Cloud Alibaba Version      | Spring Boot Version |
| --------------------------- | --------------------------------- | ------------------- |
| Spring Cloud 2020.0.1       | 2021.1                            | 2.4.2               |
| Spring Cloud Hoxton.SR9     | 2.2.6.RELEASE                     | 2.3.2.RELEASE       |
| Spring Cloud Greenwich.SR6  | 2.1.4.RELEASE                     | 2.1.13.RELEASE      |
| Spring Cloud Hoxton.SR3     | 2.2.1.RELEASE                     | 2.2.5.RELEASE       |
| Spring Cloud Hoxton.RELEASE | 2.2.0.RELEASE                     | 2.2.X.RELEASE       |
| Spring Cloud Greenwich      | 2.1.2.RELEASE                     | 2.1.X.RELEASE       |
| Spring Cloud Finchley       | 2.0.4.RELEASE(停止维护，建议升级) | 2.0.X.RELEASE       |
| Spring Cloud Edgware        | 1.5.1.RELEASE(停止维护，建议升级) | 1.5.X.RELEASE       |

`官方版本关系说明文档: https://github.com/alibaba/spring-cloud-alibaba/wiki/%E7%89%88%E6%9C%AC%E8%AF%B4%E6%98%8E `

#### 工程准备

##### 依赖管理工程(spring-cloud-alibaba-version-parent)

创建该pom工程主要用于管理依赖使用的版本。

**pom.xml**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.gitee</groupId>
    <artifactId>spring-cloud-alibaba-version-parent</artifactId>
    <version>0.0.1</version>

    <properties>
        <!-- 编码与JDK版本 -->
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
        <!-- SpringBoot SpringCloud Spring Cloud Alibaba 三件套 -->
        <spring.boot.version>2.5.2</spring.boot.version>
        <spring.cloud.version>2020.0.3</spring.cloud.version>
        <spring.cloud.alibaba.version>2021.1</spring.cloud.alibaba.version>
        <!-- JSON 转换依赖 -->
        <fastjson.version>1.2.78</fastjson.version>
        <!-- 工具包 -->
        <hutool.version>5.7.3</hutool.version>
        <mapstruct.version>1.2.0.Final</mapstruct.version>
        <lombok.version>1.18.20</lombok.version>
        <!-- Swagger -->
        <springfox.boot.starter.version>3.0.0</springfox.boot.starter.version>
    </properties>

    <!-- 开发人员 -->
    <developers>
        <developer>
            <name>wuwentao</name>
            <email>lovelyWu98k@gmail.com</email>
        </developer>
    </developers>

    <!-- 引入依赖管理 -->
    <dependencyManagement>
        <dependencies>
            <!-- spring boot 依赖 -->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>${spring.boot.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <!-- spring cloud 依赖 -->
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring.cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <!-- spring cloud alibaba 依赖 -->
            <dependency>
                <groupId>com.alibaba.cloud</groupId>
                <artifactId>spring-cloud-alibaba-dependencies</artifactId>
                <version>${spring.cloud.alibaba.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <!-- 构建方式 -->
    <build>
        <finalName>${project.name}</finalName>
        <resources>
            <resource>
                <directory>src/main/resources</directory>
                <filtering>true</filtering>
            </resource>
        </resources>
        <!-- 打包插件管理 -->
        <pluginManagement>
            <plugins>
                <!-- Spring Boot 打包插件 -->
                <plugin>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-maven-plugin</artifactId>
                    <version>${spring.boot.version}</version>
                    <configuration>
                        <finalName>${project.build.finalName}</finalName>
                        <layers>
                            <enabled>true</enabled>
                        </layers>
                    </configuration>
                    <executions>
                        <execution>
                            <goals>
                                <goal>repackage</goal>
                            </goals>
                        </execution>
                    </executions>
                </plugin>
            </plugins>
        </pluginManagement>
    </build>

</project>
```

##### 服务公共依赖工程(spring-cloud-alibaba-common)

创建该jar工程主要用于存放业务服务公用的内容，如统一响应实体，统一异常处理等等。

**pom.xml**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.gitee</groupId>
        <artifactId>spring-cloud-alibaba-version-parent</artifactId>
        <version>0.0.1</version>
    </parent>

    <groupId>com.gitee</groupId>
    <artifactId>spring-cloud-alibaba-common</artifactId>
    <version>0.0.1</version>

    <dependencies>
        <!-- 工具依赖 -->
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
            <version>${hutool.version}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-lang3</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct-jdk8</artifactId>
            <version>${mapstruct.version}</version>
        </dependency>
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct-processor</artifactId>
            <version>${mapstruct.version}</version>
        </dependency>
		<!-- fastjson 序列化 -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>${fastjson.version}</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.20</version>
            <scope>provided</scope>
        </dependency>
        <!-- 引用Swagger类 -->
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-boot-starter</artifactId>
            <version>${springfox.boot.starter.version}</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>

</project>
```

##### 请求统一响应返回类定义

创建该类用于Controller处理完成后返回给前端或调用放的返回值对象。

**Response.java**

```java
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 请求统一响应实体类
 *
 * @param <T> 返回结果类型
 * @author wentao.wu
 */
@Data
@ApiModel(value = "请求响应实体类")
public class Response<T> {
    @ApiModelProperty(value = "请求成功响应编码")
    private String code;
    @ApiModelProperty(value = "请求成功响应消息")
    private String msg;
    @ApiModelProperty(value = "请求失败响应编码")
    private String errorCode;
    @ApiModelProperty(value = "请求失败响应消息")
    private String errorMsg;
    @ApiModelProperty(value = "返回结果")
    private T result;
}
```

##### 统一业务服务异常定义

创建该类主要用户识别，发生该异常是由业务服务抛出，后期可以做全局异常统一处理。

**ServiceException.java**

```java
import lombok.Getter;

/**
 * 服务业务运行时异常
 *
 * @author wentao.wu
 */
@Getter
public class ServiceException extends RuntimeException {
    public ServiceException(String errorCode, String errorMsg) {
        super();
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    /**
     * 异常编码
     */
    private String errorCode;
    /**
     * 异常信息
     */
    private String errorMsg;
}
```

#### 注意事项

后期所有工程都依赖上方两个工程。

#### 目录

| 标题                                                  | 传送门                                                   |
| ----------------------------------------------------- | -------------------------------------------------------- |
| Nacos Server 介绍安装部署篇                           | 未更新                                                   |
| Sentinle Server 介绍安装部署篇                        | 未更新                                                   |
| Seata Server 介绍安装部署篇                           | 未更新                                                   |
| RocketMQ  Server 介绍安装部署篇                       | 未更新                                                   |
| SkyWalking 介绍安装部署篇                             | 未更新                                                   |
| Spring Cloud Alibaba 介绍及工程准备                   | [前往](https://www.cnblogs.com/SimpleWu/p/15476427.html) |
| Spring Cloud Alibaba 使用Nacos作为服务注册中心        | [前往](https://www.cnblogs.com/SimpleWu/p/15476459.html) |
| Spring Cloud Alibaba 使用Nacos作为配置管理中心        | 前往                                                     |
| Spring Cloud Alibaba 使用RestTemplate进行服务消费     | 前往                                                     |
| Spring Cloud Alibaba 使用Feign进行服务消费            | 前往                                                     |
| Spring Cloud Alibaba 使用Seata解决分布式事务          | 前往                                                     |
| Spring Cloud Alibaba 使用Gateway作为服务网关          | 前往                                                     |
| Spring Cloud Alibaba 使用Sentinel进行熔断&降级        | 前往                                                     |
| Spring Cloud Alibaba 使用SkyWalking进行分布式链路跟踪 | 前往                                                     |
| Spring Cloud Alibaba 使用RocketMQ进行消息生产         | 前往                                                     |
| Spring Cloud Alibaba 使用RocketMQ进行消息消费         | 前往                                                     |
| 持续更新中....                                        | 持续更新                                                 |

##### 源码代码存放地址
gitee: https://gitee.com/SimpleWu/spring-cloud-alibaba-example.git
cnblogs: https://www.cnblogs.com/SimpleWu