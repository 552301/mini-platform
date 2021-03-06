# Mini-Platform轻量级微服务平台

- Mini-Platform致力于打造更简洁易用的轻量级微服务治理平台，更易于实施与运维；
- 核心技术：SpringBoot、Spring Cloud、OAuth2（自研）、MyBatis、Redis、MySQL；
- 核心功能：服务注册与发现、服务网关、负载均衡、统一认证、配置中心、异常处理等；

---
## mini-config - 配置中心
- 采用Spring Cloud Config作为统一的配置中心
- 所有服务启动均需要读取配置，因此配置中心为第一个启动服务；
- 其他服务通过uri访问配置中心，当启动多个节点时，需要通过nginx等做代理达到高可用

---
## mini-discovery - 注册中心
- 采用Eureka做服务自动注册与发现；
- 所有服务启动均需注册到注册中心，因此注册中心为第二个启动服务；
- 注册中心启动多个节点时，可以在配置中心的config-common/common-discovery中配置多个节点地址

---
## mini-gateway - API网关
- 采用Zuul做服务网关；
- 外部请求均需要通过网关进行转发，因此API网关为第三启动服务；
- API网关启动多个节点时，需要通过nginx等做代理达到高可用；
- Zuul支持三种路由策略：
  1. 支持基于service-id的动态路由策略，支持负载均衡，支持服务的**自动**注册与发现，当服务地址变化后**无需手动配置**，当后端服务引入Eureka client时可选用；
  2. 支持基于url的动态路由策略，支持负载均衡，支持服务的**手动**注册与发现，当服务地址变化后**需要手动配置**，当后端服务基于传统HTTP调用时可选用；
  3. 支持基于默认url的动态路由策略，不支持负载均衡，后端服务需要单独处理负载均衡（如Nginx），支持服务的**手动**注册与发现，配置简单，可在测试中使用；
- 支持服务异常重试，建议只开启GET的重试，且确保GET的幂等，否则建议关闭；


---
## mini-admin - 监控中心
- 采用Spring Boot Admin作为统一的监控中心
- 支持钉钉告警

---
## mini-auth - 授权中心
- 为了更简单易用，OAuth Server采用自研实现。
- GrantType支持password、client_credentials、refresh_token。
- Token支持延迟吊销、滑动过期和绝对过期。
- 用户名密码验证支持直连用户中心数据库验证和调用远程服务验证两种方式。
- 密码模式授权，用于客户端与服务器之间的授权，流程如下：
![oauth-password-flow](./docs/images/oauth-password-flow.png "密码模式授权流程")
注：图例为三次请求，1.1-1.3为首次认证；2.1-2.5为通过Access Token访问后端资源；3.1-3.3为使用Refresh Token获取新的Access Token，可用于Access Token过期前刷新Token；
红色字体是Password与Client授权方式不同的地方。

- 客户端模式授权，用于服务器与服务器之间的授权，流程如下：
![oauth-client-flow](./docs/images/oauth-client-flow.png "客户端模式授权流程")

- 微服务模式授权示例，是获取到Access Token后请求后端资源的流程细化，如下：
![oauth-multi-services-flow](./docs/images/oauth-multi-services-flow.png "客户端模式授权流程")

---
## Gateway - ACL
- Gateway中集成的ACL(访问控制列表)模块，对API进行权限控制。
- 待实现

---
## Gateway - RateLimiting  
- Gateway中集成的限流模块，对API进行流量控制。
- 待实现

---
## Gateway - Other  
- Gateway中的Log、Metrics、Trace、Alert、Security、Canary等模块。
- 待实现

---
## OAuth-Client
- 使用OAuth的客户端使用；
- 客户端引用后，只需要继承BaseController便可方便的获取用户信息；
- 参考示例：[UserController.java](https://github.com/hiling/mini-platform/blob/master/modules/user/src/main/java/com/github/hiling/user/controller/UserController.java)

---
## 项目中使用的其他技术介绍
- **Lombok** 是一种 Java™ 实用工具，可用来帮助开发人员消除 Java 的冗长，
尤其是对于简单的 Java 对象（POJO）。它通过注解实现这一目的。
  - 官网地址：https://www.projectlombok.org/ 
  - 项目地址：https://github.com/rzwitserloot/lombok
  - 中文介绍：https://blog.csdn.net/motui/article/details/79012846

- **Swagger** 是一款RESTFUL接口的文档在线自动生成+功能测试功能软件。
  项目中集成Swagger3，访问地址：http://localhost:8085/swagger-ui/index.html
  - 官网地址：https://swagger.io/

- **MyBatis-Plus**（简称 MP）是一个 MyBatis 的增强工具，
在 MyBatis 的基础上只做增强不做改变，为简化开发、提高效率而生。
   - 官网地址：http://mp.baomidou.com/
   - 项目地址：https://github.com/baomidou/mybatis-plus
  
- **Prometheus** 是一套开源的新一代Metrics系统监控报警框架，是CNCF中重要的一员，它将所有信息都存储为时间序列数据；因此实现一种Profiling监控方式，
  实时分析系统运行的状态、执行时间、调用次数等，以找到系统的热点，为性能优化提供依据。
  可对核心业务指标、应用指标、系统指标等做高效的监控，可与Grafana结合打造出优秀的监控平台。
   - 官网地址：https://prometheus.io/
   - 项目地址：https://github.com/prometheus

- **Druid** 是阿里巴巴数据库事业部出品，为监控而生的数据库连接池。
  - 项目地址：https://github.com/alibaba/druid
  
- **JMH** 是一个Java的微基准测试框架，精度可以精确到微秒级。
  - 项目地址：http://openjdk.java.net/projects/code-tools/jmh/
  - 官方示例：http://hg.openjdk.java.net/code-tools/jmh/file/tip/jmh-samples/src/main/java/org/openjdk/jmh/samples/
  - 中文介绍：https://www.xncoding.com/2018/01/07/java/jmh.html
  - 项目中OAuth基准测试代码
  ![create_token_test](./docs/images/create_token_test.png "基准测试代码")
  注：测试代码：[UserApplicationTests.java](https://github.com/hiling/mini-platform/blob/master/modules/user/src/test/java/com/github/hiling/user/UserApplicationTests.java)
  - 测试结果
  ![create_token_test_result](./docs/images/create_token_test_result.png "基准测试结果")
  注：在开发环境测试，电脑配置2C(i5-6300U)/8G/SSD，本机MySQL，使用相关参数，未考虑DB缓存等影响。
  
- **Spring Boot Maven Plugin & Apache Maven Dependency Plugin** 可以将外部依赖jar与项目分离，
解决发布包过大问题。部署时可以将外部依赖包先上传至服务器，启动时需要使用参数-Dloader.path="lib/"加载外部依赖的jar包，
当依赖的外部jar包未更新时，不需要每次给服务器上传。
项目地址：
  - https://docs.spring.io/spring-boot/docs/current/maven-plugin/
  - http://maven.apache.org/components/plugins/maven-dependency-plugin/

#### 欢迎Star和Fork，微信号：HilingWang，欢迎交流！