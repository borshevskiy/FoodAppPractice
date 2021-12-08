package com.borshevskiy.foodapp.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.borshevskiy.foodapp.models.FoodRecipe
import com.borshevskiy.foodapp.util.Constants.Companion.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
class RecipesEntity(
    var foodRecipe: FoodRecipe
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}