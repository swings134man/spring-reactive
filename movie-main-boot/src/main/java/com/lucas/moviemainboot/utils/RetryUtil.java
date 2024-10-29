package com.lucas.moviemainboot.utils;

import com.lucas.moviemainboot.exception.MoviesInfoServerException;
import com.lucas.moviemainboot.exception.ReviewsServerException;
import reactor.core.Exceptions;
import reactor.util.retry.Retry;

import java.time.Duration;

public class RetryUtil {

    public static Retry retrySpec() {
        return Retry.fixedDelay(3, Duration.ofSeconds(1))
                .filter(ex -> ex instanceof MoviesInfoServerException || // 특정 예외만 재시도 허용
                        ex instanceof ReviewsServerException
                )
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) ->
                        // 예외확산: 재시도를 위한 신호 전달
                        // 재시도 예외 메세지를 출력하는 대신, 예외를 야기한 실제 이슈에 엑세스권한부여(즉 원래 예외 출력)
                        Exceptions.propagate(retrySignal.failure())
                );
    }
}
