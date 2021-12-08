package com.borshevskiy.foodapp.data.database

import androidx.room.*
import com.borshevskiy.foodapp.data.database.entities.FavoritesEntity
import com.borshevskiy.foodapp.data.database.entities.FoodJokeEntity
import com.borshevskiy.foodapp.data.database.entities.RecipesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipesEntity: RecipesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteRecipe(favoritesEntity: FavoritesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodJoke(foodJokeEntity: FoodJokeEntity)

    @Query("SELECT * FROM RECIPES_TABLE ORDER BY id ASC")
    fun readRecipes(): Flow<List<RecipesEntity>>

    @Query("SELECT * FROM FAVORITE_RECIPES_TABLE ORDER BY id ASC")
    fun readFavoriteRecipe(): Flow<List<FavoritesEntity>>

    @Query("SELECT * FROM FOOD_JOKE_TABLE ORDER BY id ASC")
    fun readFoodJoke(): Flow<List<FoodJokeEntity>>

    @Delete
    suspend fun deleteFavoriteRecipe(favoritesEntity: FavoritesEntity)

    @Query("DELETE FROM FAVORITE_RECIPES_TABLE")
    suspend fun deleteAllFavoriteRecipe()
}