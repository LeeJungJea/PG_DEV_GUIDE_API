package com.pg.api.domain

import java.time.LocalDateTime

// ApiEndpoint는 문서에 보이는 실제 API 한 개를 표현한다.
// HTTP 메서드, 경로, 버전, 상태, 설명을 함께 담는다.
// Java의 API 문서용 VO처럼 보이지만, Kotlin data class라 필드 선언이 더 간결하다.
data class ApiEndpoint(
    val id: Long? = null,
    val name: String,
    val httpMethod: String,
    val endpoint: String,
    val version: String,
    val displayOrder: Int = 999,
    var status: String = "정상 운영",
    val description: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now()
)
