package com.watercollector.app.data.repository

import com.watercollector.app.data.local.dao.CreditDao
import com.watercollector.app.data.local.dao.InvoiceDao
import com.watercollector.app.data.local.dao.MeterDao
import com.watercollector.app.data.local.dao.SubscriberDao

class SubscriberRepository(
    private val subscriberDao: SubscriberDao,
    private val invoiceDao: InvoiceDao,
    private val meterDao: MeterDao,
    private val creditDao: CreditDao
) {
    fun observeSubscribers() = subscriberDao.observeSubscribers()
    suspend fun getSubscriberById(subscriberId: Int) = subscriberDao.getById(subscriberId)
    suspend fun getOpenInvoicesForSubscriber(subscriberId: Int) = invoiceDao.getBySubscriberId(subscriberId)
    suspend fun getMetersForSubscriber(subscriberId: Int) = meterDao.getBySubscriberId(subscriberId)
    suspend fun getCreditsForSubscriber(subscriberId: Int) = creditDao.getBySubscriberId(subscriberId)
}
