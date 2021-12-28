package com.liuchao.mylog.controller;

import com.liuchao.mylog.annotation.OpLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname LogController
 * @Date 2021/12/25 下午4:06
 * @Created by liuchao58
 * @Description TODO
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/log")
public class LogController {

    @PostMapping("")
    public void health() {
        String logInfoStr = "/api/v1/log--" + "test send this log to kafka server...." + System.currentTimeMillis();
        log.debug(logInfoStr);
        log.info(logInfoStr);
        log.error(logInfoStr);
        System.out.println(logInfoStr);
    }

    @OpLog(system = "系统名称", module = "模块名称", menu = "菜单名称", function = "功能模块", content = "'日志记录内容：' + #arg0.getRequestURL()")
    @GetMapping(value = "/opLogAop")
    public void opLogAop(String paramStr) {
        log.info("===opLogAop====paramStr:{}", paramStr);
    }
}
