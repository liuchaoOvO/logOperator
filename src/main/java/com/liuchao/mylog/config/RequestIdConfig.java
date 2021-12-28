package com.liuchao.mylog.config;

import org.slf4j.MDC;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import java.util.UUID;

/**
 * @Classname config
 * @Date 2021/12/28 下午3:23
 * @Created by liuchao58
 * @Description
 * 也可以通过如下方式实现
 * 1.RequestInterceptor extends HandlerInterceptorAdapter
 * 2.配置RequestInterceptor
 * @Configuration
 * public class InterceptorConfig implements WebMvcConfigurer {
 *     @Override
 *     public void addInterceptors(InterceptorRegistry registry) {
 *         registry.addInterceptor(new RequestInterceptor());
 *     }
 * }
 *
 * <dependency>
 *     <groupId>com.alibaba</groupId>
 *     <artifactId>transmittable-thread-local</artifactId>
 *     <version>2.5.0</version>
 * </dependency>
 * 通过利用TransmittableThreadLocal 保证线程池内MDC request_id传递一致
 */
@Configuration
public class RequestIdConfig {

    @Bean
    public ServletRequestListener requestListener() {
        return new ServletRequestListener() {
            @Override
            public void requestInitialized(ServletRequestEvent sre) {
                String request_id = UUID.randomUUID().toString().replace("-", "");
                System.out.println("request_id:" + request_id);
                MDC.put("request_id", request_id);
            }
            @Override
            public void requestDestroyed(ServletRequestEvent sre) {
                MDC.put("request_id", null);
            }
        };
    }

    @Bean
    public ServletListenerRegistrationBean<ServletRequestListener> servletListenerRegistrationBean(ServletRequestListener servletRequestListener) {
        ServletListenerRegistrationBean<ServletRequestListener> servletListenerRegistrationBean = new ServletListenerRegistrationBean<>();
        servletListenerRegistrationBean.setListener(servletRequestListener);
        return servletListenerRegistrationBean;
    }
}
