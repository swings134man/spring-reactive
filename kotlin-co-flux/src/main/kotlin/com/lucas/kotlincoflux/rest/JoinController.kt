package com.lucas.kotlincoflux.rest

import com.lucas.kotlincoflux.modules.account.model.Account
import com.lucas.kotlincoflux.modules.board.model.Board
import com.lucas.kotlincoflux.modules.join.service.JoinService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/join")
class JoinController(
    private val joinService: JoinService
) {


    // board 조회 - 각각 조회후 조합 : ManyToOne
    @GetMapping("/board-ac/{id}")
    suspend fun getBoardWithAccountById(@PathVariable id: Long): ResponseEntity<Board> {
        val result = joinService.findByBoardWithAccount(id)

        return if(result != null) ResponseEntity.ok(result)
        else ResponseEntity.notFound().build()
    }

    // board 조회 All - JOIN
    @GetMapping("/boards-ac")
    suspend fun getAllBoardsWithAccounts(): ResponseEntity<List<Board>> {
        val result = joinService.findAllBoardsWithAccounts()
        return ResponseEntity.ok(result)
    }

    // ------------------------------------------------ OneToMany ------------------------------------------------
    // account 기반 조회 - Board (N)
    @GetMapping("/account/{id}")
    suspend fun getAccountWithBoards(@PathVariable id: Long): ResponseEntity<Account?> {
        val result = joinService.findByAccountWithBoards(id)

        return if(result != null) ResponseEntity.ok(result)
        else ResponseEntity.notFound().build()
    }


}