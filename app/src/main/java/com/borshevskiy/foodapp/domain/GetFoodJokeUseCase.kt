package com.borshevskiy.foodapp.domain

import javax.inject.Inject

class GetFoodJokeUseCase @Inject constructor(private val repository: FoodRepository) {

    suspend operator fun invoke() = repository.getFoodJoke()
}