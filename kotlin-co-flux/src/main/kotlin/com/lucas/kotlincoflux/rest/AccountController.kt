package com.lucas.kotlincoflux.rest

import com.lucas.kotlincoflux.modules.account.model.Account
import com.lucas.kotlincoflux.modules.account.service.AccountService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/account")
class AccountController(
    private val accountService: AccountService
) {


    @GetMapping
    suspend fun getAllAccounts(): ResponseEntity<List<Account>> {
        val result = accountService.getAllAccounts()
        return ResponseEntity.ok(result)
    }

    @GetMapping("/{id}")
    suspend fun getAccountById(@PathVariable id: Long): ResponseEntity<Account?> {
        val account = accountService.getAccountById(id)
        return if (account != null) ResponseEntity.ok(account)
        else ResponseEntity.notFound().build()
    }

}