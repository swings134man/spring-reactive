package com.lucas.moviereviewboot.router;

import com.lucas.moviereviewboot.handler.ReviewHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * Endpoint 를 담당하는 Router Class
 *
 * nest(): 중첩된 RouterFunction 을 생성, 동일한 path 의 Endpoint 를 그룹화
 */
@Configuration
public class ReviewRouter {

    @Bean
    public RouterFunction<ServerResponse> reviewRoute(ReviewHandler reviewHandler) {
        return route()
                .nest(path("/api/reviews"), builder -> {
                    builder
                            .POST("", reviewHandler::addReview) //save Review
                            .GET("", reviewHandler::getReviews) //get Review
                            .PUT("/{id}", reviewHandler::updateReview) //update Review
                            .DELETE("/{id}", reviewHandler::deleteReview); //delete Review
                })
                .GET("/api/helloworld", (request -> ServerResponse.ok().bodyValue("helloworld")))
//                .POST("/api/reviews", reviewHandler::addReview) //save Review
//                .GET("/api/reviews", reviewHandler::getReview) //save Review
                .build();
    }
}
