package com.pg.api.controller

import com.pg.api.domain.AdminInquiry
import com.pg.api.domain.AdminInquiryFile
import com.pg.api.dto.AdminInquiryDetailResponse
import com.pg.api.dto.AdminInquiryEntryResponse
import com.pg.api.dto.AdminInquiryFileResponse
import com.pg.api.dto.AdminInquiryListResponse
import com.pg.api.dto.AdminInquiryDashboardSummaryResponse
import com.pg.api.dto.ApiResponse
import com.pg.api.dto.UpdateInquiryAnswerRequest
import com.pg.api.dto.UpdateInquiryStatusRequest
import com.pg.api.service.AdminInquiryService
import org.jsoup.Jsoup
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.time.format.DateTimeFormatter
import kotlin.math.ceil

@RestController
@RequestMapping("/admin/inquiries")
// 담당자: 이정재
// AdminInquiryController는 관리자 문의 대시보드, 목록, 상세, 상태 변경, 답변 저장의 입구다.
// 요청이 많아 보여도, 실제로는 조회용과 수정용 흐름을 딱 나눠 두면 된다.
class AdminInquiryController(private val adminInquiryService: AdminInquiryService) {

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    // 대시보드 요약은 접수/미처리/평균 응답시간만 빠르게 보여 준다.
    // 화면 상단 카드가 필요로 하는 값만 가져오면 충분하다.
    @GetMapping("/dashboard-summary")
    fun getDashboardSummary(): ResponseEntity<ApiResponse<AdminInquiryDashboardSummaryResponse>> {
        val (todayReceived, unhandled, avgResponseMinutes) = adminInquiryService.getDashboardSummary()
        val payload = AdminInquiryDashboardSummaryResponse(
            todayReceivedCount = todayReceived,
            unhandledCount = unhandled,
            avgResponseMinutes = avgResponseMinutes,
        )
        return ResponseEntity.ok(ApiResponse("SUCCESS", payload))
    }

    // 미처리 문의는 관리자 홈의 보조 카드에 표시한다.
    // 많지 않게 잘라서 보내면, 첫 화면이 덜 복잡해진다.
    @GetMapping("/dashboard-unhandled")
    fun getDashboardUnhandled(
        @RequestParam(defaultValue = "5") limit: Int,
    ): ResponseEntity<ApiResponse<List<AdminInquiryEntryResponse>>> {
        val items = adminInquiryService.getRecentUnhandled(limit).map { it.toEntryResponse() }
        return ResponseEntity.ok(ApiResponse("SUCCESS", items))
    }

