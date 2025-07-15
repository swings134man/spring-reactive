package com.lucas.kotlincoflux.modules.user.repository

import com.lucas.kotlincoflux.modules.user.model.User

interface UserCustomRepository {

    suspend fun findByBoardOffsetAndLimit(id: Long? ,offset: Int, limit: Int): List<User>
}