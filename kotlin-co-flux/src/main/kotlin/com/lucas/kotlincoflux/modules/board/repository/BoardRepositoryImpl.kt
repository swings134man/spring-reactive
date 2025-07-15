package com.lucas.kotlincoflux.modules.board.repository

import com.lucas.kotlincoflux.modules.board.model.Board
import org.springframework.r2dbc.core.DatabaseClient
import java.time.LocalDateTime
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Repository

/**
 * BoardRepositoryImpl.kt: BoardCustomRepository 구현 클래스 (Implementation Class)
 *
 * @author: lucaskang(swings134man)
 * @since: 2025. 7. 15. 오전 1:38
 * @description:
 */
@Repository
class BoardRepositoryImpl(
    private val client: DatabaseClient
): BoardCustomRepository {

    /**
     * @author: lucaskang(swings134man)
     * @since: 2025. 7. 15. 오전 1:38
     * @description: 게시글 생성일을 기준으로, 특정 날짜 범위에 해당하는 게시글을 조회하는 메서드
     */
    override suspend fun findByBoardDateRange(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<Board> {
        return client.sql(
            """
        SELECT * FROM board 
        WHERE created_at BETWEEN :startDate AND :endDate
        ORDER BY created_at DESC
        """.trimIndent()
        )
            .bind("startDate", startDate)
            .bind("endDate", endDate)
            .map { row, _ ->
                Board(
                    id = row.get("id", Long::class.java),
                    title = row.get("title", String::class.java) ?: "",
                    content = row.get("content", String::class.java) ?: ""
                ).apply {
                    createdAt = row.get("created_at", LocalDateTime::class.java)
                    updatedAt = row.get("updated_at", LocalDateTime::class.java)
                }
            }
            .all()
            .collectList()
            .awaitSingleOrNull() ?: emptyList()
    }
}