package com.pg.api.repository

import com.pg.api.domain.User
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

@Mapper
// UserMapper는 MyBatis가 SQL XML과 연결하는 인터페이스다.
// 함수 이름 자체가 SQL 문장과 대응되며, Java의 DAO보다 훨씬 선언적으로 읽힌다.
interface UserMapper {
    fun findByUsername(username: String): User?

    fun findUserIdByUsername(@Param("username") username: String): Long?

    fun insertUserActivityLog(
        @Param("userId") userId: Long,
        @Param("activityType") activityType: String,
        @Param("activityTitle") activityTitle: String,
        @Param("activityDetail") activityDetail: String?,
        @Param("actorUsername") actorUsername: String?,
        @Param("ipAddress") ipAddress: String?,
        @Param("userAgent") userAgent: String?,
    ): Int
}
