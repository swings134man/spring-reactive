package com.lucas.moviemainboot.client;

import com.lucas.moviemainboot.entity.MovieInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * movies-info Module 과의 Rest 통신을 위한 WebClient 설정
 */
@Component
@RequiredArgsConstructor
public class MoviesInfoRestClient {

    private final WebClient webClient;

    @Value("${restClient.moviesInfoUrl}")
    private String moviesInfoUrl;

    public Mono<MovieInfo> retrieveMovieInfo(String movieId) {
        var url = moviesInfoUrl.concat("/{id}");

        return webClient.get()
                .uri(url, movieId)
                .retrieve()
                .bodyToMono(MovieInfo.class)
                .log();
    }
}
