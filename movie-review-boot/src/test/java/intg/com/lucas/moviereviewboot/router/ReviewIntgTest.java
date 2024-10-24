package com.lucas.moviereviewboot.router;

import com.lucas.moviereviewboot.domain.Review;
import com.lucas.moviereviewboot.repository.ReviewReactiveRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("local")
@AutoConfigureWebTestClient
public class ReviewIntgTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ReviewReactiveRepository repository;

    static final String REVIEWS_INFO_URL = "/api/reviews";

    @BeforeEach
    void setUp() {
        var reviewsList = List.of(
                new Review("abc", 1L, "Awesome Movie", 9.0),
                new Review(null, 1L, "Awesome Movie1", 9.0),
                new Review(null, 2L, "Excellent Movie", 8.0));
        repository.saveAll(reviewsList)
                .blockLast();
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll().block();
    }

    @Test
    void addReview() {
       var data = new Review(null, 1L, "Awesome Movie", 9.0);

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

    // FindAll
    @Test
    @DisplayName("모든 리뷰 조회")
    void findAll() {
        webTestClient
                .get()
                .uri(REVIEWS_INFO_URL)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Review.class)
                .value(reviews -> {
                    assertEquals(3, reviews.size());
                });
    }

    // FindById
    @Test
    void getReviewByMovieInfoId() {
        webTestClient
                .get()
                .uri(uriBuilder -> {
                    return uriBuilder.path(REVIEWS_INFO_URL)
                            .queryParam("movieInfoId", 1)
                            .build();
                })
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Review.class)
                .value(list -> {
                    System.out.println("list = " + list);
                    assertEquals(2, list.size());
                });
    }

    // Update Review
    @Test
    @DisplayName("리뷰 수정")
    void updateReview() {
        //given
        var review = new Review(null, 1L, "Awesome Movie", 9.0);
        var savedReview = repository.save(review).block();
        var reviewUpdate = new Review(null, 1L, "Not an Awesome Movie", 8.0);
        //when
        assert savedReview != null;

        webTestClient
                .put()
                .uri(REVIEWS_INFO_URL+"/{id}", savedReview.getReviewId())
                .bodyValue(reviewUpdate)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Review.class)
                .consumeWith(reviewResponse -> {
                    var updatedReview = reviewResponse.getResponseBody();
                    assert updatedReview != null;
                    System.out.println("updatedReview : " + updatedReview);
                    assertNotNull(savedReview.getReviewId());
                    assertEquals(8.0, updatedReview.getRating());
                    assertEquals("Not an Awesome Movie", updatedReview.getComment());
                });
    }

    // Delete Review
    @Test
    @DisplayName("리뷰 삭제")
    void deleteReview() {
        //given
        var review = new Review(null, 1L, "Awesome Movie", 9.0);
        var savedReview = repository.save(review).block();
        //when
        assert savedReview != null;
        webTestClient
                .delete()
                .uri(REVIEWS_INFO_URL+"/{id}", savedReview.getReviewId())
                .exchange()
                .expectStatus().isNoContent();
    }
}//class