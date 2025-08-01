package com.lucas.kotlincoflux.commons

import org.slf4j.LoggerFactory
import kotlin.jvm.java

/**
 * CommonLogger.kt: 공통 로깅 유틸
 *
 * @author: lucaskang(swings134man)
 * @since: 2025. 7. 13. 오전 1:52
 * @description: class 내부에서 val logger = logger() 를 통해 사용 가능
 */
inline fun <reified T> T.logger() = LoggerFactory.getLogger(T::class.java)!!