package com.watercollector.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.watercollector.app.data.local.entities.LocalOpenInvoiceEntity

@Dao
interface InvoiceDao {
    @Query("SELECT * FROM local_open_invoices WHERE subscriberId = :subscriberId ORDER BY invoiceDate")
    suspend fun getBySubscriberId(subscriberId: Int): List<LocalOpenInvoiceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<LocalOpenInvoiceEntity>)

    @Query("DELETE FROM local_open_invoices")
    suspend fun clearAll()
}
