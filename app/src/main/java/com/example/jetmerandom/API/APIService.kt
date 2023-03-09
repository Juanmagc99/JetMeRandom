package com.example.jetmerandom.API


import com.example.jetmerandom.data.FligthsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface APIService {

    @GET
    suspend fun getFlights(@Url urlEncoded:String):Response<FligthsResponse>

}