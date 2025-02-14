
<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">websql v4.0.0</h1>
<h4 align="center">websql 轻量级网页sql管理工具,在线执行SQL、ETL同步、动态多数据源、常用sql管理等众多功能，体积小开箱即用 ———简约而不简单</h4>
<p align="center">
	<a href="https://gitee.com/boy_0214/websql/stargazers"><img src="https://gitee.com/boy_0214/websql/badge/star.svg?theme=gvp"></a>
	<a href="https://gitee.com/boy_0214/websql/members"><img src="https://gitee.com/boy_0214/websql/badge/fork.svg?theme=gvp"></a>
	<a href="https://www.oracle.com/cn/java/technologies/downloads/"><img src="https://img.shields.io/badge/JDK-1.8+-orange)"></a>
    <a href="https://maven.apache.org"><img src="https://img.shields.io/badge/maven-3.5+-e4ec27.svg"></a>
    <a href="https://gitee.com/boy_0214/websql/blob/master/LICENSE"><img src="https://img.shields.io/badge/license-GPL3.0-blue.svg"></a>
    <a href="https://websql.cgygeo.com"><img src="https://img.shields.io/badge/demo-%E6%BC%94%E7%A4%BA%E7%8E%AF%E5%A2%83-8A2BE2.svg"></a>
    <a href="https://gitee.com/boy_0214/websql/wikis/%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98"><img src="https://img.shields.io/badge/wiki-%E5%B8%AE%E5%8A%A9%E6%96%87%E6%A1%A3-16b777.svg"></a>
</p>

---

## 前言：
- [快速开始](https://gitee.com/boy_0214/websql/wikis/Home)

- 注：学习测试请拉取 master 分支，dev 是开发分支，有很多特性并不稳定（在项目根目录执行 `git checkout master`）。

-  开源不易，点个 star 鼓励一下吧！


## webSql 介绍

**webSql** 支持动态配置`多数据源`,`权限控制`,在线`执行sql`，常用`sql文本实时获取`,`导出、打印`结果集、可控的`日志记录`，团队`数据隔离`，`危险`SQL限制运行，生产环境`数据`同步,`openapi`
ETL抽取,定时`监测数据趋势`告警功能;众多功能集一身的`SQL在线执行工具`。



### 支持的数据库产品
| 产品名称           | 适配度 | 功能描述   |
|:---------------|:----|:-------|
| mysql          | &#x2714;  | 支持所有功能 |
| oracle         | &#x2714;  | 支持所有功能 |
| H2             | &#x2714;  | 支持所有功能 |
| postgresql     | &#x2714;  | 不支持元数据 |
| sqlite         | &#x2714;  | 支持所有功能 |
| sqlserver      | &#x2714;  | 不支持元数据 |
| dm达梦           | &#x2714;  | 支持所有功能 |
| doris          | &#x2714;  | 支持所有功能   |
| TiDB           | &#x2714;  | 支持所有功能   |
| ClickHouse     | &#x2714;  | 支持所有功能   |
| kingbase8 人大金仓 | 部分  | 不支持元数据 |
| oscar 神州通用     | 部分  | 不支持元数据 |
| maxcompute 阿里云 | 部分  | 不支持元数据 |


### 软件架构
| 产品名称         | 模块 | 描述     |
|:-------------|:---|:-------|
| Spring Boot	 | 后端 | 后端框架   |
| JPA          | 后端 | 数据层    |
| Druid        | 后端 | 连接池    |
| sa-token     | 后端 | 权限框架   |
| fastjson     | 后端 | JSON工具 |
| easyCaptcha  | 后端 | 验证码    |
| huTool       | 后端 | 常用工具   |
| LaYui        | 前端 | 前端框架   |
| codeMirror   | 前端 | 编辑器    |



## WebSql 功能模块一览

WebSql 主要功能模块

- **数据源管理** —— 数据源动态配置多种数据库连接进行入池
- **SQL管理** —— SQL窗口执行脚本,它强大无比;SQL列表保存常用SQL文本
- **ETL管理** —— 定时执行脚本,跨库同步数据结果并呈现；便捷执行计划ETL抽取，数据同步等
- **日志管理** —— 执行脚本记录,登录系统会产生详细日志可供查询
- **用户管理** —— 可新增不同用户，赋予不同操作权限登录系统使用
- **团队管理** —— 不同团队之间，数据完全隔离，更细致的权限控制
- **监测管理** —— 不同团队自定义SQL，定时执行数据监测，达到阈值自定义告警通知
- **参数设置** —— 参数设置中可以操作更多细致化控制
- **openapi** —— 支持http调用系统接口形式执行SQL动作 <a href='https://gitee.com/boy_0214/websql/wikis/openapi'>查看示例</a>
- **开箱即用** —— 提供jar、docker镜像，内置H2数据库，一条命令即可启动，真正的开箱即用


### 参与开发

第一步： git clone https://gitee.com/boy_0214/websql.git

第二步： WebplsqlApplication.java启动  基于`master`开发完成后提交至`dev`分支

第三步：自测完成，gitee提交PR 至dev分支

第四步：编译打包 `mvn clean package`    部署 target/websql.jar

第五步：打包docker镜像

    docker build -f dockerfile --tag cgycms/websql:3.x --tag cgycms/websql:latest .
    docker push

## 应用部署

- 运行环境：jdk8 / jdk17
- 使用内置H2不需要独立安装数据库及创建表结构，系统会自动创建
- 默认端口：80
- 访问路径：http://localhost/index
- 指定端口号： --server.port=8080
- 默认登录账号：admin/admin


    nohup java -jar websql.jar  >>web.log &

### Docker部署

    pull拉取失败，可以加群寻找国内镜像地址

    docker pull cgycms/websql:latest

    docker run -di --name websql -p 80:80 cgycms/websql:latest
    
    docker logs websql

## 演示环境


[演示环境](https://websql.cgygeo.com)

账号密码：**demo** / **demo123**

配置低,经常GG。


## 交流群
QQ交流群：498265967 [点击加入](http://qm.qq.com/cgi-bin/qm/qr?_wv=1027&k=HHuK-ks_qF9KdaWI8UuIPzp22Qg3jSJ7&authKey=fBFgaomxUn3%2BfMgrRzHq9ZMyBZZ0eSAaU2JBO1oXe94RbnkUhlSI2SKjHjVK8Mij&noverify=0&group_code=498265967)

<img src="https://foruda.gitee.com/images/1698638140621421548/945994da_1509614.jpeg" width="230px" title="微信群" />

<br>

- 群主虽然是个菜鸟但是乐于助人。
- 第一时间收到框架更新通知。
- 第一时间收到框架 bug 通知。

## Star History
[![Star History Chart](https://api.star-history.com/svg?repos=viarotel-org/escrcpy,https:/,gitee.com/boy_0214&type=Date)](https://star-history.com/#viarotel-org/escrcpy&https:/&gitee.com/boy_0214&Date)
