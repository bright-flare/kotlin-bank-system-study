package study.bank.domains.transactions.presentation

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import study.bank.domains.transactions.application.TransactionService
import study.bank.domains.transactions.presentation.dto.DepositResponse
import study.bank.domains.transactions.presentation.dto.TransferResponse
import study.bank.types.dto.Response
import java.math.BigDecimal

@RestController
@RequestMapping("/api/v1/transactions")
class TransactionController(
    private val transactionService: TransactionService,
) {

    @PostMapping("/deposit")
    fun deposit(value: BigDecimal): Response<DepositResponse> {

        return transactionService.deposit()

    }

    @PostMapping("/transfer")
    fun transfer(value: BigDecimal): Response<TransferResponse> {

        return transactionService.transfer()

    }

}