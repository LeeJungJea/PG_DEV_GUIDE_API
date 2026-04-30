package com.pg.api.domain

import java.time.LocalDateTime

// AdminInquiry는 관리자 문의관리 화면의 기준이 되는 문의 엔티티다.
// 작성자, 카테고리, 내용, 답변, 상태, 첨부파일 여부를 함께 보관한다.
// Java에서 문의 상세 VO를 따로 두는 것처럼, 화면에서 바로 읽기 좋게 값을 묶어 둔다.
data class AdminInquiry(
    val inquiryId: Long,
    val inquiryNo: String,
    val userId: Long,
    val authorName: String,
    val authorUsername: String,
    val categoryCode: String,
    val title: String,
    val contentText: String,
    val answerContentText: String? = null,
    val status: String,
    val hasAttachments: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val answeredAt: LocalDateTime? = null,
)
