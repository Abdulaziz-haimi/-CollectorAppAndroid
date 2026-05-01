package com.watercollector.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.watercollector.app.data.local.entities.LocalSubscriberMeterEntity

@Dao
interface MeterDao {
    @Query("SELECT * FROM local_subscriber_meters WHERE subscriberId = :subscriberId ORDER BY isPrimary DESC, meterNumber")
    suspend fun getBySubscriberId(subscriberId: Int): List<LocalSubscriberMeterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<LocalSubscriberMeterEntity>)

    @Query("DELETE FROM local_subscriber_meters")
    suspend fun clearAll()
}
