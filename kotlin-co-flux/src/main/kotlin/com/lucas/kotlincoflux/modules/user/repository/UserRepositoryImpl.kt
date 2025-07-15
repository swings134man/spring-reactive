package com.lucas.kotlincoflux.modules.user.repository

import com.lucas.kotlincoflux.modules.user.model.User
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.bind
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
    private val client: DatabaseClient
): UserCustomRepository {



    /**
     * @author: lucaskang(swings134man)
     * @since: 2025. 7. 16. 오전 12:25
     * @description: 특정 ID를 가진 사용자 정보를 조회하고, 오프셋과 제한을 적용하여 결과를 반환하는 메서드
     * - ID 가 null인 경우 모든 사용자 정보를 조회
     */
    override suspend fun findByBoardOffsetAndLimit(
        id: Long?,
        offset: Int,
        limit: Int
    ): List<User> {
        val whereClause = if (id != null) "WHERE id = :id" else ""

        return client.sql(
            """
            SELECT * FROM dsl
            $whereClause
            OFFSET :offset LIMIT :limit
            """.trimIndent()
        )
        .bind("offset", offset)
        .bind("limit", limit)
            .map { row, _ ->
                User(
                    id = row.get("id", Long::class.javaObjectType),
                    name = row.get("name", String::class.java) ?: "",
                    address = row.get("address", String::class.java) ?: ""
                ).apply {
                    createdAt = row.get("created_at", java.time.LocalDateTime::class.java)
                    updatedAt = row.get("updated_at", java.time.LocalDateTime::class.java)
                }
            }
            .all()
            .collectList()
            .awaitSingleOrNull() ?: emptyList()
    }
}