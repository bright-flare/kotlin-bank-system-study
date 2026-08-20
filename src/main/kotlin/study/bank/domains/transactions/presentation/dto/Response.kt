package study.bank.domains.transactions.presentation.dto

import java.math.BigDecimal

data class DepositResponse(
    val afterBalance: BigDecimal,
)

data class TransferResponse(
    val afterFromBalance: BigDecimal,
    val afterToBalance: BigDecimal,
)
