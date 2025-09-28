# SpiderFlow

![SpiderFlow Logo](https://www.spiderflow.org/images/logo.svg)

SpiderFlow 是一个基于 Java 的可视化爬虫平台，采用流程图的方式定义爬虫逻辑。它基于 Spring Boot 3.4.4 开发，支持 JDK 24+，提供高度灵活可配置的爬虫解决方案。

通过可视化拖拽操作，用户可以轻松构建复杂的爬虫任务，无需编写复杂的代码即可实现数据抓取、处理和存储。

[![JDK Version](https://img.shields.io/badge/JDK-24+-green.svg)](https://www.oracle.com/technetwork/java/javase/downloads/index.html)
[![GitHub Release](https://img.shields.io/github/v/release/ssssssss-team/spider-flow?logo=github)](https://github.com/ssssssss-team/spider-flow/releases)
[![License](https://img.shields.io/:license-MIT-blue.svg)](LICENSE)

## 核心特性

- 🎯 **可视化设计** - 通过流程图方式定义爬虫逻辑，直观易懂
- 🔍 **多种数据提取** - 支持 XPath、JSONPath、CSS 选择器、正则表达式等多种提取方式
- 🌐 **动态页面支持** - 支持 JS 渲染页面和 AJAX 数据抓取
- 🗃️ **多数据源** - 支持 MySQL、H2 等多种数据库，提供完整的 SQL 操作支持
- ⚙️ **灵活配置** - 支持代理、Cookie 管理、定时任务等高级功能
- 🧩 **插件扩展** - 丰富的插件生态，支持 Selenium、Redis、MongoDB 等扩展
- 📊 **任务监控** - 实时监控任务执行状态，提供详细的日志记录

## 快速开始

### 环境要求

- JDK 24 或更高版本
- Maven 3.6+

### 本地运行

```bash
# 克隆项目
git clone https://github.com/ssssssss-team/spider-flow.git

# 进入项目目录
cd spider-flow

# 编译项目
mvn clean install

# 运行项目
mvn spring-boot:run
```

访问地址：http://localhost:8088

默认使用 H2 内存数据库，无需额外配置即可运行。

### 打包部署

```bash
# 打包项目
mvn clean package

# 运行打包后的应用
java -jar spider-flow-web/target/spider-flow-web-0.5.0.jar
```

## 插件扩展

SpiderFlow 支持丰富的插件扩展，满足不同场景需求：

- Selenium插件 - 浏览器自动化操作
- Redis插件 - Redis 数据库操作
- OSS插件 - 对象存储服务支持
- MongoDB插件 - MongoDB 数据库操作
- IP代理池插件 - IP 代理池管理
- OCR识别插件 - 图像文字识别功能
- 电子邮箱插件 - 邮件收发功能

## 项目截图

### 爬虫列表
![爬虫列表](https://images.gitee.com/uploads/images/2020/0412/104521_e1eb3fbb_297689.png)

### 爬虫测试
![爬虫测试](https://images.gitee.com/uploads/images/2020/0412/104659_b06dfbf0_297689.gif)

### Debug 调试
![Debug](https://images.gitee.com/uploads/images/2020/0412/104741_f9e1190e_297689.png)

### 日志查看
![日志](https://images.gitee.com/uploads/images/2020/0412/104800_a757f569_297689.png)

## 许可证

本项目采用 MIT 许可证，详情请查看 [LICENSE](LICENSE) 文件。

## 免责声明

请勿将 SpiderFlow 应用到任何可能会违反法律规定和道德约束的工作中。请友善使用 SpiderFlow，遵守蜘蛛协议，不要将 SpiderFlow 用于任何非法用途。如您选择使用 SpiderFlow 即代表您遵守此协议，作者不承担任何由于您违反此协议带来任何的法律风险和损失，一切后果由您承担。