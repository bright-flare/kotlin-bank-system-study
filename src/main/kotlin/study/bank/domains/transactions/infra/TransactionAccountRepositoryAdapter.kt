package study.bank.domains.transactions.infra

import org.springframework.stereotype.Repository
import study.bank.domains.transactions.domain.repository.TransactionAccountRepository
import study.bank.types.entity.Account
import study.bank.types.entity.User

@Repository
class TransactionAccountRepositoryAdapter(
    val transactionsAccountJpaRepository: TransactionsAccountJpaRepository,
) : TransactionAccountRepository {

    override fun findByUlid(ulid: String): Account? {
        return transactionsAccountJpaRepository.findByUlid(ulid)
    }

    override fun findByUlidAndUserOrNull(
        ulid: String,
        user: User
    ): Account? {
        return transactionsAccountJpaRepository.findByUlidAndUser(ulid, user)
    }

    override fun save(account: Account): Account {
        return transactionsAccountJpaRepository.save(account)
    }
}