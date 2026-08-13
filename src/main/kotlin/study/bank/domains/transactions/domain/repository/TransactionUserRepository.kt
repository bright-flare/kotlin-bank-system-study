package study.bank.domains.transactions.domain.repository

import study.bank.types.entity.User

interface TransactionUserRepository {

    fun findUserByUlid(ulid: String): User

}