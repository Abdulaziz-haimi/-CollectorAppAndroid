package com.watercollector.app.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation

data class ReceiptDraftWithLines(
    @Embedded val draft: LocalReceiptDraftEntity,
    @Relation(parentColumn = "localReceiptId", entityColumn = "localReceiptId")
    val lines: List<LocalReceiptDraftLineEntity>
)
