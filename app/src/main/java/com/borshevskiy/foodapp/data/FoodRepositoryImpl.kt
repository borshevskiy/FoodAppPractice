package com.borshevskiy.foodapp.data

import com.borshevskiy.foodapp.data.database.RecipesDAO
import com.borshevskiy.foodapp.data.database.entities.FavoritesEntity
import com.borshevskiy.foodapp.data.database.entities.FoodJokeEntity
import com.borshevskiy.foodapp.data.database.entities.RecipesEntity
import com.borshevskiy.foodapp.data.network.FoodRecipesApi
import com.borshevskiy.foodapp.domain.FoodRepository
import com.borshevskiy.foodapp.domain.models.Food
import com.borshevskiy.foodapp.domain.models.FoodData
import com.borshevskiy.foodapp.domain.models.FoodJoke
import com.borshevskiy.foodapp.util.NetworkResult
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.*
import retrofit2.Response
import javax.inject.Inject

@ActivityRetainedScoped
class FoodRepositoryImpl @Inject constructor(private val recipesDAO: RecipesDAO,
    private val foodRecipesApi: FoodRecipesApi): FoodRepository {

    override suspend fun getFoodList(searchQuery: Map<String, String>) = handleResponse(foodRecipesApi.getRecipes(searchQuery))

    override suspend fun searchFood(searchQuery: Map<String, String>) = handleResponse(foodRecipesApi.searchRecipes(searchQuery))

    override suspend fun getFoodJoke() = handleResponse(foodRecipesApi.getFoodJoke())

    override suspend fun insertRecipes(foodData: FoodData) {
        recipesDAO.insertRecipes(RecipesEntity(foodData))
    }

    override suspend fun insertFavoriteRecipe(food: Food) {
        recipesDAO.insertFavoriteRecipe(FavoritesEntity(0, food))
    }

    override suspend fun insertFoodJoke(foodJoke: FoodJoke) {
        recipesDAO.insertFoodJoke(FoodJokeEntity(foodJoke))
    }

    override fun readRecipes() = recipesDAO.readRecipes().map { listOfEntities ->
        listOfEntities.map { it.foodData }
    }

    override fun readFavoriteRecipes() = recipesDAO.readFavoriteRecipe().map { listOfEntities ->
        listOfEntities.map { it.food }
    }


    override fun readFoodJoke() = recipesDAO.readFoodJoke().map { listOfEntities ->
        listOfEntities.map { it.foodJoke }
    }

    override suspend fun deleteFavoriteRecipe(id: Int, food: Food) {
        recipesDAO.deleteFavoriteRecipe(FavoritesEntity(id, food))
    }

    override suspend fun deleteAllFavoriteRecipes() { recipesDAO.deleteAllFavoriteRecipe() }

    private fun <T> handleResponse(response: Response<T>): NetworkResult<T> {
        return when {
            response.message().toString().contains("timeout") -> NetworkResult.Error("Timeout")
            response.code() == 402 -> NetworkResult.Error("API Key Limited.")
            response.isSuccessful -> NetworkResult.Success(response.body()!!)
            else -> NetworkResult.Error(response.message())
        }
    }
}