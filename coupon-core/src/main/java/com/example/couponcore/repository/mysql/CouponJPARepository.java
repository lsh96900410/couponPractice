package com.example.couponcore.repository.mysql;

import com.example.couponcore.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponJPARepository extends JpaRepository<Coupon, Long> {
}
