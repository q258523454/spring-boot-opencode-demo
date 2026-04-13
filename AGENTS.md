## AGENTS.MD

## 项目概述
本项目是基于 Spring Boot 3.x、JDK 21 和 Maven 构建的 Java 后端服务，采用经典分层架构。
该文件为 AI 编码助手（如 OpenCode、Cursor）提供项目上下文与代码生成规范，采用 **DTO/BO/VO** 模式进行数据传输与展示，确保代码的可维护性和扩展性。
确保生成的代码风格一致、职责清晰、符合业界最佳实践。

## 技术栈
- **语言**：Java 21
- **构建工具**：Maven
- **框架**：Spring Boot 3.x, Spring MVC, Mybatis plus、Druid
- **数据库访问**：MyBatis（Mapper XML 方式）
- **对象映射**：MapStruct
- **切面编程**：Spring AOP
- **工具库**：Lombok (推荐)、Apache Commons、Hutool 等
- **数据库**：MySQL

## 分层架构与包职责
```
├─src
│└─main
│    ├─java
│    │  └─com
│    │      └─mysql
│    │          └─base
│    │              ├─annotation        # 存放自定义注解（如权限校验、日志、限流等）
│    │              ├─aspects           # 存放 AOP 切面类，实现横切逻辑（如日志、性能监控）
│    │              ├─config            # 存放配置类（如 WebConfig、RedisConfig、MyBatisConfig、DruidConfig）
│    │              ├─constant          # 存放常量类（静态 final 常量，如系统常量、响应码）
│    │              │  └─enums          # 存放枚举类型（如状态枚举、类型枚举）
│    │              ├─controller        # 控制器层，处理 HTTP 请求和响应（@RestController）
│    │              ├─converter         # 存放对象转换器（如 MapStruct 接口，用于 VO/DTO/BO 互转）
│    │              ├─exception         # 自定义异常类（如 BusinessException、SystemException）及全局异常处理器
│    │              ├─filter            # 过滤器（如请求日志、字符编码、JWT 校验等）
│    │              ├─manager           # 通用业务处理层，封装第三方调用、缓存、中间件操作，供 Service 调用
│    │              ├─pojo              # POJO 对象（普通 Java 对象，不包含业务逻辑）
│    │              │  ├─bo             # 业务对象（Business Object），用于业务层内部传递数据
│    │              │  ├─dto            # 数据传输对象（Data Transfer Object），用于层间传输（如 Service 返回给 Controller）
│    │              │  ├─entity         # 数据库实体类（与表一一映射，通常使用 JPA 或 MyBatis 注解）
│    │              │  └─vo             # 视图对象（View Object），用于前端展示（如 API 响应结构）
│    │              ├─repository        # MyBatis Mapper 接口（数据库访问层，与 resources/mapper 中 XML 对应）
│    │              ├─service           # 服务层接口
│    │              │  └─impl           # 服务层实现类（实现 serivce 接口，添加 @Service 注解）
│    │              ├─task              # 定时任务类（使用 @Scheduled 注解，存放批处理、定时作业）
│    │              └─utils             # 工具类（如日期处理、加密、ID 生成器、HTTP 客户端等）
│    └─resources
│        └─mapper                       # 存放 MyBatis 的 XML 映射文件（与 java/mapping 中的接口对应）
│        └─application.yml              # 存放配置
│        └─init.sql                     # 数据库初始化脚本
│
└─test                                  # 测试类,使用 JUnit 5 和 Mockito 进行测试。
│    ├─java
│    │  └─com
│    │      └─mysql
│    │          └─base
│    │              ├─controller        # controller测试
│    │              ├─integration       # 集成测试
│    │              ├─service           # service测试
```
以上目录，如果已存在，不要删除和修改。如果不存在，则创建目录

## 代码生成规范
### 1. 命名约定
#### 类命名
| 类型         | 命名规范                      | 示例                                     |
|------------|---------------------------|----------------------------------------|
| 实体类        | 表名驼峰                      | `User`, `Order`                        |
| DTO        | 功能描述 + `DTO`              | `UserLoginDTO`, `OrderCreateDTO`       |
| VO         | 功能描述 + `VO`               | `UserProfileVO`, `OrderDetailVO`       |
| BO         | 业务场景 + `BO`               | `UserAuthBO`, `OrderSummaryBO`         |
| Mapper 接口  | 实体名 + `Mapper`            | `UserMapper`                           |
| Service 接口 | 实体/领域名 + `Service`        | `UserService`                          |
| Service 实现 | 实体/领域名 + `ServiceImpl`    | `UserServiceImpl`                      |
| Controller | 实体/领域名 + `Controller`     | `UserController`                       |
| 配置类        | 功能描述 + `Config`           | `WebConfig`, `MyBatisConfig`           |
| 工具类        | 功能描述 + `Utils` 或 `Helper` | `JwtUtils`, `DateHelper`               |
| 自定义注解      | 功能描述 + 注解用途后缀             | `@RequirePermission`, `@ApiLog`        |
| 常量类        | 常量分组 + `Constants`        | `ErrorCodeConstants`, `CacheConstants` |
| 枚举         | 枚举含义 + `Enum`             | `UserStatusEnum`, `OrderTypeEnum`      |
| 异常类        | 异常类型 + `Exception`        | `BusinessException`, `SystemException` |

