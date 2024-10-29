package com.lucas.moviemainboot.client;

import com.lucas.moviemainboot.entity.Review;
import com.lucas.moviemainboot.exception.MoviesInfoClientException;
import com.lucas.moviemainboot.exception.MoviesInfoServerException;
import com.lucas.moviemainboot.exception.ReviewsClientException;
import com.lucas.moviemainboot.exception.ReviewsServerException;
import com.lucas.moviemainboot.utils.RetryUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * movies-review Module 과의 Rest 통신을 위한 WebClient 설정
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ReviewRestClient {

    private final WebClient webClient;

    @Value("${restClient.reviewUrl}")
    private String reviewUrl;

    public Flux<Review> retrieveReviews(String movieId) {
        var uri =UriComponentsBuilder.fromHttpUrl(reviewUrl)
                    .queryParam("movieInfoId", movieId)
                    .buildAndExpand().toUriString();

        return webClient.get()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    log.info("status code: {}", clientResponse.statusCode());

                    // Not Found(404) - 특정 ID 를 찾지못할경우 info Module 에서 404 기때문에 해당 서버에서는 처리 X
                    if(clientResponse.statusCode().equals(HttpStatus.NOT_FOUND)){
                        return Mono.empty();
                    }

                    // 404 가 아닌 4xx 일 경우
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(response -> Mono.error(new ReviewsClientException(response)));
                })
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
                    log.info("status code: {}", clientResponse.statusCode());

                    return clientResponse.bodyToMono(String.class)
                            .flatMap(response -> Mono.error(new ReviewsServerException("Review Server Error: " + response)));
                })
                .bodyToFlux(Review.class)
                .retryWhen(RetryUtil.retrySpec());
    }
}
