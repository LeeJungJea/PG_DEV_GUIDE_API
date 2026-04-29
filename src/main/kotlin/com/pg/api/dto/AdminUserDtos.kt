package com.pg.api.dto

// 관리자 회원 관리 화면에서 목록과 상세, 활동 로그를 주고받는 DTO다.
// 목록용과 상세용을 분리하면 API가 커져도 화면별로 필요한 필드만 가져오기 쉽다.
// Java의 응답 VO처럼 보이지만, Kotlin data class는 선언이 짧고 복사도 쉽다.
data class AdminUserEntryResponse(
    val id: String,
    val username: String,
    val name: String,
    val email: String,
    val status: String,
    val role: String,
    val phone: String? = null,
    val profileImageUrl: String? = null,
    val lastLoginAt: String? = null,
)

data class AdminUserListResponse(
    val items: List<AdminUserEntryResponse>,
    val page: Int,
    val size: Int,
    val totalCount: Long,
    val totalPages: Int,
)

data class AdminUserActivityLogResponse(
    val id: String,
    val activityType: String,
    val activityTitle: String,
    val activityDetail: String? = null,
    val actorUsername: String? = null,
    val createdAt: String,
)

data class AdminUserDetailResponse(
    val id: String,
    val username: String,
    val name: String,
    val email: String,
    val status: String,
    val role: String,
    val phone: String? = null,
    val profileImageUrl: String? = null,
    val lastLoginAt: String? = null,
    val activityLogs: List<AdminUserActivityLogResponse> = emptyList(),
)
