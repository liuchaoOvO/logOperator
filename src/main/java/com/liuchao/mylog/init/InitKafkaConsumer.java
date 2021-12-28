package com.liuchao.mylog.init;

import com.liuchao.mylog.utils.KafkaUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Classname InitKafkaConsumer
 * @Date 2021/12/26 上午10:49
 * @Created by liuchao58
 * @Description TODO
 */
@Slf4j
@Component
public class InitKafkaConsumer {

    @PostConstruct
    public void start() {
        log.info("InitKafkaConsumer start...");
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(1000);
                    KafkaUtil.consumerReceived();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        ).start();
        log.info("InitKafkaConsumer start success");
    }
}
