# jvm

# 1.基于jdk命令行工具的监控

官网地址：https://docs.oracle.com/javase/8/docs/technotes/tools/unix/index.html

## 1.1.jvm的参数类型

### 1.1.1.标准参数

- -help
- -server -client
- -version -showversion
- -cp -classpath

### 1.1.2.X参数

- 非标准化参数
- -Xint：解释执行（相当于解释型的编程语言）
- -Xcomp：第一次使用就编译成本地代码（纯编译型编程语言，不过第一次有点慢）
- -Xmixed：混合模式，JVM自己来决定是否编译成本地代码

通过java -version命令就可以查看你的jvm的模式了

```markdown
C:\Users\kevin>java -version
java version "1.8.0_181"
Java(TM) SE Runtime Environment (build 1.8.0_181-b13)
Java HotSpot(TM) 64-Bit Server VM (build 25.181-b13, mixed mode)
```

可以看到当前jvm为mixed mode混合模式，这也是jvm默认的模式。

通过指定对应的参数可以查看对应的模式

解释模式：

```markdown
C:\Users\kevin>java -Xint -version
java version "1.8.0_181"
Java(TM) SE Runtime Environment (build 1.8.0_181-b13)
Java HotSpot(TM) 64-Bit Server VM (build 25.181-b13, interpreted mode)
```

编译模式：

```markdown
C:\Users\kevin>java -Xcomp -version
java version "1.8.0_181"
Java(TM) SE Runtime Environment (build 1.8.0_181-b13)
Java HotSpot(TM) 64-Bit Server VM (build 25.181-b13, compiled mode)
```

### 1.1.3.XX参数

- 非标准化参数
- 相对不稳定
- 主要用于JVM调优和Debug

参数分类：

Boolean类型：

格式：-XX:[+-]<name>表示启用或者禁用name属性

比如：-XX:+UseConcMarkSweepGC 启用cms垃圾收集器
			-XX: +UseG1GC	启用G1垃圾收集器

非Boolean类型：

格式：-XX:<name>=<value>表示name属性的值是value

比如：-XX:MaxGCPauseMillis=500

​			-XX:GCTimeRatio=19

### 1.1.4.-Xms -Xmx

不是X参数而是XX参数

-Xms等价于-XX:InitialHeapSize	初始堆大小

-Xmx等价于-XX:MaxHeapSize	  最大堆大下

## 1.2.运行时jvm参数查看

- -XX:+PrintFlagslnitial 查看初始值

  比如：java -XX:+PrintFlagsInitial -version

  就会输出jdk的一些初始信息。

  ![1572505414748](assets/1572505414748.png)

  =表示默认值

  :=表示被用户或者jvm修改后的值

- -XX:+PrintFlagsFinal 查看最终值

- -XX:+UnlockExperimentalVMOptions解锁实验参数

- -XX:+UnlockDiagnosticVMOptions解锁诊断参数

- -XX:+ PrintCommandLineFlags:打印命令行参数

  ```markdown
  [root@yifeng ~]# java -XX:+PrintCommandLineFlags -version
  -XX:InitialHeapSize=62095040 -XX:MaxHeapSize=993520640 -XX:+PrintCommandLineFlags -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseParallelGC 
  java version "1.8.0_201"
  Java(TM) SE Runtime Environment (build 1.8.0_201-b09)
  Java HotSpot(TM) 64-Bit Server VM (build 25.201-b09, mixed mode)
  
  ```

  

## 1.3.jps查看Java进程信息

官网地址：https://docs.oracle.com/javase/8/docs/technotes/tools/unix/jps.html#CHDCGECD

jps：

```markdown
[root@yifeng bin]# jps
1921 Jps
1867 Bootstrap
```

可以看到当前运行的所有java进程

通过加上 -l参数就可以查看它的路径信息。

jps -l

```
[root@yifeng bin]# jps -l
2017 sun.tools.jps.Jps
1867 org.apache.catalina.startup.Bootstrap
```

## 1.4.jinfo查看正在运行的java进程的信息

官网地址：https://docs.oracle.com/javase/8/docs/technotes/tools/unix/jinfo.html#BCGEBFDD

查看最大内存：jinfo -flag MaxHeapSize 1867

```markdown
[root@yifeng bin]# jinfo -flag MaxHeapSize 1867
-XX:MaxHeapSize=994050048
```

查看已经被赋值过jvm参数和命令行参数：jinfo -flags 1867

```markdown
[root@yifeng bin]# jinfo -flags 1867
Attaching to process ID 1867, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 25.201-b09
Non-default VM flags: -XX:CICompilerCount=2 -XX:InitialHeapSize=62914560 -XX:MaxHeapSize=994050048 -XX:MaxNewSize=331350016 -XX:MinHeapDeltaBytes=524288 -XX:NewSize=20971520 -XX:OldSize=41943040 -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseParallelGC 
Command line:  -Djava.util.logging.config.file=/usr/local/tomcat9/conf/logging.properties -Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager -Djdk.tls.ephemeralDHKeySize=2048 -Djava.protocol.handler.pkgs=org.apache.catalina.webresources -Dorg.apache.catalina.security.SecurityListener.UMASK=0027 -Dignore.endorsed.dirs= -Dcatalina.base=/usr/local/tomcat9 -Dcatalina.home=/usr/local/tomcat9 -Djava.io.tmpdir=/usr/local/tomcat9/temp

```

查看是否使用了CMS垃圾回收器：

```markdown
[root@yifeng bin]# jinfo -flag UseConcMarkSweepGC 1867
-XX:-UseConcMarkSweepGC
```

查看是否使用了G1垃圾回收器：

```markdown
[root@yifeng bin]# jinfo -flag UseG1GC 1867
-XX:-UseG1GC
```

查看是否使用了ParallelGC垃圾回收器：

```
[root@yifeng bin]# jinfo -flag UseParallelGC 1867
-XX:+UseParallelGC
```

## 1.5.jstat查看虚拟机统计信息

官网地址：https://docs.oracle.com/javase/8/docs/technotes/tools/unix/jstat.html#BEHHGFAE

- 类装载
- 垃圾回收
- JIT编译

参数：

class: 显示有关类加载器行为的统计信息

compiler: 显示有关Java HotSpot VM即时编译器行为的统计信息。

gc: 显示有关垃圾收集堆行为的统计信息。

gccapacity: 显示有关世代容量及其相应空间的统计信息。

gccause：显示有关垃圾收集统计信息的摘要（与-gcutil相同），以及上一次和当前（如果适用）垃圾收集事件的原因。

gcnew：显示新生代行为的统计信息。

gcnewcapacity：显示有关新生代大小及其相应空间的统计信息。

gcold：显示有关老年代行为的统计信息和元空间统计信息。

gcoldcapacity：显示有关老年代的大小的统计信息。

gcmetacapacity：显示有关元空间大小的统计信息。

gcutil：显示有关垃圾收集统计信息的摘要。

printcompilation：显示Java HotSpot VM编译方法统计信息。

比如class：

查看类加载的信息：

```markdown
[root@yifeng bin]# jstat -class 1867
Loaded  Bytes  Unloaded  Bytes     Time   
  3111  6185.0        0     0.0       1.34
```

具体的参数信息所表示的含义如下所示（可在官网查看）：

-class option
Class loader statistics.

Loaded: Number of classes loaded.

Bytes: Number of kBs loaded.

Unloaded: Number of classes unloaded.

Bytes: Number of Kbytes unloaded.

Time: Time spent performing class loading and unloading operations.

查看gc信息：

![1572509300925](assets/1572509300925.png)

```markdown
[root@yifeng bin]# jstat -gc 1867
 S0C    S1C    S0U    S1U      EC       EU        OC         OU       MC     MU    CCSC   CCSU   YGC     YGCT    FGC    FGCT     GCT   
6656.0 2560.0  0.0   2544.1 30720.0   1261.1   40960.0    12325.8   19968.0 19224.6 2304.0 2115.0      5    0.039   0      0.000    0.039
```

具体表示的含义如下（可在官网查看）：

S0C、S1C、S0U、S1U：S0和S1的总量与使用量
EC、EU：Eden区总量与使用量
OC、OU:Old区总量与使用量
MC、MU： Metaspace区总量与使用量
CCSC、CCSU：压缩类空间总量与使用量
YGC、YGCT：Young GC的次数与时间
FGC、FGCT：Full GC的次数与时间
GCT:总的GC时间

定时输出信息：

```markdown
jstat -gc 1867 1000 10
```

这里的1000表示每隔一秒钟输出一次，10表示一共输出10次

![1572509782955](assets/1572509782955.png)



查看即时编译的信息：

```markdown
[root@yifeng bin]# jstat -compiler 1867
Compiled Failed Invalid   Time   FailedType FailedMethod
    2093      0       0     2.50          0
```

## 1.4.jmap+MAT实战内存溢出

![1572509987419](assets/1572509987419.png)

堆区：

​	Young：

​		survivor：

​			S0（又叫From）

​			S1（又叫To）

​		Eden

​	Old：

非堆区：

​	Metaspace

​	CCS

​	CodeCache

### 演示内存溢出场景

新建一个springboot的项目：

```java
<parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.6.RELEASE</version>
    </parent>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!-- https://mvnrepository.com/artifact/asm/asm -->
        <dependency>
            <groupId>asm</groupId>
            <artifactId>asm</artifactId>
            <version>3.3.1</version>
        </dependency>

    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
```

```java
public class User {
    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
```

