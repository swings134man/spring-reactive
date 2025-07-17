package com.lucas.kotlincoflux.modules.join.service

import com.lucas.kotlincoflux.modules.account.model.Account
import com.lucas.kotlincoflux.modules.account.service.AccountService
import com.lucas.kotlincoflux.modules.board.model.Board
import com.lucas.kotlincoflux.modules.board.service.BoardService
import com.lucas.kotlincoflux.modules.join.repository.JoinRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * JoinService.kt: Board + Account 연관관계 샘플 서비스
 *
 * @author: lucaskang(swings134man)
 * @since: 2025. 7. 16. 오후 11:21
 * @description: R2DBC 에서 join 을 사용한 연관관계 샘플 서비스
 */
@Service
class JoinService(
    private val boardService: BoardService,
    private val accountService: AccountService,
    private val joinRepository: JoinRepository
) {


    // ------------------------------------------------ OneToMany ------------------------------------------------

    // Board 기반조회 (N) Account (1) - 각각조회후 수동 set
    @Transactional(readOnly = true)
    suspend fun findByBoardWithAccount(boardId: Long): Board? {
        val board = boardService.getBoardById(boardId)
        val account = board?.korId?.let { accountService.getAccountById(it) } // board 가 null 이 아니라면 account 조회
        board?.account = account // board 의 account(@Transient) 필드에 account 설정

        return board
    }

    // Board 조회 All - JOIN Query (Custom) 사용
    @Transactional(readOnly = true)
    suspend fun findAllBoardsWithAccounts(): List<Board> {
        return joinRepository.findByBoardsWithAccount()
    }

    // ------------------------------------------------ ManyToOne ------------------------------------------------

    // Account 기반 조회 (1) Board (N) - ManyToOne : 각각조회 후 수동 set
    @Transactional(readOnly = true)
    suspend fun findByAccountWithBoards(accountId: Long): Account? {
        val account = accountService.getAccountById(accountId) ?: return null // Account 조회
        val boards = account.let { boardService.getBoardsByKorId(accountId) }  // Account 의 korId 로 Board 조회
        account.boards = boards // Account 의 boards(@Transient) 필드에 Board 리스트 설정

        return account
    }

}