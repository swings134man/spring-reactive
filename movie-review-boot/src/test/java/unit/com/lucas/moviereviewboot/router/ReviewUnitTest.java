package com.lucas.moviereviewboot.router;

import com.lucas.moviereviewboot.domain.Review;
import com.lucas.moviereviewboot.exceptionHandler.GlobalErrorHandler;
import com.lucas.moviereviewboot.handler.ReviewHandler;
import com.lucas.moviereviewboot.repository.ReviewReactiveRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Review Router Unit Test
 */
@WebFluxTest
@ContextConfiguration(classes = {ReviewRouter.class, ReviewHandler.class, GlobalErrorHandler.class}) // Bean 주입
@AutoConfigureWebTestClient
class ReviewUnitTest {

    @MockBean
    private ReviewReactiveRepository repository;

    @Autowired
    private WebTestClient webTestClient;

    static final String REVIEWS_INFO_URL = "/api/reviews";

    @Test
    void addReview() {
        var data = new Review(null, 1L, "Awesome Movie", 9.0);

        when(repository.save(isA(Review.class)))
                .thenReturn(Mono.just(new Review( "abc", 1L, "Awesome Movie", 9.0)));

        webTestClient
                .post()
                .uri(REVIEWS_INFO_URL)
                .bodyValue(data)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Review.class)
                .consumeWith(response -> {
                    var review = response.getResponseBody();
                    assert review != null;
                    assertNotNull(review.getReviewId());
                });
    }

    @Test
    @DisplayName("리뷰 추가 Exception")
    void addReview_exception() {
        var data = new Review(null, null, "Awesome Movie", -9.0);

        when(repository.save(isA(Review.class)))
                .thenReturn(Mono.just(new Review( "abc", 1L, "Awesome Movie", 9.0)));

        webTestClient
                .post()
                .uri(REVIEWS_INFO_URL)
                .bodyValue(data)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(String.class)
                .isEqualTo("rating.movieInfoId: must not ne null,rating.negative : rating is negative and please pass a non-negative value");
    }

    @Test
    @DisplayName("모든 리뷰 조회")
    void getAllReviews() {
        var data = List.of(
                new Review(null, 1L, "Awesome Movie", 9.1),
                new Review(null, 2L, "Awesome Movie2", 9.2)
                ,new Review(null, 3L, "Awesome Movie3", 9.3)
        );

        when(repository.findAll()).thenReturn(Flux.fromIterable(data));

        webTestClient.get()
                .uri(REVIEWS_INFO_URL)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Review.class)
                .value(reviews -> {
                    assertEquals(3, reviews.size());
                });
    }

    @Test
    @DisplayName("리뷰 수정")
    void updateReview() {
        var entity = new Review(null, 1L, "Awesome Movie", 9.1);

        // 기존 Entity Data
        when(repository.findById((String) any())).thenReturn(Mono.just(new Review("abc", 1L, "Not Awesome Movie", 1.1)));
        // 수정된 Entity Data
        when(repository.save(isA(Review.class))).thenReturn(Mono.just(new Review("abc", 1L, "Awesome Movie", 9.1)));

        webTestClient.put()
                .uri(REVIEWS_INFO_URL + "/{id}", "abc")
                .bodyValue(entity)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Review.class)
                .consumeWith(reviewEntityExchangeResult -> {
                    var result = reviewEntityExchangeResult.getResponseBody();

                    assert result != null;
                    System.out.println("result = " + result);
                    assertEquals(9.1, result.getRating());
                    assertEquals("Awesome Movie", result.getComment());
                });
    }

    @Test
    @DisplayName("리뷰 수정 - Exception")
    void updateReview_exception() {
        var entity = new Review(null, 1L, "Awesome Movie", 9.1);

        // 수정된 Entity Data
        when(repository.save(isA(Review.class))).thenReturn(Mono.just(new Review("abc", 1L, "Awesome Movie", 9.1)));
        // 기존 Entity Data
        when(repository.findById((String) any())).thenReturn(Mono.empty());

        webTestClient.put()
                .uri(REVIEWS_INFO_URL + "/{id}", "bbb")
                .bodyValue(entity)
                .exchange()
                .expectStatus().isNotFound();
//                .expectBody(String.class)
//                .isEqualTo("Review Not Found for the given Review id bbb");
    }

    @Test
    @DisplayName("리뷰 삭제")
    void deleteReview() {
        var id = "abc";
        when(repository.findById((String) any())).thenReturn(Mono.just(new Review("abc", 1L, "Not Awesome Movie", 1.1)));
        when(repository.deleteById((String) any())).thenReturn(Mono.empty());

        webTestClient
                .delete()
                .uri(REVIEWS_INFO_URL + "/{id}", id)
                .exchange()
                .expectStatus().isNoContent();
    }
}