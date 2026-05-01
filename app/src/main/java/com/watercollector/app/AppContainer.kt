package com.watercollector.app

import android.content.Context
import com.watercollector.app.data.local.AppDatabase
import com.watercollector.app.data.repository.AuthRepository
import com.watercollector.app.data.repository.ReceiptRepository
import com.watercollector.app.data.repository.SessionManager
import com.watercollector.app.data.repository.SubscriberRepository
import com.watercollector.app.data.repository.SyncRepository

class AppContainer(context: Context) {
    val database: AppDatabase = AppDatabase.getInstance(context)
    val sessionManager = SessionManager(context)

    val authRepository = AuthRepository(sessionManager)
    val subscriberRepository = SubscriberRepository(
        database.subscriberDao(),
        database.invoiceDao(),
        database.meterDao(),
        database.creditDao()
    )
    val receiptRepository = ReceiptRepository(database.receiptDraftDao())
    val syncRepository = SyncRepository(database, sessionManager, receiptRepository)
}
