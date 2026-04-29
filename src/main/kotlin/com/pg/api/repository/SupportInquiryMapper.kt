package com.pg.api.repository

import com.pg.api.domain.SupportInquiryCreateCommand
import com.pg.api.domain.SupportInquiryDetail
import com.pg.api.domain.SupportInquiryFileCreateCommand
import com.pg.api.domain.SupportInquiryFileSummary
import com.pg.api.domain.SupportInquirySummary
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

@Mapper
// 일반 사용자의 문의 작성과 목록/상세 조회를 담당하는 매퍼다.
// Java에서 문의 DAO를 따로 두는 것과 비슷하지만, MyBatis 인터페이스가 SQL을 더 선언적으로 묶어 준다.
interface SupportInquiryMapper {
    fun insertInquiry(command: SupportInquiryCreateCommand): Int

    fun insertInquiryFile(command: SupportInquiryFileCreateCommand): Int

    fun findRecentByUserId(
        @Param("userId") userId: Long,
        @Param("limit") limit: Int,
    ): List<SupportInquirySummary>

    fun findDetailByIdAndUserId(
        @Param("inquiryId") inquiryId: Long,
        @Param("userId") userId: Long,
    ): SupportInquiryDetail?

    fun findFilesByInquiryId(@Param("inquiryId") inquiryId: Long): List<SupportInquiryFileSummary>
}
