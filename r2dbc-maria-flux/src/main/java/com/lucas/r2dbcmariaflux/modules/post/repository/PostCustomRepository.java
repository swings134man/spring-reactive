package com.lucas.r2dbcmariaflux.modules.post.repository;

import com.lucas.r2dbcmariaflux.modules.post.domain.Post;
import reactor.core.publisher.Flux;

/**
 * @package : com.lucas.r2dbcmariaflux.modules.post.repository
 * @name : PostCustomRepository.java
 * @date : 2025. 2. 19. 오후 4:33
 * @author : lucaskang(swings134man)
 * @Description: R2DBC 에서 Query 를 사용하기 위한 interface
**/
public interface PostCustomRepository {

    Flux<Post> findByPostLimit(int limit);

}
