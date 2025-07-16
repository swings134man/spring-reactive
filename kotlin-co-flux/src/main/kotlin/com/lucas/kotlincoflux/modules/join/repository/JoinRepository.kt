package com.lucas.kotlincoflux.modules.join.repository

import com.lucas.kotlincoflux.modules.board.model.Board

interface JoinRepository {

    suspend fun findByBoardsWithAccount(): List<Board> // Board All 조회 + Account JOIN
}