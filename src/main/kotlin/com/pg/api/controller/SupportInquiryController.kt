package com.pg.api.controller

import com.pg.api.dto.ApiResponse
import com.pg.api.dto.CreateSupportInquiryResponse
import com.pg.api.dto.SupportInquiryDetailResponse
import com.pg.api.dto.SupportInquiryFileResponse
import com.pg.api.dto.SupportInquirySummaryResponse
import com.pg.api.service.SupportInquiryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.time.format.DateTimeFormatter

@RestController
@RequestMapping("/support/inquiries")
// 담당자: 이정재
// SupportInquiryController는 일반 사용자의 문의 목록, 상세, 생성 요청을 받는 입구다.
// 일반 사용자 화면에서 필요한 흐름만 빠르게 묶어 주는 컨트롤러다.
class SupportInquiryController(
    private val supportInquiryService: SupportInquiryService,
) {

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    // 사용자의 문의 목록은 username 기준으로 가져온다.
    // 본인 데이터만 보여 주는 화면이라, 기준값이 분명해야 한다.
    @GetMapping
    fun getMyInquiries(
        @RequestParam username: String,
        @RequestParam(defaultValue = "20") size: Int,
    ): ResponseEntity<ApiResponse<List<SupportInquirySummaryResponse>>> {
        return try {
            val items = supportInquiryService.getMyInquiries(username, size).map {
                SupportInquirySummaryResponse(
                    inquiryId = it.inquiryId.toString(),
                    inquiryNo = it.inquiryNo,
                    categoryCode = it.categoryCode,
                    title = it.title,
                    status = it.status,
                    createdAt = it.createdAt.format(dateTimeFormatter),
                )
            }
            ResponseEntity.ok(ApiResponse("SUCCESS", items))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(400).body(ApiResponse("ERROR", null, e.message))
        }
    }

    // 문의 상세는 본인 문의인지 확인하기 위해 username을 함께 받는다.
    // 다른 사람 문의를 못 보게 하려면, 여기서 사용자 검증을 같이 해야 한다.
    @GetMapping("/{id:\\d+}")
    fun getInquiryDetail(
        @PathVariable id: Long,
        @RequestParam username: String,
    ): ResponseEntity<ApiResponse<SupportInquiryDetailResponse?>> {
        return try {
            val (inquiry, files) = supportInquiryService.getInquiryDetail(username = username, inquiryId = id)
            val payload = SupportInquiryDetailResponse(
                inquiryId = inquiry.inquiryId.toString(),
                inquiryNo = inquiry.inquiryNo,
                categoryCode = inquiry.categoryCode,
                title = inquiry.title,
                contentText = inquiry.contentText,
                answerContentText = inquiry.answerContentText,
                status = inquiry.status,
                createdAt = inquiry.createdAt.format(dateTimeFormatter),
                updatedAt = inquiry.updatedAt.format(dateTimeFormatter),
                answeredAt = inquiry.answeredAt?.format(dateTimeFormatter),
                files = files.map {
                    SupportInquiryFileResponse(
                        fileId = it.fileId.toString(),
                        inquiryId = it.inquiryId.toString(),
                        ownerType = it.ownerType,
                        fileRole = it.fileRole,
                        originalFileName = it.originalFileName,
                        fileUrl = it.fileUrl,
                        mimeType = it.mimeType,
                        fileSizeBytes = it.fileSizeBytes,
                        createdAt = it.createdAt.format(dateTimeFormatter),
                    )
                },
            )
            ResponseEntity.ok(ApiResponse("SUCCESS", payload))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(404).body(ApiResponse("ERROR", null, e.message))
        }
    }

    // 문의 생성은 multipart로 첨부파일까지 함께 받는다.
    // 텍스트와 파일을 한 번에 보내는 구조라, 요청 형식이 조금 다르다.
    @PostMapping(consumes = ["multipart/form-data"])
    fun createInquiry(
        @RequestParam username: String,
        @RequestParam categoryCode: String,
        @RequestParam title: String,
        @RequestParam contentText: String,
        @RequestParam(required = false) files: List<MultipartFile>?,
        @RequestParam(required = false) fileKeys: List<String>?,
    ): ResponseEntity<ApiResponse<CreateSupportInquiryResponse>> {
        return try {
            val created = supportInquiryService.createInquiry(
                username = username,
                categoryCode = categoryCode,
                title = title,
                contentText = contentText,
                files = files ?: emptyList(),
                fileKeys = fileKeys ?: emptyList(),
            )
            ResponseEntity.ok(ApiResponse("SUCCESS", created))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(400).body(ApiResponse("ERROR", null, e.message))
        } catch (e: IllegalStateException) {
            ResponseEntity.status(500).body(ApiResponse("ERROR", null, e.message))
        }
    }
}
