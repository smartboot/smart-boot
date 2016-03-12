#smart-boot

##工程结构

- smart-assembly   
用于集中管理smart-boot工程中各Spring配置文件所需的属性配置
- smart-dal  
数据操作层bundle,实现数据存储读取操作。
- smart-service-integration  
与外部第三方系统对接的bundle，以供smart-boot调用第三方服务
- smart-component  
组件bundle，遵循单一职责原则，向下对接smart-dal、smart-service-integration，向上为业务层smart-service-impl提供各组件式服务
- smart-service-facade  
定义smart-boot的服务接口，一个独立的bundle,不依赖其他模块。未来第三方系统可通过该bundle提供的接口调用服务
- smart-service-impl  
该bundle通过引用smart-componet提供的各组件用于实现smart-service-facade中定义的接口。对于私有服务可直接在本bundle中定义接口，无需放置在smart-service-facade中.  

>为方便使用，也可直接调用smart-service-integration中提供的服务

- smart-shared  
该bundle完全独立于业务，主要用于提供一下工具类，可被任一bundle引用
- smart-restful  
Web层，仅负责前后端的数据交互，不建议在该bundle中进行复杂的业务处理，应统一交由smart-service-impl处理

![系统结构图](1.png)
##运行系统
dbapi-restful模块中运行BootStrap.java
> 新建工程默认运行时会报错`Could not resolve resource location pattern [classpath:mybatis/mapping/*.xml]`,那是因为工程包含了mybatis的spring配置信息dal-config.xml，而初始状态下还没生成mybatis配置文件，导致加载失败。如果项目本身不对接数据库，可去除dal-config.xml的数据库配置项，或者在完成mybatis的配置后再尝试启动项目。

##smart-boot特点
- 研发：
	模块化编程、面向服务编程、测试框架
- 部署：
	eclipse，tomcat
- 管控：
	统一上下文，动态路由，监控日志，地址池，故障	隔离，精细化管控，jvm监控。。。

##发布服务

	<bean name="userInfoServiceImpl" class="net.vinote.smartweb.service.impl.UserInfoServiceImpl"/>

	<bean id="userInfoService" class="net.vinote.sosa.rmi.RmiServerFactoryBean"
		init-method="publishService">
		<property name="interfaceName"
			value="net.vinote.smartweb.service.facade.UserInfoService"/>
		<property name="interfaceImpl" ref="userInfoServiceImpl" />
		<property name="rmiServer" ref="rmiServer" />
	</bean>

##引用外部服务
	<bean id="userService" class="net.vinote.sosa.rmi.RmiClientFactoryBean">
		<property name="rmiClient" ref="rmiClient" />
		<property name="remoteInterface"
			value="net.vinote.smartweb.service.facade.UserInfoService" />
		<property name="url" value="${smartUrl}" />
		<property name="timeout" value="5000" />
	</bean>


##日志系统log4j2
### 为什么选用log4j2?  
1. Apache Log4j 2 is an upgrade to Log4j that provides significant improvements over its predecessor, Log4j 1.x, and provides many of the improvements available in Logback while fixing some inherent problems in Logback's architecture. 一句话总结，官方号称log4j2比log4j和logback都牛逼.
2. 配置简单集中，修改smart-assembly中的log4j2.xml即可实现整个工程的日志管理。


##生成maven archetype
1.mvn clean
清楚工程编译产生的文件
2.mvn archetype:create-from-project
Then move to that generated directory and call mvn install on the created archetype.

已生成现成的archetype，参见[smart-boot-archetype](https://git.oschina.net/smartdms/smart-boot-archetype)


##推荐项目
- [smart-socket](https://git.oschina.net/smartdms/smart-socket)
- [smart-sosa](https://git.oschina.net/smartdms/smart-sosa)
- [maven-mybatisdalgen-plugin](https://git.oschina.net/smartdms/maven-mybatisdalgen-plugin)

##关于作者
Edit By [Seer](http://zhengjunweimail.blog.163.com/)  
E-mail:zhengjunweimail@163.com  
QQ:504166636

Update Date: 2016-03-15