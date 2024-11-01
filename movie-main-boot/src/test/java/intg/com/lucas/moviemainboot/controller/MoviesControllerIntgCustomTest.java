package com.lucas.moviemainboot.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("local")
@AutoConfigureWebTestClient
@AutoConfigureWireMock(port = 8084) // Spin up a WireMock server on port 8084 //${wiremock.server.port}
@TestPropertySource(
        properties = {
                "restClient.moviesInfoUrl=http://localhost:8084/api/movieInfos",
                "restClient.reviewUrl=http://localhost:8084/api/reviews"
        }
)
class MoviesControllerIntgCustomTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    @DisplayName("delete - movieInfo: 204")
    void deleteByMovieId() {
        var movieId = "abc";
        stubFor(delete(urlEqualTo("/api/movieInfos/" + movieId))
                .willReturn(aResponse()
                        .withStatus(204)
                )
        );

        stubFor(delete(urlPathEqualTo("/api/reviews"))
                .willReturn(aResponse()
                        .withStatus(204)
                )
        );

        webTestClient.delete()
                .uri("/api/movies/{id}", movieId)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @DisplayName("delete - movieInfo: Not Found")
    void deleteMovieById_notFound() {
        var movieId = "abc";
        stubFor(delete(urlEqualTo("/api/movieInfos/" + movieId))
                .willReturn(aResponse().withStatus(404))
        );

        webTestClient.delete()
                .uri("/api/movies/{id}", movieId)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .isEqualTo("MovieInfo Not Found By movieId: "+movieId);
    }

    @Test
    @DisplayName("delete - movieInfo: 4xx")
    void deleteMovieById_4xx() {
        var movieId = "abc";
        stubFor(delete(urlEqualTo("/api/movieInfos/" + movieId))
                .willReturn(aResponse().withStatus(400))
        );

        webTestClient.delete()
                .uri("/api/movies/{id}", movieId)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(String.class)
                ;
    }

    @Test
    @DisplayName("delete - movieInfo: 5xx")
    void deleteMovieById_5xx() {
        var movieId = "abc";
        stubFor(delete(urlEqualTo("/api/movieInfos/" + movieId))
                .willReturn(aResponse().withStatus(500)
                        .withBody("MovieInfo Service Unavailable"))
        );

        webTestClient.delete()
                .uri("/api/movies/{id}" ,movieId)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(String.class)
                .isEqualTo("movieInfo Server Error: MovieInfo Service Unavailable");
    }

    // -------------------- Review Delete
    // reviews -- delete
    @Test
    @DisplayName("delete - reviews: 4xx")
    void deleteReviewById_4xx() {
        var movieId = "abc";
        stubFor(delete(urlEqualTo("/api/movieInfos/" + movieId))
                .willReturn(aResponse()
                        .withStatus(204)
                )
        );

        stubFor(delete(urlPathEqualTo("/api/reviews"))
                .willReturn(aResponse()
                        .withStatus(400)
                )
        );

        webTestClient.delete()
                .uri("/api/movies/{id}", movieId)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(String.class);
    }

    @Test
    @DisplayName("delete - reviews: 5xx")
    void deleteReviewById_5xx() {
        var movieId = "abc";
        stubFor(delete(urlEqualTo("/api/movieInfos/" + movieId))
                .willReturn(aResponse()
                        .withStatus(204)
                )
        );

        stubFor(delete(urlPathEqualTo("/api/reviews"))
                .willReturn(aResponse()
                        .withStatus(500)
                )
        );

        webTestClient.delete()
                .uri("/api/movies/{id}", movieId)
                .exchange()
                .expectStatus().is5xxServerError();
    }
}