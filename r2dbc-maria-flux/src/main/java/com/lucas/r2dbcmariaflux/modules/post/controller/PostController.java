package com.lucas.r2dbcmariaflux.modules.post.controller;

import com.lucas.r2dbcmariaflux.modules.post.domain.Post;
import com.lucas.r2dbcmariaflux.modules.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/posts")
public class PostController {

    private final PostService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<Post>> savePost(@RequestBody Post post) {
        return service.savePost(post)
                .map(savedPost -> ResponseEntity.status(HttpStatus.CREATED).body(savedPost));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Post>> findById(@PathVariable Long id) {
        return service.findByPostId(id)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/all")
    public Mono<ResponseEntity<List<Post>>> findAll() {
        return service.findAllPost()
                .collectList()
                .map(ResponseEntity::ok);
    }

    // ---------------------------------------- Custom Query ----------------------------------------
    @GetMapping("/limit/{limit}")
    public Mono<ResponseEntity<List<Post>>> findPostLimit(@PathVariable("limit") int limit) {
        return service.findByPostLimit(limit)
                .collectList()
                .map(ResponseEntity::ok);
    }

    @GetMapping("/range/{start}/{end}")
    public Mono<ResponseEntity<List<Post>>> findPostByIdRange(@PathVariable Long start, @PathVariable Long end) {
        return service.findPostByIdRange(start, end)
                .collectList()
                .map(ResponseEntity::ok);
    }
}