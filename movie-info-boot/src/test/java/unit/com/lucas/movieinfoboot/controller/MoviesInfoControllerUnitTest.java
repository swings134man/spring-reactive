package com.lucas.movieinfoboot.controller;

import com.lucas.movieinfoboot.domain.MovieInfo;
import com.lucas.movieinfoboot.service.MovieInfoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

/**
 * Unit test for the MovieInfoController class.
 */
@WebFluxTest(controllers = MovieInfoController.class)
@AutoConfigureWebTestClient
public class MoviesInfoControllerUnitTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private MovieInfoService movieInfoServiceMock;

    static String MOVIES_INFO_URL = "/api/movieInfos";


    @Test
    @DisplayName("Get all movies info")
    void getAllMoviesInfo() {
        var movieInfos = List.of(new MovieInfo(null, "Batman Begins",
                        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                new MovieInfo(null, "The Dark Knight",
                        2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")),
                new MovieInfo("abc", "Dark Knight Rises",
                        2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")));

        when(movieInfoServiceMock.getAllMovieInfos()).thenReturn(Flux.fromIterable(movieInfos));

        webTestClient.get()
                .uri(MOVIES_INFO_URL)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(MovieInfo.class)
                .hasSize(3);
    }

    @Test
    @DisplayName("Get movie info by id")
    void getMovieInfoById() {
        var movieId = "abc";

        var movieInfo = new MovieInfo("abc", "Dark Knight Rises",
                2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20"));

        when(movieInfoServiceMock.getMovieInfoById(isA(String.class))).thenReturn(Mono.just(movieInfo));

        webTestClient
                .get()
                .uri(MOVIES_INFO_URL + "/{id}", movieId)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Dark Knight Rises");
//                .consumeWith(movieInfoEntityExchangeResult -> {
//                    var resultBody = movieInfoEntityExchangeResult.getResponseBody();
//                    assert resultBody != null;
//                });
    }

    @Test
    @DisplayName("Save MovieInfo")
    void addMovieInfo() {
        var entity = new MovieInfo(null, "Batman Begins1",
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));

        when(movieInfoServiceMock.addMovieInfo(isA(MovieInfo.class))).thenReturn(
                Mono.just(new MovieInfo("mockId", "Batman Begins1",
                        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")))
        );

        webTestClient.post()
                .uri(MOVIES_INFO_URL)
                .bodyValue(entity)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    var saveMovieInfo = movieInfoEntityExchangeResult.getResponseBody();

                    assert  saveMovieInfo != null;
                    assert  saveMovieInfo.getMovieInfoId() != null;
                    assertEquals("mockId", saveMovieInfo.getMovieInfoId());
                });
    }

    @Test
    @DisplayName("update MovieInfo")
    void updateMovieInfo() {
        var movieInfoId = "abc";
        var entity = new MovieInfo(null, "Dark knight Rises1",
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));

        when(movieInfoServiceMock.updatedMovieInfo(isA(MovieInfo.class), isA(String.class))).thenReturn(
                Mono.just(new MovieInfo(movieInfoId, "Dark knight Rises1",
                        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")))
        );

        webTestClient.put()
                .uri(MOVIES_INFO_URL+"/{id}", movieInfoId)
                .bodyValue(entity)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    var updatedMovieInfo = movieInfoEntityExchangeResult.getResponseBody();

                    assert  updatedMovieInfo != null;
                    assert  updatedMovieInfo.getMovieInfoId() != null;
                    assertEquals("Dark knight Rises1", updatedMovieInfo.getName());
                });
    }

    @Test
    @DisplayName("delete MovieInfo By Id")
    void deleteMovieById() {
        var movieId = "abc";

        when(movieInfoServiceMock.deletedMovieInfo(isA(String.class)))
                .thenReturn(Mono.empty());

        webTestClient.delete()
                .uri(MOVIES_INFO_URL + "/{id}", movieId)
                .exchange()
                .expectStatus().isNoContent();
    }

    // ------------------------------------------------------
    @Test
    @DisplayName("Save MovieInfo - With Validation")
    void addMovieInfoForValidation() {
        // name Must not be blank, year Must be positive value
        var entity = new MovieInfo(null, "",
                -2005, List.of(""), LocalDate.parse("2005-06-15"));

        webTestClient.post()
                .uri(MOVIES_INFO_URL)
                .bodyValue(entity)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(String.class)
                .consumeWith(stringEntityExchangeResult -> {
                    var responseBody = stringEntityExchangeResult.getResponseBody();
                    System.out.println("responseBody = " + responseBody);

                    var expectedError = "movieInfo.cast must be present, movieInfo.name must be not blank, movieInfo.year must be positive value";

                    assert responseBody != null;
                    assertEquals(expectedError, responseBody);
                });
    }
}
