package com.liuchao.mylog.utils;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import lombok.Data;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @Classname KafkaAppender
 * @Date 2021/11/13 下午5:32
 * @Created by liuchao58
 * @Description TODO
 */
@Data
public class KafkaAppender extends ConsoleAppender<ILoggingEvent> {

    public static final Logger LOGGER = LoggerFactory.getLogger(KafkaAppender.class);

    private String bootstrapServers;
    private String topic;
    private String batchSize;
    private String lingerMs;
    private String compressionType;
    private String retries;
    private String maxRequestSize;
    private String isSend;

    private Producer<String, String> producer;

    @Override
    public void start() {
        super.start();
        if ("true".equals(this.isSend)) {
            if (producer == null) {
                producer = KafkaUtil.createProducer(this.bootstrapServers, this.batchSize, this.lingerMs, this.compressionType, this.retries, this.maxRequestSize);
            }
        }

    }

    @Override
    public void stop() {
        super.stop();
        if ("true".equals(this.isSend)) {
            this.producer.close();
        }

        LOGGER.info("Stopping kafkaAppender...");
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        byte[] byteArray;
        String log;
        // 对日志格式进行解码
        byteArray = this.encoder.encode(eventObject);
        log = new String(byteArray);
        ProducerRecord<String, String> record = new ProducerRecord(this.topic, log);

        if (eventObject.getMarker() == null && "true".equals(this.isSend)) {
            //如果需要进行分析日志，可以对record进行数据结构重构下
            // producer.send(record);
            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if (exception != null) {
                        LOGGER.error("Send log to kafka failed: {}", log);
                    }
                }
            });
        }
    }


    /*public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(String batchSize) {
        this.batchSize = batchSize;
    }

    public String getLingerMs() {
        return lingerMs;
    }

    public void setLingerMs(String lingerMs) {
        this.lingerMs = lingerMs;
    }

    public String getCompressionType() {
        return compressionType;
    }

    public void setCompressionType(String compressionType) {
        this.compressionType = compressionType;
    }

    public String getRetries() {
        return retries;
    }

    public void setRetries(String retries) {
        this.retries = retries;
    }

    public String getMaxRequestSize() {
        return maxRequestSize;
    }

    public void setMaxRequestSize(String maxRequestSize) {
        this.maxRequestSize = maxRequestSize;
    }

    public Producer<String, String> getProducer() {
        return producer;
    }

    public void setProducer(Producer<String, String> producer) {
        this.producer = producer;
    }

    public String getIsSend() {
        return isSend;
    }

    public void setIsSend(String isSend) {
        this.isSend = isSend;
    }*/


}
