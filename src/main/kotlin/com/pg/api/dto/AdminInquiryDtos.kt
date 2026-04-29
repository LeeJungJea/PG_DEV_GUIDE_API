package com.pg.api.dto

// 관리자 문의 관리 화면에서 목록과 상세, 답변 저장에 쓰는 DTO다.
// Java의 방어적인 getter/setter 방식보다, Kotlin data class로 필요한 필드를 더 직선적으로 표현한다.
data class AdminInquiryEntryResponse(
    val id: String,
    val inquiryNo: String,
    val userId: String,
    val authorName: String,
    val authorUsername: String,
    val categoryCode: String,
    val title: String,
    val preview: String,
    val status: String,
    val hasAttachments: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val answeredAt: String? = null,
)

data class AdminInquiryFileResponse(
    val id: String,
    val inquiryId: String,
    val ownerType: String,
    val fileRole: String,
    val originalFileName: String,
    val fileUrl: String,
    val mimeType: String,
    val fileSizeBytes: Long,
    val createdAt: String,
)

data class AdminInquiryDetailResponse(
    val id: String,
    val inquiryNo: String,
    val userId: String,
    val authorName: String,
    val authorUsername: String,
    val categoryCode: String,
    val title: String,
    val contentText: String,
    val answerContentText: String? = null,
    val status: String,
    val hasAttachments: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val answeredAt: String? = null,
    val files: List<AdminInquiryFileResponse> = emptyList(),
)

data class AdminInquiryListResponse(
    val items: List<AdminInquiryEntryResponse>,
    val page: Int,
    val size: Int,
    val totalCount: Long,
    val totalPages: Int,
)

data class UpdateInquiryStatusRequest(
    val status: String,
)

data class UpdateInquiryAnswerRequest(
    val answerContentText: String,
    val status: String,
)

data class AdminInquiryDashboardSummaryResponse(
    val todayReceivedCount: Long,
    val unhandledCount: Long,
    val avgResponseMinutes: Int,
)
