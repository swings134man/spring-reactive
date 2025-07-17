package com.lucas.kotlincoflux.modules.board.repository

import com.lucas.kotlincoflux.modules.board.model.Board
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.r2dbc.repository.R2dbcRepository

/**
 * BoardRepository.kt: Board Repository Base Interface
 *
 * @author: lucaskang(swings134man)
 * @since: 2025. 7. 13. 오전 1:43
 * @description:
 */
interface BoardRepository : R2dbcRepository<Board, Long>, BoardCustomRepository {

    @Query("""
    SELECT b.id, b.title, b.content, b.kor_id, b.created_at, b.updated_at 
    FROM board b 
    WHERE b.kor_id = :korId
    """)
    suspend fun findByKorId(korId: Long): List<Board>?


}