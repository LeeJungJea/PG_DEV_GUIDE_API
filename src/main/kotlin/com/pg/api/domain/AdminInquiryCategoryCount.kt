package com.pg.api.domain

// Java에서 통계용 VO를 따로 두는 것처럼, 화면에 바로 그릴 수 있는 숫자만 모아 둔다.
data class AdminInquiryCategoryCount(
    val categoryCode: String,
    val count: Long,
)
