package com.lucas.kotlincoflux.modules.board.model

import com.lucas.kotlincoflux.commons.BaseField
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("board")
data class Board (
    @Id
    var id: Long? = null,
    var title: String,
    var content: String,
): BaseField()