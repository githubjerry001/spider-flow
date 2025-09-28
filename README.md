# Spider-Flow

一个基于Spring Boot的可视化网络爬虫开发平台，通过图形化界面来设计和管理网络爬虫流程。

## 特性

- 🎨 **可视化设计** - 拖拽式流程图设计，无需编码
- 🔍 **多解析支持** - XPath、JSONPath、CSS选择器、正则表达式
- 🛠️ **灵活扩展** - 自定义函数、变量管理、数据源配置
- 🔄 **任务调度** - 内置Quartz定时任务支持
- 📊 **实时监控** - 任务执行状态、日志追踪
- 📧 **通知服务** - 邮件通知支持

## 技术栈

- **框架**: Spring Boot 3.4.4
- **Java**: 24+
- **数据库**: H2 (默认) / MySQL
- **构建工具**: Maven 3.6+
- **前端**: jQuery + Bootstrap

## 快速开始

### 环境要求

- JDK 17+
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
mvn spring-boot:run
```

### 访问应用

打开浏览器访问: http://localhost:8088

## 项目结构

```
spider-flow/
├── spider-flow-api/      # 核心API定义
├── spider-flow-core/     # 核心业务逻辑实现
├── spider-flow-web/      # Web应用层
└── pom.xml               # Maven构建配置
```

## 配置说明

主要配置文件: `spider-flow-web/src/main/resources/application.properties`

```properties
# 服务端口
server.port=8088

# 线程配置
spider.thread.max=64
spider.thread.default=8

# 定时任务
spider.job.enable=false

# 数据库(默认H2文件模式)
spring.datasource.url=jdbc:h2:file:./data/spider-flow
```

## 使用指南

1. **创建流程** - 点击“新建”，拖拽组件设计爬虫流程
2. **配置节点** - 双击节点设置参数(链接、选择器等)
3. **运行测试** - 点击“运行”按钮测试流程
4. **数据输出** - 配置输出组件保存数据
5. **定时任务** - 设置Cron表达式定时执行

## 核心组件

| 组件 | 说明 |
|------|------|
| 开始 | 流程入口点 |
| 请求 | HTTP请求组件 |
| 变量 | 定义和赋值变量 |
| 循环 | 列表数据循环处理 |
| 输出 | 数据结果输出 |
| 函数 | 执行自定义函数 |

## 开发指南

### 自定义函数

在“函数管理”中添加JavaScript函数：

```javascript
function customFunction(param) {
    // 自定义逻辑
    return result;
}
```

### 数据源配置

支持MySQL、PostgreSQL等关系型数据库，在“数据源管理”中添加配置。

## 许可证

Apache License 2.0

## 贡献

欢迎提交Issue和Pull Request。关法律法规，合理使用网络爬虫技术。