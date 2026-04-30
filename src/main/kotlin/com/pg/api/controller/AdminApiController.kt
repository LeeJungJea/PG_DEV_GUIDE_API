package com.pg.api.controller

import com.pg.api.domain.ApiEndpoint
import com.pg.api.dto.AdminApiDetailResponse
import com.pg.api.dto.AdminApiEntryResponse
import com.pg.api.dto.AdminApiFieldResponse
import com.pg.api.dto.ApiResponse
import com.pg.api.dto.CreateAdminApiRequest
import com.pg.api.dto.UpdateAdminApiRequest
import com.pg.api.service.AdminApiService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.format.DateTimeFormatter

@RestController
@RequestMapping("/admin/api")
// 담당자: 김준우
// AdminApiController는 관리자 API 목록, 상세, 생성, 수정, 삭제를 받는 진입점이다.
// 서비스 레이어로 넘기기 전에 HTTP 요청과 DTO를 맞춰 주는 역할을 한다.
class AdminApiController(private val adminApiService: AdminApiService) {

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    /**
     * 전체 API 엔드포인트 목록 조회
     * GET /admin/api
     */
    // 목록 화면은 엔티티를 화면용 응답 DTO로 바꿔서 내려준다.
    @GetMapping
    fun getAllApis(): ResponseEntity<ApiResponse<List<AdminApiEntryResponse>>> {
        val apis = adminApiService.getAllApiEndpoints()
        val response = apis.map { convertToResponse(it) }
        return ResponseEntity.ok(ApiResponse("SUCCESS", response))
    }

    /**
     * 단일 API 엔드포인트 조회
     * GET /admin/api/{id}
     */
    // 상세 화면은 API 정보와 필드 목록을 함께 내려준다.
    @GetMapping("/{id}")
    fun getApi(@PathVariable id: Long): ResponseEntity<ApiResponse<AdminApiDetailResponse?>> {
        val api = adminApiService.getApiEndpointById(id)
            ?: return ResponseEntity.status(404).body(ApiResponse("ERROR", null, "API를 찾을 수 없습니다: $id"))

        val fields = adminApiService.getApiFields(api.name, api.endpoint, api.version)
        return ResponseEntity.ok(ApiResponse("SUCCESS", convertToDetailResponse(api, fields.map { convertFieldToResponse(it) })))
    }

    /**
     * API 엔드포인트 등록
     * POST /admin/api
     */
    // 새 API 등록은 요청 DTO를 도메인 엔티티로 바꾼 뒤 서비스로 넘긴다.
    @PostMapping
    fun createApi(@RequestBody request: CreateAdminApiRequest): ResponseEntity<ApiResponse<AdminApiEntryResponse>> {
        return try {
            val newApi = ApiEndpoint(
                name = request.name,
                httpMethod = request.method,
                endpoint = request.endpoint,
                version = request.version,
                displayOrder = request.displayOrder,
                status = request.status,
                description = request.description
            )
            val created = adminApiService.createApiEndpoint(newApi, request.fields)
            ResponseEntity.status(201).body(ApiResponse("SUCCESS", convertToResponse(created)))
        } catch (e: Exception) {
            ResponseEntity.status(400).body(ApiResponse("ERROR", null, e.message))
        }
    }

    /**
     * API 엔드포인트 수정
     * PUT /admin/api/{id}
     */
    // 수정은 기존 id를 기준으로 업데이트한다.
    @PutMapping("/{id}")
    fun updateApi(
        @PathVariable id: Long,
        @RequestBody request: UpdateAdminApiRequest
    ): ResponseEntity<ApiResponse<AdminApiEntryResponse>> {
        return try {
            val updateApi = ApiEndpoint(
                name = request.name,
                httpMethod = request.method,
                endpoint = request.endpoint,
                version = request.version,
                displayOrder = request.displayOrder,
                status = request.status ?: "정상 운영",
                description = request.description
            )
            val updated = adminApiService.updateApiEndpoint(id, updateApi, request.fields)
            ResponseEntity.ok(ApiResponse("SUCCESS", convertToResponse(updated)))
        } catch (e: Exception) {
            ResponseEntity.status(400).body(ApiResponse("ERROR", null, e.message))
        }
    }

    /**
     * API 엔드포인트 삭제
     * DELETE /admin/api/{id}
     */
    // 삭제는 서비스에서 실제 DB 정리를 담당한다.
    @DeleteMapping("/{id}")
    fun deleteApi(@PathVariable id: Long): ResponseEntity<ApiResponse<Any>> {
        return try {
            adminApiService.deleteApiEndpoint(id)
            ResponseEntity.ok(ApiResponse("SUCCESS", null, "삭제되었습니다."))
        } catch (e: Exception) {
            ResponseEntity.status(400).body(ApiResponse("ERROR", null, e.message))
        }
    }

    /**
     * API 상태 업데이트
     * PATCH /admin/api/{id}/status
     */
    // 상태 변경은 목록 재정렬과 화면 배지를 위해 별도 엔드포인트로 분리했다.
    @PatchMapping("/{id}/status")
    fun updateApiStatus(
        @PathVariable id: Long,
        @RequestParam status: String
    ): ResponseEntity<ApiResponse<AdminApiEntryResponse>> {
        return try {
            val updated = adminApiService.updateApiStatus(id, status)
            ResponseEntity.ok(ApiResponse("SUCCESS", convertToResponse(updated)))
        } catch (e: Exception) {
            ResponseEntity.status(400).body(ApiResponse("ERROR", null, e.message))
        }
    }

    private fun convertToResponse(api: ApiEndpoint): AdminApiEntryResponse {
        return AdminApiEntryResponse(
            id = api.id.toString(),
            name = api.name,
            endpoint = api.endpoint,
            method = api.httpMethod,
            version = api.version,
            displayOrder = api.displayOrder,
            status = api.status,
            lastModified = api.updatedAt.format(dateTimeFormatter),
            description = api.description
        )
    }

    private fun convertToDetailResponse(
        api: ApiEndpoint,
        fields: List<AdminApiFieldResponse>
    ): AdminApiDetailResponse {
        return AdminApiDetailResponse(
            id = api.id.toString(),
            name = api.name,
            endpoint = api.endpoint,
            method = api.httpMethod,
            version = api.version,
            displayOrder = api.displayOrder,
            status = api.status,
            lastModified = api.updatedAt.format(dateTimeFormatter),
            description = api.description,
            fields = fields
        )
    }

    private fun convertFieldToResponse(field: com.pg.api.domain.ApiEndpointField): AdminApiFieldResponse {
        return AdminApiFieldResponse(
            id = field.id.toString(),
            fieldScope = field.fieldScope,
            fieldLocation = field.fieldLocation,
            fieldName = field.fieldName,
            fieldType = field.fieldType,
            requiredYn = field.requiredYn,
            fieldOrder = field.fieldOrder,
            description = field.description,
            sampleValue = field.sampleValue,
            defaultValue = field.defaultValue
        )
    }
}
