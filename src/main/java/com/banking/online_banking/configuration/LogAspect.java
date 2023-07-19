package com.banking.online_banking.configuration;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
//import org.slf4j.LoggerFactory;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Order(0)
@Aspect
@Configuration
public class LogAspect {


    @Around(value = "com.banking.online_banking.configuration.Interceptor.globalPointCut()")
    public Object calculateMethodTimeAdvice(ProceedingJoinPoint joinPoint) throws Throwable {

        final Logger classLogger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        if(!classLogger.isDebugEnabled()){
            return joinPoint.proceed();
        }

        String className = joinPoint.getTarget().getClass().getName();
        String methodName = ((MethodSignature)joinPoint.getSignature()).getMethod().getName();
        String methodArgs = Stream.of(joinPoint.getArgs()).collect(Collectors.toList()).toString();
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime-startTime;

        LogMessage logMessage = LogMessage.
                builder()
                .className(className)
                        .methodName(methodName)
                                .methodArgs(methodArgs)
                                        .elapsedTimeInMillis(elapsedTime)
                                            .result(result)
                .build();

        classLogger.debug("LogAspect : {}", logMessage);


        return result;

    }
}
