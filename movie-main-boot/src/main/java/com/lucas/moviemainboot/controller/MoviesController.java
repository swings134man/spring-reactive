package com.lucas.moviemainboot.controller;

import com.lucas.moviemainboot.client.MoviesInfoRestClient;
import com.lucas.moviemainboot.client.ReviewRestClient;
import com.lucas.moviemainboot.entity.Movie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/movies")
public class MoviesController {

    private final MoviesInfoRestClient moviesInfoRestClient;
    private final ReviewRestClient reviewRestClient;

    /**
     * 영화 1개에 대한 정보,리뷰 조회
     */
    @GetMapping("/{id}")
    public Mono<Movie> retrieveMovieById(@PathVariable("id") String movieId) {

        return moviesInfoRestClient.retrieveMovieInfo(movieId)
                        .flatMap(movieInfo -> {
                            var reviewsListMono = reviewRestClient.retrieveReviews(movieId)
                                                    .collectList();
                            return reviewsListMono.map(reviews -> new Movie(movieInfo, reviews));
                        });
    }

    // DELETE
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteMovieInfoById(@PathVariable("id") String movieId) {
        return moviesInfoRestClient.deleteInfoById(movieId)
                .then(reviewRestClient.deleteReviewsById(movieId))
                .then(Mono.just(ResponseEntity.noContent().build()))
                ;
    }

}
