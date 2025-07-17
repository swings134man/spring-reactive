package com.lucas.kotlincoflux.modules.board.service

import com.lucas.kotlincoflux.commons.logger
import com.lucas.kotlincoflux.modules.board.model.Board
import com.lucas.kotlincoflux.modules.board.repository.BoardRepository
import com.lucas.kotlincoflux.utils.toLocalDateTime
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

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

    // ------------------------------------------ Custom Query Methods ------------------------------------------
    /**
     * @author: lucaskang(swings134man)
     * @since: 2025. 7. 15. 오전 1:58
     * @description: 게시글 생성일을 기준으로, 특정 날짜 범위에 해당하는 게시글을 조회하는 메서드
     */
    @Transactional(readOnly = true)
    suspend fun findByBoardDateRange(startDate: String, endDate: String): List<Board> {
        return boardRepository.findByBoardDateRange(startDate.toLocalDateTime(), endDate.toLocalDateTime())
    }

    @Transactional(readOnly = true)
    suspend fun getBoardsByKorId(korId: Long): List<Board>? {
        return boardRepository.findByKorId(korId)
    }
}// class