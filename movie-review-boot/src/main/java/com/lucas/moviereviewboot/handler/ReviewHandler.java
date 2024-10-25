package com.lucas.moviereviewboot.handler;

import com.lucas.moviereviewboot.domain.Review;
import com.lucas.moviereviewboot.exception.ReviewDataException;
import com.lucas.moviereviewboot.exception.ReviewNotFoundException;
import com.lucas.moviereviewboot.repository.ReviewReactiveRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;


/**
 * Router 에 대한 Handler Class
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ReviewHandler {

    private final Validator validator;
    private final ReviewReactiveRepository reviewReactiveRepository;

    /**
     * Review ADD Handler (SAVE)
     * @param request
     * @return
     */
    public Mono<ServerResponse> addReview(ServerRequest request) {

        return request.bodyToMono(Review.class)
                .doOnNext(this::validate)
                .flatMap(reviewReactiveRepository::save)
                .flatMap(review -> ServerResponse.status(HttpStatus.CREATED).bodyValue(review));
    }

    /**
     * Review Class 의 Validation 을 위한 Method
     * @param review
     */
    private void validate(Review review) {
        var constraintViolations = validator.validate(review);
        log.info("constraintViolations : {}", constraintViolations);

        // Msg
        if(!constraintViolations.isEmpty()) {
            var errMessage = constraintViolations
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .sorted()
                    .collect(Collectors.joining(","));
            throw new ReviewDataException(errMessage);
        }
    }

    /**
     * Review GET Handler FindALL
     * @param request
     * @return
     */
    public Mono<ServerResponse> getReviews(ServerRequest request) {

        var movieInfoId = request.queryParam("movieInfoId");

        if(movieInfoId.isPresent()) {
            var reviewFlux = reviewReactiveRepository.findByMovieInfoId(Long.valueOf(movieInfoId.get()));
            return buildReviewResponse(reviewFlux);
        }else {
            var reviewsFlux = reviewReactiveRepository.findAll();
            return buildReviewResponse(reviewsFlux);
        }
    }

    // Flux<Review> 를 ServerResponse.ok().body() 로 반환
    private static Mono<ServerResponse> buildReviewResponse(Flux<Review> reviewFlux) {
        return ServerResponse.ok().body(reviewFlux, Review.class);
    }

    /**
     * Review UPDATE Handler
     * @param request
     * @return
     */
    public Mono<ServerResponse> updateReview(ServerRequest request) {
        var reviewId = request.pathVariable("id");

        var existingReview = reviewReactiveRepository.findById(reviewId);
//                .switchIfEmpty(Mono.error(new ReviewNotFoundException("Review Not Found for the given Review id " + reviewId))); // Error Message Body 처리

        return existingReview
                .flatMap(review -> request.bodyToMono(Review.class)
                        .map(requestReview -> {
                            review.setComment(requestReview.getComment());
                            review.setRating(requestReview.getRating());
                            return review;
                        })
                        .flatMap(reviewReactiveRepository::save)
                        .flatMap(savedReview -> ServerResponse.ok().bodyValue(review)))
                .switchIfEmpty(ServerResponse.notFound().build()); // 단순 404 return
    }

    /**
     * Review DELETE Handler
     * @param request
     * @return
     */
    public Mono<ServerResponse> deleteReview(ServerRequest request) {
        var reviewId = request.pathVariable("id");

        var findResult = reviewReactiveRepository.findById(reviewId);

        // DELETE 시 Mono<Void> 를 반환 -> then() 으로 NO_CONTENT 반환
        return findResult.flatMap(review -> reviewReactiveRepository.deleteById(reviewId)
                .then(ServerResponse.noContent().build()));
//        return reviewReactiveRepository.deleteById(reviewId)
//                .then(ServerResponse.ok().build());
    }
}
