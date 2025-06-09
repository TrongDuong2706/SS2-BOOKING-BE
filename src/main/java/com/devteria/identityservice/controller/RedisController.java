package com.devteria.identityservice.controller;

import com.devteria.identityservice.service.BaseRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redis")
@RequiredArgsConstructor
public class RedisController {

    private final BaseRedisService redisService;
    @PostMapping
    public void set(){
        redisService.set("Linhi", "Trong");
    }
}
