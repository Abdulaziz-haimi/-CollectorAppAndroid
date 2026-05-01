package com.watercollector.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.watercollector.app.data.local.entities.LocalReceiptDraftEntity
import com.watercollector.app.data.local.entities.LocalReceiptDraftLineEntity
import com.watercollector.app.data.local.entities.ReceiptDraftWithLines
import kotlinx.coroutines.flow.Flow

@Dao
interface ReceiptDraftDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: LocalReceiptDraftEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLines(items: List<LocalReceiptDraftLineEntity>)

    @Query("SELECT * FROM local_receipt_drafts ORDER BY localReceiptId DESC")
    fun observeAll(): Flow<List<LocalReceiptDraftEntity>>

    @Query("SELECT * FROM local_receipt_drafts WHERE syncStatus = 'Pending' ORDER BY localReceiptId DESC")
    fun observePending(): Flow<List<LocalReceiptDraftEntity>>

    @Transaction
    @Query("SELECT * FROM local_receipt_drafts WHERE syncStatus = 'Pending' ORDER BY localReceiptId")
    suspend fun getPendingWithLines(): List<ReceiptDraftWithLines>

    @Query("SELECT localReceiptId FROM local_receipt_drafts WHERE localPaymentGuid = :guid LIMIT 1")
    suspend fun findLocalReceiptIdByGuid(guid: String): Long?

    @Query(
        """
        UPDATE local_receipt_drafts
        SET syncStatus = :syncStatus,
            syncBatchRef = :syncBatchRef,
            serverImportId = :serverImportId,
            serverStatus = :serverStatus,
            rejectedReason = :rejectedReason,
            updatedAt = :updatedAt,
            sentAt = :sentAt
        WHERE localReceiptId = :localReceiptId
        """
    )
    suspend fun updateSyncState(
        localReceiptId: Long,
        syncStatus: String,
        syncBatchRef: String?,
        serverImportId: Int?,
        serverStatus: String?,
        rejectedReason: String?,
        updatedAt: String?,
        sentAt: String?
    )

    @Transaction
    suspend fun insertDraftWithLines(draft: LocalReceiptDraftEntity, lines: List<LocalReceiptDraftLineEntity>): Long {
        val id = insert(draft)
        if (lines.isNotEmpty()) insertLines(lines.map { it.copy(localReceiptId = id) })
        return id
    }
}
