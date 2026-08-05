package study.bank.domains.bank.service

import com.github.f4b6a3.ulid.UlidCreator
import org.slf4j.Logger
import org.springframework.stereotype.Service
import study.bank.common.exception.CustomException
import study.bank.common.exception.ErrorCode
import study.bank.common.logging.Logging
import study.bank.common.logging.Logging.logFor
import study.bank.common.transaction.Transactional
import study.bank.domains.bank.repository.BankAccountRepository
import study.bank.domains.bank.repository.BankUserRepository
import study.bank.types.dto.Response
import study.bank.types.dto.ResponseProvider
import study.bank.types.entity.Account
import java.math.BigDecimal
import java.lang.Math.random
import java.time.LocalDateTime

@Service
class BankService (
    private val transaction: Transactional,
    private val bankUserRepository: BankUserRepository,
    private val bankAccountRepository: BankAccountRepository,
    private val logger: Logger = Logging.getLogger(BankService::class.java),
){

    fun createAccount(ulid: String): Response<String> = logFor(logger) { log ->

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

            try {
                bankAccountRepository.save(account)
            } catch (e: Exception) {
                throw CustomException(ErrorCode.FAILED_TO_SAVE_DATA)
            }

        }

        return@logFor ResponseProvider.success("success")
    }

    fun balance(userUlid: String, accountUlid: String): Response<BigDecimal> = logFor(logger) { log ->

        log["userUlid"] = userUlid
        log["accountUlid"] = accountUlid

        return@logFor transaction.run {

            val account = bankAccountRepository.findByUlid(accountUlid) ?: throw CustomException(ErrorCode.FAILED_TO_SAVE_DATA)
            if (account.user.ulid == userUlid) {
                throw CustomException(ErrorCode.MISMATCH_ACCOUNT)
            }

            ResponseProvider.success(account.balance)
        }

    }

    fun removeAccount(userUlid: String, accountUlid: String): Response<String> = logFor(logger) { log ->

        log["userUlid"] = userUlid
        log["accountUlid"] = accountUlid
        transaction.run {
            val user = bankUserRepository.findByUlid(userUlid) ?: throw CustomException(ErrorCode.FAILED_TO_SAVE_DATA)
            val account = bankAccountRepository.findByUlid(accountUlid) ?: throw CustomException(ErrorCode.FAILED_TO_SAVE_DATA)
            if (account.user.ulid == user.ulid) throw CustomException(ErrorCode.MISMATCH_ACCOUNT)
            if (account.balance.compareTo(BigDecimal.ZERO) != 0) throw CustomException(ErrorCode.MISMATCH_ACCOUNT)

            val updateAccount = account.copy(
                isDeleted = true,
                deletedAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
            )

            bankAccountRepository.save(updateAccount)

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