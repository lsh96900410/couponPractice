package com.example.couponcore;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ActiveProfiles("test")
@TestPropertySource(properties = "spring.config.name=application-core") // application-core.yml 파일 속성 사용
@SpringBootTest(classes = CouponCoreConfiguration.class) // CouponCoreConfiguration.class 해당 클래스 사용하여 Test 진행
public class TestConfig {
}
