package com.pg.api.domain

import java.time.LocalDateTime

// Java에서 파일 업로드용 command DTO를 따로 두는 것과 비슷하게, 저장용 값만 묶어서 전달한다.
data class SupportInquiryFileCreateCommand(
    var fileId: Long? = null,
    val inquiryId: Long,
    val ownerType: String,
    val fileRole: String,
    val originalFileName: String,
    val storedFileName: String,
    val fileUrl: String,
    val mimeType: String,
    val fileSizeBytes: Long,
    val inlineKey: String?,
    val sortOrder: Int,
    val uploadedByUserId: Long,
    val createdAt: LocalDateTime,
)
