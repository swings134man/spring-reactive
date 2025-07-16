package com.lucas.kotlincoflux.modules.account.model

import com.lucas.kotlincoflux.commons.BaseField
import com.lucas.kotlincoflux.modules.board.model.Board
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

/**
 * Account.kt: kor Table 을 Account Entity 로 사용
 *
 * @author: lucaskang(swings134man)
 * @since: 2025. 7. 16. 오후 5:40
 * @description:
 */
@Table("kor")
data class Account(
    @Id
    var id: Long? = null,

    var name: String,
    var age: Int,
    var isActive: Boolean = true,

    @Transient
    var boards: List<Board>? = null
): BaseField()