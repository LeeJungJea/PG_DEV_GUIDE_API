package com.pg.api.domain

import java.time.LocalDateTime

// SupportInquiryCreateCommand는 문의 insert 시점에 필요한 값을 모아 둔 command 객체다.
// 일반 조회용 엔티티와 달리, 생성에만 필요한 필드를 한 번에 담아 전달한다.
data class SupportInquiryCreateCommand(
    var inquiryId: Long? = null,
    val inquiryNo: String,
    val userId: Long,
    val categoryCode: String,
    val title: String,
    val contentText: String,
    val status: String,
    val priority: String,
    val hasAttachments: String,
    val viewCount: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
