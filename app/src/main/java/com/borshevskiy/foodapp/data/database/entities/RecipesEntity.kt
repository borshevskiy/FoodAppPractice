package com.borshevskiy.foodapp.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.borshevskiy.foodapp.domain.models.FoodData

@Entity(tableName = "recipes_table")
class RecipesEntity(var foodData: FoodData) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}