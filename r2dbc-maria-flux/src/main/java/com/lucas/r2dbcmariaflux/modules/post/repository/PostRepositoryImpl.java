package com.lucas.r2dbcmariaflux.modules.post.repository;

import com.lucas.r2dbcmariaflux.modules.post.domain.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * @package : com.lucas.r2dbcmariaflux.modules.post.repository
 * @name : PostRepositoryImpl.java
 * @date : 2025. 2. 19. 오후 4:35
 * @author : lucaskang(swings134man)
 * @Description: Interface 구현체, 즉 사용자 정의 Query 를 사용하기 위한 구현체
**/
@Repository
@RequiredArgsConstructor
@Slf4j
public class PostRepositoryImpl implements PostCustomRepository{

    private final DatabaseClient client;

    @Override
    public Flux<Post> findByPostLimit(int limit) {
        return client.sql("SELECT * FROM post LIMIT :limit")
                .bind("limit", limit)
//                .map((row, rowMetadata) -> Post.builder()
//                        .id(row.get("id", Long.class))
//                        .title(row.get("title", String.class))
//                        .content(row.get("content", String.class))
//                        .authorId(row.get("author_id", Long.class))
//                        .build()) // 해당 방법은 createdAt, updatedAt 가 null 로 들어가는 문제가 있음
                .map((row, data) -> {
                    Post post = Post.builder()
                            .id(row.get("id", Long.class))
                            .title(row.get("title", String.class))
                            .content(row.get("content", String.class))
                            .authorId(row.get("author_id", Long.class))
                            .build();

                    post.setCreatedAt(row.get("created_at", java.time.LocalDateTime.class));
                    post.setUpdatedAt(row.get("updated_at", java.time.LocalDateTime.class));

                    return post;
                })
                .all();
    }
}