```java
public class Metaspace extends ClassLoader {
	
    public static List<Class<?>> createClasses() {
        // 类持有
        List<Class<?>> classes = new ArrayList<Class<?>>();
        // 循环1000w次生成1000w个不同的类。
        for (int i = 0; i < 10000000; ++i) {
            ClassWriter cw = new ClassWriter(0);
            // 定义一个类名称为Class{i}，它的访问域为public，父类为java.lang.Object，不实现任何接口
            cw.visit(Opcodes.V1_1, Opcodes.ACC_PUBLIC, "Class" + i, null,
                    "java/lang/Object", null);
            // 定义构造函数<init>方法
            MethodVisitor mw = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>",
                    "()V", null, null);
            // 第一个指令为加载this
            mw.visitVarInsn(Opcodes.ALOAD, 0);
            // 第二个指令为调用父类Object的构造函数
            mw.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object",
                    "<init>", "()V");
            // 第三条指令为return
            mw.visitInsn(Opcodes.RETURN);
            mw.visitMaxs(1, 1);
            mw.visitEnd();
            Metaspace test = new Metaspace();
            byte[] code = cw.toByteArray();
            // 定义类
            Class<?> exampleClass = test.defineClass("Class" + i, code, 0, code.length);
            classes.add(exampleClass);
        }
        return classes;
    }
}
```

```java
@Controller
public class MemoryController {

    private List<User> userList = new ArrayList<>();
    private List<Class> classList = new ArrayList<>();
    /**
     * -Xmx32M -Xms32M
     **/
    @GetMapping("/heap")
    public String heap(){
        System.out.println("堆内存溢出进入");
        int i = 0;
        while (true){
            userList.add(new User(i++, UUID.randomUUID().toString()));
        }
    }


    /**
     * -XX:MetaspaceSize=32M -XX:MaxMetaspaceSize=32M
     **/

    @GetMapping("/nonheap")
    public String nonHeap(){
        System.out.println("非堆内存溢出进入");
        int i = 0;
        while (true){
            classList.addAll(Metaspace.createClasses());
        }
    }

}
```

启动项目前，将对应的虚拟机参数设置好，然后在浏览器端进行访问。

访问heap方法时会出现堆内存溢出：

```markdown
堆内存溢出进入
Exception in thread "http-nio-8080-ClientPoller" Exception in thread "http-nio-8080-BlockPoller" java.lang.OutOfMemoryError: Java heap space
Exception in thread "http-nio-8080-exec-1" java.lang.OutOfMemoryError: Java heap space
```

访问nonheap方法时会出现Metaspace内存溢出：

```markdown
非堆内存溢出进入
Exception in thread "http-nio-8080-exec-1" java.lang.OutOfMemoryError: Metaspace
```

### 导出内存映像文件

#### 内存溢出自动导出：

-XX:+HeapDumpOnOutOfMemoryError

-XX:HeapDumpPath=./

启动前加入上面的参数，然后启动后访问heap这个地址。程序会自动给我们导出一个内存映像文件java_pid20180.hprof

```
堆内存溢出进入
java.lang.OutOfMemoryError: GC overhead limit exceeded
Dumping heap to ./\java_pid20180.hprof ...
Heap dump file created [46734514 bytes in 0.339 secs]
Exception in thread "http-nio-8080-exec-1" java.lang.OutOfMemoryError: GC overhead limit exceeded
```

#### 使用jmap命令手动导出：

格式：jmap -dump:format=b,file=heap.hprof 99996

#### mat分析内存溢出：

官网下载地址：https://www.eclipse.org/mat/downloads.php

下载完成解压就可以使用，启动exe文件之后，打开刚才导出的dump文件。

![1572593474047](assets/1572593474047.png)

点击report下面的leak Suspects，可以看到是哪个文件报的错。

![1572593666254](assets/1572593666254.png)

点击上面的条形图表可以查看对象。

![1572593943633](assets/1572593943633.png)

然后再正则Regex这里输入我们自己的包com.yifeng

![1572594005014](assets/1572594005014.png)

![1572594062242](assets/1572594062242.png)

user对象创建了134021个，明显这里是有问题的，占有的Retained Heap接近20M，Shallow Heap接近32M

通过在User这一行鼠标右键 Merge Shortest Paths to GC Roots,然后再点击倒数第二个

exclude all phanto/weak/soft etc .Reference排除所有的弱引用虚引用。

这里就可以清晰的查看到User对象的引用链。

TaskThread--->MemoryController-->userList---user.

![1572594551634](assets/1572594551634.png)

第二个树状图表也可以查看到对象内存的情况。

![1572594755597](assets/1572594755597.png)

## 1.5.jstack实战死循环与死锁

查看所有线程的运行状态。

jstack 1867

线程运行状态示意图。

- NEW
- RUNABLE
- TIMED_WAITING
- WAITING
- BLOCKED
- TERMINATED

![1572598684807](assets/1572598684807.png)

新建一个CpuController来模拟死循环：

```java
@RestController
public class CpuController {

    /**
     * 死循环
     * */
    @RequestMapping("/loop")
    public List<Long> loop(){
        String data = "{\"data\":[{\"partnerid\":]";
        return getPartneridsFromJson(data);
    }

    public List<Long> getPartneridsFromJson(String data){
        //{\"data\":[{\"partnerid\":982,\"count\":\"10000\",\"cityid\":\"11\"},{\"partnerid\":983,\"count\":\"10000\",\"cityid\":\"11\"},{\"partnerid\":984,\"count\":\"10000\",\"cityid\":\"11\"}]}
        //上面是正常的数据
        List<Long> list = new ArrayList<Long>(2);
        if(data == null || data.length() <= 0){
            return list;
        }
        int datapos = data.indexOf("data");
        if(datapos < 0){
            return list;
        }
        int leftBracket = data.indexOf("[",datapos);
        int rightBracket= data.indexOf("]",datapos);
        if(leftBracket < 0 || rightBracket < 0){
            return list;
        }
        String partners = data.substring(leftBracket+1,rightBracket);
        if(partners == null || partners.length() <= 0){
            return list;
        }
        while(partners!=null && partners.length() > 0){
            int idpos = partners.indexOf("partnerid");
            if(idpos < 0){
                break;
            }
            int colonpos = partners.indexOf(":",idpos);
            int commapos = partners.indexOf(",",idpos);
            if(colonpos < 0 || commapos < 0){
                //partners = partners.substring(idpos+"partnerid".length());//1
                continue;
            }
            String pid = partners.substring(colonpos+1,commapos);
            if(pid == null || pid.length() <= 0){
                //partners = partners.substring(idpos+"partnerid".length());//2
                continue;
            }
            try{
                list.add(Long.parseLong(pid));
            }catch(Exception e){
                //do nothing
            }
            partners = partners.substring(commapos);
        }
        return list;
    }
}
```

使用top命令查看CPU的状态：

然后在浏览器访问loop，

可以看到cpu的负载疯狂飙升一会儿就达到了100%，我们对开几个窗口进行访问，可以发现cpu的利用率还在飙升，一会儿就到达了接近200%。

![1572941146445](assets/1572941146445.png)

那么在生产环境中，我们如何来定位代码的问题呢，首先我们可以看到这个进程的PID为10125，

然后利用jstack命令将这个进程的堆栈信息进行输出。

```
jstack 10125 > 10125.txt
```

然后运行top命令查看10125这个进程里面的线程信息。

```
top -p 10125 -H
```

可以看到这几个线程的CPU使用率很高。

![1572941293410](assets/1572941293410.png)

我们选择其中的一个PID，如10154，然后将这个id转换成16进制。

```
[root@yifeng ~]# printf "%x" 10154
27aa
```

可以看到线程id为：27aa

然后在10125.txt中搜索这个27aa

![1572941641849](assets/1572941641849.png)

可以看到，这个线程处于RUNNABLE状态，而且在执行这两个方法。

```java
at com.yifeng.jvm.controller.CpuController.getPartneridsFromJson(CpuController.java:53)
at com.yifeng.jvm.controller.CpuController.loop(CpuController.java:24)
```

明显就是我们刚才写的那个死循环的代码，我们可在文件中搜索一下这个方法，可以看到刚好有四个这样的方法，我们不难得出，刚好就是那四个线程，因为我们在浏览器端开了四个窗口进行访问，所以后台开启了四个线程进行处理。通过jstack可以快速的定位是哪个线程的问题，在实际生产环境中也可以使用。

下面来演示一个死锁的问题。

```java
private Object lock1 = new Object();
private Object lock2 = new Object();

/**
     * 死锁
     * */
@RequestMapping("/deadlock")
public String deadlock(){
    new Thread(()->{
        synchronized(lock1) {
            try {Thread.sleep(1000);}catch(Exception e) {}
            synchronized(lock2) {
                System.out.println("Thread1 over");
            }
        }
    }) .start();
    new Thread(()->{
        synchronized(lock2) {
            try {Thread.sleep(1000);}catch(Exception e) {}
            synchronized(lock1) {
                System.out.println("Thread2 over");
            }
        }
    }) .start();
    return "deadlock";
}
```

代码的逻辑很容易理解，上面的线程获取lock1，睡眠1秒获取lock2；下面的线程正好相反，随着程序的运行，那么很容易就会发生死锁。

启动服务浏览器端访问这个接口：

```
http://49.232.160.164:8888/deadlock
```

成功输出“deadlock”，

使用jstack命令，将这个进程的输出信息重定向到文件然后导出到本地进行查看

直接翻到文件末尾：

```
Found one Java-level deadlock:
=============================
"Thread-5":
  waiting to lock monitor 0x00007fca043ee6f8 (object 0x00000000c5282028, a java.lang.Object),
  which is held by "Thread-4"
"Thread-4":
  waiting to lock monitor 0x00007fca28004b68 (object 0x00000000c5282038, a java.lang.Object),
  which is held by "Thread-5"

Java stack information for the threads listed above:
===================================================
"Thread-5":
	at com.yifeng.jvm.controller.CpuController.lambda$deadlock$1(CpuController.java:47)
	- waiting to lock <0x00000000c5282028> (a java.lang.Object)
	- locked <0x00000000c5282038> (a java.lang.Object)
	at com.yifeng.jvm.controller.CpuController$$Lambda$393/1877085248.run(Unknown Source)
	at java.lang.Thread.run(Thread.java:748)
"Thread-4":
	at com.yifeng.jvm.controller.CpuController.lambda$deadlock$0(CpuController.java:39)
	- waiting to lock <0x00000000c5282038> (a java.lang.Object)
	- locked <0x00000000c5282028> (a java.lang.Object)
	at com.yifeng.jvm.controller.CpuController$$Lambda$392/1809683939.run(Unknown Source)
	at java.lang.Thread.run(Thread.java:748)

Found 1 deadlock.
```

