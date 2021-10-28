#### 为什么需要配置中心？

动态配置管理是 Nacos 的三大功能之一，通过动态配置服务，我们可以在所有环境中以集中和动态的方式管理所有应用程序或服务的配置信息。

动态配置中心可以实现配置更新时无需重新部署应用程序和服务即可使相应的配置信息生效，这极大了增加了系统的运维能力。

#### 服务配置中心

##### 工程改造

继续使用之前的工程:`spring-cloud-alibaba-service-user`

pom.xml中增加

```xml
<!-- nacos 配置中心 -->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
</dependency>
<!-- 使用 bootstrap -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-bootstrap</artifactId>
</dependency>
```

将application.yaml变更为bootstrap.yaml，并且增加nacos配置

```yaml
server:
  port: 8080
spring:
  application:
    name: service-user
  cloud:
    nacos:
      config:
        server-addr: localhost:8848
        #指定命名空间 对应dev环境
        namespace: 7e3699fa-09eb-4d47-8967-60f6c98da94a
        #指定分组 案例组
        group: EXAMPLE-GROUP
        #指定集群环境 华南
        cluster-name: HuaNan
        #指定配置文件的类型，默认是 properties
        file-extension: properties
         #前缀默认应用名称${spring.application.name}
        prefix: ${spring.application.name}
```

获取配置文件的规则为:`${spring.cloud.nacos.config.prefix}-${spring.profiles.active}.${spring.cloud.nacos.config.file-extension}`

按照bootstrap.yaml中的配置意思就是，在dev命名空间EXAMPLE-GROUP组中寻找DataId为:service-user.properties的配置文件。

##### Nacos创建配置文件

进入Nacos Web界面菜单配置管理->配置列表->进入dev命名空间 创建配置文件

![](https://gitee.com/SimpleWu/spring-cloud-alibaba-example/raw/master/cnblogs/images/nacos/20211028141458.png)

![](https://gitee.com/SimpleWu/spring-cloud-alibaba-example/raw/master/cnblogs/images/nacos/20211028141508.png)

##### 业务服务使用配置

NacosConfigController.java

```java
import com.gtiee.example.common.exception.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Nacos Config
 *
 * @author wentao.wu
 */
@RequestMapping(value = "/nacos/config/")
@RestController
public class NacosConfigController {
    @Value("${nacos.config.msg}")
    private String msg;
    @GetMapping("/getMsg")
    public Response<String> getMsg() {
        Response<String> response = new Response<>();
        response.setCode("1");
        response.setMsg(msg);
        return response;
    }
}
```

重启spring-cloud-alibaba-service-user工程，请求访问接口：http://localhost:8080/nacos/config/getMsg 请求成功返回值为

```JSON
{
    "code": "1",
    "msg": "这是一条存放在配置中心的消息",
    "errorCode": null,
    "errorMsg": null,
    "result": null
}
```

代码中的成员变量msg读取的就是配置中心键值对的键为nacos.config.msg的配置。

##### 动态刷新配置

需要动态刷新配置，只需要在NacosConfigController.java中增加类注解@RefreshScope

```java
@RequestMapping(value = "/nacos/config/")
@RestController
@RefreshScope//标注该类对配置进行监听，动态刷新
public class NacosConfigController {
	...省略重复代码
}
```

增加注解后在Nacos Web界面中对配置中的nacos.config.msg对应值进行修改为: 你好,Nacos！。修改完成后重新发布即可，当Nacos Client接收到Nacos Server推送的配置变更消息则会立即刷新变量。再次访问http://localhost:8080/nacos/config/getMsg 请求成功返回值为

```json
{
    "code": "1",
    "msg": "你好,Nacos！",
    "errorCode": null,
    "errorMsg": null,
    "result": null
}
```

#### 源码代码存放地址
gitee: https://gitee.com/SimpleWu/spring-cloud-alibaba-example.git
cnblogs: https://www.cnblogs.com/SimpleWu
持续更新目录:https://www.cnblogs.com/SimpleWu/p/15476427.html