package com.salesforce.aspect;


import com.salesforce.feign.FeignSecurity;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
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

    @Before("execution(* com.salesforce.controller.*.*(..))")
    public void validateTokenAspect() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String authorizationHeader =  request.getHeader(HttpHeaders.AUTHORIZATION);
        String method = request.getMethod();
        String url = request.getRequestURI();
        String fullPath = url + "_" + method;
        if (fullPath.equals("/actions/active/all_GET") ||
                fullPath.equals("/actions/history/all_GET")) {
            if (!feignSecurity.checkUserRole(authorizationHeader, "ROLE_ADMIN")) {
                throw new SecurityException("Unauthorized access 403");
            } else {
                System.out.println("Valid");
            }
        }
        String endPoint = getMethodType(method);
        if (!feignSecurity.checkUserRole(authorizationHeader, endPoint)) {
            throw new SecurityException("Unauthorized access 403");
        } else {
            System.out.println("Valid");
        }
    }

    private String getMethodType(String method) {
        return switch (method) {
            case "GET" -> "ROLE_USER";
            case "POST" -> "ROLE_CREATE_ACTION";
            case "PUT" -> "ROLE_UPDATE_ACTION";
            case "DELETE" -> "ROLE_DELETE_ACTION";
            default -> throw new IllegalArgumentException("Invalid endpoint: ");
        };
    }

    private String getEndPoint1(String fullPath) {
        return switch (fullPath) {
            case "/actions/active/all_GET",
                    "/actions/active/{accountId}_GET",
                    "/actions/history/all_GET",
                    "/actions/history/{accountId}_GET" -> "ROLE_USER";
            case "/actions/create_POST" -> "ROLE_CREATE_ACTION";
            case "/actions/{id}_PUT",
                    "/actions/disable/{id}_PUT",
                    "/actions/enable/{id}_PUT",
                    "/actions/restore/{id}_PUT" -> "ROLE_UPDATE_ACTION";
            case "/actions/{id}_DELETE" -> "ROLE_DELETE_ACTION";
            default -> throw new IllegalArgumentException("Invalid endpoint: " + fullPath);
        };
    }
}
