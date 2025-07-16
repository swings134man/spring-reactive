package com.lucas.kotlincoflux.modules.account.service

import com.lucas.kotlincoflux.modules.account.model.Account
import com.lucas.kotlincoflux.modules.account.repository.AccountRepository
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountService(
    private val accountRepository: AccountRepository
) {


    @Transactional(readOnly = true)
    suspend fun getAllAccounts():  List<Account> {
        return accountRepository.findAll()
            .collectList()
            .awaitSingleOrNull() ?: emptyList()
    }

    @Transactional(readOnly = true)
    suspend fun getAccountById(id: Long): Account? {
        return accountRepository.findById(id)
            .awaitSingleOrNull()
    }

}