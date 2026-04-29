package com.pg.api.dto

import java.math.BigDecimal

// 결제 취소용 요청과 응답을 구분한 DTO다.
// 환불/취소는 결제보다 금액 필드가 더 민감하므로 BigDecimal을 써서 정밀도를 유지한다.
// Java에서도 BigDecimal을 쓰지만, Kotlin data class와 함께 쓰면 값 전달 구조가 더 간단해진다.
data class GuideCancelRequest(
    val orderId: String,
    val cancelAmount: Long? = null,
    val cancelReason: String? = null,
)

data class GuideCancelResponse(
    val orderId: String,
    val paymentMethodId: String? = null,
    val status: String,
    val cancelAmount: Long? = null,
    val remainAmount: Long? = null,
    val canceledAt: String? = null,
    val paymentId: String? = null,
)

// 외부 취소 API에 넘기는 내부 요청 DTO다.
data class CancelRequest(
    val paymentId: String,
    val cancelAmount: BigDecimal? = null,
    val cancelReason: String? = null,
)

data class CancelResponse(
    val status: String,
    val data: CancelData,
)

// 취소 처리 후 내부적으로 정리해 둘 세부 결과다.
data class CancelData(
    val cancelId: String,
    val paymentId: String,
    val cancelledAmount: BigDecimal,
    val remainedAmount: BigDecimal,
    val status: String,
    val createdAt: String,
)
