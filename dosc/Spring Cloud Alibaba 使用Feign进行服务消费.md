#### 为什么使用Feign？

Feign可以把Rest的请求进行隐藏，伪装成类似SpringMVC的Controller一样。你不用再自己拼接url，拼接参数等等操作，一切都交给Feign去做。

#### 使用Feign进行消费

将需要使用feign的工程增加一下依赖

pom.xml

```xml
<!-- openfeign 服务发现调用 -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

启动类增加启用feign注解EnableFeignClients

UserServiceApplication.java

```java
/**
 * 用户中心服务启动类
 *
 * @author wentao.wu
 */
@EnableDiscoveryClient//服务注册
@EnableFeignClients//服务发现
@SpringBootApplication
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
```

创建feign调用接口

```java
/**
 * service-member服务远程调用接口
 * @author wentao.wu
 */
@FeignClient(name = "service-member")
public interface MemberInfoControllerClient {
    /**
     * 通过用户名称获取会员信息
     * @param username
     * @return
     */
    @GetMapping("/member/info/getUserMember/{username}")
    public Response<Map<String, Object>> getUserMember(@PathVariable("username") String username);
}
```

创建请求进行feign消费

FeignConsumerController.java

```java
import com.gitee.eample.user.service.feign.MemberInfoControllerClient;
import com.gtiee.example.common.exception.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Feign template consumer
 *
 * @author wentao.wu
 */
@RestController
@RequestMapping("/feign/consumer")
public class FeignConsumerController {
    @Autowired
    private MemberInfoControllerClient client;

    @GetMapping("/getUserInfo/{username}")
    public Response<Map<String, Object>> getUserInfo(@PathVariable("username") String username) {
        Response<Map<String, Object>> response = client.getUserMember(username);
        Map<String, Object> userinfo = new HashMap<>();
        userinfo.put("userage", "100");
        userinfo.put("email", "xxx@email.com");
        response.getResult().putAll(userinfo);
        response.setMsg("获取用户信息成功!");
        return response;
    }
}
```

以上代码通过Feign生命消费者接口方法，Feign将自动生成代理方法进行远程调用。访问请求http://localhost:8080/feign/consumer/getUserInfo/zhangsan 进行消费返回结果为

```json
{
    "code": "1",
    "msg": "获取用户信息成功!",
    "errorCode": null,
    "errorMsg": null,
    "result": {
        "level": "vip1",
        "username": "zhangsan",
        "userage": "100",
        "email": "xxx@email.com"
    }
}
```

#### 源码代码存放地址

gitee: https://gitee.com/SimpleWu/spring-cloud-alibaba-example.git
cnblogs: https://www.cnblogs.com/SimpleWu