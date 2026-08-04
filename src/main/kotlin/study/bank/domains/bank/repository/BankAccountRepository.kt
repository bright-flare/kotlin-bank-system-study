package study.bank.domains.bank.repository

import org.springframework.data.jpa.repository.JpaRepository
import study.bank.types.entity.Account

interface BankAccountRepository : JpaRepository<Account, String> {

}