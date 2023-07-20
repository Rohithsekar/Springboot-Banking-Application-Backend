package com.banking.online_banking.configuration;

import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Interceptor {

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerPointCut(){

    }

    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void servicePointCut(){

    }

    @Pointcut("within(com.banking.online_banking.exception.ExceptionHandler)")
    public void exceptionPointCut(){

    }

    @Pointcut("this(com.banking.online_banking.repository.CustomerRepository) || " +
            "this(com.banking.online_banking.repository.RoleRepository)")
    public void repositoryPointCut(){

    }

    @Pointcut("execution(* com.banking.online_banking..*(..))")
    public void applicationPointCut(){

    }


    @Pointcut("applicationPointCut() && (controllerPointCut() || servicePointCut() || repositoryPointCut()" +
            "|| exceptionPointCut())")
    public void globalPointCut(){

    }





}
