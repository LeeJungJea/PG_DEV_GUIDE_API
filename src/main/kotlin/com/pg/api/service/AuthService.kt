package com.pg.api.service

import com.pg.api.dto.LoginRequest
import com.pg.api.dto.LoginResponse
import com.pg.api.dto.LoginResponseData
import com.pg.api.repository.UserMapper
import org.springframework.stereotype.Service

@Service
class AuthService(private val userMapper: UserMapper) {

    // 담당자: 김준우
    // 로그인 성공/실패 판단은 서비스 레이어에서 처리하고, 컨트롤러는 HTTP 흐름만 담당한다.
    // Java/Spring에서도 같은 계층 구조를 쓰지만, Kotlin의 run 블록과 식 표현을 쓰면 분기 로직이 짧아진다.
    fun login(request: LoginRequest, ipAddress: String?, userAgent: String?): LoginResponse {
        println("Login attempt for user: ${request.username}")

        val user = userMapper.findByUsername(request.username)
            ?: run {
                // run 블록은 실패 처리와 예외 던지기를 하나의 흐름으로 묶을 때 유용하다.
                logActivityByUsername(
                    username = request.username,
                    activityType = "LOGIN_FAIL",
                    activityTitle = "로그인 실패",
                    activityDetail = "존재하지 않는 사용자로 로그인을 시도했습니다.",
                    actorUsername = request.username,
                    ipAddress = ipAddress,
                    userAgent = userAgent,
                )
                throw IllegalArgumentException("로그인 실패")
            }

        if (user.password != request.password) {
            // 비밀번호가 맞지 않으면 실패 로그를 남기고 예외를 던진다.
            logActivityByUsername(
                username = user.username,
                activityType = "LOGIN_FAIL",
                activityTitle = "로그인 실패",
                activityDetail = "비밀번호 불일치",
                actorUsername = user.username,
                ipAddress = ipAddress,
                userAgent = userAgent,
            )
            throw IllegalArgumentException("로그인 실패")
        }

        println("User found: ${user.username}, Role: ${user.role}")
        // 성공 시에도 활동 로그를 남겨, 이후 관리자 화면이나 감사 로그에서 추적할 수 있게 한다.
        logActivityByUsername(
            username = user.username,
            activityType = "LOGIN_SUCCESS",
            activityTitle = "로그인 성공",
            activityDetail = "회원이 로그인했습니다.",
            actorUsername = user.username,
            ipAddress = ipAddress,
            userAgent = userAgent,
        )

        return LoginResponse(
            data = LoginResponseData(
                accessToken = "mock-jwt-token-${user.username}-${System.currentTimeMillis()}",
                username = user.username,
                email = user.email,
                role = user.role,
            ),
            message = "로그인 성공",
        )
    }

    private fun logActivityByUsername(
        username: String,
        activityType: String,
        activityTitle: String,
        activityDetail: String?,
        actorUsername: String?,
        ipAddress: String?,
        userAgent: String?,
    ) {
        // 사용자명을 먼저 ID로 바꾼 뒤 로그를 저장한다.
        // DB는 보통 숫자 ID를 기준으로 연결하는 편이 더 안정적이다.
        val userId = userMapper.findUserIdByUsername(username) ?: return
        userMapper.insertUserActivityLog(
            userId = userId,
            activityType = activityType,
            activityTitle = activityTitle,
            activityDetail = activityDetail,
            actorUsername = actorUsername,
            ipAddress = ipAddress,
            userAgent = userAgent,
        )
    }
}
