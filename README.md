这个Repository是学习响应式编程框架的demo实现。包括的框架为：
* Java 8集合与Future
* RxJava 2.x
* Akka Streams


### 环境

可以通过Maven命令进行测试、打包等。

如果要构建Idea项目，运行命令：

```
mvn idea:idea
```

运行测试(测试较多，运行测试的时间较长)：

```
mvn test
```

如果要打包，可以运行命令：

```
mvn package
```

如果希望在打包时，忽略测试，则运行命名了：

```
mvn package -DskipTests
```



