package com.liuchao.mylog.aop;

import com.liuchao.mylog.annotation.OpLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @Classname OpLogAspect
 * @Date 2021/12/28 下午3:33
 * @Created by liuchao58
 * @Description TODO
 */
@Slf4j
@Aspect
@Component
public class OpLogAspect {
    private static final String PointcutURL = "@annotation(com.liuchao.mylog.annotation.OpLog)";
    private static final String STR_SUCCESS = "[执行成功] ";
    private static final String STR_ERROR = "[执行失败] ";
    private static final String STR_ERRORMSG = " [错误信息] ";

    /**
     * 定义切点，作用在使用了{@link OpLog}注解的地方
     */
    @Pointcut("@annotation(com.liuchao.mylog.annotation.OpLog)")
    public void logPointCut() {


    }

    @Before("logPointCut()")
    public void beforeMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("【前置通知】the method 【" + methodName + "】 begins with " + Arrays.asList(joinPoint.getArgs()));
        try {
            String targetName = joinPoint.getTarget().getClass().getName();
            Class<?> targetClass = Class.forName(targetName);
            Method[] methods = targetClass.getMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName) && method.getAnnotation(OpLog.class) != null) {
                    log.info("==beforeMethod   begin===");
                    log.info("System:==" + method.getAnnotation(OpLog.class).system());
                    log.info("Module:==" + method.getAnnotation(OpLog.class).module());
                    log.info("Menu:==" + method.getAnnotation(OpLog.class).menu());
                    log.info("Function:==" + method.getAnnotation(OpLog.class).function());
                    log.info("Content:==" + method.getAnnotation(OpLog.class).content());
                    log.info("==beforeMethod   end===");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterReturning(returning = "result", pointcut = PointcutURL)
    public void afterReturningOperationLog(JoinPoint joinPoint, Object result) throws ClassNotFoundException {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("【返回通知】the method 【" + methodName + "】 ends with 【" + result + "】");

        //todo 逻辑部分 比如保存日志信息入库、发送到redis/存储到ES 等

    }
}
