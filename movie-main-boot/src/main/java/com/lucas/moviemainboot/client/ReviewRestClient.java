package com.lucas.moviemainboot.client;

import com.lucas.moviemainboot.entity.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

/**
 * movies-review Module 과의 Rest 통신을 위한 WebClient 설정
 */
@Component
@RequiredArgsConstructor
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
                .bodyToFlux(Review.class);
    }
}
