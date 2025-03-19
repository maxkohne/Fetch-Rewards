package com.maxkohne.fetchrewards.data.api

import retrofit2.http.GET

internal interface ItemsApi {
    @GET("hiring.json")
    suspend fun getItems(): List<ItemResponse>
}