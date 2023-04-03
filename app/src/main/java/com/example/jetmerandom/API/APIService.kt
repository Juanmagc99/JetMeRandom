package com.example.jetmerandom.API


import com.example.jetmerandom.data.FlightsResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.Url

interface APIService {

    @GET("search")
    suspend fun getFlights(
        @Query("fly_from") fly_from:String,
        @Query("fly_to") fly_to:String,
        @Query("date_from") date_from:String,
        @Query("date_to") date_to:String,
        @Query("rerturn_from") return_from:String,
        @Query("rerturn_to") return_to:String,
        @Query("limit") limit:String,
        @Header("apikey") apiKey: String,
    ): Response<FlightsResponse>

}