package study.bank.domains.transactions.presentation.dto

import jakarta.validation.constraints.NotBlank
import java.math.BigDecimal

data class DepositRequest(
    @field:NotBlank
    val toAccountId: String,

    @field:NotBlank
    val toUlid: String,

    @field:NotBlank
    val value: BigDecimal,
)

data class TransferRequest(
    @field:NotBlank
    val fromAccountId: String,

    @field:NotBlank
    val toAccountId: String,

    @field:NotBlank
    val fromUlid: String,

    @field:NotBlank
    val value: BigDecimal,
)