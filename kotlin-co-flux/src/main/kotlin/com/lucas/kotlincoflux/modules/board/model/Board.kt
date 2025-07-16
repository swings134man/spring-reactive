package com.lucas.kotlincoflux.modules.board.model

import com.lucas.kotlincoflux.commons.BaseField
import com.lucas.kotlincoflux.modules.account.model.Account
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("board")
data class Board (
    @Id
    var id: Long? = null,
    var title: String,
    var content: String,

    var korId: Long? = null, // FK (Account ID)

    @Transient
    var account: Account? = null
): BaseField()