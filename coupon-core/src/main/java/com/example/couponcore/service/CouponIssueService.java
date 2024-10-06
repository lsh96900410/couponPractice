package com.example.couponcore.service;

import com.example.couponcore.exception.CouponIssueException;
import com.example.couponcore.exception.ErrorCode;
import com.example.couponcore.model.Coupon;
import com.example.couponcore.model.CouponIssue;
import com.example.couponcore.repository.mysql.CouponIssueJPARepository;
import com.example.couponcore.repository.mysql.CouponIssueRepository;
import com.example.couponcore.repository.mysql.CouponJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.couponcore.exception.ErrorCode.COUPON_NOT_EXIST;
import static com.example.couponcore.exception.ErrorCode.DUPLICATED_COUPON_ISSUE;

@Service
@RequiredArgsConstructor
public class CouponIssueService {

    private final CouponJPARepository couponJPARepository;
    private final CouponIssueJPARepository couponIssueJPARepository;
    private final CouponIssueRepository couponIssueRepository;

    @Transactional
    public void issue(long couponId, long userId){

            //Coupon coupon = findCoupon(couponId);
            Coupon coupon = findCouponWithLock(couponId);

            coupon.issue();

            saveCouponIssue(couponId, userId);

    }

    /*
    트랜잭션 내부에서 lock 작업을 진행하는 경우 발생하는 문제점

    트랜잭션 시작
    
    lock 획득
    Coupon coupon = findCoupon(couponId);  -> 1번 요청 Transaction Commit X + 2번 요청 조회 시 작업 과정 포함 X 데이터 조회
    coupon.issue();
    saveCouponIssue(couponId, userId);
    lock 반납

    1번 요청 

    트랜잭션 커밋
     */
    
    

    @Transactional(readOnly = true)
    public Coupon findCoupon(long couponId){

        return couponJPARepository.findById(couponId).orElseThrow(() -> {
            throw new CouponIssueException(COUPON_NOT_EXIST,"쿠폰 정책이 존재하지 않습니다. %s".formatted(couponId));
        });

    }

    /* MySQL Lock 설정 쿼리 */
    @Transactional(readOnly = true)
    public Coupon findCouponWithLock(long couponId){

        return couponJPARepository.findCouponWithLock(couponId).orElseThrow(() -> {
            throw new CouponIssueException(COUPON_NOT_EXIST,"쿠폰 정책이 존재하지 않습니다. %s".formatted(couponId));
        });

    }

    @Transactional
    public CouponIssue saveCouponIssue(long couponId, long userId){

        checkAlreadyIssuance(couponId,userId);

        CouponIssue issue = CouponIssue.builder()
                .couponId(couponId)
                .userId(userId)
                .build();

        return couponIssueJPARepository.save(issue);
    }




    private void checkAlreadyIssuance(long couponId, long userId){

        CouponIssue issue = couponIssueRepository.findFirstCoupon(couponId, userId);

        if(issue != null){
            throw new CouponIssueException(DUPLICATED_COUPON_ISSUE,
                    "이미 발급된 쿠폰입니다. user_id: %s, coupon_id: %s".formatted(userId,couponId));
        }

    }

}
