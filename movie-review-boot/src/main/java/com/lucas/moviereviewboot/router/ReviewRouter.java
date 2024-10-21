package com.lucas.moviereviewboot.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * Endpoint 를 담당하는 Router Class
 */
@Configuration
public class ReviewRouter {

    @Bean
    public RouterFunction<ServerResponse> reviewRoute(){
        return route()
                .GET("/api/helloworld", (request -> ServerResponse.ok().bodyValue("helloworld")))
                .build();
    }
}