可以看到，发现了一个死锁，

Thread-5等待一个锁，但是这个锁被Thread-4所持有

Thread-4等待一个锁，但是这个锁被Thread-5所持有

下面给出了这两个线程在程序中的代码位置。

## 1.6.JVisualVM

在命令行输入：jvisualvm即可打开。

```
jvisualvm
```

![1573024604037](assets/1573024604037.png)

比如我们双击idea进程，就可以打开idea进程的所有信息。

**1.概述**

主要展示idea的基本属性，和启动的jvm参数信息以及系统属性。

![1573024740672](assets/1573024740672.png)

**2.监视**

主要是通过图形化的形式展示：CPU、内存、类、线程的实时信息。同时这里还可以进行堆Dump，进行内存映像文件的分析，同时也可以手动导入外部的内存映像文件进行分析。

![1573024889837](assets/1573024889837.png)

**3.线程**

主要是展示线程的实时信息。

![1573024953857](assets/1573024953857.png)

**4.抽样**

可以对内存和CPU进行分析，查看哪个对象占得CPU或者内存比较高。

![1573025030176](assets/1573025030176.png)

**插件安装：**

点击菜单栏区域的工具然后点击插件，点击可用插件，然后勾选一些插件，最后点击安装即可。

这里我们将BTrece和VisualGC安装上。

**连接远程主机：**

首先在远程这里添加一个远程主机，输入地址即可。

![1573029466895](assets/1573029466895.png)

添加主机成功之后，有两种方式可以监控远程的java进程

1.jmx

2.jstatd

jmx监控远程Tomcat：

修改catalina.sh:

端口跟IP修改成自己的即可。

在如下位置加入：

![1573029922408](assets/1573029922408.png)

```
JAVA_OPTS="$JAVA_OPTS $JSSE_OPTS"的上面
```

加入以下配置。这里的端口可以自定义只要不冲突就行。

```
JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote #开启远程访问
-Dcom.sun.management.jmxremote.port=9004 #端口
-Dcom.sun.management.jmxremote.authenticate=false #是否需要认证
-Dcom.sun.management.jmxremote.ssl=false #是否需要ssl
-Djava.net.preferIPv4Stack=true #优先支持IPv4
-Diava.rmi.server.hostname=172.213.150.33"
```

然后再重启Tomcat。

添加jmx连接：

![1573029999408](assets/1573029999408.png)

这时可能会报一个错误。



![1573030033111](assets/1573030033111.png)

这时我们需要配置服务器端的hostname。

1.将network中的HOSTNAME修改成本机的IP地址。如果没有自己添加一行

```
vim /etc/sysconfig/network
```

```
NETWORKING=yes
HOSTNAME=172.213.150.33
```

修改完成之后，重启网络服务

```
service network restart
```

2.修改hosts为本机IP地址，将127.0.0.1修改成自己的IP地址

```
172.213.150.33 yifeng yifeng
172.213.150.33 localhost.localdomain localhost
172.213.150.33 localhost4.localdomain4 localhost4
```

然后就可以正常访问了。连接之后的操作跟本地差不多。

监控普通的Java程序。方式其实差不多，只是在启动的时候将这些参数给加上。

比如我们启动一个springboot的jar程序是这样的

```
nohup java -jar jvm-monitor-1.0-SNAPSHOT.jar &
```

现在则是这样的

```
nohup java -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9005 
-Dcom.sun.management.jmxremote.authenticate=false 
-Dcom.sun.management.jmxremote.ssl=false 
-Djava.net.preferIPv4Stack=true 
-Diava.rmi.server.hostname=172.213.150.33
-jar jvm-monitor-1.0-SNAPSHOT.jar &
```

启动之后就可以连接了。

![1573030685077](assets/1573030685077.png)

![1573030718513](assets/1573030718513.png)

## 1.7.BTrace

### 安装：

BTrace可以动态地向目标应用程序的字节码注入追踪代码

Java ComplierApi、 JVMTI、 Agent、 Instrumentation+ASM

github地址：https://github.com/btraceio/btrace

可以通过源码安装或者直接下载release包：https://github.com/btraceio/btrace/releases/tag/v1.3.11.3

下载解压之后，配置好环境变量即可。

```
BTRACE_HOME
D:\mygithub\jvm\btrace-bin-1.3.11.3
然后加入到path中
%BTRACE_HOME%\bin
```

### 运行：

运行BTrace脚本的两种方式：

1.在JVisualVM中添加BTrace插件，添加classpath

2.使用命令行 btrace <pid> <script>

新建一个controller：

```java
@RestController
public class BTraceController {

    @RequestMapping("/arg1")
    public String arg1(@RequestParam("name") String name){
        return "hello " + name;
    }

}
```

访问：http://localhost:8888/arg1?name=yifeng

浏览器正确输出：hello yifeng

首先将我们下载下来的BTrace的三个jar包引入到工程中，如果是maven工程那么像一下方式引入：

systemPath改成自己本地的路径即可，注意版本号。

```xml
<dependency>
            <groupId>com.sun.btrace</groupId>
            <artifactId>btrace-agent</artifactId>
            <version>1.3.11.3</version>
            <type>jar</type>
            <scope>system</scope>
            <systemPath>D:\mygithub\jvm\btrace-bin-1.3.11.3\build\btrace-agent.jar</systemPath>
        </dependency>
        <dependency>
            <groupId>com.sun.btrace</groupId>
            <artifactId>btrace-boot</artifactId>
            <version>1.3.11.3</version>
            <type>jar</type>
            <scope>system</scope>
            <systemPath>D:\mygithub\jvm\btrace-bin-1.3.11.3\build\btrace-boot.jar</systemPath>
        </dependency>
        <dependency>
            <groupId>com.sun.btrace</groupId>
            <artifactId>btrace-client</artifactId>
            <version>1.3.11.3</version>
            <type>jar</type>
            <scope>system</scope>
            <systemPath>D:\mygithub\jvm\btrace-bin-1.3.11.3\build\btrace-client.jar</systemPath>
        </dependency>
```

然后新建一个类：clazz就是需要监控的类的全路径，method就是方法名，location这里的Kind.ENTRY就是参数进入的时候进行拦截。方法里面就是打印参数，打印class。

```java
package com.yifeng.jvm.btrace;

import com.sun.btrace.AnyType;
import com.sun.btrace.BTraceUtils;
import com.sun.btrace.annotations.BTrace;
import com.sun.btrace.annotations.Kind;
import com.sun.btrace.annotations.Location;
import com.sun.btrace.annotations.OnMethod;
import com.sun.btrace.annotations.ProbeClassName;
import com.sun.btrace.annotations.ProbeMethodName;

/**
 * @author kevin
 * @version v1.0
 * @description
 * @date 2019-11-07 16:28
 **/
@BTrace
public class PrintArgSimple {

    @OnMethod(
            clazz="com.yifeng.jvm.controller.BTraceController",
            method="arg1",
            location=@Location(Kind.ENTRY)
    )
    public static void anyRead(@ProbeClassName String pcn, @ProbeMethodName String pmn, AnyType[] args) {
        BTraceUtils.printArray(args);
        BTraceUtils.println(pcn+","+pmn);
        BTraceUtils.println();
    }
}
```

下面演示怎么运行这个脚本。

cmd然后进入到这个类所在的目录：

先使用jps查看一下这个工程的pid

然后运行：

```
btrace 15928 PrintArgSimple.java
```

然后我们浏览器再请求一次，可以看到在控制台输出了参数信息和类的路径方法名。

```
[yifeng, ]
com.yifeng.jvm.controller.BTraceController,arg1
```

使用JVisualVM运行：

在进程上面右键然后点击btrace application。

进入之后将我们的程序代码复制进去，点击start。然后浏览器访问一下。

```
[yifeng, ]
com.yifeng.jvm.controller.BTraceController,arg1
```

### **BTrace详解：**

#### 拦截方法：

##### 普通方法：@OnMethod( clazz="",method="" )

##### 构造函数：@OnMethod( clazz="",method="<init>" )

在BTraceController中新增一个方法

```java
@RequestMapping("/constructor")
    public User constructor(User user){
        return user;
    }
```

新建一个类来拦截构造函数：

```java
@BTrace
public class PrintConstructor {

    @OnMethod(
            clazz="com.yifeng.jvm.pojo.User",
            method="<init>",
            location=@Location(Kind.ENTRY)
    )
    public static void anyRead(@ProbeClassName String pcn, @ProbeMethodName String pmn, AnyType[] args) {
        BTraceUtils.printArray(args);
        BTraceUtils.println(pcn+","+pmn);
        BTraceUtils.println();
    }
}
```

运行工程在浏览器输入：http://localhost:8888/constructor?name=yifeng&id=1

界面正常显示user对象，然后运行btrace命令：

```
 btrace 150504 .\PrintConstructor.java
```

在浏览器再次访问这个地址：可以看到成功的拦截到

改变输入的参数：http://localhost:8888/constructor?name=kevin&id=2，也可以拦截到：

```java
[1, yifeng, ]
com.yifeng.jvm.pojo.User,<init>

[2, kevin, ]
com.yifeng.jvm.pojo.User,<init>

```

##### 拦截同名方法，用参数区分。

在BTraceController中新建两个方法

```java
@RequestMapping("/same1")
    public String same(@RequestParam("name") String name){
        return "hello " + name;
    }

    @RequestMapping("/same2")
    public String same(@RequestParam("name") String name, @RequestParam("id") Integer id){
        return "hello " + name + "," + id;
    }
```

