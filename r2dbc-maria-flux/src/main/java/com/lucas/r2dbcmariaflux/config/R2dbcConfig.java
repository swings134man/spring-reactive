package com.lucas.r2dbcmariaflux.config;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.ReactiveAuditorAware;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import reactor.core.publisher.Mono;

@Configuration
@EnableR2dbcAuditing // Auditing(Create, Update)
@EnableTransactionManagement // Transaction 활성화(R2DBC 에서는 R2Dbc Transaction Manager 사용 필요)
public class R2dbcConfig {

    private final ConnectionFactory connectionFactory;

    public R2dbcConfig(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    /**
     * Reactive Transaction Manager
     */
    @Bean
    public ReactiveTransactionManager transactionManager() {
        return new R2dbcTransactionManager(connectionFactory);
    }

    /**
     * AuditorAware 설정(createBy, updateBy)
     */
    @Bean
    public ReactiveAuditorAware<String> auditorAware() {
        // TODO: SecurityContext 에서 사용자 정보 가져오기(없으면 system)
        return () -> Mono.just("system");
    }
}