    // 목록 조회는 필터와 페이징을 함께 처리한다.
    // 검색 조건이 붙을 수 있으니, 여기서 한 번에 정리해 둔다.
    @GetMapping
    fun getInquiries(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false) keyword: String?,
        @RequestParam(required = false) status: String?,
        @RequestParam(required = false) categoryCode: String?,
        @RequestParam(required = false) fromDate: String?,
        @RequestParam(required = false) toDate: String?,
    ): ResponseEntity<ApiResponse<AdminInquiryListResponse>> {
        return try {
            val sanitizedPage = page.coerceAtLeast(1)
            val sanitizedSize = size.coerceIn(1, 100)
            val (items, totalCount) = adminInquiryService.getInquiries(
                page = sanitizedPage,
                size = sanitizedSize,
                keyword = keyword,
                status = status,
                categoryCode = categoryCode,
                fromDate = fromDate,
                toDate = toDate,
            )
            val totalPages = if (totalCount == 0L) 1 else ceil(totalCount.toDouble() / sanitizedSize.toDouble()).toInt()
            val payload = AdminInquiryListResponse(
                items = items.map { it.toEntryResponse() },
                page = sanitizedPage,
                size = sanitizedSize,
                totalCount = totalCount,
                totalPages = totalPages,
            )
            ResponseEntity.ok(ApiResponse("SUCCESS", payload))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(400).body(ApiResponse("ERROR", null, e.message))
        }
    }

    // 상세 화면은 본문과 첨부파일을 함께 보낸다.
    // 본문과 파일을 따로 받으면 화면이 다시 합쳐야 해서 번거롭다.
    @GetMapping("/{id:\\d+}")
    fun getInquiryDetail(@PathVariable id: Long): ResponseEntity<ApiResponse<AdminInquiryDetailResponse?>> {
        val inquiry = adminInquiryService.getInquiryById(id)
            ?: return ResponseEntity.status(404).body(ApiResponse("ERROR", null, "문의를 찾을 수 없습니다: $id"))

        val files = adminInquiryService.getInquiryFiles(id)
        return ResponseEntity.ok(ApiResponse("SUCCESS", inquiry.toDetailResponse(files)))
    }

    // 상태만 바꾸는 요청은 간단한 PATCH로 처리한다.
    // 전체 수정이 아니라 값 하나만 바뀌면 PATCH가 더 어울린다.
    @PatchMapping("/{id}/status")
    fun updateInquiryStatus(
        @PathVariable id: Long,
        @RequestBody request: UpdateInquiryStatusRequest,
    ): ResponseEntity<ApiResponse<AdminInquiryEntryResponse?>> {
        return try {
            val updated = adminInquiryService.updateInquiryStatus(id, request.status)
            ResponseEntity.ok(ApiResponse("SUCCESS", updated.toEntryResponse()))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(400).body(ApiResponse("ERROR", null, e.message))
        }
    }

    // 답변 본문만 저장하는 경우의 엔드포인트다.
    // 답변 텍스트와 상태 변경을 분리하면 흐름이 훨씬 읽기 쉬워진다.
    @PatchMapping("/{id}/answer")
    fun updateInquiryAnswer(
        @PathVariable id: Long,
        @RequestBody request: UpdateInquiryAnswerRequest,
    ): ResponseEntity<ApiResponse<AdminInquiryEntryResponse?>> {
        return try {
            val updated = adminInquiryService.updateInquiryAnswer(
                id = id,
                answerContentText = request.answerContentText,
                status = request.status,
            )
            ResponseEntity.ok(ApiResponse("SUCCESS", updated.toEntryResponse()))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(400).body(ApiResponse("ERROR", null, e.message))
        }
    }

    // 파일 첨부가 포함되면 multipart로 별도 처리한다.
    // 텍스트와 파일 전송 방식이 다르기 때문에, 이 엔드포인트는 분리해 둔다.
    @PatchMapping("/{id}/answer", consumes = ["multipart/form-data"])
    fun updateInquiryAnswerMultipart(
        @PathVariable id: Long,
        @RequestParam answerContentText: String,
        @RequestParam status: String,
        @RequestParam(required = false) files: List<MultipartFile>?,
        @RequestParam(required = false) fileKeys: List<String>?,
    ): ResponseEntity<ApiResponse<AdminInquiryEntryResponse?>> {
        return try {
            val updated = adminInquiryService.updateInquiryAnswer(
                id = id,
                answerContentText = answerContentText,
                status = status,
                files = files ?: emptyList(),
                fileKeys = fileKeys ?: emptyList(),
            )
            ResponseEntity.ok(ApiResponse("SUCCESS", updated.toEntryResponse()))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(400).body(ApiResponse("ERROR", null, e.message))
        } catch (e: IllegalStateException) {
            ResponseEntity.status(500).body(ApiResponse("ERROR", null, e.message))
        }
    }

    private fun AdminInquiry.toEntryResponse() = AdminInquiryEntryResponse(
        id = inquiryId.toString(),
        inquiryNo = inquiryNo,
        userId = userId.toString(),
        authorName = authorName,
        authorUsername = authorUsername,
        categoryCode = categoryCode,
        title = title,
        preview = toPreviewText(contentText),
        status = status,
        hasAttachments = hasAttachments == "Y",
        createdAt = createdAt.format(dateTimeFormatter),
        updatedAt = updatedAt.format(dateTimeFormatter),
        answeredAt = answeredAt?.format(dateTimeFormatter),
    )

    private fun AdminInquiry.toDetailResponse(files: List<AdminInquiryFile>) = AdminInquiryDetailResponse(
        id = inquiryId.toString(),
        inquiryNo = inquiryNo,
        userId = userId.toString(),
        authorName = authorName,
        authorUsername = authorUsername,
        categoryCode = categoryCode,
        title = title,
        contentText = contentText,
        answerContentText = answerContentText,
        status = status,
        hasAttachments = hasAttachments == "Y",
        createdAt = createdAt.format(dateTimeFormatter),
        updatedAt = updatedAt.format(dateTimeFormatter),
        answeredAt = answeredAt?.format(dateTimeFormatter),
        files = files.map { it.toResponse() },
    )

    private fun AdminInquiryFile.toResponse() = AdminInquiryFileResponse(
        id = fileId.toString(),
        inquiryId = inquiryId.toString(),
        ownerType = ownerType,
        fileRole = fileRole,
        originalFileName = originalFileName,
        fileUrl = fileUrl,
        mimeType = mimeType,
        fileSizeBytes = fileSizeBytes,
        createdAt = createdAt.format(dateTimeFormatter),
    )

    private fun toPreviewText(content: String): String {
        return Jsoup.parse(content).text().replace('\n', ' ').trim().take(120)
    }
}
