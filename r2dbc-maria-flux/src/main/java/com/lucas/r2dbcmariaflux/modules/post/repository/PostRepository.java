package com.lucas.r2dbcmariaflux.modules.post.repository;

import com.lucas.r2dbcmariaflux.modules.post.domain.Post;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface PostRepository extends R2dbcRepository<Post, Long> {

}
