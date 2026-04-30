package com.pg.api.domain

import java.time.LocalDateTime

// ApiDefinition은 API 묶음의 상위 정의다.
// 버전별 세부 엔드포인트를 묶어 관리할 때 기준이 되는 메타 엔티티다.
// Java에서 상위 VO나 엔티티를 따로 두는 것처럼, 버전별 세부 엔드포인트를 묶는 기준이 된다.
data class ApiDefinition(
    var id: Long? = null,
    val apiCode: String,
    val apiName: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
