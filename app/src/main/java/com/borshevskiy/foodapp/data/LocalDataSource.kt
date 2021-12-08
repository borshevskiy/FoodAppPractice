package com.borshevskiy.foodapp.data

import com.borshevskiy.foodapp.data.database.RecipesDAO
import com.borshevskiy.foodapp.data.database.entities.FavoritesEntity
import com.borshevskiy.foodapp.data.database.entities.FoodJokeEntity
import com.borshevskiy.foodapp.data.database.entities.RecipesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipesDAO: RecipesDAO
) {

    suspend fun insertRecipes(recipesEntity: RecipesEntity) {
        recipesDAO.insertRecipes(recipesEntity)
    }

    suspend fun insertFavoriteRecipe(favoritesEntity: FavoritesEntity) {
        recipesDAO.insertFavoriteRecipe(favoritesEntity)
    }

    suspend fun insertFoodJoke(foodJokeEntity: FoodJokeEntity) {
        recipesDAO.insertFoodJoke(foodJokeEntity)
    }

    fun readRecipes(): Flow<List<RecipesEntity>> {
        return recipesDAO.readRecipes()
    }

    fun readFavoriteRecipes(): Flow<List<FavoritesEntity>> {
        return recipesDAO.readFavoriteRecipe()
    }

    fun readFoodJoke(): Flow<List<FoodJokeEntity>> {
        return recipesDAO.readFoodJoke()
    }

    suspend fun deletetFavoriteRecipe(favoritesEntity: FavoritesEntity) {
        recipesDAO.deleteFavoriteRecipe(favoritesEntity)
    }

    suspend fun deleteAllFavoriteRecipes() {
        recipesDAO.deleteAllFavoriteRecipe()
    }
}