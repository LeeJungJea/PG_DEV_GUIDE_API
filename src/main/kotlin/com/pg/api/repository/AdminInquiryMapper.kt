package com.pg.api.repository

import com.pg.api.domain.AdminInquiry
import com.pg.api.domain.AdminInquiryFile
import com.pg.api.domain.AdminInquiryResponseTimePair
import com.pg.api.domain.SupportInquiryFileCreateCommand
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import java.time.LocalDateTime

@Mapper
// 관리자 문의관리에서 목록, 상세, 답변, 첨부파일, 대시보드 통계를 읽고 쓰는 매퍼다.
// Java의 DAO 계층처럼 보이지만, MyBatis 인터페이스라 SQL 연결이 더 직접적이다.
interface AdminInquiryMapper {
    fun findPage(
        @Param("offset") offset: Int,
        @Param("limit") limit: Int,
        @Param("keyword") keyword: String?,
        @Param("status") status: String?,
        @Param("categoryCode") categoryCode: String?,
        @Param("sortDirection") sortDirection: String,
        @Param("fromDateTime") fromDateTime: LocalDateTime,
        @Param("toDateTimeExclusive") toDateTimeExclusive: LocalDateTime,
    ): List<AdminInquiry>

    fun countAll(
        @Param("keyword") keyword: String?,
        @Param("status") status: String?,
        @Param("categoryCode") categoryCode: String?,
        @Param("fromDateTime") fromDateTime: LocalDateTime,
        @Param("toDateTimeExclusive") toDateTimeExclusive: LocalDateTime,
    ): Long

    fun findById(@Param("id") id: Long): AdminInquiry?

    fun findFilesByInquiryId(@Param("inquiryId") inquiryId: Long): List<AdminInquiryFile>

    fun updateStatus(
        @Param("id") id: Long,
        @Param("status") status: String,
        @Param("updatedAt") updatedAt: LocalDateTime,
    ): Int

    fun updateAnswerAndStatus(
        @Param("id") id: Long,
        @Param("answerContentText") answerContentText: String,
        @Param("status") status: String,
        @Param("updatedAt") updatedAt: LocalDateTime,
    ): Int

    fun insertInquiryFile(command: SupportInquiryFileCreateCommand): Int

    fun countTodayReceived(
        @Param("fromDateTime") fromDateTime: LocalDateTime,
        @Param("toDateTimeExclusive") toDateTimeExclusive: LocalDateTime,
    ): Long

    fun countUnhandled(): Long

    fun findAnsweredResponseTimePairsInRange(
        @Param("fromDateTime") fromDateTime: LocalDateTime,
        @Param("toDateTimeExclusive") toDateTimeExclusive: LocalDateTime,
    ): List<AdminInquiryResponseTimePair>

    fun findRecentUnhandled(@Param("limit") limit: Int): List<AdminInquiry>
}
