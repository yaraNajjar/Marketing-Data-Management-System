package com.tsfn.loaders.aspect;


import com.tsfn.loaders.feign.FeignSecurity;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

@Component
@Aspect
public class TokenExtractionAspect {

    @Autowired
    private FeignSecurity feignSecurity;

    @Before("execution(* com.tsfn.loaders.controller.MarketingDataController.triggerManual())")
    public void validateTokenAspect() throws Exception {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String method = request.getMethod();
        String url = request.getRequestURI();
        String fullPath = url + "_" + method;
        if (fullPath.equals("/loader/trigger_GET")) {
            if (!feignSecurity.checkUserRole(authorizationHeader, "ROLE_TRIGGER")) {
                throw new Exception("403 : don't have permission to access.");
            } else {
                System.out.println("Valid");
            }
        } else {
            throw new Exception("403 : don't have permission to access.");

        }


    }
}
