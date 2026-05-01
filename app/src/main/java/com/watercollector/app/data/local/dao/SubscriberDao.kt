package com.watercollector.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.watercollector.app.data.local.entities.LocalSubscriberEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SubscriberDao {
    @Query("SELECT * FROM local_subscribers ORDER BY subscriberName")
    fun observeSubscribers(): Flow<List<LocalSubscriberEntity>>

    @Query("SELECT * FROM local_subscribers WHERE subscriberId = :subscriberId LIMIT 1")
    suspend fun getById(subscriberId: Int): LocalSubscriberEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<LocalSubscriberEntity>)

    @Query("DELETE FROM local_subscribers")
    suspend fun clearAll()
}
