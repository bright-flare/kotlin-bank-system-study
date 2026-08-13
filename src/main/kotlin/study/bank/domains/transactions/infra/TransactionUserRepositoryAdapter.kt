package study.bank.domains.transactions.infra

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import study.bank.domains.transactions.domain.repository.TransactionAccountRepository
import study.bank.domains.transactions.domain.repository.TransactionUserRepository
import study.bank.types.entity.Account
import study.bank.types.entity.User

@Repository
class TransactionUserRepositoryAdapter(
    private val transactionsUserJpaRepository: TransactionsUserJpaRepository,
) : TransactionUserRepository {

    override fun findUserByUlid(ulid: String): User {
        return transactionsUserJpaRepository.findByUlid(ulid)
    }

}