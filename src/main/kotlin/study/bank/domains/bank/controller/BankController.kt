package study.bank.domains.bank.controller

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import study.bank.domains.bank.service.BankService
import study.bank.types.dto.Response
import java.math.BigDecimal

@RestController
@RequestMapping("/api/v1/bank")
class BankController(
    private val bankService: BankService
) {

    @PostMapping("/create/{ulid}")
    fun handle(
        @PathVariable("ulid", required = true) ulid: String,
    ) : Response<String> {
        return bankService.createAccount(ulid)
    }

    @GetMapping("/balance/{userUlid}/{accountUlid}")
    fun balance(
        @PathVariable("userUlid", required = true) userUlid: String,
        @PathVariable("accountUlid", required = true) accountUlid: String,
    ): Response<BigDecimal> {
        return bankService.balance(userUlid = userUlid, accountUlid = accountUlid)
    }

    @DeleteMapping("/balance/{userUlid}/{accountUlid}")
    fun removeAccount(
        @PathVariable("userUlid", required = true) userUlid: String,
        @PathVariable("accountUlid", required = true) accountUlid: String,
    ): Response<String> {
        return bankService.removeAccount(userUlid = userUlid, accountUlid = accountUlid)
    }
}