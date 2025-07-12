package com.lucas.kotlincoflux.commons

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

/**
 * BaseField.kt: Model 공통 필드 정의
 *
 * @author: lucaskang(swings134man)
 * @since: 2025. 7. 13. 오전 1:38
 * @description:
 */
abstract class BaseField {

    @CreatedDate
    var createdAt: LocalDateTime? = null

    @LastModifiedDate
    var updatedAt: LocalDateTime? = null
}