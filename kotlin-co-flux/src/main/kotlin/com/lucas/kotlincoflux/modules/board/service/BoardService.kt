package com.lucas.kotlincoflux.modules.board.service

import com.lucas.kotlincoflux.commons.logger
import com.lucas.kotlincoflux.modules.board.model.Board
import com.lucas.kotlincoflux.modules.board.repository.BoardRepository
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * BoardService.kt:
 *
 * @author: lucaskang(swings134man)
 * @since: 2025. 7. 13. 오전 1:43
 * @description: 
 */
@Service
class BoardService(
    private val boardRepository: BoardRepository
) {

    val logger = logger()

    @Transactional
    suspend fun save(board: Board): Board {
        return if (board.id == null) {
            createBoard(board)
        } else {
            updateBoard(board.id!!, board)
        }
    }

    private suspend fun createBoard(board: Board): Board {
        return boardRepository.save(board)
            .awaitSingle()
    }

    private suspend fun updateBoard(id: Long, updatedBoard: Board): Board {
        return if (boardRepository.existsById(id).awaitSingleOrNull() == true) {
            updatedBoard.id = id
            boardRepository.save(updatedBoard).awaitSingle()
        } else {
            throw IllegalArgumentException("Board with id $id not found")
        }
    }

    @Transactional(readOnly = true)
    suspend fun getAllBoards(): List<Board> {
        return boardRepository.findAll()
            .collectList()
            .awaitSingle()
    }

    @Transactional(readOnly = true)
    suspend fun getBoardById(id: Long): Board? {
        return boardRepository.findById(id)
            .awaitSingleOrNull()
    }

    @Transactional
    suspend fun deleteBoard(id: Long) {
        if (boardRepository.existsById(id).awaitSingleOrNull() == true) {
            boardRepository.deleteById(id).awaitSingle()
        } else {
            throw IllegalArgumentException("Board with id $id not found")
        }
    }
}// class