package com.pg.api.domain

import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * 취소 내역 정보
 */
// Cancellation은 한 번의 취소 시도를 표현하는 엔티티다.
// 결제 금액과 남은 금액을 함께 보관해 부분 취소를 계산할 수 있게 한다.
// Java의 취소 이력 VO처럼 볼 수 있고, 부분 취소 계산에 필요한 값만 담는다.
data class Cancellation(
    val id: Long? = null,
    val cancelId: String,
    val paymentId: Long,
    val cancelAmount: BigDecimal,
    val remainedAmount: BigDecimal,
    val cancelReason: String? = null,
    val cancelledAt: LocalDateTime = LocalDateTime.now()
)
