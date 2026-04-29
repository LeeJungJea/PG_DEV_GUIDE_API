package com.pg.api.repository

import com.pg.api.domain.AdminUser
import com.pg.api.domain.UserActivityLog
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

@Mapper
// 관리자 회원관리용 페이징 조회와 활동 로그 조회를 담당하는 매퍼다.
// Java에서 회원 DAO를 나누는 방식과 비슷하지만, MyBatis 인터페이스라 SQL 연결이 더 직접적이다.
// Java의 DAO처럼 보이지만, MyBatis 메서드 이름이 SQL ID와 바로 연결된다.
interface AdminUserMapper {
    fun findPage(
        @Param("keyword") keyword: String?,
        @Param("status") status: String?,
        @Param("limit") limit: Int,
        @Param("offset") offset: Int,
    ): List<AdminUser>

    fun countAll(
        @Param("keyword") keyword: String?,
        @Param("status") status: String?,
    ): Long

    fun findById(@Param("id") id: Long): AdminUser?

    fun findActivityLogsByActorUsername(
        @Param("actorUsername") actorUsername: String,
        @Param("limit") limit: Int,
    ): List<UserActivityLog>
}
