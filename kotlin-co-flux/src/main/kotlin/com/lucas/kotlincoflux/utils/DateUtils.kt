package com.lucas.kotlincoflux.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * DateUtils.kt: 날짜 관련 유틸리티 클래스
 *
 * @author: lucaskang(swings134man)
 * @since: 2025. 7. 15. 오전 1:53
 * @description: 날짜 및 시간 관련 유틸리티 메서드를 제공하는 클래스
 * - 타입에 따른 날짜 타입 포맷팅을 제공함.
 */

/**
 * @author: lucaskang(swings134man)
 * @since: 2025. 7. 15. 오전 1:57
 * @description: String Type 의 날짜 문자열을 LocalDateTime 으로 변환하는 확장 함수
 * - "yyyy-MM-dd HH:mm:ss" 형식의 문자열을 LocalDateTime 객체로 변환한다.
 */
fun String.toLocalDateTime(): LocalDateTime {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return LocalDateTime.parse(this, formatter)
}