在浏览器访问这两个方法，正常输出。

新建一个类来拦截这个方法，这里我们拦截有name和id两个参数的方法

```java
@BTrace
public class PrintSame {

    @OnMethod(
            clazz="com.yifeng.jvm.controller.BTraceController",
            method="same"
    )
    public static void anyRead(@ProbeClassName String pcn, @ProbeMethodName String pmn, String name, Integer id) {
        BTraceUtils.println(pcn+","+pmn+","+name+","+id);
        BTraceUtils.println();
    }
}
```

运行btrace命令：

```
 btrace 41516 .\PrintSame.java
```

浏览器我们这时输入网址：http://localhost:8888/same2?name=kevin&id=2

控制台正确拦截到了我们的信息

```
com.yifeng.jvm.controller.BTraceController,same,kevin,2
```

我们在输入：http://localhost:8888/same1?name=kevin

此时则不会进行拦截。

#### 拦截时机

##### Kind.ENTRY：入口，默认值

前面已经演示过了

##### Kind.RETURN：返回

这里以拦截arg1这个方法的返回值为例，新建一个类。只需要将location这里设置成Kind.RETURN，然后参数这里加上@Return注解。

```java
@BTrace
public class PrintReturn {
	
	@OnMethod(
	        clazz="com.yifeng.jvm.controller.BTraceController",
	        method="arg1",
	        location=@Location(Kind.RETURN)
	)
	public static void anyRead(@ProbeClassName String pcn, @ProbeMethodName String pmn, @Return AnyType result) {
		BTraceUtils.println(pcn+","+pmn + "," + result);
		BTraceUtils.println();
    }
}
```

运行我们的工程，然后运行btrace命令，我们请求arg1这个方法。观察btrace窗口的控制台。

```
btrace 91336 .\PrintReturn.java
```

成功输出信息：

```
com.yifeng.jvm.controller.BTraceController,arg1,hello yifeng
```

##### Kind.THROW：异常

在controller中新建一个方法，这里手动制造一个异常异常并使用try catch进行包裹，但是在catch语句中什么都不做。

```java
@RequestMapping("/exception")
public String exception() {
    try {
        System.out.println("start...");
        System.out.println(1/0);
        System.out.println("end...");
    }catch(Exception e) {
        //
    }
    return "success";
}
```

首先我们浏览器访问一下这个接口，返回success，并且控制台也没有异常信息。下面我们就通过btrace来重现这个异常信息。

首先新建一个类来捕获这个异常信息并且将堆栈信息进行打印。

```java
@BTrace 
public class PrintOnThrow {    
    // store current exception in a thread local
    // variable (@TLS annotation). Note that we can't
    // store it in a global variable!
    @TLS 
    static Throwable currentException;

    // introduce probe into every constructor of java.lang.Throwable
    // class and store "this" in the thread local variable.
    @OnMethod(
        clazz="java.lang.Throwable",
        method="<init>"
    )
    public static void onthrow(@Self Throwable self) {//new Throwable()
        currentException = self;
    }

    @OnMethod(
        clazz="java.lang.Throwable",
        method="<init>"
    )
    public static void onthrow1(@Self Throwable self, String s) {//new Throwable(String msg)
        currentException = self;
    }

    @OnMethod(
        clazz="java.lang.Throwable",
        method="<init>"
    )
    public static void onthrow1(@Self Throwable self, String s, Throwable cause) {//new Throwable(String msg, Throwable cause)
        currentException = self;
    }

    @OnMethod(
        clazz="java.lang.Throwable",
        method="<init>"
    )
    public static void onthrow2(@Self Throwable self, Throwable cause) {//new Throwable(Throwable cause)
        currentException = self;
    }

    // when any constructor of java.lang.Throwable returns
    // print the currentException's stack trace.
    @OnMethod(
        clazz="java.lang.Throwable",
        method="<init>",
        location=@Location(Kind.RETURN)
    )
    public static void onthrowreturn() {
        if (currentException != null) {
        	BTraceUtils.Threads.jstack(currentException);
        	BTraceUtils.println("=====================");
            currentException = null;
        }
    }
}
```

这里对所有Throwable的构造方法都进行了拦截，根据java的运行机制，子类在初始化之前必定会先初始化父类，而且java中的Exception和Error都继承于Throwable，所以所有的异常错误信息都会来初始化这个Throwable对象，所以我们如果对它的构造方法进行拦截，那么所有的异常都将被拦截到。

就来运行这个方法。

```
 btrace 91336 .\PrintOnThrow.java
```

再次请求exception这个方法，观察控制台。

```
java.lang.ArithmeticException: / by zero
        com.yifeng.jvm.controller.BTraceController.exception(BTraceController.java:41)
```

这时就会捕获到这个异常信息。

##### Kind.Line：行

这里主要是查看某一行代码是否执行到了，还是以exception这个方法为例。

![1573525110191](assets/1573525110191.png)

可以看到打印start这里时40行，而打印end这里是42行，下面我们新建一个类来看看这两行代码是否执行到了。

```java
@BTrace
public class PrintLine {
	
	@OnMethod(
	        clazz="com.yifeng.jvm.controller.BTraceController",
	        method="exception",
	        location=@Location(value=Kind.LINE, line=40)
	)
	public static void anyRead(@ProbeClassName String pcn, @ProbeMethodName String pmn, int line) {
		BTraceUtils.println(pcn+","+pmn + "," +line);
		BTraceUtils.println();
    }
}
```

运行这个方法。

```
btrace 91336 .\PrintLine.java
```

然后访问exception这个方法。

```
com.yifeng.jvm.controller.BTraceController,exception,40
```

这里成功打印，我们,将这个line=40改成line=42，再次运行btrace命令，然后访问exception方法。

这时控制台没有任何输出，说明这一行没有执行。

如果我们想知道这个方法里面所有行的运行情况怎么办呢，btrace这里也提供了一个参数就是-1，line=-1，修改完成之后，我们重新启动这个btrace命令，访问exception方法观察控制台。

```java
com.yifeng.jvm.controller.BTraceController,exception,40

com.yifeng.jvm.controller.BTraceController,exception,41

com.yifeng.jvm.controller.BTraceController,exception,43

com.yifeng.jvm.controller.BTraceController,exception,46
```

可以很清楚看到这个方法中到底执行了哪几行代码，实际的生产中，我们也可以通过这种打印所有行的方式来查看哪里的代码没有执行，那么他的上面就肯定抛异常了，也可以通过拦截Throwable的形式，打印堆栈信息。

##### 拦截复杂类型。

拦截对象参数：

新建一个方法。

```java
@RequestMapping("/arg2")
public User arg2(User user){
    return user;
}
```

新建一个类，对这个方法进行拦截。

```java
@BTrace
public class PrintArgComplex {
	
	
	@OnMethod(
	        clazz="com.yifeng.jvm.controller.BTraceController",
	        method="arg2",
	        location=@Location(Kind.ENTRY)
	)
	public static void anyRead(@ProbeClassName String pcn, @ProbeMethodName String pmn, User user) {
		//print all fields
		BTraceUtils.printFields(user);
		//print one field
		Field filed2 = BTraceUtils.field("com.yifeng.jvm.pojo.User", "name");
		BTraceUtils.println(BTraceUtils.get(filed2, user));
		BTraceUtils.println(pcn+","+pmn);
		BTraceUtils.println();
    }
}
```

这里对所有的字段进行了打印，然后下面也可以对单独的一个字段进行打印需要指定对象的全路径。

然后启动工程。访问这个arg2方法，正常返回。

运行btrace命令

```
btrace 16768 .\PrintArgComplex.java
```

这时会报异常

![1573527403219](assets/1573527403219.png)

这里我们必须指定classpath，不然它找不到user对象。找到class文件的路径。

```
 btrace -cp "D:\mygithub\jvm\jvm-monitor\target\class
es"  16768 .\PrintArgComplex.java
```

再次访问arg2方法。这时控制台就正确打印了我们需要的信息。

```
{id=1, name=yifeng, }
yifeng
com.yifeng.jvm.controller.BTraceController,arg2
```

正则表达式拦截：

新建一个类

```java
@BTrace
public class PrintRegex {
	
	@OnMethod(
	        clazz="com.yifeng.jvm.controller.BTraceController",
	        method="/.*/"
	)
	public static void anyRead(@ProbeClassName String pcn, @ProbeMethodName String pmn) {
		BTraceUtils.println(pcn+","+pmn);
		BTraceUtils.println();
    }
}
```

这里的.*意思是拦截这个类里面的所有方法。

运行这个java文件，然后我们访问controller中的一些方法，看是否都进行了拦截。

```
btrace 16768 .\PrintRegex.java
```

```
com.yifeng.jvm.controller.BTraceController,arg2

com.yifeng.jvm.controller.BTraceController,arg1

com.yifeng.jvm.controller.BTraceController,exception
```

通过控制台我们知道，所有的方法都被拦截到了。

拦截系统属性，VM参数，操作系统属性等：

新建一个类，里面直接进行打印。

```java
@BTrace
public class PrintJinfo {
    static {
    	BTraceUtils.println("System Properties:");
    	BTraceUtils.printProperties();
    	BTraceUtils.println("VM Flags:");
    	BTraceUtils.printVmArguments();
    	BTraceUtils.println("OS Enviroment:");
    	BTraceUtils.printEnv();
    	BTraceUtils.exit(0);
    }
}
```

运行这个java文件。可以看到所有的信息都已经被打印了

```
btrace 16768 .\PrintJinfo.java
```

![1573528113265](assets/1573528113265.png)

![1573528159015](assets/1573528159015.png)

![1573528182673](assets/1573528182673.png)

#### **注意事项：**

默认只能本地运行。

生产环境下可以使用，但是被修改的字节码不会被还原

# 2.Tomcat性能监控与调优

## 2.1.Tomcat远程Debug

JDWP

修改startup.sh

