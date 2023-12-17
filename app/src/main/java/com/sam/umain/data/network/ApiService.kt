package com.sam.umain.data.network

import com.google.gson.JsonObject
import com.sam.umain.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("restaurants")
    suspend fun getRestaurants(): Response<JsonObject>


    @GET("filter/{id}")
    suspend fun getFilterById(
        @Path("id") id: String
    ): Response<JsonObject>


    @GET("open/{id}")
    suspend fun getRestaurantStatus(
        @Path("id") id: String
    ): Response<JsonObject>


}