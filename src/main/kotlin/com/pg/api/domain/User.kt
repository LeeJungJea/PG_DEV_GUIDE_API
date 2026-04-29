package com.pg.api.domain

// User는 로그인 인증에 필요한 최소한의 사용자 정보만 담는 도메인이다.
// Java의 인증용 User VO처럼 보이지만, 실제로는 필요한 필드만 최소 단위로 담는다.
data class User(
    val username: String,
    val password: String,
    val email: String,
    val role: String
)
