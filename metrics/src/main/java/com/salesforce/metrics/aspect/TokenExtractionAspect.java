package com.salesforce.metrics.aspect;

import com.salesforce.metrics.feign.FeignAction;
import com.salesforce.metrics.feign.FeignSecurity;
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

    @Autowired
    private FeignAction feignAction;

    @Before("execution(* com.salesforce.metrics.controller.*.*(..))")
    public void validateTokenAspect() throws Exception {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String authorizationHeader =  request.getHeader(HttpHeaders.AUTHORIZATION);
        String method = request.getMethod();
        String url = request.getRequestURI();
        String fullPath = url + "_" + method;
        if (fullPath.equals("/metrics/active/all_GET") ||
                fullPath.equals("/metrics/history/all_GET")) {
            if (!feignSecurity.checkUserRole(authorizationHeader, "ROLE_ADMIN")) {
                throw new SecurityException("Unauthorized access 403");
            }
        }
        String endPoint = getMethodType(method);
        System.out.println(endPoint);
        if (!feignSecurity.checkUserRole(authorizationHeader, endPoint)) {
            throw new Exception("403 : don't have permission to access.");
        }
    }

    private String getMethodType(String method) {
        return switch (method) {
            case "GET" -> "ROLE_USER";
            case "POST" -> "ROLE_CREATE_METRIC";
            case "PUT" -> "ROLE_UPDATE_METRIC";
            case "DELETE" -> "ROLE_DELETE_METRIC";
            default -> throw new IllegalArgumentException("Invalid endpoint: ");
        };
    }

    private String getEndPoint(String fullPath) {
        return switch (fullPath) {
            case "/active/all_GET",
                    "/metrics/active/{accountId}_GET",
                    "/metrics/history/{accountId}_GET",
                    "/metrics/history/all_GET" -> "ROLE_USER";
            case "/metrics/create_POST" -> "ROLE_CREATE_METRIC";
            case "/metrics/{id}_PUT", "/metrics/revive/{id}_PUT" -> "ROLE_UPDATE_METRIC";
            case "/metrics/{id}_DELETE" -> "ROLE_DELETE_METRIC";
            default -> throw new IllegalArgumentException("Invalid endpoint: " + fullPath);
        };
    }
}
