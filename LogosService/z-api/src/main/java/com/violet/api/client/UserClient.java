package com.violet.api.client;

import com.violet.common.domain.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@FeignClient("user-service")
public interface UserClient {
    @GetMapping("/api/users")
    Result getUserById(@RequestParam("id") Long id);

    @GetMapping("/api/users/list")
    Result getUsersByIds(@RequestParam("ids") Set<Long> ids);
}
