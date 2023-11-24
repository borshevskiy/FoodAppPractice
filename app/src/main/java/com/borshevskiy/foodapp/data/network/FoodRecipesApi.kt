package com.borshevskiy.foodapp.data.network

import com.borshevskiy.foodapp.BuildConfig
import com.borshevskiy.foodapp.domain.models.FoodJoke
import com.borshevskiy.foodapp.domain.models.FoodData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface FoodRecipesApi {

    @GET("/recipes/complexSearch")
    suspend fun getRecipes(
        @QueryMap queries: Map<String, String>
    ): Response<FoodData>

    @GET("/recipes/complexSearch")
    suspend fun searchRecipes(
        @QueryMap searchQuery: Map<String, String>
    ): Response<FoodData>

    @GET("food/jokes/random")
    suspend fun getFoodJoke(
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY
    ): Response<FoodJoke>

}