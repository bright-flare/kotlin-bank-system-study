package study.bank.domains.transactions.infra

import org.springframework.data.jpa.repository.JpaRepository
import study.bank.types.entity.Account
import study.bank.types.entity.User

interface TransactionsAccountJpaRepository : JpaRepository<Account, String> {

    fun findByUlidAndUser(ulid: String, user: User): Account?
    fun findByUlid(ulid: String): Account?

}