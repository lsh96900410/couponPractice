package com.example.couponcore.repository.mysql;

import com.example.couponcore.model.Coupon;
import com.example.couponcore.model.CouponIssue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponIssueJPARepository extends JpaRepository<CouponIssue, Long> {
}
