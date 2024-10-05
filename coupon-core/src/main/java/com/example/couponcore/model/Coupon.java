package com.example.couponcore.model;

import com.example.couponcore.exception.CouponIssueException;
import com.example.couponcore.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.example.couponcore.exception.ErrorCode.INVALID_COUPON_ISSUE_DATE;
import static com.example.couponcore.exception.ErrorCode.INVALID_COUPON_ISSUE_QUANTITY;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name= "coupons")
public class Coupon extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false)
    private String title;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private CouponType couponType;

    private Integer totalQuantity; // Null 허용 - 객체 타입

    @Column (nullable = false)
    private int issuedQuantity;

    @Column (nullable = false)
    private int discountAmount;

    @Column (nullable = false)
    private int minAvailableAmount;

    @Column (nullable = false)
    private LocalDateTime dateIssueStart;

    @Column (nullable = false)
    private LocalDateTime dateIssueEnd;


    /* 검증 메서드 */

    public boolean availableIssueQuantity(){

        if(totalQuantity == null) return true;

        return totalQuantity > issuedQuantity;
    }


    public boolean availableIssueDate(){
        LocalDateTime now = LocalDateTime.now();

        return dateIssueStart.isBefore(now) && dateIssueEnd.isAfter(now);
    }
    
    public void issue(){
        // 수량 검증, 기간 검증
        if(!availableIssueQuantity()){
            throw new CouponIssueException(INVALID_COUPON_ISSUE_QUANTITY,
                    "발급 가능한 수량을 초과했습니다. total: %s, issued: %s".formatted(totalQuantity,issuedQuantity));
        }
        if(!availableIssueDate()){
            throw new CouponIssueException(INVALID_COUPON_ISSUE_DATE,
                    "발급 가능한 일자가 아닙니다. request: %s, issueStart: %s, issueEnd: %s".formatted(LocalDateTime.now(),dateIssueStart,dateIssueEnd));
        }
        
        issuedQuantity++;
    }
}
