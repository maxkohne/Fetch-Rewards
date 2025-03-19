package com.maxkohne.fetchrewards.core.sync.engine

import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteFullException
import com.maxkohne.fetchrewards.core.sync.common.SyncError
import com.maxkohne.fetchrewards.core.sync.common.SyncResult
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

interface SyncEngine {
    suspend fun <ResponseType> syncData(
        executeApiCall: suspend () -> ResponseType,
        saveResponse: suspend (ResponseType) -> Unit
    ): SyncResult
}

internal class SyncEngineImpl @Inject constructor() : SyncEngine {

    override suspend fun <ResponseType> syncData(
        executeApiCall: suspend () -> ResponseType,
        saveResponse: suspend (ResponseType) -> Unit
    ): SyncResult {
        val response = try {
            // Sync items from the API
            executeApiCall()
        } catch (e: IOException) {
            // This isn't always a connection issue, but just doing this as an example.
            // Should be more robust in real world app.
            return SyncResult.Failure(error = SyncError.ConnectivityError)
        } catch (e: HttpException) {
            // Showcasing how to propagate relevant network errors to the UI
            val error = when (e.code()) {
                503 -> SyncError.ServerDown
                403 -> SyncError.Forbidden
                else -> SyncError.General
            }
            return SyncResult.Failure(error = error)
        } catch (e: SerializationException) {
            // Serialization error when parsing the json response
            return SyncResult.Failure(error = SyncError.General)
        }

        try {
            saveResponse(response)
        } catch (e: SQLiteFullException) {
            return SyncResult.Failure(error = SyncError.StorageFull)
        } catch (e: SQLiteException) {
            return SyncResult.Failure(error = SyncError.General)
        }

        return SyncResult.Success
    }
}