package com.watercollector.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.watercollector.app.data.local.entities.CachedLoginEntity

@Dao
interface CachedLoginDao {

    @Query("SELECT * FROM cached_login WHERE userName = :userName LIMIT 1")
    suspend fun getByUserName(userName: String): CachedLoginEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: CachedLoginEntity)

    @Query("SELECT * FROM cached_login ORDER BY cachedAt DESC LIMIT 1")
    suspend fun getLastLogin(): CachedLoginEntity?
}