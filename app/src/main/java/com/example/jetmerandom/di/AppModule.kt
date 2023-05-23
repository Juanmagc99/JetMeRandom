package com.example.jetmerandom.di

import android.content.Context
import androidx.room.Room
import com.example.jetmerandom.data.database.FlightDatabase
import com.example.jetmerandom.data.network.APIClients
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val FLIGHT_DB_NAME = "flight_database"

    @Singleton
    @Provides
    fun provideAPIClients(): APIClients {
        return Retrofit.Builder()
            .baseUrl("https://example.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIClients::class.java)
    }


    @Singleton
    @Provides
    fun providesRoom(@ApplicationContext context:Context) =
        Room.databaseBuilder(context, FlightDatabase::class.java, FLIGHT_DB_NAME).build()

    @Singleton
    @Provides
    fun providesFlightDao(db:FlightDatabase) = db.getFlightDao()
}