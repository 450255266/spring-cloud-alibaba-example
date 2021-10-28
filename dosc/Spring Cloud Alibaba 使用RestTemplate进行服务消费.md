#### 创建服务提供者工程

创建spring-cloud-alibaba-service-member工程，会员中心服务该服务提供用户会员信息。

pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.gitee</groupId>
    <artifactId>spring-cloud-alibaba-service-member</artifactId>
    <version>0.0.1</version>

    <dependencyManagement>
        <dependencies>
            <!-- spring boot 依赖 -->
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
        <!-- 负载均衡 -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-loadbalancer</artifactId>
        </dependency>
        <!-- 使用 bootstrap -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-bootstrap</artifactId>
        </dependency>
    </dependencies>
</project>
```

MemberServiceApplication.java 启动类

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.RestController;

/**
 * 会员中心服务启动类
 *
 * @author wentao.wu
 */
@RestController
@EnableDiscoveryClient
@SpringBootApplication
public class MemberServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MemberServiceApplication.class, args);
    }
}
```

Bootstrap.yaml

```yaml
server:
  port: 8081
spring:
  application:
    name: service-member
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
      config:
        server-addr: localhost:8848
        #指定命名空间 对应dev环境
        namespace: 7e3699fa-09eb-4d47-8967-60f6c98da94a
        #指定分组 案例组
        group: EXAMPLE-GROUP
        #指定集群环境 华南
        cluster-name: HuaNan
        #指定配置文件的类型，默认是properties
        file-extension: properties
        #前缀${spring.application.name}
        prefix: service-user
```

MemberInfoController.java 会员信息获取接口

```java
/**
 * 会员信息接口
 *
 * @author wentao.wu
 */
@RestController
@RequestMapping("/member/info")
public class MemberInfoController {
    /**
     * 获取用户会员信息
     *
     * @param username
     * @return
     */
    @GetMapping("/getUserMember/{username}")
    public Response<Map<String, Object>> getUserMember(@PathVariable("username") String username) {
        Response<Map<String, Object>> response = new Response<>();
        response.setCode("1");
        response.setMsg("获取会员信息成功!");

        //从数据库根据用户查询会员信息
        Map<String, Object> result = new HashMap<>();
        result.put("level", "vip1");
        result.put("username", username);
        response.setResult(result);
        return response;
    }
}
```

#### 使用RestTemplate进行消费

使用前面文章创建的用户中心服务spring-cloud-alibaba-service-user进行服务消费。

增加RestTemplate注入配置

RestTemplateConfig.java

```java
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * RestTample配置
 */
@Configuration
public class RestTemplateConfig {
    /**
     * 注入RestTample模板并且开启负载均衡
     */
    @Bean
    @LoadBalanced
    public RestTemplate getRestTample(){
        return new RestTemplate();
    }
}
```

**获取用户信息同时包含用户会员信息**

增加RestTamplteConsumerController.java

```java
import java.util.HashMap;
import java.util.Map;

/**
 * rest template consumer
 *
 * @author wentao.wu
 */
@RestController
@RequestMapping("/rest/consumer")
public class RestTamplteConsumerController {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @GetMapping("/getUserInfo/{username}")
    public Response<Map<String, Object>> getUserInfo(@PathVariable("username") String username) {
        // 通过的负载均衡接口获取服务实例信息
        ServiceInstance serviceInstance = loadBalancerClient.choose("service-member");
        String url = "http://" + serviceInstance.getServiceId() + ":" + serviceInstance.getPort() + "/member/info/getUserMember/" + username;
        String result = restTemplate.getForObject(url, String.class);
        Response<Map<String, Object>> response = (Response<Map<String, Object>>) JSONObject.parse(result);
        Map<String,Object> userinfo = new HashMap<>();
        userinfo.put("userage","100");
        userinfo.put("email","xxx@email.com");
        response.getResult().putAll(userinfo);
        response.setMsg("获取用户信息成功!");
        return response;
    }

}
```

请求用户服务获取信息:http://localhost:8080/rest/consumer/getUserInfo/zhangsan 返回值为

```json
{
    "code": "1",
    "msg": "获取会员信息成功!",
    "errorCode": null,
    "errorMsg": null,
    "result": {
        "level": "vip1",
        "userage": "100",
        "email": "xxx@email.com",
        "username": "zhangsan"
    }
}
```

以上代码通过负载均衡客户端获取到会员服务的一个实例并且使用实例的ip与端口拼接成一个请求路径，并且带上具体访问的请求地址对会员服务进行请求，请求到会员信息后将会员信息与用户信息合并返回到前端。

#### 源码代码存放地址

gitee: https://gitee.com/SimpleWu/spring-cloud-alibaba-example.git
cnblogs: https://www.cnblogs.com/SimpleWu