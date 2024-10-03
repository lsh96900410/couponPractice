package com.example.couponapi;

import com.example.couponcore.CouponCoreConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(CouponCoreConfiguration.class)
@SpringBootApplication
public class CouponApiApplication {

    public static void main(String[] args) {

        //해당 모듈 yml + Core Import yml 파일 사용 설정
        System.setProperty("spring.config.name","application-core,application-api");

        SpringApplication.run(CouponApiApplication.class, args);
    }

}
