package com.example.jetmerandom.API


import com.example.jetmerandom.data.FlightsResponse
import com.example.jetmerandom.data.image.ImageResponse
import com.example.jetmerandom.data.position.LocationResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {

    @GET("search")
    suspend fun getFlights(
        @Query("fly_from") fly_from:String,
        @Query("date_from") date_from:String,
        @Query("date_to") date_to:String,
        @Query("rerturn_from") return_from:String,
        @Query("rerturn_to") return_to:String,
        @Query("limit") limit:Int,
        @Query("nights_in_dst_from") nights_in_dst_from:Int,
        @Query("nights_in_dst_to") nights_in_dst_to:Int,
        @Query("flight_type") flight_type:String,
        @Header("apikey") apiKey: String,
    ): Response<FlightsResponse>

    @GET("https://api.teleport.org/api/urban_areas/slug:{city_name}/images/")
    suspend fun getImage(
        @Path("city_name") city_name:String,
    ): Response<ImageResponse>

    @GET("http://api.positionstack.com/v1/forward?access_key=")
    suspend fun getCitiesLocation(
        @Query("query") city_name:String,
    ): Response<LocationResponse>


}