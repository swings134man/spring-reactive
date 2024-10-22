package com.lucas.moviereviewboot.handler;

import com.lucas.moviereviewboot.domain.Review;
import com.lucas.moviereviewboot.repository.ReviewReactiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Router 에 대한 Handler Class
 */
@Component
@RequiredArgsConstructor
public class ReviewHandler {

    private final ReviewReactiveRepository reviewReactiveRepository;

    /**
     * Review ADD Handler (SAVE)
     * @param request
     * @return
     */
    public Mono<ServerResponse> addReview(ServerRequest request) {

        return request.bodyToMono(Review.class)
                .flatMap(reviewReactiveRepository::save)
                .flatMap(review -> ServerResponse.status(HttpStatus.CREATED).bodyValue(review));
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

        return existingReview
                .flatMap(review -> request.bodyToMono(Review.class)
                        .map(requestReview -> {
                            review.setComment(requestReview.getComment());
                            review.setRating(requestReview.getRating());
                            return review;
                        })
                        .flatMap(reviewReactiveRepository::save)
                        .flatMap(savedReview -> ServerResponse.ok().bodyValue(review))
                );
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
