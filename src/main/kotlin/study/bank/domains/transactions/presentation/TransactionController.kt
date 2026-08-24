package study.bank.domains.transactions.presentation

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import study.bank.domains.transactions.application.TransactionService
import study.bank.domains.transactions.presentation.dto.DepositRequest
import study.bank.domains.transactions.presentation.dto.DepositResponse
import study.bank.domains.transactions.presentation.dto.TransferRequest
import study.bank.domains.transactions.presentation.dto.TransferResponse
import study.bank.types.dto.Response

@RestController
@RequestMapping("/api/v1/transactions")
class TransactionController(
    private val transactionService: TransactionService,
) {

    @PostMapping("/deposit")
    fun deposit(@RequestBody request: DepositRequest): Response<DepositResponse> {
        return transactionService.deposit(request.toUlid, request.toAccountId, request.value)

    }

    @PostMapping("/transfer")
    fun transfer(@RequestBody request: TransferRequest): Response<TransferResponse> {

        return transactionService.transfer(request.fromUlid, request.fromAccountId, request.toAccountId, request.value)

    }

}