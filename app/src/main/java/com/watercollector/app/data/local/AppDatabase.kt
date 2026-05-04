package com.watercollector.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.watercollector.app.data.local.dao.CachedLoginDao
import com.watercollector.app.data.local.dao.CreditDao
import com.watercollector.app.data.local.dao.InvoiceDao
import com.watercollector.app.data.local.dao.MeterDao
import com.watercollector.app.data.local.dao.ReceiptDraftDao
import com.watercollector.app.data.local.dao.SubscriberDao
import com.watercollector.app.data.local.entities.CachedLoginEntity
import com.watercollector.app.data.local.entities.LocalOpenInvoiceEntity
import com.watercollector.app.data.local.entities.LocalReceiptDraftEntity
import com.watercollector.app.data.local.entities.LocalReceiptDraftLineEntity
import com.watercollector.app.data.local.entities.LocalSubscriberCreditEntity
import com.watercollector.app.data.local.entities.LocalSubscriberEntity
import com.watercollector.app.data.local.entities.LocalSubscriberMeterEntity

@Database(
    entities = [
        LocalSubscriberEntity::class,
        LocalSubscriberMeterEntity::class,
        LocalOpenInvoiceEntity::class,
        LocalSubscriberCreditEntity::class,
        LocalReceiptDraftEntity::class,
        LocalReceiptDraftLineEntity::class,
        CachedLoginEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun subscriberDao(): SubscriberDao
    abstract fun invoiceDao(): InvoiceDao
    abstract fun meterDao(): MeterDao
    abstract fun creditDao(): CreditDao
    abstract fun receiptDraftDao(): ReceiptDraftDao
    abstract fun cachedLoginDao(): CachedLoginDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `cached_login` (
                        `userName` TEXT NOT NULL,
                        `passwordHash` TEXT NOT NULL,
                        `baseUrl` TEXT NOT NULL,
                        `token` TEXT NOT NULL,
                        `collectorId` INTEGER NOT NULL,
                        `collectorName` TEXT,
                        `fullName` TEXT,
                        `deviceCode` TEXT NOT NULL,
                        `cachedAt` INTEGER NOT NULL,
                        PRIMARY KEY(`userName`)
                    )
                    """.trimIndent()
                )
            }
        }

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "collector_local.db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                    .also { INSTANCE = it }
            }
    }
}