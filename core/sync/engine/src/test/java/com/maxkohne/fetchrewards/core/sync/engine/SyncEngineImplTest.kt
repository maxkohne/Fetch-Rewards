package com.maxkohne.fetchrewards.core.sync.engine

import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteFullException
import com.google.common.truth.Truth
import com.maxkohne.fetchrewards.core.sync.common.SyncError
import com.maxkohne.fetchrewards.core.sync.common.SyncResult
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerializationException
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

internal class SyncEngineImplTest {

    private val syncEngine = SyncEngineImpl()

    @Test
    fun `syncData success`() = runTest {
        val result = syncEngine.syncData(
            executeApiCall = { "" },
            saveResponse = { }
        )

        Truth.assertThat(result).isEqualTo(SyncResult.Success)
    }

    @Test
    fun `syncData IOException`() = runTest {
        val result = syncEngine.syncData(
            executeApiCall = { throw IOException() },
            saveResponse = { }
        )

        Truth.assertThat(result).isEqualTo(SyncResult.Failure(SyncError.ConnectivityError))
    }

    @Test
    fun `syncData HttpException`() = runTest {
        val errorMap = mapOf(
            mock<Response<*>>().stub { on { code() } doReturn 503 } to SyncError.ServerDown,
            mock<Response<*>>().stub { on { code() } doReturn 403 } to SyncError.Forbidden,
            mock<Response<*>>() to SyncError.General,
        )
        errorMap.forEach { (response, expectedError) ->
            val result = syncEngine.syncData(
                executeApiCall = { throw HttpException(response) },
                saveResponse = { }
            )

            Truth.assertThat(result).isEqualTo(SyncResult.Failure(expectedError))
        }
    }

    @Test
    fun `syncData SerializationException`() = runTest {
        val result = syncEngine.syncData(
            executeApiCall = { throw SerializationException() },
            saveResponse = { }
        )

        Truth.assertThat(result).isEqualTo(SyncResult.Failure(SyncError.General))
    }

    @Test
    fun `syncData SQLiteFullException`() = runTest {
        val result = syncEngine.syncData(
            executeApiCall = { "" },
            saveResponse = { throw SQLiteFullException() }
        )

        Truth.assertThat(result).isEqualTo(SyncResult.Failure(SyncError.StorageFull))
    }

    @Test
    fun `syncData SQLiteException`() = runTest {
        val result = syncEngine.syncData(
            executeApiCall = { "" },
            saveResponse = { throw SQLiteException() }
        )

        Truth.assertThat(result).isEqualTo(SyncResult.Failure(SyncError.General))
    }
}