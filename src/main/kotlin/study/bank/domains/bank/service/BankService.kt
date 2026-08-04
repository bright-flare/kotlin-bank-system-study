package study.bank.domains.bank.service

import com.github.f4b6a3.ulid.UlidCreator
import org.slf4j.Logger
import org.springframework.stereotype.Service
import study.bank.common.exception.CustomException
import study.bank.common.exception.ErrorCode
import study.bank.common.logging.Logging
import study.bank.common.transaction.Transactional
import study.bank.domains.bank.repository.BankAccountRepository
import study.bank.domains.bank.repository.BankUserRepository
import study.bank.types.dto.Response
import study.bank.types.dto.ResponseProvider
import study.bank.types.entity.Account
import java.math.BigDecimal
import java.lang.Math.random

@Service
class BankService (
    private val transaction: Transactional,
    private val bankUserRepository: BankUserRepository,
    private val bankUserAccountRepository: BankAccountRepository,
    private val logger: Logger = Logging.getLogger(BankService::class.java),
){

    fun createAccount(ulid: String): Response<String> = Logging.logFor(logger) { log ->

        log["userUlid"] = ulid
        transaction.run {

            val user = bankUserRepository.findByUlid(ulid) ?: throw RuntimeException("Ulid was not found")
            val ulid = UlidCreator.getUlid().toString()
            val accountNumber = generateRandomAccountNumber()

            val account = Account(
                ulid = ulid,
                user = user,
                accountNumber = accountNumber,
            )

            bankUserAccountRepository.save(account).runCatching {
                throw CustomException(ErrorCode.FAILED_TO_SAVE_DATA)
            }

            try {
                bankUserAccountRepository.save(account)
            } catch (e: Exception) {
                throw CustomException(ErrorCode.FAILED_TO_SAVE_DATA)
            }

        }

        return@logFor ResponseProvider.success("success")
    }

    fun balance(userUlid: String, accountUlid: String): Response<BigDecimal> = Logging.logFor(logger) { log ->

        log["userUlid"] = userUlid
        log["accountUlid"] = accountUlid
        transaction.run {

        }

    }

    fun removeAccount(userUlid: String, accountUlid: String): Response<String> = Logging.logFor(logger) { log ->

        log["userUlid"] = userUlid
        log["accountUlid"] = accountUlid
        transaction.run {

        }

        return@logFor ResponseProvider.success("success")
    }

    private fun generateRandomAccountNumber(): String {

        val bankCode = "003"
        val section = "12"
        val random = random().toString()

        return "$bankCode-$section-$random"

    }


}