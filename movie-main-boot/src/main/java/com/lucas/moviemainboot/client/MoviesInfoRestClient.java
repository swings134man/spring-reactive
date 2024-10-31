package com.lucas.moviemainboot.client;

import com.lucas.moviemainboot.entity.MovieInfo;
import com.lucas.moviemainboot.exception.MoviesInfoClientException;
import com.lucas.moviemainboot.exception.MoviesInfoServerException;
import com.lucas.moviemainboot.utils.RetryUtil;
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
        // Util 로 분리
//        var retrySpec = Retry.fixedDelay(3, Duration.ofSeconds(1))
//                .filter(ex -> ex instanceof MoviesInfoServerException) // MoviesInfoServerException 발생 시 재시도, 다른 Exception 이라면 재시도 X
//                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) ->
//                        // 예외확산: 재시도를 위한 신호 전달
//                        // 재시도 예외 메세지를 출력하는 대신, 예외를 야기한 실제 이슈에 엑세스권한부여(즉 원래 예외 출력)
//                        Exceptions.propagate(retrySignal.failure())
//                );

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
//                .retry(3) // 3번까지 재시도 총 4번 요청
//                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1))) // 3초 간격으로 3번 재시도: 기존의 Server Error Msg 가 변경됨
                .retryWhen(RetryUtil.retrySpec())
                .log();
    }


    public Mono<Void> deleteInfoById(String movieId) {
        return webClient.delete()
                .uri(moviesInfoUrl.concat("/{id}"), movieId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    log.info("status code: {}", clientResponse.statusCode());

                    if(clientResponse.statusCode().equals(HttpStatus.NOT_FOUND)) {
                        return Mono.error(new MoviesInfoClientException("MovieInfo Not Found By movieId: " + movieId, clientResponse.statusCode().value()));
                    }

                    // 4xx 에러여야 하는데 200 으로 떨어짐?
//                    return clientResponse.bodyToMono(String.class)
//                            .flatMap(response -> Mono.error(new MoviesInfoClientException(response, clientResponse.statusCode().value())));
                    return Mono.error(new MoviesInfoClientException("MovieInfo Client Error", clientResponse.statusCode().value()));
                })
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
                    log.info("status code: {}", clientResponse.statusCode());
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(response -> Mono.error(new MoviesInfoServerException("movieInfo Server Error: " + response)));
                })
                .bodyToMono(Void.class);

    }
}
