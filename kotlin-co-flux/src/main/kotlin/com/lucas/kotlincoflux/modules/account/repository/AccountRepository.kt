package com.lucas.kotlincoflux.modules.account.repository

import com.lucas.kotlincoflux.modules.account.model.Account
import org.springframework.data.r2dbc.repository.R2dbcRepository

interface AccountRepository: R2dbcRepository<Account, Long>, AccountCustomRepository {
}