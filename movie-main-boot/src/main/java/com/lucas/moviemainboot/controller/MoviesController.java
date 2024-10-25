package com.lucas.moviemainboot.controller;

import com.lucas.moviemainboot.entity.MovieInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/movies")
public class MoviesController {

    /**
     * 영화 1개에 대한 정보,리뷰 조회
     */
    @GetMapping("/{id}")
    public Mono<MovieInfo> retrieveMovieById(@PathVariable String movieId) {

        return null;
    }
}
