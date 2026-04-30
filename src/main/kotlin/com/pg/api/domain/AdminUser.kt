package com.pg.api.domain

import java.time.LocalDateTime

// AdminUser는 관리자 목록과 상세 패널에서 보여 줄 회원 정보를 담는 도메인이다.
// Java에서 흔히 보던 POJO와 비슷하지만, Kotlin data class라서 선언이 더 짧다.
data class AdminUser(
    val id: Long,
    val username: String,
    val name: String,
    val email: String,
    val status: String,
    val role: String,
    val phone: String? = null,
    val profileImageUrl: String? = null,
    val lastLoginAt: LocalDateTime? = null,
)
