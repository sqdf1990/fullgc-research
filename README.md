通过maven打包
```
mvn clean package
```
运行jar包命令
```
java  -jar -Xms1528m -Xmx1528m -Xmn848m -XX:MetaspaceSize=192m -XX:MaxMetaspaceSize=256m -XX:+UseConcMarkSweepGC -XX:MaxTenuringThreshold=8 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=本地路径/error.dump -XX:+ExplicitGCInvokesConcurrentAndUnloadsClasses -XX:CMSInitiatingOccupancyFraction=90 -XX:+PrintGCDateStamps -XX:+UseCMSInitiatingOccupancyOnly -XX:+UnlockDiagnosticVMOptions -XX:+UnsyncloadClass -XX:+UseParNewGC -XX:+PrintGCDetails -Xloggc:本地路径/gc_detail.log -XX:ActiveProcessorCount=4 当前工程路径/fullgc-reasech/target/fullgc-research-1.0.jar
```

上面启动命令给堆内存分配了1528MB，新生代848MB，剩下老年代700MB左右。采用CMS垃圾回收器。

CMSInitiatingOccupancyFraction=90表示老年代占用超过90%的时候执行fullgc
MaxTenuringThreshold=8表示对象经过8次younggc的时候会被放入老年代，如果是大对象，没到次数也会被放入老年代
gc_detail.log文件中搜索FullGC相关的日志：
[GC (CMS Initial Mark)表示这是CMS开始对老年代进行垃圾圾收集的初始标记阶段，该阶段从垃圾回收的“根对象”开始，且只扫描直接与“根对象”直接关联的对象，并做标记，需要暂停用户线程（Stop The Word，下面统称为STW），速度很快。
理解gc log相关内容参考：https://www.jianshu.com/p/ba768d8e9fec

