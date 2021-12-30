# git 
git clone https://github.com/liuchaoOvO/logOperator.git \
git remote set-url origin https://github.com/liuchaoOvO/logOperator.git \
git push -u origin master

# logOperator
步骤\
1.下载安装Kafka 参考blog：https://blog.csdn.net/CoderTnT/article/details/121145500 \
2.修改zookeeper.properties、server.properties配置信息\
3.再先部署执行zookeeper\
./zookeeper-server-start.sh  ../config/zookeeper.properties\
4.再后部署执行kafka\
./kafka‐server‐start.sh ‐daemon ../config/server.properties\
5.先删除topic springbootLogInfo 防止误影响 一般可忽略该步骤。多次重复测试验证，最好执行\
/kafka-topics.sh --delete --topic springbootLogInfo   --bootstrap-server 127.0.0.1:9092\
6.启动一个测试消费者 用于消费topic：springbootLogInfo的消息。即消费该logOperator服务采集的日志\
./kafka-console-consumer.sh  --bootstrap-server 127.0.0.1:9092 --topic  springbootLogInfo
7.查看kafka 磁盘持久化文件使用情况\
cd /tmp/kafka-logs\
du -H -h\
