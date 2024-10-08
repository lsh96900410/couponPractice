package com.example.couponapi.service;

import com.example.couponapi.controller.dto.CouponIssueRequestDto;
import com.example.couponcore.component.DistributeLockExecutor;
import com.example.couponcore.service.AsyncCouponIssueServiceV1;
import com.example.couponcore.service.CouponIssueService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponIssueRequestService {

    private final CouponIssueService couponIssueService;
    private final DistributeLockExecutor distributeLockExecutor;
    private final AsyncCouponIssueServiceV1 asyncCouponIssueServiceV1;
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());


    public void asyncIssueRequestV1(CouponIssueRequestDto requestDto){

        asyncCouponIssueServiceV1.issue(requestDto.couponId(), requestDto.userId());

        log.info("쿠폰 발급 완료. couponId: %s, userId: %s".formatted(requestDto.couponId(), requestDto.userId()));
    }


    // MySQL X Lock 설정 -> Record Read Lock
    // SELECT ~~ FOR UPDATE : 조회 레코드에 대한 X-Lock 획득
    public void issueRequestV1(CouponIssueRequestDto requestDto){

        couponIssueService.issue(requestDto.couponId(), requestDto.userId());

        log.info("쿠폰 발급 완료. couponId: %s, userId: %s".formatted(requestDto.couponId(), requestDto.userId()));
    }

    /*
    Redis Lock 설정 -> Lock 이름 설정을 통해 진행

    public void issueRequestV1(CouponIssueRequestDto requestDto){

        distributeLockExecutor.execute("lock_"+requestDto.couponId(),10000,10000,
                () -> { couponIssueService.issue(requestDto.couponId(), requestDto.userId());
        });

        log.info("쿠폰 발급 완료. couponId: %s, userId: %s".formatted(requestDto.couponId(), requestDto.userId()));
    }
    */


     /*
    public void issueRequestV1(CouponIssueRequestDto requestDto){

        synchronized (this) {
            couponIssueService.issue(requestDto.couponId(), requestDto.userId());
        }
        log.info("쿠폰 발급 완료. couponId: %s, userId: %s".formatted(requestDto.couponId(), requestDto.userId()));
    }


    Transaction 내부에서 lock 작업 시 발생 문제 상황 해결 방법 -> 외부에 lock 적용

    lock 획득
    트랜잭션 시작

    Coupon coupon = findCoupon(couponId);
    coupon.issue();
    saveCouponIssue(couponId, userId);

    트랜잭션 커밋

    lock 반납

    문제점
    -> synchronized : Java Application 종속 -> 여러 서버로 확장될 경우 lock 제대로 관리 X
    
    -> 분산락 구현 하여 해결

    1. mysql -

    2. redis -
     */
}