在Tomcat的bin目录下面的启动文件startup.sh文件的末尾一行的start前面加上jpda。

![1573529093744](assets/1573529093744.png)

修改catalina.sh

搜索jpda

![1573529334683](assets/1573529334683.png)

在JPDA_ADDRESS这里，将localhost:8000，改成一个其他的端口如：12345.

然后启动Tomcat，启动成功之后，通过浏览器端访问这个Tomcat确保是正常运行的。

在本地我们新建一个springboot的工程。

新建一个controller用于测试，

```java
@RestController
@RequestMapping("/tomcat")
public class TomcatController {

    @RequestMapping("/hello")
    public String hello(){
        String str = "";
        for (int i = 0; i < 10; i++) {
            str += i;
        }
        return str;
    }
}
```

然后将我们的工程打成一个war包，这里只需要修改两个地方即可。

1.在pom文件中，将打包方式改成war。

```xml
<packaging>war</packaging>
```

2.修改启动类，让他继承SpringBootServletInitializer，然后重写configure方法。

```java
@SpringBootApplication
public class JvmTomcatApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(JvmTomcatApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(JvmTomcatApplication.class);
    }
}
```

将我们的包丢到服务器的Tomcat中去，改个名字，因为默认打包的名字太长了，然后再重新启动Tomcat。

启动成功之后，先用8080端口来查看一下是否能够成功访问，能成功的话下面我们就来开始配置断点调试。

在idea中，点击菜单栏区域的  run-->Edit Configurations

然后点击+新建一个这里选择Remote。

![1573542418390](assets/1573542418390.png)

name可以随便取，host填写你的服务器地址，port就是jpda那个端口，jdk选择对应的即可。然后点击apply，ok

![1573542688423](assets/1573542688423.png)

配置如果成功的话，控制台就会出现下面的提示信息。

```
Connected to the target VM, address: '192.168.1.2:12345', transport: 'socket'
```

在我们的controller方法中打上断点。然后再浏览器访问这个方法。可以看到成功的进入了断点。

```
http://192.168.1.2:8080/jvm-tomcat/tomcat/hello
```

![1573543971763](assets/1573543971763.png)

到这里为止，Tomcat的远程debug就差不多了。



## 2.2.tomcat-manager监控

文档：docs/html-manager-howto.html

步骤：

1.conf/tomcat-users.xml添加用户

2.conf/Catalina/localhost/manager.xml配置允许的远程连接

3.重启

tomcat-users.xml配置

```xml
<role rolename="tomcat"/>
<role rolename="tomcat-status"/>
<role rolename="manager-gui"/>
<user username="tomcat" password="123456" roles="tomcat,tomcat-status,manager-gui"/>
```

manager.xml默认是没有的，这里我们新建一个

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Context privileged="true" antiResourceLocking="false"
         docBase="${catalina.home}/webapps/manager">
  <Valve className="org.apache.catalina.valves.RemoteAddrValve"
         allow="127\.0\.0\.1" />
</Context>
```

启动我们的tomcat。

输出地址进行访问：http://127.0.0.1:8080/manager

这里提示我们输入用户名密码，我们输入上面配置的即可进入。

![1573545710128](assets/1573545710128.png)

点击Manager栏目这里的Server Status。可以看到我们应用的一些基本信息。jvm内存情况和线程情况

![1573545796840](assets/1573545796840.png)

## 2.3.psi-probe监控

官网地址：https://github.com/psi-probe/psi-probe

将代码下载到本地，然后在根目录执行：mvn package,不过这种方式非常慢，有时候可能由于网络的问题会打包失败。所以这里我们选择直接下载war包。

war包地址：https://github.com/psi-probe/psi-probe/releases

war包下载完成之后，直接丢到tomcat的webapp目录下面然后重启tomcat即可。

注意：这里同上面的tomcat-manager一样，也是需要配置用户和权限的。

将war包放进去之后直接启动tomcat。

访问地址：http://127.0.0.1:8080/probe/

主界面如下所示：

![1573626191109](assets/1573626191109.png)

Applications：应用的统计信息，jsp预编译

Data Sources：数据源

Deployment：可以上传war包进行部署

Logs：日志信息

Threads：线程信息

Cluster：集群

System：系统的基本信息包括硬件信息，内存的使用信息等

Connections：有哪些连接者

Certificates：证书信息

QuickCheck：基本环境验证

## 2.4.Tomcat调优

### 2.4.1.内存优化

### 2.4.2.线程优化

docs/config/http.html

maxConnections:

服务器在任何给定时间将接受和处理的最大连接数。达到此数目后，服务器将接受但不处理另一个连接。在处理的连接数降至**maxConnections**以下之前，该附加连接将被阻止，此时服务器将再次开始接受和处理新的连接。请注意，一旦达到限制，操作系统仍然可以根据`acceptCount`设置接受连接。默认值因连接器类型而异。对于NIO和NIO2，默认值为`10000`。对于APR /本机，默认值为`8192`。

请注意，对于Windows上的APR /本机，配置的值将减小为1024的最大倍，该最大值小于或等于maxConnections。这样做是出于性能原因。如果设置为-1，将禁用maxConnections功能，并且不计算连接数。

acceptCount:

使用所有可能的请求处理线程时，传入连接请求的最大队列长度。队列已满时收到的任何请求都将被拒绝。默认值为100。

MaxThread:

此**Connector**将创建的请求处理线程的最大数量，确定了可以处理的同时请求的最大数量。如果未指定，则此属性设置为200。如果执行程序与此连接器相关联，则此属性将被忽略，因为连接器将使用执行程序而不是内部线程池执行任务。请注意，如果配置了执行程序，则将正确记录为此属性设置的任何值，但是会报告该值（例如，通过JMX）， `-1`以明确未使用它。

minSpareThreads：

始终保持运行状态的最小线程数。如果未指定，`10`则使用默认值。如果执行程序与此连接器相关联，则此属性将被忽略，因为连接器将使用执行程序而不是内部线程池执行任务。请注意，如果配置了执行程序，则将正确记录为此属性设置的任何值，但是会报告该值（例如，通过JMX）， `-1`以明确未使用它。

### 2.4.3.配置优化

docs/config/host.html

autoDeploy：

周期性的检查是否有新的应用加入，如果有就自动部署。生产环境设为false。

enableLookups：

设置为`true`是否要调用以 `request.getRemoteHost()`执行DNS查找以返回远程客户端的实际主机名。设置为`false`跳过DNS查找并改为以字符串形式返回IP地址（从而提高性能）。默认情况下，DNS查找被禁用。

docs/config/context.html：

reloadable：

设置为`true`，如果你想Catalina 监测 `/WEB-INF/classes/`和`/WEB-INF/lib`下面的class更改，如果检测到变化自动重新加载Web应用程序。此功能在应用程序开发期间非常有用，但是它需要大量的运行时开销，因此不建议在已部署的生产应用程序上使用。这就是为什么此属性的默认设置为*false的原因*。但是，您可以使用[Manager](../manager-howto.html) Web应用程序来触发按需重新加载已部署的应用程序。

conf/server.xml

```xml
 <Connector port="8080" protocol="HTTP/1.1"
               connectionTimeout="20000"
			   URIEncoding="utf-8"
               redirectPort="8443" />
```

jsp禁用session。

# 3.nginx性能监控与调优

## 3.1.安装

1.安装先决条件yum-utils

```
yum install yum-utils
```

2.设置nginx packages repository

```
vim /etc/yum.repos.d/nginx.repo
```

```
[nginx-stable]
name=nginx stable repo
baseurl=http://nginx.org/packages/centos/$releasever/$basearch/
gpgcheck=1
enabled=1
gpgkey=https://nginx.org/keys/nginx_signing.key
module_hotfixes=true

[nginx-mainline]
name=nginx mainline repo
baseurl=http://nginx.org/packages/mainline/centos/$releasever/$basearch/
gpgcheck=1
enabled=0
gpgkey=https://nginx.org/keys/nginx_signing.key
module_hotfixes=true
```

```
yum install nginx
```

3.启动nginx

配置文件路径：

```
/etc/nginx
```

![1573635039544](assets/1573635039544.png)

nginx.conf是它的主配置文件

```conf
user  nginx; #用户
worker_processes  1; #工作进程数

error_log  /var/log/nginx/error.log warn; #错误日志
pid        /var/run/nginx.pid; #pid


events {
    worker_connections  1024; #工作连接数
}


