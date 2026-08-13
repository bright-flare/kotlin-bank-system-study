package study.bank.domains.transactions.application

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import study.bank.common.cache.RedisClient
import study.bank.common.cache.RedisKeyProvider
import study.bank.domains.transactions.domain.repository.TransactionAccountRepository
import study.bank.domains.transactions.domain.repository.TransactionUserRepository
import study.bank.domains.transactions.presentation.dto.DepositResponse
import study.bank.types.dto.ResponseProvider
import java.math.BigDecimal

@Service
class TransactionService(
    private val transactionAccountRepository: TransactionAccountRepository,
    private val transactionUserRepository: TransactionUserRepository,
    private val redisClient: RedisClient,
) {

    @Transactional
    fun deposit(ulid: String, accountId: String, value: BigDecimal) {

        val key = RedisKeyProvider.bankMutexKey(ulid, accountId)

        redisClient.invokeWithMutex(key) {

            val user = transactionUserRepository.findUserByUlid(ulid)
            val account = transactionAccountRepository.findByUlidAndUser(accountId, user)

            account.deposit(value)

            transactionAccountRepository.save(account)

            ResponseProvider.success(DepositResponse(afterBalance = account.balance))

        }
    }

    @Transactional
    fun transfer(fromUlid: String, fromAccountId: String, toAccountId: String, value: BigDecimal) {

        val key = RedisKeyProvider.bankMutexKey(ulid = fromUlid, accountUlid = fromAccountId)

        redisClient.invokeWithMutex(key) {

        }
    }
}