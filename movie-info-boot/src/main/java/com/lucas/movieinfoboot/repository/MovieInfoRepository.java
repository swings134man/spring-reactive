package com.lucas.movieinfoboot.repository;

import com.lucas.movieinfoboot.domain.MovieInfo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface MovieInfoRepository extends ReactiveMongoRepository<MovieInfo, String> {

    Mono<MovieInfo> findByName(String name);
    Flux<MovieInfo> findByYear(Integer year);
}