http {
    include       /etc/nginx/mime.types; #mime.types
    default_type  application/octet-stream; #默认类型，二进制流

    log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
                      '$status $body_bytes_sent "$http_referer" '
                      '"$http_user_agent" "$http_x_forwarded_for"';

    access_log  /var/log/nginx/access.log  main;

    sendfile        on; 
    #tcp_nopush     on;

    keepalive_timeout  65; #长连接

    #gzip  on;

    include /etc/nginx/conf.d/*.conf; #包含的其他配置文件
}
```

mime.types:

```
types {
    text/html                                        html htm shtml;
    text/css                                         css;
    text/xml                                         xml;
    image/gif                                        gif;
    image/jpeg                                       jpeg jpg;
    application/javascript                           js;
    application/atom+xml                             atom;
    application/rss+xml                              rss;

    text/mathml                                      mml;
    text/plain                                       txt;
    text/vnd.sun.j2me.app-descriptor                 jad;
    text/vnd.wap.wml                                 wml;
    text/x-component                                 htc;

    image/png                                        png;
    image/svg+xml                                    svg svgz;
    image/tiff                                       tif tiff;
    image/vnd.wap.wbmp                               wbmp;
    image/webp                                       webp;
    image/x-icon                                     ico;
    image/x-jng                                      jng;
    image/x-ms-bmp                                   bmp;

    font/woff                                        woff;
    font/woff2                                       woff2;

    application/java-archive                         jar war ear;
    application/json                                 json;
    application/mac-binhex40                         hqx;
    application/msword                               doc;
    application/pdf                                  pdf;
    application/postscript                           ps eps ai;
    application/rtf                                  rtf;
    application/vnd.apple.mpegurl                    m3u8;
    application/vnd.google-earth.kml+xml             kml;
    application/vnd.google-earth.kmz                 kmz;
    application/vnd.ms-excel                         xls;
    application/vnd.ms-fontobject                    eot;
    application/vnd.ms-powerpoint                    ppt;
    application/vnd.oasis.opendocument.graphics      odg;
    application/vnd.oasis.opendocument.presentation  odp;
    application/vnd.oasis.opendocument.spreadsheet   ods;
    application/vnd.oasis.opendocument.text          odt;
    application/vnd.openxmlformats-officedocument.presentationml.presentation
                                                     pptx;
    application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
                                                     xlsx;
    application/vnd.openxmlformats-officedocument.wordprocessingml.document
                                                     docx;
    application/vnd.wap.wmlc                         wmlc;
    application/x-7z-compressed                      7z;
    application/x-cocoa                              cco;
    application/x-java-archive-diff                  jardiff;
    application/x-java-jnlp-file                     jnlp;
    application/x-makeself                           run;
    application/x-perl                               pl pm;
    application/x-pilot                              prc pdb;
    application/x-rar-compressed                     rar;
    application/x-redhat-package-manager             rpm;
    application/x-sea                                sea;
    application/x-shockwave-flash                    swf;
    application/x-stuffit                            sit;
    application/x-tcl                                tcl tk;
    application/x-x509-ca-cert                       der pem crt;
    application/x-xpinstall                          xpi;
    application/xhtml+xml                            xhtml;
    application/xspf+xml                             xspf;
    application/zip                                  zip;

    application/octet-stream                         bin exe dll;
    application/octet-stream                         deb;
    application/octet-stream                         dmg;
    application/octet-stream                         iso img;
    application/octet-stream                         msi msp msm;

    audio/midi                                       mid midi kar;
    audio/mpeg                                       mp3;
    audio/ogg                                        ogg;
    audio/x-m4a                                      m4a;
    audio/x-realaudio                                ra;

    video/3gpp                                       3gpp 3gp;
    video/mp2t                                       ts;
    video/mp4                                        mp4;
    video/mpeg                                       mpeg mpg;
    video/quicktime                                  mov;
    video/webm                                       webm;
    video/x-flv                                      flv;
    video/x-m4v                                      m4v;
    video/x-mng                                      mng;
    video/x-ms-asf                                   asx asf;
    video/x-ms-wmv                                   wmv;
    video/x-msvideo                                  avi;
}

```

conf.d/default.conf

```
server {
    listen       80; #监听端口
    server_name  localhost; #服务名

    #charset koi8-r;
    #access_log  /var/log/nginx/host.access.log  main;

    location / {
        root   /usr/share/nginx/html; #本地根目录
        index  index.html index.htm; #根路径访问的文件
    }

    #error_page  404              /404.html;

    # redirect server error pages to the static page /50x.html
    #
    error_page   500 502 503 504  /50x.html;
    location = /50x.html {
        root   /usr/share/nginx/html;
    }

    # proxy the PHP scripts to Apache listening on 127.0.0.1:80
    #
    #location ~ \.php$ {
    #    proxy_pass   http://127.0.0.1;
    #}

    # pass the PHP scripts to FastCGI server listening on 127.0.0.1:9000
    #
    #location ~ \.php$ {
    #    root           html;
    #    fastcgi_pass   127.0.0.1:9000;
    #    fastcgi_index  index.php;
    #    fastcgi_param  SCRIPT_FILENAME  /scripts$fastcgi_script_name;
    #    include        fastcgi_params;
    #}

    # deny access to .htaccess files, if Apache's document root
    # concurs with nginx's one
    #
    #location ~ /\.ht {
    #    deny  all;
    #}
}

```

启动：

```
nginx
systemctl start nginx
```

```
[root@yifeng nginx]# ps -ef | grep nginx
root     17790     1  0 16:59 ?        00:00:00 nginx: master process nginx
nginx    17791 17790  0 16:59 ?        00:00:00 nginx: worker process
root     17854 13534  0 17:00 pts/2    00:00:00 grep --color=auto nginx
```

其他命令：

停止：

```
systemctl stop nginx
nginx -s stop
```

重启

```
nginx -s reload
```

查看版本信息及安装信息

```
nginx -V
```

注意：

配置反向代理要关闭selinux

```
setenforce 0
```

## 3.2.ngx_http_stub_status监控连接信息

注意：要在安装的时候将这个模块给安装上，使用yum安装默认是有的，如果使用的是源码安装这个模块必须要加上。

然后找到nginx的配置文件，这里我们在conf.d目录下面的default.conf里面进行配置。加入以下配置即可

```
location = /nginx_status {
        stub_status on;
        access_log 0ff;
        allow 127.0.0.1;
        deny all;
}
```

配置完之后，重启nginx。

然后浏览器访问

```
http://192.168.1.4/nginx_status
```

这是会出现一个403，因为上面只能允许127.0.0.1也就是本机才能访问所以我们在本机执行以下wget命令

```
wget 127.0.0.1/nginx_status
```

执行完成之后，会在当前目录下生成一个nginx_status文件，查看这个文件输出如下：

```
Active connections: 1 
server accepts handled requests
 5 5 4 
Reading: 0 Writing: 1 Waiting: 0
```

Active connections：当前活动的客户端连接数包含正在等待的连接数

server accepts handled requests：服务器 接收  处理  请求的连接数

Reading: 0 Writing: 1 Waiting: 0：当前请求  写回客户端 等待的连接数

## 3.3.ngxtop监控请求信息

官网地址：https://github.com/lebinh/ngxtop

安装python-pip

```
yum install epel-release
yum install python-pip
```

安装ngxtop

```
pip install ngxtop
```

```
ngxtop
```

![1573699306220](assets/1573699306220.png)

指定配置文件：

```
ngxtop -c /etc/nginx/nginx.conf
```

查询状态是200：

```
ngxtop -c /etc/nginx/nginx.conf -i 'status == 200'
```

查询访问最多ip：

```
ngxtop -c /etc/nginx/nginx.conf -g remote_addr
```

![1573699543912](assets/1573699543912.png)

## 3.4.nginx-rrd监控

## 3.5.nginx优化

### 3.5.1.增加工作线程数和并发连接数

worker_processes 2; #cpu，一般cpu有几核就配置几个

```
events {
    worker_connections  1024;#每一个进程打开的最大连接数，包含了nginx与客户端和nginx与upstream之间的连接
    multi_accept on;#可以一次建立多个连接
    use epoll;#使用epoll模型
}
```

测试：修改nginx.conf

```
worker_processes  4;

error_log  /var/log/nginx/error.log warn;
pid        /var/run/nginx.pid;


events {
    worker_connections  10240;
    multi_accept on;
    use epoll;
}
```

重启：nginx -s reload

查看进程：

```
[root@yifeng nginx]# ps -ef | grep nginx
nginx     4898 21751  0 12:09 ?        00:00:00 nginx: worker process
nginx     4899 21751  0 12:09 ?        00:00:00 nginx: worker process
nginx     4900 21751  0 12:09 ?        00:00:00 nginx: worker process
nginx     4901 21751  0 12:09 ?        00:00:00 nginx: worker process
root      4919 18955  0 12:09 pts/0    00:00:00 grep --color=auto nginx
root     21751     1  0 10:09 ?        00:00:00 nginx: master process nginx
```

可以看到启动了一个master进程，四个worker进程

### 3.5.3.启用长连接

```
upstream server_pool{
	server localhost:8080 weight=1 max_fails=2 fail_timeout=30s;
	server localhost:8081 weight=1 max_fails=2 fail_timeout=30s;
	keepalive 300; #300个长连接
}   
```

```
location / {
	#root   /usr/share/nginx/html;
	#index  index.html index.htm;
	proxy_http_version 1.1;
	proxy_set_header Upgrade $http_upgrade;
	proxy_set_header Connection "upgrade";
	proxy_pass http://server_pool/;
}

```

### 3.5.4.启用缓存压缩

```
gzip on #开启压缩
gzip http_version 1.1; #http版本
gzip_disable "MSIE[1-6]\.(?!.*SV1)", #IE1-6禁用
gzip proxied any: #所有代理服务器都开启
gzip_types text/plain text/css application/javascript application/x-javascript application/json application/xml application/vnd.ms-fontobject application/x-font-ttf application/svg+xml application/x-icon #需要开启的文件类型
gzip_vary on: #Vary: Accept-encoding
gzip_static on;#如果有压缩好的直接使用
```

### 3.5.5.操作系统优化

```
配置文件/etc/sysctl.conf
sysctl -w net.ipv4.tcp_syncookies=1#防止一个套接字在有过多试图连接到达时引起过载
sysctl -w net.core.somaxconn=1024#默认128,连接队列
sysctl -w net ipv4.tcp_fin_timeout=10非 timewaite的超时时间
sysctl-w net.ipv4.tcp_tw_reuse=1#os直接使用 timewait的连接
sysctl- w net Ipv4.tcp_tw_recycle=0#回收禁用

```

```
配置文件/etc/ security/ limits.conf
hard nofile 204800
soft nofile 204800
soft core unlimited
soft stack 204800
```

```
seattle
on;#减少文件在应用和内核之间拷贝
tcp nopush on;#当数据包达到一定大小再发送
tcp_ nodelay off#有数据随时发送
```

# 4.GC调优

## 4.1.jvm内存结构

官网地址：https://docs.oracle.com/javase/specs/jvms/se8/html/

### 4.1.1.运行时数据区

![1573707243533](assets/1573707243533.png)

程序计数器（program counter Register）：

JVM支持多线程同时执行,每一个线程都有自己的 PC Register,线程正在执行的方法叫做当前方法,如果是非native方法, PC Register里面存放的就是当前正在执行的指令的地址,如果是native方法,则为空。

Java虚拟机栈（ Java Virtual Machine Stacks）：

Java虚拟机桟( Java Virtual Machine Stacks)是线程私有的,它的生命周期与线程相同。虚拟机栈描述的是Java方法执行的内存模型:每个方法在执行的同时都会创建一个栈帧,用于存储局部变量表、操作数栈、动态链接、方法出口等信息。每一个方法从调用直至执行完成的过程,就对应着一个栈帧在虚拟机栈中入栈到出栈的过程。

堆（Heap）：

Java维( Java Heap)是Java虚拟机所管理的内存中最大的一块。堆是被所有线程共享的一块内存区域,在虚拟机启动时创建。此内存区域的唯一目的就是存放对象实例,几乎所有的对象实例都在这里分配内存。
Java堆可以处于物理上不连续的内存空间中,只要逻辑上是连续的即可。

方法区（Method Area）：

方法区与Java雄一样,是各个线程共享的内存区域,它用于存储已被虚拟机加载的类信息、常量、静态变量、即时编译器编译后的代码等数据。虽然Java虚拟机规范把方法区描述为堆的一个逻辑部分,但是它却有一个别名叫做Non-Heap(非雄),目的是与Java雄区分开来。

在jdk7及以下版本就是Permgen space，jdk8以上就叫做meta space

运行时常量池（Run-Time Constant Pool）：

运行时常量池( Run-time Constant Pool)是方法区的一部分。Class文件中除了有类的版本、字段、方法、接口等描述信息外,还有一项信息是常量池( Constant Pool Table),用于存放编译期生成的各种字面量和符号引用,这部分内容将在类加载后进入方法区的运行时常量池中存放。

本地方法栈（ Native Method Stacks）：

本地方法栈( Native Method Stack)与虚拟机栈所发挥的作用是非常相似的,它们之间的区別不过是虚拟机栈为虚拟机执行Java方法(也就是字节码)服务,而本地方法桟则为虚拟机使用到的Native方法服务。

### 4.1.2.jvm的内存结构

![1573716168185](assets/1573716168185.png)

S0和S1在同一时刻只有一个有数据。

Metaspace（元空间）非堆区：

Metaspace= Class、 Package、 Method、 Field、字节码、常量池、符号引用等等

CCS：压缩类空间，启动了32位的短指针才有效。

CodeCache：JIT即时编译后的本地代码、JNI使用的C代码

设置启动元空间参数大小：

```
-XX:MetaspaceSize=128M
-XX:MaxMetaspaceSize=128M
```

启用CCS(是否启用这个不会影响Metaspace空间大小)：

```
-XX:+UseCompressedClassPointers
```

可以使用 -Xint完全解释这种方式来启动，然后对比默认的启动，可以发现-Xint这种方式启动后，Metaspace的空间变小了，因为解释执行没有本地代码，所有不会有codeCache。

### 4.1.3.jvm常用参数

- -Xms -Xmx最小堆内存和最大堆内存
- -XX:NewSize -XX:MaxNewSize 新生代大小，最大新生代大小
- -XX:NewRatio -XX:SurvivorRatio 新生代和老年代的比例，Eden区和survivor区的比例
- -XX:MetaspaceSize -XX:MaxMetaspaceSize 元空间大小，最大元空间大小
- -XX:+UseCompressedClassPointers 启用压缩指针
- XX:CompressedClassspaceSize 设置压缩类空间大小
- XX:lnitialCodeCacheSize CodeCache大小
- -XX: ReservedCodeCacheSize CodeCache最大大小

## 4.2.垃圾回收算法

引用计数法

可达性分析法

根节点:类加载器、 Thread、虚拟机栈的本地变量表、 static成员、常量引用、本地方法栈的变量等等

标记清除法：

算法：

算法分为“标记”和“清除"两个阶段:首先标记出所有需要回收的对象,在标记完成后统一回收所有。

缺点：

效率不高。标记和清除两个过程的效率都不高。

产生碎片。碎片太多会景致提前GC

复制法：

算法：

它将可用内存按容量划分为大小相等的两块,每次只使用其中的一块。当这块的内存用完了,就将还存活着的对象复制到另外一块上面,然后再把已使用过的内存空间一次清理掉。

缺点：

实现简单,运行高效,但是空间利用率低

标记整理法：

算法：

标记过程仍然与“标记-清除"算法一样,但后续步骤不是直接对可回收对象进行清理,而是让所有存活的对象都向一端移动,然后直接清理掉端边界以外的内存

缺点：

没有了内存碎片,但是整理内存比较耗时。

分带垃圾回收：

Young区用复制算法
Old区用标记清除或就者标记整理

对象分配：

对象优先在Eden区分配
大对象直接进入老年代: -XX:PretenureSizeThreshold(超过设置的这个值就会直接进入老年代)

长期存活对象进入老年代: XX:MaxTenuringThreshold XX:+PrintTenuringDistribution -xx:TargetSurvivorRatio

## 4.3.垃圾收集器

- 串行收集器 Serial: Serial、 Serial Old
- 并行收集器 Parallel: Parallel Scavenge、 Parallel old,吞吐量
- 并发收集器 Concurrent:CMS、G1,停顿时间

串行：只有一个线程执行垃圾收集，垃圾收集工作时用户线程就必须等待，使用于一些小型的嵌入式设备

并行VS并发

并行( Parallel):指多条垃圾收集线程并行工作,但此时用户线程仍然处于等待状态。适合科学计算、后台处理等弱交互场景
并发( Concurrent):指用户线程与垃圾收集线程同时执行(但不一定是并行的,可能会交替执行),垃圾收集线程在执行的时候不会停顿用户程序的运行。适合对响应时间有要求的场景,比如Web

停顿时间VS吞吐量

停顿时间:垃圾收集器做垃圾回收中断应用执行的时间。-XX:MaxGcPauseMillis
吞吐量:花在垃圾收集的时间和花在应用时间的占比。
-XX: GcTimeRatio=<n>,垃圾收集时间占:1/1+n

串行收集器：

吞吐量优先

-XX:+UseSerialGC -XX:+UseSerialOldGC

并行收集器:

XX:+UseParallelGC  -XX:+UseParallelOldGC

Server模式下的默认收集器

并发收集器：

响应时间优先

CMS: XX:+UseConcMarkSweepGC -XX:+UseParNewGC(新生代)
G1:-XX:+UseG1GC

垃圾收集器搭配：

![1573720527094](assets/1573720527094.png)

如何选择垃圾收集器：

垃圾收集器：https://docs.oracle.com/javase/8/docs/technotes/guides/vm/gctuning/toc.html

官网地址：https://docs.oracle.com/javase/8/docs/technotes/guides/vm/gctuning/collectors.html

优先调整堆的大小让服务器自己来选择

如果内存小于100M,使用串行收集器

如果是单核,并且没有停顿时间的要求,串行或者VM自己选

如果允许停顿时间超过1秒,选择并行或者JVM自己选

如果响应时间最重要,并且不能超过1秒,使用并发收集器

并行垃圾收集器：

-XX:+UseParallelGC手动开启, Server默认开启
-XX: ParallelGCThreads=<N>多少个GC线程
CPU>8 N=5/8
CPU<8 N=CPU

自适应：

-XX:MaxGCPauseMillis=<N>
-XX:GcTimeRatio=<N>
-Xmx<N>

动态内存调整：

-XX:YoungGenerationSizelncrement=<Y> young区
-XX:TenuredGenerationSizelncrement=<T>old区
-XX:AdaptiveSizeDecrementScaleFactor=<D>吞吐量

并发垃圾收集器：

并发收集

低停顿低延迟

老年代收集器

1. CMS initial mark:初始标记Root,STW
2. CMS concurrent mark:并发标记
3. CMS- concurrent- preclean:并发预清理
4. CMS remark:重新标记,STW
5. CMS concurrent sweep并发清除
6. CMS-concurrent-reset:并发重置

缺点：

CPU敏感
浮动垃圾
空间碎片

CMS相关参数

-XX:ConcGCThreads:并发的GC线程数
-XX:+UseCMSCompactAtFullCollection: FullGC之后做压缩
-XX: CMSFullGCsBeforeCompaction:多少次 FullGC之后压缩一次

-XX: CMSInitiatingOccupancyFraction: 触发FullGC
-XX:+UseCMSInitiatingOccupancyOnly：是否动态调
-XX:+ CMSScavengeBeforeRemark: FullGC之前先做YGC

XX:+CMSClassUnloadingEnabled:启用回收Perm区

iCMS:适用于单核或者双核，jdk8已经废弃

G1 Collector

jdk开始使用

简介：

Garbage-First（G1）垃圾收集器是一种服务器样式的垃圾收集器，适用于具有大内存的多处理器计算机。它试图以高概率满足垃圾收集（GC）暂停时间目标，同时实现高吞吐量。全堆操作（例如全局标记）与应用程序线程同时执行。这样可以防止与堆或活动数据大小成比例的中断。G1的首要重点是为运行需要大堆且GC延迟有限的应用程序的用户提供解决方案。这意味着堆大小约为6 GB或更大，并且稳定且可预测的暂停时间低于0.5秒。

基本概念

![1573783676267](assets/1573783676267.png)

Region：存放数据的区块
SATB: Snapshot-At-The-Beginning,它是通过 Root Tracing得到的,GC开始时候存活对象的快照。
RSet:记录了其他 Region中的对象引用本 Region中对象的关系,属于points-into结枃(谁引用了我的对象)

YoungGC：

新对象进入Eden区

存活对象拷贝到 Survivor区

存活时间达到年龄阈值时,对象晋升Old区

MixedGC：

不是 FullGC,回收所有的 Young和部分Old

global concurrent marking

1. Initial marking phase:标记 GC Root,STW

2. Root region scanning phase:标记存活 Region
3. Concurrent marking phase:标记存活的对象

4. Remark phase:重新标记,STW
5. Cleanup phase:部分STW

MixedGC时机：

InitiatingHeapOccupancyPercent
堆占有率达到这个数值则触发 global concurrent marking,默认45%
G1HeapWastePercent
在 global concurrent marking结束之后,可以知道区有多少空间要被回收,在每次YGC之后和再次发生 Mixed GC之前,会检查垃圾占比是否达到此参数,只有达到了,下次才会发生 Mixed GC。

G1MixedGCLiveThresholdPercent
Old区的 region被回收时候的存活对象占比
G1MixedGCCountTarget
一次 global concurrent marking之后,最多执行 Mixed GC的次数

G1OldCSetRegionThresholdPercent
一次Mixed GC中能被选入CSet的最多Old区的 Region数量

-XX:+UseG1GC开启G1
-XXK:G1HeapRegionSize=n, region的大小,1-32M,2048个
-XX: MaxGCPauseMillis=200最大停顿时间

-XX: G1NewSizePercent -XX:G1MaxNewSizePercent
-XX:G1ReservePercent=10保留防止 to space溢出
-XX: ParallelGCThreads=n SWT线程数
-XX: ConcGCThreads=n 并发线程数=1/4*并行

年轻代大小:避免使用-Xmn、-XX: Newratio等显式设置Young区大小,会覆盖暂停时间目标
暂停时间目标:暂停时向不要太严苛,其吞吐量目标是90%的应用程序时间和10%的垃圾回收时间,太严苛会直接影响到吞吐量

是否需要切换到G1

50%以上的堆被存活对象占用
对象分配和晋升的速度变化非常大
垃圾回收时间特别长,超过了1秒

## 4.4.GC日志分析工具

在线工具:http://gceasy.io/
GCViewer

日志相关参数：

-XX:+PrintGCDetails -XX: +PrintGCTimeStamps
-XX:+PrintGCDateStamps
-Xloggc: $CATALINA_HOME/logs/gc. log
-XX: +PrintHeapAtGC -XX:+PrintTenuringDistribution

4.5.GC调优步骤

打印GC日志
根据日志得到关键性能指标
分析GC原因,调优JM参数

# 5.字节码

## 5.1字节码指令javap

```
用法: javap <options> <classes>
其中, 可能的选项包括:
  -help  --help  -?        输出此用法消息
  -version                 版本信息
  -v  -verbose             输出附加信息
  -l                       输出行号和本地变量表
  -public                  仅显示公共类和成员
  -protected               显示受保护的/公共类和成员
  -package                 显示程序包/受保护的/公共类
                           和成员 (默认)
  -p  -private             显示所有类和成员
  -c                       对代码进行反汇编
  -s                       输出内部类型签名
  -sysinfo                 显示正在处理的类的
                           系统信息 (路径, 大小, 日期, MD5 散列)
  -constants               显示最终常量
  -classpath <path>        指定查找用户类文件的位置
  -cp <path>               指定查找用户类文件的位置
  -bootclasspath <path>    覆盖引导类文件的位置
```

新建一个java测试类：

```java
public class Test1 {
    public static void main(String[] args) {
        int a = 2;
        int b = 3;
        int c = a + b;
        System.out.println(c);
    }
}
```

找到这个java对应的class文件的目录，然后执行命令

```
javap -verbose Test1.class > test1.txt
```

打开test1.txt，内容如下：

```
Classfile /D:/mygithub/jvm/jvm-monitor/target/classes/com/yifeng/jvm/bytecode/Test1.class
  Last modified 2019-11-15; size 627 bytes
  MD5 checksum 2e2e05b6d30ce44388d0553429a60be7
  Compiled from "Test1.java"
public class com.yifeng.jvm.bytecode.Test1
  minor version: 0
  major version: 52
  flags: ACC_PUBLIC, ACC_SUPER
Constant pool:
   #1 = Methodref          #5.#24         // java/lang/Object."<init>":()V
   #2 = Fieldref           #25.#26        // java/lang/System.out:Ljava/io/PrintStream;
   #3 = Methodref          #27.#28        // java/io/PrintStream.println:(I)V
   #4 = Class              #29            // com/yifeng/jvm/bytecode/Test1
   #5 = Class              #30            // java/lang/Object
   #6 = Utf8               <init>
   #7 = Utf8               ()V
   #8 = Utf8               Code
   #9 = Utf8               LineNumberTable
  #10 = Utf8               LocalVariableTable
  #11 = Utf8               this
  #12 = Utf8               Lcom/yifeng/jvm/bytecode/Test1;
  #13 = Utf8               main
  #14 = Utf8               ([Ljava/lang/String;)V
  #15 = Utf8               args
  #16 = Utf8               [Ljava/lang/String;
  #17 = Utf8               a
  #18 = Utf8               I
  #19 = Utf8               b
  #20 = Utf8               c
  #21 = Utf8               MethodParameters
  #22 = Utf8               SourceFile
  #23 = Utf8               Test1.java
  #24 = NameAndType        #6:#7          // "<init>":()V
  #25 = Class              #31            // java/lang/System
  #26 = NameAndType        #32:#33        // out:Ljava/io/PrintStream;
  #27 = Class              #34            // java/io/PrintStream
  #28 = NameAndType        #35:#36        // println:(I)V
  #29 = Utf8               com/yifeng/jvm/bytecode/Test1
  #30 = Utf8               java/lang/Object
  #31 = Utf8               java/lang/System
  #32 = Utf8               out
  #33 = Utf8               Ljava/io/PrintStream;
  #34 = Utf8               java/io/PrintStream
  #35 = Utf8               println
  #36 = Utf8               (I)V
{
  public com.yifeng.jvm.bytecode.Test1();
    descriptor: ()V
    flags: ACC_PUBLIC
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0
         1: invokespecial #1                  // Method java/lang/Object."<init>":()V
         4: return
      LineNumberTable:
        line 9: 0
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0       5     0  this   Lcom/yifeng/jvm/bytecode/Test1;

  public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V
    flags: ACC_PUBLIC, ACC_STATIC
    Code:
      stack=2, locals=4, args_size=1
         0: iconst_2
         1: istore_1
         2: iconst_3
         3: istore_2
         4: iload_1
         5: iload_2
         6: iadd
         7: istore_3
         8: getstatic     #2                  // Field java/lang/System.out:Ljava/io/PrintStream;
        11: iload_3
        12: invokevirtual #3                  // Method java/io/PrintStream.println:(I)V
        15: return
      LineNumberTable:
        line 11: 0
        line 12: 2
        line 13: 4
        line 14: 8
        line 15: 15
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      16     0  args   [Ljava/lang/String;
            2      14     1     a   I
            4      12     2     b   I
            8       8     3     c   I
    MethodParameters:
      Name                           Flags
      args
}
SourceFile: "Test1.java"

```

字节码解析：

LocalVariableTable本地变量表:

​		Start  Length  Slot  Name   Signature
​            0      16     	0  	args   [Ljava/lang/String;
​            2      14     	1     	a   	I
​            4      12     	2     	b   	I
​            8       8     	3     	 c   	I

0   args   string

1	a	   int

2	b	   int

3	c	   int

```
0: iconst_2 #把常量2压到操作数栈中
1: istore_1 #把操作数栈的栈顶元素取出存储到本地变量1中，也就是a中
2: iconst_3 #把常量3压到操作数栈中
3: istore_2 #把操作数栈的栈顶元素取出存储到本地变量2中，也就是b中
4: iload_1  #本地变量1压栈
5: iload_2  #本地变量2压栈
6: iadd		#把栈中的元素进行相加结果存储到栈顶元素中
7: istore_3 #取出栈顶元素存储到本地变量3中
8: getstatic     #2 Fieldref字段引用                  // Field java/lang/System.out:Ljava/io/PrintStream;
11: iload_3 #本地变量3压栈
12: invokevirtual #3 Methodref方法引用                 // Method java/io/PrintStream.println:(I)V
15: return
```

i++和++i：

![1573803715285](assets/1573803715285.png)

字符串拼接：

```java
String s = "";
for (int i=0; i<10; i++){	
	s += i;
}
```

底层是new StringBuilder(s); 然后再append(i)，然后StringBuilder.tostring()的值赋给s



String constant Variable

类、方法、变量尽量指定final修饰。

```java
public static void f1(){
   	final String x = "hello";
    final String y = x + "world";
    String z= x + y;
    System.out.printin(z); 
}
```

```java
public static void f1(){
   	final String x = "hello";
    String y = x + "world";
    String z= x + y;
    System.out.printin(z); 
}
```

常用代码优化方法：

尽量重用对象,不要循环创建对象,比如:for循环字符串拼接

容器类初始化的时候指定长度

```java
List<String> collection = new ArrayList<String>(5);
Map<String, String> map = new Hashmap<String, String>(32);
```

ArrayList随机遍历快, LinkedList添加删除快

集合遍历尽量减少重复计算

使用 Entry遍历Map

```java
for(Map.Entry<String, String> entry: map.entryset()){
    String key =entry.getKey();
    String value= entry.getValue();
}
```

大数组复制用 System. arraycopy()，底层使用的是native方法

尽量使用基本类型而不是包装类型

不要手动调用 System.gc()

及时消除过期对象的引用,防止内存泄露

尽量使用局部变量,减小变量的作用域

尽量使用非同步的容器 **ArrayList** VS Vector

尽量减小同步作用范围, synchronized方法VS**代码块**

ThreadLocal缓存线程不安全的对象, SimpleDateFormat

尽量使用延迟加载

尽量减少使用反射,加缓存

尽量使用连接池、线程池、对象池、绶存

及时释放资源I/O流、 Socket、数据库连接

慎用异常,不要用抛异常来表示正常的业务逻辑

String操作尽量少用正则表达式  **replace**  VS replaceAll

日志输出注意使用不同的级别

日志中参数拼接使用占位符

log. info("orderId:"+ orderId);不推荐
log. info("ordered:{}", ordered);推荐