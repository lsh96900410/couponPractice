package com.example.couponcore.service;

import com.example.couponcore.TestConfig;
import com.example.couponcore.exception.CouponIssueException;
import com.example.couponcore.exception.ErrorCode;
import com.example.couponcore.model.Coupon;
import com.example.couponcore.model.CouponIssue;
import com.example.couponcore.model.CouponType;
import com.example.couponcore.repository.mysql.CouponIssueJPARepository;
import com.example.couponcore.repository.mysql.CouponIssueRepository;
import com.example.couponcore.repository.mysql.CouponJPARepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static com.example.couponcore.exception.ErrorCode.*;


class CouponIssueServiceTest extends TestConfig {

    @Autowired
    CouponIssueService sut;

    @Autowired
    CouponIssueJPARepository couponIssueJPARepository;

    @Autowired
    CouponIssueRepository couponIssueRepository;

    @Autowired
    CouponJPARepository couponJPARepository;


    @BeforeEach
    void clean(){
        couponJPARepository.deleteAllInBatch();
        couponIssueJPARepository.deleteAllInBatch();
    }



    @Test
    @DisplayName("쿠폰 발급 내역이 존재하면 예외를 반환")
    void saveCouponIssue_1(){
        // given
        CouponIssue couponIssue = CouponIssue.builder()
                .couponId(1L)
                .userId(1L)
                .build();

        couponIssueJPARepository.save(couponIssue);

        // when & then
        CouponIssueException exception = Assertions.assertThrows(CouponIssueException.class, () ->{
            sut.saveCouponIssue(couponIssue.getCouponId(),couponIssue.getUserId());
        });

        Assertions.assertEquals(exception.getErrorCode(), ErrorCode.DUPLICATED_COUPON_ISSUE);


    }



    @Test
    @DisplayName("쿠폰 발급 내역이 존재하지 않는다면 쿠폰 발급")
    void saveCouponIssue_2(){
        // given
        long couponId = 1L;
        long userId = 1L;

        // when
        CouponIssue result = sut.saveCouponIssue(couponId, userId);

        // then
        Assertions.assertTrue(couponIssueJPARepository.findById(result.getId()).isPresent());

    }



    @Test
    @DisplayName("발급 수량, 기한, 중복 발급 문제가 없다면 쿠폰 발급")
    void issue_1(){
        // given
        long userId = 1;
        Coupon coupon = Coupon.builder()
                .couponType(CouponType.FIRST_COME_FIRST_SERVED)
                .title("선착순 테스트 쿠폰")
                .totalQuantity(100)
                .issuedQuantity(0)
                .dateIssueStart(LocalDateTime.now().minusDays(1))
                .dateIssueEnd(LocalDateTime.now().plusDays(1))
                .build();

        couponJPARepository.save(coupon);

        // when
        sut.issue(coupon.getId(), userId);

        // then
        Coupon couponResult = couponJPARepository.findById(coupon.getId()).get();

        Assertions.assertEquals(couponResult.getIssuedQuantity(),1);

        CouponIssue couponIssueResult = couponIssueRepository.findFirstCoupon(coupon.getId(),userId);

        Assertions.assertNotNull(couponIssueResult);
        
    }



    @Test
    @DisplayName("발급 수량에 문제가 있다면 예외를 반환")
    void issue_2(){
        // given
        long userId = 1;
        Coupon coupon = Coupon.builder()
                .couponType(CouponType.FIRST_COME_FIRST_SERVED)
                .title("선착순 테스트 쿠폰")
                .totalQuantity(100)
                .issuedQuantity(100)
                .dateIssueStart(LocalDateTime.now().minusDays(1))
                .dateIssueEnd(LocalDateTime.now().plusDays(1))
                .build();

        couponJPARepository.save(coupon);

        // when & then
        CouponIssueException exception = Assertions.assertThrows(CouponIssueException.class, () -> {
            sut.issue(coupon.getId(), userId);
        });
        
        Assertions.assertEquals(exception.getErrorCode(), INVALID_COUPON_ISSUE_QUANTITY);

    }



    @Test
    @DisplayName("발급 기한에 문제가 있다면 예외를 반환")
    void issue_3(){
        // given
        long userId = 1;
        Coupon coupon = Coupon.builder()
                .couponType(CouponType.FIRST_COME_FIRST_SERVED)
                .title("선착순 테스트 쿠폰")
                .totalQuantity(100)
                .issuedQuantity(0)
                .dateIssueStart(LocalDateTime.now().minusDays(2))
                .dateIssueEnd(LocalDateTime.now().minusDays(1))
                .build();

        couponJPARepository.save(coupon);

        // when & then
        CouponIssueException exception = Assertions.assertThrows(CouponIssueException.class, () -> {
            sut.issue(coupon.getId(), userId);
        });

        Assertions.assertEquals(exception.getErrorCode(), INVALID_COUPON_ISSUE_DATE);

    }



    @Test
    @DisplayName("중복 발급 검증에 문제가 있다면 예외를 반환")
    void issue_4(){
        // given
        long userId = 1;
        Coupon coupon = Coupon.builder()
                .couponType(CouponType.FIRST_COME_FIRST_SERVED)
                .title("선착순 테스트 쿠폰")
                .totalQuantity(100)
                .issuedQuantity(0)
                .dateIssueStart(LocalDateTime.now().minusDays(1))
                .dateIssueEnd(LocalDateTime.now().plusDays(1))
                .build();

        couponJPARepository.save(coupon);

        CouponIssue couponIssue = CouponIssue.builder()
                .couponId(coupon.getId())
                .userId(userId)
                .build();

        couponIssueJPARepository.save(couponIssue);


        // when & then
        CouponIssueException exception = Assertions.assertThrows(CouponIssueException.class, () -> {
            sut.issue(coupon.getId(), userId);
        });

        Assertions.assertEquals(exception.getErrorCode(), DUPLICATED_COUPON_ISSUE);

    }


    @Test
    @DisplayName("쿠폰이 존재하지 않는다면 예외 반환")
    void issue_5(){
        // given
        long userId = 1;
        long couponId = 1;

        // when & then
        CouponIssueException exception = Assertions.assertThrows(CouponIssueException.class, () -> {
            sut.issue(couponId, userId);
        });

        Assertions.assertEquals(exception.getErrorCode(), COUPON_NOT_EXIST);

    }


}