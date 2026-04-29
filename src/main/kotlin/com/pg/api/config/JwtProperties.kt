package com.pg.api.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "security.jwt")
// application.yml의 security.jwt.* 값을 한 번에 묶어 받는 설정 객체다.
// Java의 getter/setter 중심 설정 클래스보다, Kotlin data class로 더 간결하게 표현할 수 있다.
// 설정값만 담는 객체라서, 비즈니스 로직은 전혀 넣지 않고 값 보관에만 집중한다.
data class JwtProperties(
    var secret: String = "CJPG_SUPER_SECRET_TOKEN_KEY_2026_1234567890",
    var issuer: String = "pg-dev-guide",
    var expirationSeconds: Long = 3600
)
