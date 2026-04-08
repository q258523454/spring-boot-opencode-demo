# Java Spring Boot 项目开发规范

> 本文件为 OpenCode AI Agent 提供项目特定的上下文与编码约束，生成代码时必须严格遵循。

## 1. 技术栈与约定

- **JDK**：17
- **框架**：Spring Boot 3.2+
- **ORM**：MyBatis-Plus 3.5+
- **工具库**：Lombok（必须使用）、Hutool
- **API 文档**：Knife4j（基于 Swagger 3 / OpenAPI 3）
- **参数校验**：Spring Validation
- **对象映射**：MapStruct（优先）或 BeanUtil
- **统一响应**：`Result<T>` 类（位于 `util` 包）
- **日志**：`@Slf4j`（Lombok），禁止 `System.out`

## 2. 项目包结构及职责
```
├─src
│└─main
│    ├─java
│    │  └─com
│    │      └─xxx
│    │          └─project
│    │              ├─annotation        # 存放自定义注解（如权限校验、日志、限流等）
│    │              ├─aspects           # 存放 AOP 切面类，实现横切逻辑（如日志、性能监控）
│    │              ├─config            # 存放配置类（如 WebConfig、RedisConfig、MyBatisConfig）
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
└─test                                  # 测试类
```



## 3. 各层编码规范与代码模板

### 3.1 Controller 层规范

**强制要求**：
- 类注解：`@RestController` + `@RequestMapping("/api/v1/模块名")` + `@Api(tags = "模块说明")`
- 方法注解：`@ApiOperation` + 相应 HTTP 方法注解（`@GetMapping`、`@PostMapping` 等）
- 参数校验：使用 `@Valid` + 校验注解（`@NotNull`、`@NotBlank` 等）
- 返回值：必须为 `Result<T>` 或 `Result<PageResult<T>>`
- **禁止**在 Controller 中直接调用 Mapper 或编写业务逻辑
- 使用 `@RequiredArgsConstructor` 注入依赖（Lombok 构造器注入）

```java
@RestController
@RequestMapping("/api/v1/user")
@Api(tags = "用户管理")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @ApiOperation("根据ID查询用户")
    public Result<UserVO> getUserById(@PathVariable Long id) {
        log.info("查询用户详情，ID: {}", id);
        UserVO userVO = userService.getUserById(id);
        return Result.success(userVO);
    }

    @PostMapping
    @ApiOperation("新增用户")
    public Result<Void> addUser(@Valid @RequestBody UserAddDTO dto) {
        userService.addUser(dto);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("分页查询用户")
    public Result<PageResult<UserVO>> pageUsers(@Valid UserPageQuery query) {
        PageResult<UserVO> page = userService.pageUsers(query);
        return Result.success(page);
    }
}
```
### 3.2 Service 层规范
**强制要求：**
- 接口定义在 `service` 包，实现类在 `service.impl` 包
- 实现类添加 `@Service` 和 `@Slf4j`
- 多表操作、写操作必须添加 `@Transactional(rollbackFor = Exception.class)`
- 优先使用 MyBatis-Plus 提供的 `ServiceImpl<M, T>` 基类
- 实体与 DTO/VO 转换使用 MapStruct（定义 Converter 接口）
- 禁止在 Service 中处理 `HttpServletRequest` / `HttpServletRespons`
示例代码：
```java
// 接口
public interface UserService extends IService<User> {
    UserVO getUserById(Long id);
    void addUser(UserAddDTO dto);
    PageResult<UserVO> pageUsers(UserPageQuery query);
}

// 实现类
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;
    private final UserConverter userConverter;

    @Override
    public UserVO getUserById(Long id) {
        User user = this.getById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return userConverter.toVO(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUser(UserAddDTO dto) {
        // 业务校验（如用户名唯一性）
        User user = userConverter.toEntity(dto);
        this.save(user);
        log.info("新增用户成功，ID: {}", user.getId());
    }

    @Override
    public PageResult<UserVO> pageUsers(UserPageQuery query) {
        Page<User> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .like(StringUtils.hasText(query.getUsername()), User::getUsername, query.getUsername());
        this.page(page, wrapper);
        return PageResult.convert(page, userConverter::toVO);
    }
}
```
### 3.3 Dao / Mapper 层规范
**强制要求：**
- 接口必须添加 `@Mapper` 注解
- 必须继承 MyBatis-Plus 的 `BaseMapper<T>`
- 复杂查询优先使用 XML 映射文件，简单查询可使用 `@Select`
- **禁止**在 Mapper 中编写业务逻辑
示例代码：
```java
@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     * 自定义联表查询示例（建议放在 XML 中）
     */
    List<UserWithOrdersVO> selectUserWithOrders(@Param("userId") Long userId);
}
```

### 3.4 Entity 层规范
**强制要求：**
- 使用 Lombok `@Data`
- 使用 MyBatis-Plus 注解：`@TableName`、`@TableId`、`@TableField`、`@TableLogic` 等
- 日期类型统一使用 `LocalDateTime`
- 逻辑删除字段添加 `@TableLogic`
- 自动填充字段（如 `createTime`、`updateTime`）需配合 `MetaObjectHandler`
```java
@Data
@TableName("t_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String username;
    private String password;
    private String email;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    private Integer deleted;
}
```

## 4. 特殊注意事项

### 4.1 日志规范
- 所有类必须使用 `@Slf4j`
- 关键操作必须打印入参和出参
- 异常捕获必须记录完整堆栈：`log.error("错误信息", e)`
- **禁止**使用 `System.out.println()`

### 4.2 异常处理规范
- 自定义业务异常 `BusinessException`
- 全局异常处理器使用 `@RestControllerAdvice`
- 所有异常最终返回 `Result.error()`

### 4.3 数据库命名规范
- 表名：前缀 `t_` + 小写下划线（如 `t_user_order`）
- 字段名：小写下划线（如 `user_name`）
- 实体类属性：驼峰命名（MyBatis-Plus 自动映射）

- ## 5. 禁止事项（OpenCode 必须严格遵守）
  | 禁止行为                             | 正确做法                                     |
  |:---------------------------------|:-----------------------------------------|
  | ❌ Controller 直接调用 Mapper         | 必须通过 Service                             |
  | ❌ Service 中处理 HttpServletRequest | 由 Controller 或拦截器处理                      |
  | ❌ Entity 中编写业务方法                 | Entity 仅作为数据载体                           |
  | ❌ 返回 `null` 作为接口响应               | 返回 `Result.success()` 或 `Result.error()` |
  | ❌ 使用 `System.out` 打印日志           | 使用 `@Slf4j` + `log.info/error`           |
  | ❌ 在循环中执行数据库操作                    | 使用批量插入或批量更新                              |
  | ❌ 字符串硬编码业务常量                     | 定义常量类或枚举                                 |


## 6. 示例：完整业务流程代码片段

当用户要求“新增用户接口”时，OpenCode 应生成以下四个文件：

1. **UserController** → 仅做参数校验和调用 Service
2. **UserService / UserServiceImpl** → 包含业务逻辑和事务
3. **UserMapper** → 继承 BaseMapper
4. **UserAddDTO** → 入参对象，包含校验注解

**以上规范即为本项目不可妥协的编码标准，所有生成的代码必须严格符合。**







