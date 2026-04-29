package com.pg.api.dto

// 관리자 API 관리 화면에서 쓰는 목록/상세/저장 DTO다.
// 요청과 응답, 그리고 필드 편집용 DTO를 분리해 두면 화면과 API 구조가 서로 헷갈리지 않는다.
// Java에서 DTO를 여러 클래스로 나누는 방식과 비슷하지만, Kotlin data class는 더 가볍게 쓸 수 있다.
data class AdminApiEntryResponse(
    val id: String,
    val name: String,
    val endpoint: String,
    val method: String,
    val version: String,
    val displayOrder: Int,
    val status: String,
    val lastModified: String,
    val description: String? = null
)

data class AdminApiFieldResponse(
    val id: String,
    val fieldScope: String,
    val fieldLocation: String,
    val fieldName: String,
    val fieldType: String,
    val requiredYn: String,
    val fieldOrder: Int,
    val description: String? = null,
    val sampleValue: String? = null,
    val defaultValue: String? = null
)

data class AdminApiDetailResponse(
    val id: String,
    val name: String,
    val endpoint: String,
    val method: String,
    val version: String,
    val displayOrder: Int,
    val status: String,
    val lastModified: String,
    val description: String? = null,
    val fields: List<AdminApiFieldResponse> = emptyList()
)

data class AdminApiFieldRequest(
    val fieldScope: String,
    val fieldLocation: String,
    val fieldName: String,
    val fieldType: String,
    val requiredYn: String,
    val fieldOrder: Int,
    val description: String? = null,
    val sampleValue: String? = null,
    val defaultValue: String? = null
)

data class CreateAdminApiRequest(
    val name: String,
    val method: String,
    val endpoint: String,
    val version: String,
    val displayOrder: Int = 999,
    val status: String = "정상 운영",
    val description: String? = null,
    val fields: List<AdminApiFieldRequest> = emptyList()
)

data class UpdateAdminApiRequest(
    val name: String,
    val method: String,
    val endpoint: String,
    val version: String,
    val displayOrder: Int = 999,
    val status: String? = null,
    val description: String? = null,
    val fields: List<AdminApiFieldRequest> = emptyList()
)
