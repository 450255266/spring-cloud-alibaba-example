#### 为什么需要注册中心?

在分布式架构中，服务会注册到这里，当服务需要调用其它服务时，就到这里找到服务的地址，进行调用；服务管理,核心是有个服务注册表，心跳机制动态维护 ；

#### 服务注册

创建普通SpringBoot工程(spring-cloud-alibaba-service-user)该工程当前用于使用Nacos进行服务注册。

**pom.xml**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.gitee</groupId>
    <artifactId>spring-cloud-alibaba-service-user</artifactId>
    <version>0.0.1</version>
    <dependencyManagement>
        <dependencies>
            <!-- 依赖管理父工程 -->
            <dependency>
                <groupId>com.gitee</groupId>
                <artifactId>spring-cloud-alibaba-version-parent</artifactId>
                <version>${project.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
    <dependencies>
        <!-- 公共模块 -->
        <dependency>
            <groupId>com.gitee</groupId>
            <artifactId>spring-cloud-alibaba-common</artifactId>
            <version>${project.version}</version>
        </dependency>
        <!-- web服务 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <exclusions>
                <exclusion>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-tomcat</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <!-- undertow服务器 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-undertow</artifactId>
        </dependency>
        <!-- 参数校验 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        <!-- nacos 服务治理 -->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>
        <!-- nacos 配置中心 -->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
        </dependency>
        <!-- openfeign 服务发现调用 -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>
    </dependencies>
</project>
```

**application.yaml**

```yaml
server:
  port: 8080
spring:
  application:
    name: service-user
  cloud:
    nacos:
      discovery:
        #注册中心地址
        server-addr: localhost:8848
        #服务元数据
        metadata:
          version: 0.0.1
          appname: ${spring.application.name}
```

**UserServiceApplication.java**

```java
/**
 * 用户中心服务启动类
 *
 * @author wentao.wu
 */
@EnableDiscoveryClient//服务注册
@SpringBootApplication
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
```

**HelloWorldController.java**

```java
/**
 * HelloWorld Web
 *
 * @author wentao.wu
 */
@RequestMapping(value = "/hello/")
@RestController
public class HelloWorldController {
    @GetMapping("/say/{name}")
    public Response<String> say(@PathVariable("name") String name) {
        Response<String> response = new Response<>();
        response.setCode("1");
        response.setMsg("你好啊," + name + "!");
        return response;
    }
}
```

以上内容是整个工程当前的内容，直接运行访问:http://localhost:8080/hello/say/nacos，后者输出:

```json
{
    "code": "1",
    "msg": "你好啊,nacos!",
    "errorCode": null,
    "errorMsg": null,
    "result": null
}
```

看到以上返回内容代表整个服务完全启动成功。进入nacos管理界面：http://localhost:8848/nacos 默认登录账号与密码为 nacos/nacos

进入服务管理菜单->服务列表就能够看到我们注册进去的服务。

![](https://gitee.com/SimpleWu/spring-cloud-alibaba-example/raw/master/cnblogs/images/nacos/20211028095355.png)

点击详情即可查看服务元数据:

![image-20211028095526214](https://gitee.com/SimpleWu/spring-cloud-alibaba-example/raw/master/cnblogs/images/nacos/20211028095526214.png)

从Nacos之中服务列表能够看到我们所注册的服务意味这服务已经注册到Nacos Server端。

#### 多环境服务隔离

用于进行租户粒度的配置隔离。不同的命名空间下，可以存在相同的 Group 或 Data ID 的配置。Namespace 的常用场景之一是不同环境的配置的区分隔离，例如开发测试环境和生产环境的资源（如配置、服务）隔离等。

在资源不够的情况下，开发环境(dev),综合测试环境(sit),业务测试环境(uat)通常会公用一套Nacos Server，使用Namespace 来区分环境，然后再通过dataID来指定每个服务的配置文件，这样就可以每个环境的服务注册发现不会相互干扰，并且也不会用到其他环境的配置文件。

##### 创建命名空间

从Nacos菜单中命名空间点击新建命名空间，输入空间名称与描述，命名空间ID可填可不填，不填则会自动生成一串UUID的字符。

![](https://gitee.com/SimpleWu/spring-cloud-alibaba-example/raw/master/cnblogs/images/nacos/20211028100210.png)

![](https://gitee.com/SimpleWu/spring-cloud-alibaba-example/raw/master/cnblogs/images/nacos/20211028100357.png)

` 记录自动生成的命名空间ID:7e3699fa-09eb-4d47-8967-60f6c98da94a `

##### 工程使用对应的命名空间

application.yaml

```yaml
server:
  port: 8080
spring:
  application:
    name: service-user
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
        metadata:
          version: 0.0.1
          appname: ${spring.application.name}
        #指定命名空间 对应dev环境
        namespace: 7e3699fa-09eb-4d47-8967-60f6c98da94a
        #指定分组 案例组
        group: EXAMPLE-GROUP
        #指定集群环境 华南
        cluster-name: HuaNan
```

这里添加了3个配置。

Namespace：使用dev环境的命名空间id指定该服务部署在dev环境下与其他命名空间服务进行隔离。

Group: 标识该服务属于哪个项目组。

Cluster-name：标识该服务部署在华南地区。

Nacos Server最终由namespace->group->ServiceID->Cluster-name获取到对应的服务实例列表。

配置好之后运行UserServiceApplication.java对服务进行重启，重启之后服务将重新注册到Nacos中并且在Web界面服务列表中Dev命名空间进行显示。

![](https://gitee.com/SimpleWu/spring-cloud-alibaba-example/raw/master/cnblogs/images/nacos/20211028101230.png)

![](https://gitee.com/SimpleWu/spring-cloud-alibaba-example/raw/master/cnblogs/images/nacos/20211028101252.png)

##### 注意事项

**Namespace与Group配置后只有Namespace与Group相同的才能够算作一套系统，只有同一套系统才能够内部进行快速访问，如使用feign进行远程调用时，GROUP=金融系统与GROUP=电商系统两个不同的GROUP在其中是不能够直接通过Feign服务发现进行访问的，对应的业务服务只会发现对应Namespace与Group中的服务。**

#### 源码代码存放地址
gitee: https://gitee.com/SimpleWu/spring-cloud-alibaba-example.git
cnblogs: https://www.cnblogs.com/SimpleWu

