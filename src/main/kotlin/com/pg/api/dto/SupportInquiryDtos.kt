package com.pg.api.dto

// 일반 사용자 문의 화면에서 목록, 상세, 파일 정보를 주고받는 DTO다.
// Java의 여러 응답 객체를 흩어 놓는 방식보다, Kotlin data class로 묶어 두면 읽는 흐름이 더 단순하다.
data class CreateSupportInquiryResponse(
    val inquiryId: String,
    val inquiryNo: String,
    val uploadedFileCount: Int,
)

data class SupportInquirySummaryResponse(
    val inquiryId: String,
    val inquiryNo: String,
    val categoryCode: String,
    val title: String,
    val status: String,
    val createdAt: String,
)

data class SupportInquiryDetailResponse(
    val inquiryId: String,
    val inquiryNo: String,
    val categoryCode: String,
    val title: String,
    val contentText: String,
    val answerContentText: String?,
    val status: String,
    val createdAt: String,
    val updatedAt: String,
    val answeredAt: String?,
    val files: List<SupportInquiryFileResponse>,
)

data class SupportInquiryFileResponse(
    val fileId: String,
    val inquiryId: String,
    val ownerType: String,
    val fileRole: String,
    val originalFileName: String,
    val fileUrl: String,
    val mimeType: String,
    val fileSizeBytes: Long,
    val createdAt: String,
)
