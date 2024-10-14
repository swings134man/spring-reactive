package com.lucas.movieinfoboot.repository;

import com.lucas.movieinfoboot.domain.MovieInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// MovieInfoRepository : Integration Test
@DataMongoTest
@ActiveProfiles("local")
class MovieInfoRepositoryIntgTest {

    @Autowired
    MovieInfoRepository movieInfoRepository;

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
    @DisplayName("1. findAll")
    void findAll() {
        // when
        var movieInfoFlux = movieInfoRepository.findAll().log();

        // then
        StepVerifier.create(movieInfoFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    @DisplayName("2. findById")
    void findById() {
        // when
        var movieInfoMono = movieInfoRepository.findById("abc").log();

        // then
        StepVerifier.create(movieInfoMono)
                .assertNext(movieInfo -> {
                    assertEquals("Dark Knight Rises", movieInfo.getName());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("3. save - saveMovieInfo")
    void saveMovieInfo() {
        MovieInfo entity = new MovieInfo(null, "Batman Begins for Save",
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));

        // when
        Mono<MovieInfo> movieInfoMono = movieInfoRepository.save(entity).log(); // Return Mono

        // then
        StepVerifier.create(movieInfoMono)
                .assertNext(movieInfo -> {
                    assertNotNull(movieInfo.getMovieInfoId());
                    assertEquals("Batman Begins for Save", movieInfo.getName());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("3. update - updateMovieInfo")
    void updateMovieInfo() {
        var entity = movieInfoRepository.findById("abc").block(); // block() : 데이터가 조회될때까지 대기 TestCase 에서만 사용
        entity.setYear(2029);

        // when
        Mono<MovieInfo> movieInfoMono = movieInfoRepository.save(entity).log(); // Return Mono

        // then
        StepVerifier.create(movieInfoMono)
                .assertNext(movieInfo -> {
                    assertEquals(2029, movieInfo.getYear());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("1. delete")
    void deleteMovieInfo() {
        // when
        movieInfoRepository.deleteById("abc").block();

        var movieInfoFlux = movieInfoRepository.findAll().log();

        // then
        StepVerifier.create(movieInfoFlux)
                .expectNextCount(2)
                .verifyComplete();
    }
}