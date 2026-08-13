package study.bank.domains.transactions.domain.repository

import study.bank.common.exception.CustomException
import study.bank.common.exception.ErrorCode
import study.bank.types.entity.Account
import study.bank.types.entity.User

interface TransactionAccountRepository {

    fun findByUlidAndUser(ulid: String, user: User): Account = findByUlidAndUserOrNull(ulid, user)
        ?: throw CustomException(ErrorCode.FAILED_TO_FIND_ACCOUNT)

    fun findByUlidAndUserOrNull(ulid: String, user: User): Account?

    fun findByUlid(ulid: String): Account?

    fun save(account: Account): Account

}