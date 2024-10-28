package com.lucas.moviemainboot.controller;

import com.lucas.moviemainboot.entity.Movie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("local")
@AutoConfigureWebTestClient
@AutoConfigureWireMock(port = 8084) // Spin up a WireMock server on port 8084
@TestPropertySource(
        properties = {
                "restClient.moviesInfoUrl=http://localhost:8084/api/movieInfos",
                "restClient.reviewUrl=http://localhost:8084/api/reviews"
        }
)
class MoviesControllerIntgTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    @DisplayName("Retrieve Movie By ID: 200 OK")
    void retrieveMovieById() {
        var movieId = "abc";
        // MovieInfo: you can receive response, If you request this Specific Url
        stubFor(get(urlEqualTo("/api/movieInfos/" + movieId))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("movieinfo.json")));

        // reviews: you use to QueryParam, But We can use Easy way to get response
        // UrlPathEqualTo is, if path is same, you can get response -> not need to QueryParam
        stubFor(get(urlPathEqualTo("/api/reviews"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("reviews.json")));

        webTestClient.get()
                .uri("/api/movies/{id}", movieId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Movie.class)
                .consumeWith(movieEntityExchangeResult -> {
                    var result = movieEntityExchangeResult.getResponseBody();

                    assert Objects.requireNonNull(result).getReviewsList().size() == 2;
                    assertEquals("Batman Begins", result.getMovieInfo().getName());
                });
    }

    @Test
    @DisplayName("Retrieve Movie By ID: (moviesInfo)4xx Error")
    void retrieveMovieById_4xx() {
        var movieId = "abc";
        stubFor(get(urlEqualTo("/api/movieInfos/" + movieId))
                .willReturn(aResponse().withStatus(404)));

        stubFor(get(urlPathEqualTo("/api/reviews"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("reviews.json")));

        webTestClient.get()
                .uri("/api/movies/{id}", movieId)
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody(String.class)
                .isEqualTo("MovieInfo Not Found by MoviesId: abc");
    }

    @Test
    @DisplayName("Retrieve Movie By ID: (reviews)4xx Error")
    void retrieveMovieById_4xx_reviews() {
        var movieId = "abc";
        stubFor(get(urlEqualTo("/api/movieInfos/" + movieId))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("movieinfo.json")));

        stubFor(get(urlPathEqualTo("/api/reviews"))
                .willReturn(aResponse()
                        .withStatus(404)));

        webTestClient.get()
                .uri("/api/movies/{id}", movieId)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Movie.class)
                .consumeWith(movieEntityExchangeResult -> {
                    var result = movieEntityExchangeResult.getResponseBody();

                    assert Objects.requireNonNull(result).getReviewsList().size() == 0;
                    assertEquals("Batman Begins", result.getMovieInfo().getName());
                });
    }

    @Test
    @DisplayName("Retrieve Movie By ID: (moviesInfo)5xx Error")
    void retrieveMovieById_5xx() {
        var movieId = "abc";
        stubFor(get(urlEqualTo("/api/movieInfos/" + movieId))
                .willReturn(aResponse().withStatus(500)
                        .withBody("MovieInfo Service Unavailable")
                ));

        webTestClient.get()
                .uri("/api/movies/{id}", movieId)
                .exchange()
                .expectStatus()
                .is5xxServerError()
                .expectBody(String.class)
                .isEqualTo("MoviesInfo Server Error: MovieInfo Service Unavailable");
    }

}//class