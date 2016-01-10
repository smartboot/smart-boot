#dbapi

##工程结构

- dbapi-assembly   
用于集中管理dbapi工程中各Spring配置文件所需的属性配置
- dbapi-dal  
数据操作层bundle,实现数据存储读取操作。
- dbapi-service-integration  
与外部第三方系统对接的bundle，以供dbapi调用第三方服务
- dbapi-component  
dbapi组件bundle，遵循单一职责原则，向下对接dbapi-dal、dbapi-service-integration，向上为业务层dbapi-service-impl提供各组件式服务
- dbapi-service-facade  
定义dbapi的服务接口，一个独立的bundle,不依赖dbapi其他模块。未来第三方系统可通过该bundle提供的接口调用dbapi服务
- dbapi-service-impl  
该bundle通过引用dbapi-componet提供的各组件用于实现dbapi-service-facade中定义的接口。对于私有服务可直接在本bundle中定义接口，无需放置在dbapi-service-facade中.  
>为方便使用，也可直接调用dbapi-service-integration中提供的服务

- dbapi-shared  
该bundle完全独立于业务，主要用于提供一下工具类，可被任一bundle引用
- dbapi-portal  
Web层，仅负责前后端的数据交互，不建议在该bundle中进行复杂的业务处理，应统一交由dbapi-service-impl处理

![系统结构图](1.png)
##运行系统
dbapi-portal模块中执行命令`mvn tomcat7:run`

##日志系统log4j2
### 为什么选用log4j2?  
1. Apache Log4j 2 is an upgrade to Log4j that provides significant improvements over its predecessor, Log4j 1.x, and provides many of the improvements available in Logback while fixing some inherent problems in Logback's architecture. 一句话总结，官方号称log4j2比log4j和logback都牛逼.
2. 配置简单集中，修改dbapi-assembly中的log4j2.xml即可实现整个工程的日志管理。