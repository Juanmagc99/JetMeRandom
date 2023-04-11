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
        @Query("return_to") return_to:String,
        @Query("return_from") return_from:String,
        @Query("limit") limit:Int,
        @Query("nights_in_dst_from") nights_in_dst_from:Int,
        @Query("nights_in_dst_to") nights_in_dst_to:Int,
        @Query("flight_type") flight_type:String,
        @Query("ret_from_diff_airport") ret_from_diff_airport:Int = 0,
        @Query("ret_to_diff_airport") ret_to_diff_airport:Int = 0,
        @Query("price_from") price_from:Int,
        @Query("price_to") price_to:Int,
        @Query("max_stopovers") max_stopovers:Int,
        @Query("adults") adults:Int,
        @Query("children") children:Int,

        @Header("apikey") apiKey: String,
    ): Response<FlightsResponse>

    @GET("https://api.teleport.org/api/urban_areas/slug:{city_name}/images/")
    suspend fun getImage(
        @Path("city_name") city_name:String,
    ): Response<ImageResponse>

    @GET("http://api.positionstack.com/v1/forward?access_key=e4b9d40ee5bd077711b0d8ef32bbc534")
    suspend fun getCitiesLocation(
        @Query("query") city_name:String,
    ): Response<LocationResponse>


}