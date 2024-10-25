package com.lucas.moviemainboot.client;

import com.lucas.moviemainboot.entity.MovieInfo;
import com.lucas.moviemainboot.exception.MoviesInfoClientException;
import com.lucas.moviemainboot.exception.MoviesInfoServerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * movies-info Module 과의 Rest 통신을 위한 WebClient 설정
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MoviesInfoRestClient {

    private final WebClient webClient;

    @Value("${restClient.moviesInfoUrl}")
    private String moviesInfoUrl;

    public Mono<MovieInfo> retrieveMovieInfo(String movieId) {
        var url = moviesInfoUrl.concat("/{id}");

        return webClient.get()
                .uri(url, movieId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    log.info("status code: {}", clientResponse.statusCode());

                    // Not Found(404) 일 경우
                    if(clientResponse.statusCode().equals(HttpStatus.NOT_FOUND)){
                        return Mono.error(new MoviesInfoClientException("MovieInfo Not Found by MoviesId: " + movieId, clientResponse.statusCode().value()));
                    }

                    // 404 가 아닌 4xx 일 경우
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(response -> Mono.error(new MoviesInfoClientException(response, clientResponse.statusCode().value())));
                })
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
                    log.info("status code: {}", clientResponse.statusCode());

                    return clientResponse.bodyToMono(String.class)
                            .flatMap(response -> Mono.error(new MoviesInfoServerException("MoviesInfo Server Error: " + response)));
                })
                .bodyToMono(MovieInfo.class)
                .log();
    }
}
