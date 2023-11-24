package com.borshevskiy.foodapp.domain

import javax.inject.Inject

class SearchFoodUseCase @Inject constructor(private val repository: FoodRepository) {

    suspend operator fun invoke(searchQuery: Map<String, String>) = repository.searchFood(searchQuery)

}