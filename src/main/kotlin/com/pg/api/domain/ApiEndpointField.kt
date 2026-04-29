package com.pg.api.domain

// ApiEndpointField는 요청/응답 필드 하나를 표현한다.
// 위치, 타입, 필수 여부, 예시값을 함께 담아 문서화와 입력 검증에 쓴다.
// Java에서 필드 메타데이터 VO를 따로 두는 것처럼, 문서와 검증에 필요한 정보만 모은다.
data class ApiEndpointField(
    val id: Long,
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
