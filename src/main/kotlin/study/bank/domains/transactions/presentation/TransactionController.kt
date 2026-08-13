package study.bank.domains.transactions.presentation

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import study.bank.domains.transactions.application.TransactionService
import java.math.BigDecimal

@RestController
class TransactionController(
    private val transactionService: TransactionService,
) {

    @PostMapping("/deposit")
    fun deposit(value: BigDecimal) {

    }

}