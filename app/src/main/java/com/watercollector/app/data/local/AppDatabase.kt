package com.watercollector.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.watercollector.app.data.local.dao.CreditDao
import com.watercollector.app.data.local.dao.InvoiceDao
import com.watercollector.app.data.local.dao.MeterDao
import com.watercollector.app.data.local.dao.ReceiptDraftDao
import com.watercollector.app.data.local.dao.SubscriberDao
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
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun subscriberDao(): SubscriberDao
    abstract fun invoiceDao(): InvoiceDao
    abstract fun meterDao(): MeterDao
    abstract fun creditDao(): CreditDao
    abstract fun receiptDraftDao(): ReceiptDraftDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "collector_local.db"
            ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
        }
    }
}
