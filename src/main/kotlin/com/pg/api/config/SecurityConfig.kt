package com.pg.api.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@EnableConfigurationProperties(JwtProperties::class)
class SecurityConfig {

    // Java의 @Bean 설정과 비슷하지만, Kotlin에서는 함수 본문을 더 짧게 쓸 수 있다.
    // 이 빈은 Spring Security가 비밀번호를 안전하게 비교할 때 사용하는 인코더를 등록한다.
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}
