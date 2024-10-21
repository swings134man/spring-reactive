package com.lucas.movieinfoboot.controller;

import com.lucas.movieinfoboot.domain.MovieInfo;
import com.lucas.movieinfoboot.repository.MovieInfoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("local")
@AutoConfigureWebTestClient
class MovieInfoControllerIntgTest {

    @Autowired
    MovieInfoRepository movieInfoRepository;

    @Autowired
    WebTestClient webTestClient;

    static String MOVIES_INFO_URL = "/api/movieInfos";

    // Data Setup
    @BeforeEach
    void setUp() {
        var movieInfos = List.of(new MovieInfo(null, "Batman Begins",
                        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                new MovieInfo(null, "The Dark Knight",
                        2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")),
                new MovieInfo("abc", "Dark Knight Rises",
                        2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")));

        movieInfoRepository.saveAll(movieInfos)
                .blockLast(); // blockLast() : 데이터가 모두 저장될때까지 대기 TestCase 에서만 사용
    }

    // Data Clear
    @AfterEach
    void tearDown() {
        movieInfoRepository.deleteAll().block();
    }

    @Test
    @DisplayName("영화 정보 저장")
    void addMovieInfo() {
        var entity = new MovieInfo(null, "Batman Begins1",
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));

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
                });
    }

    @Test
    @DisplayName("모든 영화 정보 조회")
    void getAllMovies() {
        webTestClient.get()
                .uri(MOVIES_INFO_URL)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(MovieInfo.class)
                .hasSize(3);
    }

    @Test
    @DisplayName("모든 영화 정보 조회 - Year 파라미터로 조회")
    void getAllMoviesByYear() {

        var uri = UriComponentsBuilder.fromUriString(MOVIES_INFO_URL)
                .queryParam("year", 2005)
                .build().toUri();

        webTestClient.get()
                .uri(uri)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(MovieInfo.class)
                .hasSize(1);
    }

    @Test
    @DisplayName("영화 정보 조회 단건 - ID")
    void getMovieById() {

        var movieInfoId = "abc";

        webTestClient.get()
                .uri(MOVIES_INFO_URL + "/{id}", movieInfoId)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Dark Knight Rises");

                // 아래 케이스도 가능
//                .expectBody(MovieInfo.class)
//                .consumeWith(movieInfoEntityExchangeResult -> {
//                    var responseResult = movieInfoEntityExchangeResult.getResponseBody();
//                    assertNotNull(responseResult);
//                });

    }

    @Test
    @DisplayName("영화 정보 수정")
    void updateMovieInfo() {

        var movieInfoId = "abc";
        var entity = new MovieInfo(null, "Dark knight Rises1",
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));

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
    @DisplayName("영화 정보 삭제")
    void deleteMovieInfo() {
        var movieInfoId = "abc";

        webTestClient.delete()
                .uri(MOVIES_INFO_URL + "{id}", movieInfoId)
                .exchange()
                .expectStatus().isNoContent();
    }


    @Test
    @DisplayName("영화 정보 수정 - Not Found")
    void updateMovieInfo_notFound() {

        var movieInfoId = "def";
        var entity = new MovieInfo(null, "Dark knight Rises1",
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));

        webTestClient.put()
                .uri(MOVIES_INFO_URL+"/{id}", movieInfoId)
                .bodyValue(entity)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    @DisplayName("영화 정보 조회 단건 - Not Found")
    void getMovieById_notFound() {

        var movieInfoId = "def";

        webTestClient.get()
                .uri(MOVIES_INFO_URL + "/{id}", movieInfoId)
                .exchange()
                .expectStatus()
                .isNotFound();
    }
}