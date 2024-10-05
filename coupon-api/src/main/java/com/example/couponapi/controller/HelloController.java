package com.example.couponapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("hello")
    public String hello() throws InterruptedException{
        Thread.sleep(500); // 요청 당 0.5초를 쉼 -> 초당 2건 처리
        /*
            초당 2건을 처리 x N (서버에서 동시에 처리할 수 있는 수) = tomcat Thread POOL MAX 수 : 200
            서버 API 모니터링 진행하면서 Tomcat Thread Pool 수량 증감 결정

            -> Thread Pool 수 결정 조건 공부하기
         */
        return "hello !! testing OK !! ";
    }
}
