package com.liuchao.mylog.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Arrays;
import java.util.Properties;

/**
 * @Classname KafkaUtil
 * @Date 2021/11/13 下午5:33
 * @Created by liuchao58
 * @Description TODO
 */
@Slf4j
public class KafkaUtil {

    public static Producer<String, String> createProducer(
            String bootstrapServers, String batchSize, String lingerMs,
            String compressionType, String retries, String maxRequestSize) {
        // 当配置项为IS_UNDEFINED时,使用默认值
        if (bootstrapServers == null) {
            bootstrapServers = "127.0.0.1:9092";
        }
        if (batchSize.contains("IS_UNDEFINED")) {
            batchSize = "50000";
        }
        if (lingerMs.contains("IS_UNDEFINED")) {
            lingerMs = "60000";
        }

        if (retries.contains("IS_UNDEFINED")) {
            retries = "3";
        }
        if (maxRequestSize.contains("IS_UNDEFINED")) {
            maxRequestSize = "5242880";
        }
        Properties properties = new Properties();
        // kafka地址,集群用逗号分隔开
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        // acks取值:
        // 0: kafka不返回确认信息,不保证record是否被收到,因为没有返回所以重试机制不会生效
        // 1: partition leader确认record写入到日志中,但不保证信息是否被正确复制
        // all: leader会等待所有信息被同步后返回确认信息
        properties.put(ProducerConfig.ACKS_CONFIG, "1");
        properties.put(ProducerConfig.RETRIES_CONFIG, Integer.valueOf(retries));
        // 批量发送,当达到batch size最大值触发发送机制(10.0后支持批量发送)
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, Integer.valueOf(batchSize));
        // 该配置是指在batch.size数量未达到时,指定时间内也会推送数据
        properties.put(ProducerConfig.LINGER_MS_CONFIG, Integer.valueOf(lingerMs));
        // 配置缓存
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        if (!compressionType.contains("IS_UNDEFINED")) {
            // 指定压缩算法
            properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, compressionType);
        }
        // 每个请求的消息大小
        properties.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, Integer.valueOf(maxRequestSize));
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        return new KafkaProducer<String, String>(properties);
    }

    public static void main(String[] args) {
        Producer producer = KafkaUtil.createProducer(
                "127.0.0.1:9092", "5", "1000",
                "gzip", "3", "5242880");
        ProducerRecord<String, String> record =
                new ProducerRecord<String, String>("springbootLogInfo", "test-log-key", "test-log-value");
        producer.send(record);
        //   consumerReceived();
    }

    public static void consumerReceived() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "127.0.0.1:9092");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("springbootLogInfo"));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(1000);
            for (ConsumerRecord<String, String> record : records) {
             //   log.info("kafka consumer received --offset:{}, key :{}, value :{}", record.offset(), record.key(), record.value());
                System.out.println("kafka consumer received:" + record.value());
            }
        }
    }
}
