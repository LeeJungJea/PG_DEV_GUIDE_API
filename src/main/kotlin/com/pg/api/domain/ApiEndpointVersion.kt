package com.pg.api.domain

import java.time.LocalDateTime

// ApiEndpointVersion은 하나의 API 정의에 대한 버전별 스냅샷이다.
// 같은 API라도 버전이 달라질 수 있으므로 분리해서 관리한다.
// Java에서 버전별 이력 VO를 따로 두는 것처럼, 같은 API의 변화를 분리해 보관한다.
data class ApiEndpointVersion(
    var id: Long? = null,
    val apiDefinitionId: Long,
    val version: String,
    val endpoint: String,
    val httpMethod: String,
    val displayOrder: Int = 999,
    val status: String,
    val description: String? = null,
    val isCurrent: String = "Y",
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
