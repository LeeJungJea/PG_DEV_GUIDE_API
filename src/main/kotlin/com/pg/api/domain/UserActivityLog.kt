package com.pg.api.domain

import java.time.LocalDateTime

// UserActivityLog는 로그인, 실패, 관리자 행동 같은 사용자 이력을 남기는 로그 엔티티다.
// Java의 감사 로그 VO와 비슷하게, 나중에 화면이나 분석에서 다시 읽을 수 있게 남긴다.
data class UserActivityLog(
    val id: Long,
    val userId: Long,
    val activityType: String,
    val activityTitle: String,
    val activityDetail: String? = null,
    val actorUsername: String? = null,
    val createdAt: LocalDateTime,
)
