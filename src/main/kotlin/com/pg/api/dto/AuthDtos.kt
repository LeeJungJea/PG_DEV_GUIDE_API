package com.pg.api.dto

// 로그인 요청/응답에 쓰는 DTO 묶음이다.
// Java의 getter/setter DTO보다 Kotlin data class가 훨씬 짧고 읽기 쉽다.
// 생성자 파라미터에 바로 val을 붙여 두면, Java처럼 필드와 접근자를 따로 쓰지 않아도 된다.
data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val data: LoginResponseData,
    val message: String? = null
)

// 로그인 성공 시 프론트가 화면 분기와 토큰 저장에 바로 활용하는 응답 본문이다.
data class LoginResponseData(
    val accessToken: String,
    val tokenType: String = "Bearer",
    val expiresAt: String = "2099-12-31T23:59:59",
    val username: String,
    val email: String,
    val role: String
)
