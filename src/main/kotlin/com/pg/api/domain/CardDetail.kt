package com.pg.api.domain

/**
 * 카드 결제 상세 정보
 */
// Java에서 결제 본문과 카드 상세 VO를 분리하는 것과 비슷하게, 카드 정보만 따로 담아 둔다.
data class CardDetail(
    val paymentId: Long,
    val issuer: String? = null,
    val cardNumber: String? = null,
    val installmentMonth: Int = 0
)
