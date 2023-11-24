package com.borshevskiy.foodapp.di

import com.borshevskiy.foodapp.BuildConfig
import com.borshevskiy.foodapp.data.network.FoodRecipesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS).build()
    }

    @Singleton
    @Provides
    fun provideApiService(okHttpClient: OkHttpClient): FoodRecipesApi = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL).client(okHttpClient).addConverterFactory(GsonConverterFactory.create())
        .build().create(FoodRecipesApi::class.java)
}