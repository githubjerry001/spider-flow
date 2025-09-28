# Spider-Flow

一个基于Spring Boot的可视化网络爬虫开发平台，通过图形化界面来设计和管理网络爬虫流程。

## ✨ 特性

- 🎨 **可视化设计** - 拖拽式流程图设计，支持实时调试和测试
- 🔍 **多解析支持** - XPath、JSONPath、CSS选择器、正则表达式
- 🛠️ **灵活扩展** - 自定义函数、变量管理、数据源配置
- 🔄 **任务调度** - 内置Quartz定时任务，支持Cron表达式
- 📊 **实时监控** - WebSocket实时推送执行状态、日志和数据
- 📧 **通知服务** - 支持邮件通知（开始、结束、异常）
- 💾 **数据持久化** - H2文件数据库，支持数据完整持久化
- 🚀 **REST API** - 完整的REST API支持异步执行和管理

## 🛠️ 技术栈

- **后端框架**: Spring Boot 3.4.4
- **Java版本**: JDK 24
- **数据库**: H2 (默认文件模式) / MySQL
- **任务调度**: Quartz 2.3.2
- **WebSocket**: 实时通信支持
- **构建工具**: Maven 3.6+
- **前端技术**: jQuery + Bootstrap + WebSocket

## 🚀 快速开始

### 环境要求

- JDK 17+ (推荐JDK 24)
- Maven 3.6+

### 安装运行

```bash
# 克隆项目
git clone https://github.com/githubjerry001/spider-flow.git
cd spider-flow

# 编译构建
mvn clean install

# 启动应用
cd spider-flow-web
java -jar target/spider-flow.jar
```

### 访问应用

浏览器访问: http://localhost:8088

## 📁 项目结构

```
spider-flow/
├── spider-flow-api/        # 核心API定义和接口
├── spider-flow-core/       # 核心业务逻辑实现
├── spider-flow-web/        # Web应用层和前端资源
│   ├── data/              # H2数据库文件目录
│   └── workspace/         # 爬虫工作空间
└── pom.xml                # Maven构建配置
```

## ⚙️ 配置说明

主要配置文件: `spider-flow-web/src/main/resources/application.properties`

### 基础配置

```properties
# 服务端口
server.port=8088

# 线程池配置
spider.thread.max=64          # 平台最大线程数
spider.thread.default=8       # 单任务默认线程数

# 定时任务
spider.job.enable=false       # 设置为true启用定时任务

# 工作空间
spider.workspace=./workspace  # 爬虫任务工作目录
```

### 数据库配置

```properties
# H2数据库(默认文件模式，数据持久化)
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.url=jdbc:h2:file:./data/spider-flow;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=MySQL
spring.datasource.username=sa
spring.datasource.password=
```

### 邮件通知配置

```properties
# 邮件服务配置
spring.mail.host=smtp.qq.com
spring.mail.port=465
spring.mail.username=your@qq.com
spring.mail.password=your-password

# 通知内容模板
spider.notice.subject=spider-flow流程通知
spider.notice.content.start=流程开始执行：{name}，开始时间：{currentDate}
spider.notice.content.end=流程执行完毕：{name}，结束时间：{currentDate}
spider.notice.content.exception=流程发生异常：{name}，异常时间：{currentDate}
```

## 📖 使用指南

### 基本操作流程

1. **创建爬虫流程** - 点击"新建"，使用拖拽组件设计爬虫流程
2. **配置节点参数** - 双击节点设置请求URL、选择器、变量等
3. **实时调试测试** - 使用调试模式实时查看执行过程和数据
4. **配置数据输出** - 设置输出组件保存抓取的数据
5. **设置定时任务** - 配置Cron表达式实现定期执行
6. **通知配置** - 设置邮件通知监控任务状态

### 核心组件说明

| 组件类型 | 功能说明 | 使用场景 |
|---------|---------|----------|
| **开始** | 流程入口节点 | 每个流程的起始点 |
| **请求** | HTTP请求组件 | 抓取网页、调用API接口 |
| **变量** | 变量定义和赋值 | 存储中间数据、配置参数 |
| **循环** | 列表数据循环处理 | 批量处理、分页抓取 |
| **输出** | 数据结果输出 | 保存到数据库、文件等 |
| **函数** | 自定义函数执行 | 复杂数据处理逻辑 |

## 🔧 高级功能

### WebSocket实时监控

- **实时日志**: 执行过程中的详细日志实时推送
- **数据预览**: 抓取数据实时显示和格式化
- **调试模式**: 节点级别的断点调试功能
- **状态监控**: 任务执行状态实时更新

### REST API接口

```bash
# 获取爬虫列表
GET /spider/flows

# 保存爬虫配置
POST /spider/save

# 异步执行爬虫
POST /rest/runAsync/{id}

# 同步执行爬虫
POST /rest/run/{id}

# 停止执行任务
POST /rest/stop/{taskId}

# 删除爬虫
POST /spider/remove
```

### 自定义函数开发

在"函数管理"中添加JavaScript函数：

```javascript
function customFunction(param) {
    // 数据处理逻辑
    return processedData;
}
```

### 数据源管理

支持多种数据库连接：
- MySQL
- PostgreSQL  
- H2数据库
- 其他JDBC兼容数据库

## 🔒 注意事项

- 请遵守目标网站的robots.txt协议
- 合理控制访问频率，避免对目标服务器造成压力
- 遵守相关法律法规，合理使用网络爬虫技术
- 生产环境建议使用MySQL等外部数据库

## 📄 许可证

Apache License 2.0

## 🤝 贡献

欢迎提交Issue和Pull Request来改进项目。

---

**免责声明**: 本工具仅供学习和研究使用，使用者应当遵守相关法律法规。