package study.bank.domains.bank.service

import org.slf4j.Logger
import org.slf4j.event.LoggingEvent
import org.springframework.stereotype.Service
import study.bank.common.logging.Logging
import study.bank.common.transaction.Transactional
import study.bank.domains.bank.repository.BankAccountRepository
import study.bank.domains.bank.repository.BankUserRepository
import study.bank.types.dto.Response
import java.math.BigDecimal

@Service
class BankService (
    private val transaction: Transactional,
    private val userRepository: BankUserRepository,
    private val userAccountRepository: BankAccountRepository,
    private val logger: Logger = Logging.getLogger(BankService::class.java),
){

    fun createAccount(ulid: String): Response<String> = Logging.logFor(logger) { log ->

        log["userUlid"] = ulid
        transaction.run {

        }
    }

    fun balance(userUlid: String, accountUlid: String): Response<BigDecimal> = Logging.logFor(logger) { log ->

        log["userUlid"] = userUlid
        log["accountUlid"] = accountUlid
        transaction.run {

        }
    }

    fun removeAccount(userUlid: String, accountUlid: String): Response<BigDecimal> = Logging.logFor(logger) { log ->

        log["userUlid"] = userUlid
        log["accountUlid"] = accountUlid
        transaction.run {

        }
    }

}