#### 变量命名
| 变量类型         | 命名规范                                   | 示例                                                       |
|--------------|----------------------------------------|----------------------------------------------------------|
| 成员变量（实例/静态）  | 小驼峰，见名知意                               | `private String userName;`                               |
| 局部变量         | 小驼峰，避免过长但不要牺牲语义                        | `List<UserDTO> activeUsers;`                             |
| 方法参数         | 小驼峰，与 DTO 字段名尽量保持一致                    | `public void update(UserDTO UserDTO)`                    |
| 常量           | 全大写 + 下划线                              | `public static final String DEFAULT_TIMEZONE = "GMT+8";` |
| 枚举常量         | 全大写 + 下划线                              | `PENDING, APPROVED, REJECTED`                            |
| 泛型类型参数       | 单个大写字母，有意义时可多个字母（如 `T`、`R`、`ID`、`DTO`） | `public interface Converter<S, T>`                       |
| 布尔变量         | 以 `is`、`has`、`can`、`should` 开头         | `private boolean isActive;`                              |
| 集合变量         | 用复数或类型 + `List`/`Map` 结尾               | `List<User> userList;` 或 `Map<Long, User> userMap;`      |
| Builder 模式字段 | 与实体字段一致，不额外加前缀                         | `User.builder().username("john").build();`               |

#### 目录结构
严格按照`分层架构与包职责`执行

### 2. 各层职责与约束
整体调用链路：
`Controller → Service → Manager → Repository`
或者：
`Controller → Service → Repository`

- **Controller**
  - 只负责接收请求、参数校验（`@Valid`）、调用 Service、封装统一响应。
  - 禁止包含业务逻辑。
  - 使用 `@RestController` 、 `@RequestMapping` 、`@RequestParam`、`@PathVariable` 等业界统一注解。
  - 返回类型统一为 `ResultVO<T>` 或 `ResponseEntity`。

- **Service**
  - 接口`Service`定义业务方法，实现类`ServiceImpl`处理核心业务逻辑。
  - 使用`@Service`注解
  - 事务管理使用 `@Transactional`，通常加在实现类或接口方法上。`@Transactional(rollbackFor = Exception.class)`
  - 可调用多个 `Repository` 或 `Manager` 完成业务。
  - 涉及对象转换时调用 Converter（MapStruct）。

- **Repository (Mapper)**
  - 接口添加 `@Repository`
  - 复杂查询写在对应的 XML 映射文件中（`resources/mapper` 下）。
  - 方法命名遵循 MyBatis 规范：`insert`, `update`, `delete`, `selectById`, `selectList` 等。

- **Manager**
  - 封装对第三方 API、缓存（Redis）、消息队列等通用能力的调用。
  - 降低 Service 复杂度，提高复用性。
  - 使用 `@Component` 注解。

- **Converter**
  - 使用 MapStruct 接口定义转换方法，编译期生成实现。
  - 命名：`XxxConverter`。
  - 示例：`UserConverter.INSTANCE.dtoToEntity(UserDTO dto)`。

- **异常处理**
  - 自定义业务异常继承 `RuntimeException`，提供错误码和消息。
  - 全局异常处理器使用 `@RestControllerAdvice`，捕获异常并返回统一错误响应。

- **工具类**
  - 所有方法均为静态方法，类使用 `final` 修饰并私有构造器。
  - 提供通用的无状态功能。

