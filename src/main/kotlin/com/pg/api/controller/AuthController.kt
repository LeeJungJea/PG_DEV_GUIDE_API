package com.pg.api.controller

import com.pg.api.dto.LoginRequest
import com.pg.api.dto.LoginResponse
import com.pg.api.dto.LoginResponseData
import com.pg.api.service.AuthService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(private val authService: AuthService) {

    // 담당자: 김준우
    // HTTP 요청 바디를 DTO로 받고, 서비스 레이어에 전달하는 전형적인 Spring MVC 컨트롤러 패턴이다.
    // Java의 서블릿 직접 처리보다, @RequestBody와 ResponseEntity로 요청/응답을 더 명확하게 분리한다.
    @PostMapping("/login")
    fun login(
        @RequestBody request: LoginRequest,
        httpRequest: HttpServletRequest,
    ): ResponseEntity<LoginResponse> {
        return try {
            val ipAddress = extractClientIp(httpRequest)
            val userAgent = httpRequest.getHeader("User-Agent")
            val response = authService.login(request, ipAddress, userAgent)
            ResponseEntity.ok(response)
        } catch (e: IllegalArgumentException) {
            println("Auth Error: ${e.message}")
            ResponseEntity.status(401).body(
                LoginResponse(
                    data = LoginResponseData(accessToken = "", username = "", email = "", role = ""),
                    message = e.message,
                ),
            )
        } catch (e: Exception) {
            println("Server Error during login: ${e.message}")
            e.printStackTrace()
            ResponseEntity.status(500).body(
                LoginResponse(
                    data = LoginResponseData(accessToken = "", username = "", email = "", role = ""),
                    message = "서버 오류가 발생했습니다.",
                ),
            )
        }
    }

    // X-Forwarded-For는 프록시나 로드밸런서를 거칠 때 원래 클라이언트 IP를 담는 헤더다.
    // 없으면 request.remoteAddr를 사용해 직접 연결된 IP를 기본값으로 쓴다.
    private fun extractClientIp(request: HttpServletRequest): String? {
        val xForwardedFor = request.getHeader("X-Forwarded-For")
        if (!xForwardedFor.isNullOrBlank()) {
            return xForwardedFor.split(",").first().trim()
        }
        return request.remoteAddr
    }
}
