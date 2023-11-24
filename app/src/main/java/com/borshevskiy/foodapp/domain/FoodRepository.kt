package com.borshevskiy.foodapp.domain

import com.borshevskiy.foodapp.domain.models.Food
import com.borshevskiy.foodapp.domain.models.FoodData
import com.borshevskiy.foodapp.domain.models.FoodJoke
import com.borshevskiy.foodapp.util.NetworkResult
import kotlinx.coroutines.flow.Flow

interface FoodRepository {

    suspend fun getFoodList(searchQuery: Map<String, String>): NetworkResult<FoodData>

    suspend fun searchFood(searchQuery: Map<String, String>): NetworkResult<FoodData>

    suspend fun getFoodJoke(): NetworkResult<FoodJoke>

    suspend fun insertRecipes(foodData: FoodData)

    suspend fun insertFavoriteRecipe(food: Food)

    suspend fun insertFoodJoke(foodJoke: FoodJoke)

    fun readRecipes(): Flow<List<FoodData>>

    fun readFavoriteRecipes(): Flow<List<Food>>

    fun readFoodJoke(): Flow<List<FoodJoke>>

    suspend fun deleteFavoriteRecipe(id: Int, food: Food)

    suspend fun deleteAllFavoriteRecipes()

}