package com.pg.api.domain

import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * 결제 마스터 정보
 */
// Payment는 결제의 기준이 되는 중심 엔티티다.
// DB의 payments 테이블 한 행과 대응하고, 취소 내역과 카드 상세를 함께 묶어 볼 수 있게 한다.
// Java에서 JPA 엔티티나 MyBatis용 도메인 객체로 나누는 것과 비슷하게, 결제의 기준값을 담는다.
data class Payment(
    val id: Long? = null,
    val paymentId: String,
    val tid: String? = null,
    val mid: String,
    val orderId: String,
    val amount: BigDecimal,
    var status: String = "READY",
    val paymentMethod: String? = null,
    val goodsName: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var approvedAt: LocalDateTime? = null,
    var cardDetail: CardDetail? = null,
    val cancellations: MutableList<Cancellation> = mutableListOf()
)
