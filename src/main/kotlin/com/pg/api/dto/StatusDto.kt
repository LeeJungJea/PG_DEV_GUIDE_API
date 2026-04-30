package com.pg.api.dto

import java.math.BigDecimal

// 결제 상태 조회 응답과 내부 DB 연동용 상태 응답을 함께 정의한다.
// 외부 API 응답과 내부 도메인 응답은 구조가 다를 수 있어서 분리해 두는 편이 안전하다.
// Java에서 서로 다른 VO를 나눠 두는 것과 같은 이유로, 역할이 다른 응답은 따로 둔다.
data class StatusResponse(
    val status: String,
    val tid: String,
    val amount: Long,
    val paid_at: String,
    val method: String,
    val card_info: CardInfo? = null,
    val receipt_url: String? = null
)

data class CardInfo(
    val issuer: String,
    val number: String,
    val quota: Int
)

// DB 연동 후 프론트에 돌려줄 결제 상태 응답 DTO다.
data class PaymentStatusResponse(
    val paymentId: String,
    val tid: String?,
    val status: String,
    val amount: BigDecimal,
    val remainedAmount: BigDecimal,
    val goodsName: String?,
    val method: String?,
    val cardInfo: Map<String, Any?>?
)
