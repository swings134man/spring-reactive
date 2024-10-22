package com.lucas.moviereviewboot.router;

import com.lucas.moviereviewboot.domain.Review;
import com.lucas.moviereviewboot.repository.ReviewReactiveRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
                new Review(null, 1L, "Awesome Movie", 9.0),
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


}//class