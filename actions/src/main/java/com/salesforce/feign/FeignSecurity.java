package com.salesforce.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;


@FeignClient(value = "FeignSecurity", url = "${services.security.url:http://localhost:8080}")
public interface FeignSecurity {


    @GetMapping("/{endPoint}") //security
    boolean checkUserRole(@RequestHeader("Authorization") String token,
                          @PathVariable("endPoint") String endPoint) ;


}



