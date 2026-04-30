package com.pg.api.dto

import java.math.BigDecimal

// 결제 요청, 결제 승인 결과, 결제 상태 조회 응답을 각각 분리해 둔 DTO다.
// 같은 "결제"라도 화면과 API 단계가 다르므로 요청/응답을 구분해 두는 편이 좋다.
// Java에서는 한 DTO에 getter/setter를 많이 붙이기 쉬운데, Kotlin은 목적별로 나누면 더 읽기 쉽다.
data class GuidePaymentRequest(
    val paymentMethodId: String? = null,
    val orderId: String? = null,
    val userId: String,
    val itemName: String,
    val amount: Long,
    val approvalUrl: String? = null,
    val cancelUrl: String? = null,
    val failUrl: String? = null,
)

// 공통 API 응답 래퍼다. status, data, message를 한 번에 담아 프론트에서 처리하기 쉽게 만든다.
data class ApiResponse<T>(
    val status: String,
    val data: T? = null,
    val message: String? = null,
)

// 결제 요청을 보낸 뒤 프론트가 다음 화면으로 넘길 때 쓰는 응답 DTO다.
data class GuidePaymentRequestResponse(
    val orderId: String,
    val paymentMethodId: String,
    val paymentId: String? = null,
    val status: String,
    val amount: Long,
    val approvedAt: String? = null,
    val nextRedirectPcUrl: String? = null,
)

// 결제 상태 조회 결과를 담는 DTO다.
data class GuidePaymentStatusResponse(
    val orderId: String,
    val userId: String? = null,
    val amount: Long,
    val status: String,
    val paymentMethodId: String? = null,
    val paymentId: String? = null,
    val createdAt: String? = null,
    val approvalAt: String? = null,
)
