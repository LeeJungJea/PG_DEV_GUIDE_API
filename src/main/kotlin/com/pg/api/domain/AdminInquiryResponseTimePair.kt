package com.pg.api.domain

import java.time.LocalDateTime

// Java에서 계산용 VO를 따로 두는 것처럼, 평균 응답 시간을 구하기 위한 값만 담는다.
data class AdminInquiryResponseTimePair(
    val createdAt: LocalDateTime,
    val answeredAt: LocalDateTime,
)
