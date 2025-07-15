package com.lucas.kotlincoflux.rest

import com.lucas.kotlincoflux.modules.user.model.User
import com.lucas.kotlincoflux.modules.user.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService
) {


    @PostMapping
    suspend fun createUser(@RequestBody user: User): ResponseEntity<User> {
        val createdUser = userService.save(user)
        return ResponseEntity.ok(createdUser)
    }

    @GetMapping
    suspend fun getAllUsers(): ResponseEntity<List<User>> {
        val users = userService.getAllUsers()
        return ResponseEntity.ok(users)
    }

    @GetMapping("/{id}")
    suspend fun getUserById(@PathVariable id: Long): ResponseEntity<User?> {
        val user = userService.getUserById(id)
        return if (user != null) ResponseEntity.ok(user)
        else ResponseEntity.notFound().build()
    }

    // ------------------------------------------ Custom Query Methods ------------------------------------------
    @GetMapping("/limit")
    suspend fun findByBoardOffsetAndLimit(
        @RequestParam id: Long?,
        @RequestParam offset: Int,
        @RequestParam limit: Int
    ): ResponseEntity<List<User>> {
        val users = userService.findByBoardOffsetAndLimit(id, offset, limit)
        return ResponseEntity.ok(users)
    }

}