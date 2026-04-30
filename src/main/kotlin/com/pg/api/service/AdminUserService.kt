package com.pg.api.service

import com.pg.api.domain.AdminUser
import com.pg.api.domain.UserActivityLog
import com.pg.api.repository.AdminUserMapper
import org.springframework.stereotype.Service

@Service
// 담당자: 노혜정
// 관리자 회원 관리 서비스는 목록 필터링, 상세 조회, 활동 로그 조회를 맡는다.
// Java 서비스 계층처럼 화면용 조회 규칙을 모으되, Kotlin으로 쓰면 페이징 정리 흐름이 더 간결하다.
class AdminUserService(private val adminUserMapper: AdminUserMapper) {

    // page, size, keyword, status를 먼저 정리한 뒤, 필요한 범위만 페이징 조회한다.
    fun getUsers(
        page: Int,
        size: Int,
        keyword: String?,
        status: String?,
    ): Pair<List<AdminUser>, Long> {
        val sanitizedPage = page.coerceAtLeast(1)
        val sanitizedSize = size.coerceIn(1, 100)
        val normalizedKeyword = keyword?.trim()?.takeIf { it.isNotEmpty() }
        val normalizedStatus = status?.trim()?.uppercase()?.takeIf { it == "ACTIVE" || it == "INACTIVE" }
        val offset = (sanitizedPage - 1) * sanitizedSize

        val users = adminUserMapper.findPage(normalizedKeyword, normalizedStatus, sanitizedSize, offset)
        val totalCount = adminUserMapper.countAll(normalizedKeyword, normalizedStatus)

        return users to totalCount
    }

    // 우측 상세 패널에 필요한 단일 회원 정보를 읽어 온다.
    fun getUserById(id: Long): AdminUser? = adminUserMapper.findById(id)

    // 활동 로그는 최신 몇 개만 보여 주는 편이 화면이 덜 복잡하다.
    fun getUserActivityLogsByActor(username: String, limit: Int = 4): List<UserActivityLog> =
        adminUserMapper.findActivityLogsByActorUsername(username, limit)
}
