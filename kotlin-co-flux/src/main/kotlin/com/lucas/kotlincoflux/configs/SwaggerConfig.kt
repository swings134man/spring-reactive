package com.lucas.kotlincoflux.configs

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * SwaggerConfig.kt: Swagger UI
 *
 * @author: lucaskang(swings134man)
 * @since: 2025. 7. 13. 오전 2:04
 * @description: http://localhost:8080/swagger-ui/index.html
 */
@Configuration
class SwaggerConfig {

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .components(Components())
            .info(configurationInfo())
    }

    private fun configurationInfo(): Info {
        return Info()
            .title("kotlin-co-flux API's")
            .description("kotlin & Coroutine - R2DBC + postgresql Demo Application")
            .version("1.0.0")
    }

}