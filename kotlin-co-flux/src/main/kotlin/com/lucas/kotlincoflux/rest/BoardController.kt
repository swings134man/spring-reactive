package com.lucas.kotlincoflux.rest

import com.lucas.kotlincoflux.modules.board.model.Board
import com.lucas.kotlincoflux.modules.board.service.BoardService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/board")
class BoardController(
    private val boardService: BoardService
) {

    @PostMapping("/")
    suspend fun createBoard(@RequestBody board: Board): ResponseEntity<Board> {
        val createdBoard = boardService.save(board)
        return ResponseEntity.ok(createdBoard)
    }


    @GetMapping("/")
    suspend fun getAllBoards(): ResponseEntity<List<Board>> {
        // Logic to fetch all boards
        val boards = boardService.getAllBoards()
        return ResponseEntity.ok(boards)
    }

    @GetMapping("/{id}")
    suspend fun getBoardById(@PathVariable id: Long): ResponseEntity<Board?> {
        val board = boardService.getBoardById(id)

        return if (board != null) ResponseEntity.ok(board)
        else ResponseEntity.notFound().build()
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun deleteBoard(@PathVariable id: Long) {
        boardService.deleteBoard(id)
    }


    // ------------------------------------------ Custom Query Methods ------------------------------------------
    @GetMapping("/date-range")
    suspend fun getBoardsByDateRange(
        @RequestParam startDate: String,
        @RequestParam endDate: String
    ): ResponseEntity<List<Board>> {
        val boards = boardService.findByBoardDateRange(startDate, endDate)
        return ResponseEntity.ok(boards)
    }

}