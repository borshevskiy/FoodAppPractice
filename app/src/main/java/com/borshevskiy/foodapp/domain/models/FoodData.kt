package com.borshevskiy.foodapp.domain.models

import com.google.gson.annotations.SerializedName

data class FoodData(@SerializedName("results") val results: List<Food>)