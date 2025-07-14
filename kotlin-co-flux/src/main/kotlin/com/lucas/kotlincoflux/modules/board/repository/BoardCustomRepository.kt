package com.lucas.kotlincoflux.modules.board.repository

import com.lucas.kotlincoflux.modules.board.model.Board
import java.time.LocalDateTime

/**
 * BoardCustomRepository.kt: 커스텀한 R2DBC Query 를 위한 Interface
 *
 * @author: lucaskang(swings134man)
 * @since: 2025. 7. 15. 오전 1:33
 * @description: 추상화 메서드를 정의하여, impl Class 에서 구현할 수 있도록 한다.
 */
interface BoardCustomRepository {

    suspend fun findByBoardDateRange(startDate: LocalDateTime, endDate: LocalDateTime): List<Board>
}