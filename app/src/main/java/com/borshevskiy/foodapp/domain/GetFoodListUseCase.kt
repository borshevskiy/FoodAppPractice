package com.borshevskiy.foodapp.domain

import javax.inject.Inject

class GetFoodListUseCase @Inject constructor(private val repository: FoodRepository) {

    suspend operator fun invoke(searchQuery: Map<String, String>) = repository.getFoodList(searchQuery)
}