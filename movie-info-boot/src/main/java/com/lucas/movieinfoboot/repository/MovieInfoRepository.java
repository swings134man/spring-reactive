package com.lucas.movieinfoboot.repository;

import com.lucas.movieinfoboot.domain.MovieInfo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieInfoRepository extends ReactiveMongoRepository<MovieInfo, String> {
}
