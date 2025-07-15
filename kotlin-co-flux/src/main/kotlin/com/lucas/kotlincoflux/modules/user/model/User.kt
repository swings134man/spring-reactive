package com.lucas.kotlincoflux.modules.user.model

import com.lucas.kotlincoflux.commons.BaseField
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

/**
 * User.kt: DB Table 'dsl' 매핑 - 회원 개인 정보 도메인
 *
 * @author: lucaskang(swings134man)
 * @since: 2025. 7. 15. 오후 11:54
 * @description: 회원 개인 정보 도메인
 */
@Table("dsl")
data class User (
    @Id
    var id: Long? = null,
    var name: String,
    var address: String,
): BaseField()