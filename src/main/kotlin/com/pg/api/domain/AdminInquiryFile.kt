package com.pg.api.domain

import java.time.LocalDateTime

// AdminInquiryFile은 문의에 연결된 첨부파일이나 답변 이미지 정보를 나타낸다.
// Java에서 첨부파일 메타 VO를 따로 두는 것처럼, 본문과 파일을 분리해 다룬다.
data class AdminInquiryFile(
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
