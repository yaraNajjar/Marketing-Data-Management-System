package com.jwt.implementation.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "FeignAction", url = "${services.actions.url:http://localhost:9097/controller_action}")
public interface FeignAction {

	@GetMapping("/feign/security/{accountId}")
	boolean deleteUserAndActions(@PathVariable("accountId") Long accountId);

}
