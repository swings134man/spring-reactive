package com.lucas.r2dbcmariaflux.modules.post.repository;

import com.lucas.r2dbcmariaflux.modules.post.domain.Post;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface PostRepository extends R2dbcRepository<Post, Long>, PostCustomRepository {

    // @Query 사용 - Method 에 대한 impl 필요 없음
    @Query("SELECT * FROM post WHERE id BETWEEN :start AND :end")
    Flux<Post> findPostByIdRange(Long start, Long end);
}
