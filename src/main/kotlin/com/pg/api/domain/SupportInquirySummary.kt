package com.pg.api.domain

import java.time.LocalDateTime

// SupportInquirySummary는 사용자 마이페이지나 목록 화면에 보여 줄 문의 요약 정보다.
// Java에서 목록용 summary VO를 두는 것처럼, 필요한 필드만 가볍게 담는다.
// 화면 목록은 이 정도 정보만 있으면 충분해서, 무거운 상세 필드는 빼 둔다.
data class SupportInquirySummary(
    val inquiryId: Long,
    val inquiryNo: String,
    val categoryCode: String,
    val title: String,
    val status: String,
    val createdAt: LocalDateTime,
)

// SupportInquiryDetail은 사용자가 문의 상세를 열었을 때 필요한 본문과 답변까지 포함한다.
// Java에서 상세 VO를 따로 두는 것처럼, 목록보다 조금 더 많은 필드를 함께 싣는다.
data class SupportInquiryDetail(
    val inquiryId: Long,
    val inquiryNo: String,
    val categoryCode: String,
    val title: String,
    val contentText: String,
    val answerContentText: String? = null,
    val status: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val answeredAt: LocalDateTime? = null,
)

// SupportInquiryFileSummary는 문의 첨부파일 목록을 보여 줄 때 쓰는 요약 정보다.
// Java에서 파일 목록용 VO를 따로 두는 것처럼, 미리보기와 식별에 필요한 값만 담는다.
data class SupportInquiryFileSummary(
    val fileId: Long,
    val inquiryId: Long,
    val ownerType: String,
    val fileRole: String,
    val originalFileName: String,
    val fileUrl: String,
    val mimeType: String,
    val fileSizeBytes: Long,
    val createdAt: LocalDateTime,
)
