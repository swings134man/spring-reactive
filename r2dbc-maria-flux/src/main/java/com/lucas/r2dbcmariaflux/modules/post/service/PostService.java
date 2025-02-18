package com.lucas.r2dbcmariaflux.modules.post.service;

import com.lucas.r2dbcmariaflux.modules.post.domain.Post;
import com.lucas.r2dbcmariaflux.modules.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public Mono<Post> savePost(Post post) {
        if(post.getId() == null) {
            return createPost(post);
        } else {
            return updatePost(post);
        }
    }

    private Mono<Post> createPost(Post post) {
        return postRepository.save(post)
                .doOnSuccess(p -> log.info("Post saved: {}", p));
    }

    private Mono<Post> updatePost(Post post) {
        return postRepository.findById(post.getId())
                .flatMap(p -> {
                    p.setId(post.getId());
                    p.setTitle(post.getTitle());
                    p.setContent(post.getContent());
                    p.setAuthorId(post.getAuthorId());
                    return postRepository.save(p);
                })
                .doOnSuccess(p -> log.info("Post updated: {}", p));
    }

    @Transactional(readOnly = true)
    public Flux<Post> findAllPost() {
        return postRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Mono<Post> findByPostId(Long id) {
        return postRepository.findById(id);
    }
}
