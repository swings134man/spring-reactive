package com.lucas.kotlincoflux.modules.user.service

import com.lucas.kotlincoflux.commons.logger
import com.lucas.kotlincoflux.modules.user.model.User
import com.lucas.kotlincoflux.modules.user.repository.UserRepository
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository
) {

    val logger = logger()

    @Transactional
    suspend fun save(user: User): User {
        return if (user.id == null) {
            createUser(user)
        } else {
            updateUser(user.id!!, user)
        }
    }

    private suspend fun createUser(user: User): User {
        return userRepository.save(user)
            .awaitSingle()
    }

    private suspend fun updateUser(id: Long, updatedUser: User): User {
        return if (userRepository.existsById(id).awaitSingleOrNull() == true) {
            updatedUser.id = id
            userRepository.save(updatedUser).awaitSingle()
        } else {
            throw IllegalArgumentException("User with id $id not found")
        }
    }


    @Transactional(readOnly = true)
    suspend fun getAllUsers(): List<User> {
        return userRepository.findAll()
            .collectList()
            .awaitSingleOrNull() ?: emptyList()
    }

    @Transactional(readOnly = true)
    suspend fun getUserById(id: Long): User? {
        return userRepository.findById(id)
            .awaitSingleOrNull()
    }

    // ------------------------------------------ Custom Query Methods ------------------------------------------
    @Transactional(readOnly = true)
    suspend fun findByBoardOffsetAndLimit(
        id: Long?,
        offset: Int,
        limit: Int
    ): List<User> {
        return userRepository.findByBoardOffsetAndLimit(id, offset, limit)
    }

}