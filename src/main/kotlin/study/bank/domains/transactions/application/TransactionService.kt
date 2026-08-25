package study.bank.domains.transactions.application

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import study.bank.common.cache.RedisClient
import study.bank.common.cache.RedisKeyProvider
import study.bank.common.exception.CustomException
import study.bank.common.exception.ErrorCode
import study.bank.domains.transactions.domain.repository.TransactionAccountRepository
import study.bank.domains.transactions.domain.repository.TransactionUserRepository
import study.bank.domains.transactions.presentation.dto.DepositResponse
import study.bank.domains.transactions.presentation.dto.TransferResponse
import study.bank.types.dto.Response
import study.bank.types.dto.ResponseProvider
import java.math.BigDecimal

@Service
class TransactionService(
    private val transactionAccountRepository: TransactionAccountRepository,
    private val transactionUserRepository: TransactionUserRepository,
    private val redisClient: RedisClient,
) {

    @Transactional
    fun deposit(ulid: String, accountId: String, value: BigDecimal): Response<DepositResponse>? {

        val key = RedisKeyProvider.bankMutexKey(ulid, accountId)

        return redisClient.invokeWithMutex(key) {

            val user = transactionUserRepository.findUserByUlid(ulid)
            val account = transactionAccountRepository.findByUlidAndUser(accountId, user)

            account.deposit(value)

            transactionAccountRepository.save(account)

            ResponseProvider.success(DepositResponse(afterBalance = account.balance))

        }
    }

    @Transactional
    fun transfer(fromUlid: String, fromAccountId: String, toAccountId: String, value: BigDecimal): Response<TransferResponse>? {

        val key = RedisKeyProvider.bankMutexKey(ulid = fromUlid, accountUlid = fromAccountId)

        return redisClient.invokeWithMutex(key) {

            val fromAccount = transactionAccountRepository.findByUlid(ulid = fromUlid) ?: throw CustomException(ErrorCode.FAILED_TO_FIND_ACCOUNT)
            val toAccount = transactionAccountRepository.findByUlid(ulid = fromAccountId) ?: throw CustomException(ErrorCode.FAILED_TO_FIND_ACCOUNT)

            if (fromAccount.user.ulid != fromUlid) {
                throw CustomException(ErrorCode.FAILED_TO_FIND_ACCOUNT)
            } else if (fromAccount.balance < value) {
                throw CustomException(ErrorCode.NOT_ENOUGH_VALUE)
            } else if (value < BigDecimal.ZERO) {
                throw CustomException(ErrorCode.VALUE_MUST_NOT_BE_UNDER_ZERO)
            }

            fromAccount.balance = fromAccount.balance.subtract(value)
            toAccount.balance = toAccount.balance.add(value)

            transactionAccountRepository.save(fromAccount)
            transactionAccountRepository.save(toAccount)

            return@invokeWithMutex ResponseProvider.success(
                TransferResponse(
                    afterFromBalance = fromAccount.balance,
                    afterToBalance = toAccount.balance
                )
            )
        }

    }
}