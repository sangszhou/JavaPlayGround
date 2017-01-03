在删除了不必要的 maven plugin 之后，aspectj 终于可以工作了，在不需要打包的情况下，只需要两个 maven 插件，一个是 maven-compile-plugin 另一个是 aspectj-maven-plugin, 依赖方面不再需要 aspectj-weaver, 只需要一个依赖 aspectjrt.

因为在 Student 那个 package 下使用了 Spring aop, 所以需要引入 spring 依赖，使用 Spring 依赖后不需要 new class 可以直接从 bean 中获取，使用 spring-aop 也不需要 aspectj-weaver 依赖

此外，打包的情况下还要添加一个插件

```java
            <plugin>
                <artifactId>maven-assembly-plugin</artifactId>
                <configuration>
                    <descriptorRefs>
                        <descriptorRef>jar-with-dependencies</descriptorRef>
                    </descriptorRefs>
                    <archive>
                        <manifest>
                            <mainClass>com.jayway.blog.YourClass</mainClass>
                        </manifest>
                    </archive>
                </configuration>
            </plugin>

```

原理: 从 compile 后的代码中可以看出，被织入的代码是在 compile 时发生的，可以在 target 类中查看代码, 发现 class 文件已经发生了替换

替换 jar 文件。假如引入了第三方的 jar 包，需要修改它的部分实现，那么也可以通过这种办法来做，只不过不能为已有的代码加 annotation, 只能通过函数的签名识别了

**runtime weaver**:
假如某个 jar 进程已经启动了，需要动态的替换它的 .class 文件，那么需要使用 aspectj-weaver.jar 和 -javaagent: 参数，已经放到 CLASSPATH 的 jar 包完成, 在我们的场景下，使用的是 `HadoopMonitorProxyClient.jar`

```shell
使用 aspectj 编译 jar

ajc -1.7 -d bin -cp $CLASSPATH:/ws/aspectj/src/lib/jackson-annotations-2.1.2.jar:/ws/aspectj/src/lib/com.fasterxml.jackson.core.jar:/ws/aspectj/src/lib/com.fasterxml.jackson.databind.jar:/ws/aspectj/src/lib/hadoop-common-2.7.0.jar:/ws/aspectj/src/lib/ezmorph-1.0.6.jar:/ws/aspectj/src/lib/json-lib-2.4-jdk15.jar:/ws/aspectj/src/lib/httpclient-4.5.2.jar:/ws/aspectj/src/lib/httpcore-4.4.4.jar:/ws/aspectj/src/lib/jackson-core-asl-1.8.5.jar:/ws/aspectj/src/lib/jackson-mapper-asl-1.8.5.jar:/ws/aspectj/src/lib/commons-beanutils-1.9.2.jar:/ws/aspectj/src/lib/commons-collections4-4.1.jar:/ws/aspectj/src/lib/commons-lang3-3.4.jar:/ws/aspectj/src/lib/commons-logging-1.2.jar src/utils/HadoopEvent.java src/hadoop/FileSystemInterceptor.java -outxml -outjar HadoopMonitorProxyClient.jar


jar uvf HadoopMonitorProxyClient.jar META-INF/aop-ajc.xml

scp HadoopMonitorProxyClient.jar vdeadmin@host1:/users/vdeadmin/hdfs_open_hook/HadoopMonitorProxy
```

把 aspectj-weaver.jar 和 下面这个优化用的 xml 文件以及 Classpath 中的 jar) 文件都放在 CLASSPATH 中

优化配置文件 aop-ajc.xml

```xml
<aspectj>
	<aspects>
		<aspect name="hadoop.FileSystemInterceptor"/>
	</aspects>

	<weaver options="-warn:none -Xlint:default Xlint:ignore -Xset:weaveJavaxPackages=true -Xset:weaveJavaPackages=true">
		<!-- <exclude within="org.apache.hadoop.conf..*">
		<exclude within="org.apache.hadoop.crypto..*">
		<exclude within="org.apache.hadoop.ha..*">
		<exclude within="org.apache.hadoop.http..*">
		<exclude within="org.apache.hadoop.io..*">	
		<exclude within="org.apache.hadoop.ipc..*">
		<exclude within="org.apache.hadoop.jmx..*">
		<exclude within="org.apache.hadoop.log..*">
		<exclude within="org.apache.hadoop.metrics..*">
		<exclude within="org.apache.hadoop.metrics2..*">
		<exclude within="org.apache.hadoop.net..*">	
		<exclude within="org.apache.hadoop.record..*">
		<exclude within="org.apache.hadoop.security..*">
		<exclude within="org.apache.hadoop.service..*">			
		<exclude within="org.apache.hadoop.tools..*">
		<exclude within="org.apache.hadoop.tracing..*">
		<exclude within="org.apache.hadoop.util.*."> -->

		<!-- <include within="org.apache.hadoop.fs.*"/>
		<include within="org.apache.hadoop.fs.shell.*"/> -->
		<include within="com.mapr.fs..*"/>
	</weaver>	
</aspectj>
```