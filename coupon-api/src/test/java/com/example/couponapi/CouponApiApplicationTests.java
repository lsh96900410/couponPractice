package com.example.couponapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

/* value 값 설정 변수 할당 로직 관련 추가*/
@ActiveProfiles("test")
@TestPropertySource(properties = "spring.config.name=application-core")
@SpringBootTest
class CouponApiApplicationTests {

    @Test
    void contextLoads() {
    }

}
