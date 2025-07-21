package com.lucas.kotlincoflux.modules.join.repository

import com.lucas.kotlincoflux.modules.account.model.Account
import com.lucas.kotlincoflux.modules.board.model.Board
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class JoinRepositoryImpl(
    private val client: DatabaseClient
): JoinRepository {


    // Board All 조회 + Account JOIN : ManyToOne
    override suspend fun findByBoardsWithAccount(): List<Board> {
        return client.sql(
            """
            SELECT 
                b.id AS b_id, b.title, b.content, b.kor_id, b.created_at AS b_created_at, b.updated_at AS b_updated_at,
                a.id AS a_id, a.name, a.age, a.is_active, a.created_at AS a_created_at, a.updated_at AS a_updated_at
            FROM board b
            LEFT JOIN kor a ON b.kor_id = a.id
            """
        )
            .map { row, _ ->
                val account = Account(
                    id = row["a_id"] as Long?,
                    name = row["name"] as String,
                    age = row["age"] as Int,
                    isActive = row["is_active"] as Boolean
                ).apply {
                    createdAt = row["a_created_at"] as? LocalDateTime
                    updatedAt = row["a_updated_at"] as? LocalDateTime
                }

                Board(
                    id = row["b_id"] as Long?,
                    title = row["title"] as String,
                    content = row["content"] as String,
                    korId = row["kor_id"] as Long?,
                    account = account
                ).apply {
                    createdAt = row["b_created_at"] as? LocalDateTime
                    updatedAt = row["b_updated_at"] as? LocalDateTime
                }
            }
            .all()
            .collectList()
            .awaitSingleOrNull() ?: emptyList()
    }
}