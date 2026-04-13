# Student CRUD API 文档

## 功能概述
本项目实现了Student（学生）的完整CRUD功能，并集成了Redis缓存。

## 技术栈
- Spring Boot 3.x
- MyBatis Plus
- Redisson (Redis客户端)
- MapStruct (对象转换)
- Lombok

## API接口

### 1. 创建学生
```
POST /api/students
Content-Type: application/json

Request Body:
{
  "name": "张三",
  "age": 20
}

Response:
{
  "id": 1,
  "name": "张三",
  "age": 20
}
```

### 2. 查询学生（带缓存）
```
GET /api/students/{id}

Response:
{
  "id": 1,
  "name": "张三",
  "age": 20
}
```

### 3. 更新学生
```
PUT /api/students/{id}
Content-Type: application/json

Request Body:
{
  "name": "李四",
  "age": 21
}

Response:
{
  "id": 1,
  "name": "李四",
  "age": 21
}
```

### 4. 删除学生
```
DELETE /api/students/{id}

Response: 200 OK
```

### 5. 分页查询学生列表
```
GET /api/students?name=&pageNum=1&pageSize=10

Response:
{
  "records": [...],
  "total": 100,
  "size": 10,
  "current": 1,
  "pages": 10
}
```

## 缓存设计

### 缓存策略
- 使用Redisson作为Redis客户端
- 查询学生信息时，先从缓存获取，缓存不存在则查询数据库并写入缓存
- 更新/删除学生时，自动清除相关缓存
- 缓存过期时间：30分钟

### 缓存Key设计
- 单个学生：`student:info:id:{id}`
- 常量定义：`CacheConstants`类

### 缓存位置
- Service层实现缓存逻辑
- 使用Redisson的RBucket进行缓存操作

## 项目结构
```
src/main/java/com/mysql/base/
├── config/
│   └── RedissonConfig.java          # Redis配置
├── constant/
│   └── CacheConstants.java          # 缓存常量
├── controller/
│   └── StudentController.java       # REST控制器
├── converter/
│   └── StudentConverter.java        # MapStruct转换器
├── exception/
│   ├── BusinessException.java       # 业务异常
│   └── GlobalExceptionHandler.java  # 全局异常处理
├── pojo/
│   ├── bo/
│   │   └── StudentBO.java
│   ├── dto/
│   │   ├── StudentCreateDTO.java
│   │   ├── StudentQueryDTO.java
│   │   └── StudentUpdateDTO.java
│   ├── entity/
│   │   └── Student.java
│   └── vo/
│       ├── ResultVO.java
│       └── StudentVO.java
├── repository/
│   └── StudentMapper.java           # MyBatis Mapper
└── serivce/
    ├── StudentService.java          # Service接口
    └── impl/
        └── StudentServiceImpl.java  # Service实现（含缓存）
```

## 运行项目

### 1. 环境要求
- JDK 21
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+

### 2. 数据库初始化
执行 `src/main/resources/init.sql` 中的SQL脚本

### 3. 配置文件
修改 `application.yml` 中的数据库和Redis连接信息

### 4. 编译运行
```bash
mvn clean package
java -jar target/spring-boot-opencode-demo.jar
```

## 测试

### 运行所有测试
```bash
mvn test
```

### 测试结果
- StudentServiceTest: 2个测试用例 ✓
- StudentControllerTest: 3个测试用例 ✓
- 总计：5个测试全部通过

## 注意事项
1. 确保Redis服务已启动
2. 确保MySQL服务已启动并创建了对应数据库
3. Redis连接信息在application.yml中配置
