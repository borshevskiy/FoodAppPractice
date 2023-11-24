package com.borshevskiy.foodapp.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.borshevskiy.foodapp.domain.models.Food

@Entity(tableName = "favorite_recipes_table")
class FavoritesEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var food: Food
)