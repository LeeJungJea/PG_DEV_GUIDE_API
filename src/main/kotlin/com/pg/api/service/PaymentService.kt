package com.pg.api.service

import com.pg.api.domain.Cancellation
import com.pg.api.domain.Payment
import com.pg.api.dto.CancelRequest
import com.pg.api.dto.PaymentStatusResponse
import com.pg.api.repository.PaymentMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.util.*

@Service
// 담당자: 김준우
// 결제 서비스는 결제 생성, 상태 계산, 취소 처리처럼 금액과 상태가 엮인 로직을 담당한다.
class PaymentService(private val paymentMapper: PaymentMapper) {

    // 결제 요청이 들어오면 먼저 DB에 기록한다.
    @Transactional
    fun requestPayment(payment: Payment): Payment {
        paymentMapper.insertPayment(payment)
        return payment
    }

    // 결제 상태는 취소 금액 누적분을 빼서 남은 금액까지 계산한다.
    fun getPaymentStatus(paymentId: String): PaymentStatusResponse? {
        val payment = paymentMapper.findByPaymentId(paymentId) ?: return null
        
        // 총 취소 금액 합계 계산
        val totalCancelledAmount = payment.cancellations.fold(BigDecimal.ZERO) { acc, cancel -> acc.add(cancel.cancelAmount) }
        val remainedAmount = payment.amount.subtract(totalCancelledAmount)

        return PaymentStatusResponse(
            paymentId = payment.paymentId,
            tid = payment.tid,
            status = payment.status,
            amount = payment.amount,
            remainedAmount = remainedAmount,
            goodsName = payment.goodsName,
            method = payment.paymentMethod,
            cardInfo = payment.cardDetail?.let { 
                mapOf("issuer" to it.issuer, "cardNumber" to it.cardNumber, "installmentMonth" to it.installmentMonth) 
            }
        )
    }

    // 전체 취소와 부분 취소를 같은 메서드에서 처리한다.
    @Transactional
    fun cancelPayment(request: CancelRequest): Cancellation {
        val payment = paymentMapper.findByPaymentId(request.paymentId)
            ?: throw IllegalArgumentException("해당 결제 건을 찾을 수 없습니다: ${request.paymentId}")

        // 이미 전액 취소된 건은 다시 취소할 수 없다.
        if (payment.status == "CANCELLED") {
            throw IllegalStateException("이미 전액 취소된 결제입니다.")
        }

        // 취소 금액이 없으면 전액 취소로 간주한다.
        val cancelAmount = request.cancelAmount ?: payment.amount
        
        // 기존 취소 금액 합계 계산
        val totalCancelledSoFar = payment.cancellations.fold(BigDecimal.ZERO) { acc, c -> acc.add(c.cancelAmount) }
        val newTotalCancelled = totalCancelledSoFar.add(cancelAmount)
        val remainedAmount = payment.amount.subtract(newTotalCancelled)

        // 취소 후 잔액이 음수가 되면 잘못된 요청이다.
        if (remainedAmount < BigDecimal.ZERO) {
            throw IllegalArgumentException("취소 요청 금액이 남은 잔액을 초과합니다.")
        }

        // 취소 내역 저장
        val cancellation = Cancellation(
            cancelId = "CXL_" + UUID.randomUUID().toString().substring(0, 8).uppercase(),
            paymentId = payment.id!!,
            cancelAmount = cancelAmount,
            remainedAmount = remainedAmount,
            cancelReason = request.cancelReason
        )
        paymentMapper.insertCancellation(cancellation)

        // 결제 상태 업데이트
        val newStatus = if (remainedAmount == BigDecimal.ZERO) "CANCELLED" else "PARTIAL_CANCELLED"
        paymentMapper.updateStatus(payment.paymentId, newStatus)

        return cancellation
    }
}
