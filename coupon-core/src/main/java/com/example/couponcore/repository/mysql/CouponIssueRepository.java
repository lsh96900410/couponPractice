package com.example.couponcore.repository.mysql;

import com.example.couponcore.model.CouponIssue;
import com.example.couponcore.model.QCouponIssue;
import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.example.couponcore.model.QCouponIssue.couponIssue;

@Repository
@RequiredArgsConstructor
public class CouponIssueRepository {

    private final JPQLQueryFactory queryFactory;


    public CouponIssue findFirstCoupon(long couponId, long userId){
        return queryFactory.selectFrom(couponIssue)
                .where(couponIssue.couponId.eq(couponId))
                .where(couponIssue.userId.eq(userId))
                .fetchFirst();
    }

}
