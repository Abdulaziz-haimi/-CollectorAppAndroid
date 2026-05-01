package com.watercollector.app.data.remote

import com.watercollector.app.data.remote.model.ImportDecisionsResponse
import com.watercollector.app.data.remote.model.LoginRequest
import com.watercollector.app.data.remote.model.LoginResponse
import com.watercollector.app.data.remote.model.ReceivablesExportResponse
import com.watercollector.app.data.remote.model.UploadBatchRequest
import com.watercollector.app.data.remote.model.UploadBatchResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("api/mobile-sync/receivables")
    suspend fun getReceivables(@Query("asOfDate") asOfDate: String?): ReceivablesExportResponse

    @POST("api/mobile-sync/upload-batch")
    suspend fun uploadBatch(@Body request: UploadBatchRequest): UploadBatchResponse

    @GET("api/mobile-sync/import-decisions")
    suspend fun getImportDecisions(
        @Query("deviceCode") deviceCode: String,
        @Query("changedAfter") changedAfter: String?
    ): ImportDecisionsResponse
}
