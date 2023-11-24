package com.borshevskiy.foodapp.data.database

import androidx.room.TypeConverter
import com.borshevskiy.foodapp.domain.models.FoodData
import com.borshevskiy.foodapp.domain.models.Food
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecipesTypeConverter {

    var gson = Gson()

    @TypeConverter
    fun foodRecipeToString(foodData: FoodData): String = gson.toJson(foodData)

    @TypeConverter
    fun stringToFoodRecipe(data: String): FoodData = gson.fromJson(data, object : TypeToken<FoodData>() {}.type)

    @TypeConverter
    fun resultToString(food: Food): String = gson.toJson(food)

    @TypeConverter
    fun stringToResult(data: String): Food =gson.fromJson(data, object : TypeToken<Food>() {}.type)
}