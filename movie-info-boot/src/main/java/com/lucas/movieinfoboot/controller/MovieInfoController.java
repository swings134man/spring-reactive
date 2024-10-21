package com.lucas.movieinfoboot.controller;

import com.lucas.movieinfoboot.domain.MovieInfo;
import com.lucas.movieinfoboot.service.MovieInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class MovieInfoController {

    private final MovieInfoService movieInfoService;

    @PostMapping("/movieInfos")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MovieInfo> addMovieInfo(@RequestBody @Valid MovieInfo movieInfo) {
        return movieInfoService.addMovieInfo(movieInfo).log();
    }

    @GetMapping("/movieInfos")
    public Flux<MovieInfo> getAllMovieInfos(@RequestParam(required = false, value = "year") Integer year) {
        log.info("year = {}", year);

        if(null != year){
            return movieInfoService.getMovieInfoByYear(year).log();
        }

        return movieInfoService.getAllMovieInfos().log();
    }

    @GetMapping("/movieInfos/{id}")
    public Mono<ResponseEntity<MovieInfo>> getMovieInfoById(@PathVariable String id) {
        return movieInfoService.getMovieInfoById(id)
                .map(ResponseEntity.ok()::body)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .log();
    }

    @PutMapping("/movieInfos/{id}")
    public Mono<ResponseEntity<MovieInfo>> updateMovieInfo(@RequestBody MovieInfo updatedMovieInfo, @PathVariable String id) {
        return movieInfoService.updatedMovieInfo(updatedMovieInfo, id)
                .map(ResponseEntity.ok()::body)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .log();
    }

    @DeleteMapping("/movieInfos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteMovieInfo(@PathVariable String id) {
        return movieInfoService.deletedMovieInfo(id).log();
    }
}
