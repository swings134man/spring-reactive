package com.lucas.kotlincoflux.modules.board.service

import com.lucas.kotlincoflux.modules.board.model.Board
import com.lucas.kotlincoflux.modules.board.repository.BoardRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor

@ActiveProfiles("test")
@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class BoardServiceTest(
    private val boardService: BoardService,
    private val boardRepository: BoardRepository
) : BehaviorSpec({

    extension(SpringExtension)

    beforeEach {
        // 각 테스트 시작 전에 트랜잭션 내에서 데이터 삭제
        runBlocking {
            boardRepository.deleteAll()
        }
    }


    given("board create - Success Test") {
        When("새로운 게시글을 작성하면") {
            then("게시글이 성공적으로 저장되어야 한다") {
                val board = Board(
                    title = "제목입니다",
                    content = "내용입니다"
                )

                val savedBoard = boardService.save(board)

                savedBoard.id shouldBe 1L
                savedBoard.title shouldBe "제목입니다"
                savedBoard.content shouldBe "내용입니다"

                // 데이터베이스에 저장된 게시글 확인
                val results = boardService.getAllBoards()
                println("results: $results")
                results.size shouldBe 1
            }
        }
    }


//    given("board create - Rollback Test") {
//        When("중간에 예외가 나면") {
//            then("데이터는 롤백되어야 한다") {
//
//                println("두 번째 테스트 시작 전 게시글 수: ${boardService.getAllBoards().size}")
//
//                try {
//                    val board = Board(
//                        title = "제목입니다1",
//                        content = "내용입니다1"
//                    )
//
//                    boardRepository.save(board)
//                    println("예외 발생 전 게시글 수: ${boardService.getAllBoards().size}")
//
//                    throw RuntimeException("강제 예외")
//
//                } catch (_: Exception) {
//                    val results = boardService.getAllBoards()
//                    println("예외 발생 후 게시글 수: ${results.size}")
//                    results.size shouldBe 0
//                }
//            }
//        }
//    }

})