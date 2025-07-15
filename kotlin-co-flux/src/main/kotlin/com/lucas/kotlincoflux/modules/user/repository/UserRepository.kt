package com.lucas.kotlincoflux.modules.user.repository

import com.lucas.kotlincoflux.modules.user.model.User
import org.springframework.data.r2dbc.repository.R2dbcRepository

interface UserRepository: R2dbcRepository<User, Long>, UserCustomRepository{
}