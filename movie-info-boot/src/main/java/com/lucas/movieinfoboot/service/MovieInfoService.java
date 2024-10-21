package com.lucas.movieinfoboot.service;

import com.lucas.movieinfoboot.domain.MovieInfo;
import com.lucas.movieinfoboot.repository.MovieInfoRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MovieInfoService {

    private final MovieInfoRepository movieInfoRepository;


    public Mono<MovieInfo> addMovieInfo(MovieInfo movieInfo) {
        return movieInfoRepository.save(movieInfo);
    }

    public Flux<MovieInfo> getAllMovieInfos() {
        return movieInfoRepository.findAll();
    }

    public Mono<MovieInfo> getMovieInfoById(String id) {
        return movieInfoRepository.findById(id);
    }

    public Mono<MovieInfo> updatedMovieInfo(MovieInfo updatedMovieInfo, String id) {
        // Find
        return movieInfoRepository.findById(id)
                .flatMap(info -> {
                    info.setCast(updatedMovieInfo.getCast());
                    info.setName(updatedMovieInfo.getName());
                    info.setRelease_date(updatedMovieInfo.getRelease_date());
                    info.setYear(updatedMovieInfo.getYear());

                    return movieInfoRepository.save(info);
                });
    }

    public Mono<Void> deletedMovieInfo(String id) {
        return movieInfoRepository.deleteById(id);
    }

    public Flux<MovieInfo> getMovieInfoByYear(Integer year) {
        return movieInfoRepository.findByYear(year);
    }

    public Mono<MovieInfo> getMovieInfoByName(String name) {
        return movieInfoRepository.findByName(name);
    }
}
