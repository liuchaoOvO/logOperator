package com.liuchao.mylog.annotation;

import java.lang.annotation.*;

/**
 * @Classname OpLog
 * @Date 2021/12/28 下午3:29
 * @Created by liuchao58
 * @Description TODO
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OpLog {

    /**
     * 系统
     */
    String system() default "";

    /**
     * 模块
     */
    String module() default "";

    /**
     * 菜单
     */
    String menu() default "";

    /**
     * 功能
     */
    String function() default "";

    /**
     * 日志内容
     */
    String content() default "";

}