package com.pg.api.controller

import com.pg.api.domain.AdminUser
import com.pg.api.domain.UserActivityLog
import com.pg.api.dto.AdminUserActivityLogResponse
import com.pg.api.dto.AdminUserDetailResponse
import com.pg.api.dto.AdminUserEntryResponse
import com.pg.api.dto.AdminUserListResponse
import com.pg.api.dto.ApiResponse
import com.pg.api.service.AdminUserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.format.DateTimeFormatter
import kotlin.math.ceil

@RestController
@RequestMapping("/admin/users")
// 담당자: 노혜정
// AdminUserController는 회원 목록과 회원 상세를 관리자 화면에 제공하는 입구다.
// 목록과 상세를 한 곳에서 넘기되, 화면이 필요한 값만 차분하게 맞춰 내려준다.
class AdminUserController(private val adminUserService: AdminUserService) {

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    // 회원 목록은 페이징과 필터를 적용해 내려준다.
    // 검색어와 상태값을 함께 받기 때문에, 결과를 너무 복잡하지 않게 정리하는 편이 좋다.
    @GetMapping
    fun getUsers(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(required = false) keyword: String?,
        @RequestParam(required = false) status: String?,
    ): ResponseEntity<ApiResponse<AdminUserListResponse>> {
        val sanitizedPage = page.coerceAtLeast(1)
        val sanitizedSize = size.coerceIn(1, 100)
        val (users, totalCount) = adminUserService.getUsers(sanitizedPage, sanitizedSize, keyword, status)
        val totalPages = if (totalCount == 0L) 1 else ceil(totalCount.toDouble() / sanitizedSize.toDouble()).toInt()
        val payload = AdminUserListResponse(
            items = users.map { it.toEntryResponse() },
            page = sanitizedPage,
            size = sanitizedSize,
            totalCount = totalCount,
            totalPages = totalPages,
        )

        return ResponseEntity.ok(ApiResponse("SUCCESS", payload))
    }

    // 회원 상세는 활동 로그까지 함께 보여 준다.
    // 우측 패널이 한 번에 채워지도록, 본문과 이력을 묶어서 반환한다.
    @GetMapping("/{id}")
    fun getUserDetail(@PathVariable id: Long): ResponseEntity<ApiResponse<AdminUserDetailResponse?>> {
        val user = adminUserService.getUserById(id)
            ?: return ResponseEntity.status(404).body(ApiResponse("ERROR", null, "회원을 찾을 수 없습니다: $id"))

        val activityLogs = adminUserService.getUserActivityLogsByActor(user.username).map { it.toResponse() }
        return ResponseEntity.ok(ApiResponse("SUCCESS", user.toDetailResponse(activityLogs)))
    }

    private fun AdminUser.toEntryResponse() = AdminUserEntryResponse(
        id = id.toString(),
        username = username,
        name = name,
        email = email,
        status = status,
        role = role,
        phone = phone,
        profileImageUrl = profileImageUrl,
        lastLoginAt = lastLoginAt?.format(dateTimeFormatter),
    )

    private fun AdminUser.toDetailResponse(activityLogs: List<AdminUserActivityLogResponse>) = AdminUserDetailResponse(
        id = id.toString(),
        username = username,
        name = name,
        email = email,
        status = status,
        role = role,
        phone = phone,
        profileImageUrl = profileImageUrl,
        lastLoginAt = lastLoginAt?.format(dateTimeFormatter),
        activityLogs = activityLogs,
    )

    private fun UserActivityLog.toResponse() = AdminUserActivityLogResponse(
        id = id.toString(),
        activityType = activityType,
        activityTitle = activityTitle,
        activityDetail = activityDetail,
        actorUsername = actorUsername,
        createdAt = createdAt.format(dateTimeFormatter),
    )
}
