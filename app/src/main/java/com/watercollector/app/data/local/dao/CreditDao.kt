package com.watercollector.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.watercollector.app.data.local.entities.LocalSubscriberCreditEntity

@Dao
interface CreditDao {
    @Query("SELECT * FROM local_subscriber_credits WHERE subscriberId = :subscriberId ORDER BY creditDate DESC")
    suspend fun getBySubscriberId(subscriberId: Int): List<LocalSubscriberCreditEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<LocalSubscriberCreditEntity>)

    @Query("DELETE FROM local_subscriber_credits")
    suspend fun clearAll()
}
