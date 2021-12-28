package com.liuchao.mylog;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class MyLogApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyLogApplication.class, args);
        log.info("----log info level :test hello world send to console----");
        log.debug("----log debug level :test hello world send to console----");
    }

}
