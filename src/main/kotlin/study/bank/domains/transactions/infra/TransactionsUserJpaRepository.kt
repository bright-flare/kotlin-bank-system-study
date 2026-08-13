package study.bank.domains.transactions.infra

import org.springframework.data.jpa.repository.JpaRepository
import study.bank.types.entity.User

interface TransactionsUserJpaRepository : JpaRepository<User, String> {

    fun findByUlid(ulid: String): User

}