### 3. 注解使用建议
- **Lombok**（推荐）
  - `@Data`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor` 用于 POJO。
  - `@Slf4j` 用于日志。
- **Spring**
  - `@Value` 读取配置。
- **校验**
  - 使用 `@NotNull`, `@NotBlank`, `@Size` 等 JSR-303 注解。
- **类注释**：
  - 使用 Javadoc 格式，说明类的功能、作者固定为zhang、创建时间。
- **方法注释**：
  - 说明方法功能、参数含义、返回值、异常情况。
- **关键代码注释**： 
  - 对复杂逻辑或算法添加详细注释。
- **避免无意义注释**：
  - 无意义的注释不需要写，如 "get user" 这类显而易见的注释。

### 4. 代码模板示例
#### Controller 示例

```java
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResultVO<UserVO> register(@Valid @RequestBody UserDTO userDTO) {
        UserVO userVO = userService.register(userDTO);
        return ResultVO.success(userVO);
    }
}
```

#### Service 实现示例
接口层
注意：使用到`mybatis plus`功能
```java
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserService extends IService<User>{
    UserVO register(UserDTO userDTO);
}
```
实现层  
注意：使用到`mybatis plus`功能
```java
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private final UserMapper userMapper;
    
    @Autowired
    private final UserConverter userConverter;

    @Override
    @Transactional
    public UserVO register(UserDTO userDTO) {
        // 业务校验...
        User user = userConverter.dtoToUser(userDTO);
        userMapper.insert(user);
        return userConverter.entityToUserBO(user);
    }
}
```

#### Converter 示例（MapStruct）
```java
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserConverter {
    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    User dtoToUser(UserDTO userDTO);
    
    UserVO entityToUserVO(User User);
}
```

#### 全局异常处理
```java
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResultVO<Void> handleBusinessException(BusinessException e) {
        log.warn("业务异常：{}", e.getMessage());
        return ResultVO.error(e.getCode(), e.getMessage());
    }
}
```

## 配置与资源文件
- **application.yml**：Spring Boot 核心配置文件，按环境拆分（`application-dev.yml`, `application-prod.yml`）。
- **init.sql**：数据库初始化脚本（表结构、初始数据）。
- **resources/mapper**：MyBatis XML 映射文件，命名与 Mapper 接口保持一致。

## 性能优化
- 使用缓存（如 Redis）减少数据库访问
- 避免 N+1 查询问题
- 适当使用异步处理（`@Async`）
- 查询数据库的时候，使用分页查询

## 测试规范
### 基本测试规范
- 单元测试类放在 `src/test/java` 下，包结构与 `main` 保持一致。
- 使用 JUnit 5 + Mockito。
- 测试类命名：`XxxServiceTest`, `XxxControllerTest`。
- 测试接口必须全部通过
### 测试代码示例
#### controller层
```java
@Slf4j
@WebMvcTest(GreetingController.class)
class GreetingControllerMockTest {

    @Autowired
    private MockMvc mockMvc;

    // 模拟 GreetingService，不加载真实实现
    @MockitoBean
    private GreetingService service;

    @Test
    void greet_shouldUseMockedService() throws Exception {
        when(service.greet(anyString())).thenReturn("Mocked Hello");
        MockHttpServletRequestBuilder param = get("/greet").param("name", "Test");
        MvcResult mvcResult = mockMvc.perform(param)
                .andExpect(status().isOk())
                .andExpect(content().string("Mocked Hello"))
                .andReturn();
        log.info("response is :" + mvcResult.getResponse().getContentAsString());
    }
}
```
#### service层
```java
@Slf4j
@ExtendWith(MockitoExtension.class)
class GreetingServiceUnitTest {

    private GreetingService service;


    /**
     * 每个测试方法执行前
     */
    @BeforeEach
    public void setUp() {
        service = new GreetingService();
    }

    /**
     * 不启动 Spring 上下文
     * 纯单元测试
     */
    @Test
    void greet_shouldReturnFormattedMessage() {
        // given
        String name = "JUnit5";

        // when
        String result = service.greet(name);
        log.info(result);

        // then
        assertThat(result).isEqualTo("Hello, JUnit5!");
    }
}
```
#### 集成测试 integration

## AI 代码生成要求
1. **新增功能时**：
- 先定义 POJO：`Entity/DTO/BO/VO`，再编写 `Mapper`，接着是 `Service` 接口与实现，最后写 `Controller`。
- 使用 `Converter` 完成对象转换，避免手写 `BeanUtils.copyProperties`。
2. **修改现有代码**：
- 遵循原有命名和结构，不随意引入新风格。
- 保持方法单一职责，避免过长方法（建议不超过 100 行）。
3. **生成代码前**：
- 检查是否已有可复用的 Manager 或 Utils。
- 确保异常处理完备，日志记录关键操作。
4. **输出格式**：
- 提供完整的类文件内容，包含必要的 import 和注解。
- 添加简明的 Javadoc 注释说明类和方法用途。
4. **代码可直接编译运行，无语法错误**：
- 代码可直接编译运行，无语法错误
- 业务代码生成，必须同时生成对应的Test类

## AI 代码生成禁止行为
1. 禁止修改项目包结构、目录层级
2. 禁止在 Controller 编写业务逻辑
3. 禁止使用魔法值、硬编码配置、硬编码响应码 
4. 禁止捕获异常不处理、禁止空 catch 块 
5. 禁止 POJO 类编写业务方法 
6. 禁止跨层直接调用数据库 
7. 尽量避免使用过时 API
8. 禁止使用不安全 API
9. 禁止直接使用秘钥、密码明文