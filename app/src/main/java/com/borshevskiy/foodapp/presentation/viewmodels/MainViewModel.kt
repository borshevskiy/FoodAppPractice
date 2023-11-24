package com.borshevskiy.foodapp.presentation.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.borshevskiy.foodapp.data.FoodRepositoryImpl
import com.borshevskiy.foodapp.domain.models.Food
import com.borshevskiy.foodapp.domain.models.FoodData
import com.borshevskiy.foodapp.domain.models.FoodJoke
import com.borshevskiy.foodapp.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: FoodRepositoryImpl, application: Application): AndroidViewModel(application) {

    /** ROOM DATABASE */

    val readRecipes: LiveData<List<FoodData>> = repository.readRecipes().asLiveData()
    val readFavoriteRecipes: LiveData<List<Food>> = repository.readFavoriteRecipes().asLiveData()
    val readFoodJoke: LiveData<List<FoodJoke>> = repository.readFoodJoke().asLiveData()

    private fun insertRecipes(foodData: FoodData) =
        viewModelScope.launch(Dispatchers.IO) { repository.insertRecipes(foodData) }

    fun insertFavoriteRecipe(food: Food) =
        viewModelScope.launch(Dispatchers.IO) { repository.insertFavoriteRecipe(food) }

    private fun insertFoodJoke(foodJoke: FoodJoke) =
        viewModelScope.launch(Dispatchers.IO) { repository.insertFoodJoke(foodJoke) }

    fun deleteFavoriteRecipe(id: Int, food: Food) =
        viewModelScope.launch(Dispatchers.IO) { repository.deleteFavoriteRecipe(id, food) }

    fun deleteAllFavoriteRecipes() =
        viewModelScope.launch(Dispatchers.IO) { repository.deleteAllFavoriteRecipes() }



    /** RETROFIT */

    var recipeResponse: MutableLiveData<NetworkResult<FoodData>> = MutableLiveData()
    var searchedRecipeResponse: MutableLiveData<NetworkResult<FoodData>> = MutableLiveData()
    var foodJokeResponse: MutableLiveData<NetworkResult<FoodJoke>> = MutableLiveData()

    fun getRecipes(queries: Map<String,String>) = viewModelScope.launch { getRecipesSafeCall(queries) }

    fun searchRecipes(searchQuery: Map<String,String>) = viewModelScope.launch { searchRecipesSafeCall(searchQuery) }

    fun getFoodJoke() = viewModelScope.launch { getFoodJokeSafeCall() }

    private suspend fun getFoodJokeSafeCall() {
        foodJokeResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                foodJokeResponse.value = repository.getFoodJoke()
                val foodJoke = foodJokeResponse.value!!.data
                if (foodJoke != null) offlineCacheFoodJokes(foodJoke)
            } catch (e: Exception) {
                foodJokeResponse.value = NetworkResult.Error("Jokes not found.")
            }
        } else foodJokeResponse.value = NetworkResult.Error("No Internet Connection.")
    }

    private suspend fun searchRecipesSafeCall(searchQuery: Map<String, String>) {
        searchedRecipeResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                searchedRecipeResponse.value = repository.searchFood(searchQuery)
            } catch (e: Exception) {
                searchedRecipeResponse.value = NetworkResult.Error("Recipes not found.")
            }
        } else searchedRecipeResponse.value = NetworkResult.Error("No Internet Connection.")
    }

    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipeResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                recipeResponse.value = repository.getFoodList(queries)
                val foodRecipe = recipeResponse.value!!.data
                if (foodRecipe != null) offlineCacheRecipes(foodRecipe)
            } catch (e: Exception) {
                recipeResponse.value = NetworkResult.Error("Recipes not found.")
            }
        } else recipeResponse.value = NetworkResult.Error("No Internet Connection.")
    }

    private fun offlineCacheRecipes(foodData: FoodData) { insertRecipes(foodData) }

    private fun offlineCacheFoodJokes(foodJoke: FoodJoke) { insertFoodJoke(foodJoke) }

    private fun hasInternetConnection(): Boolean {
            val connectivityManager = getApplication<Application>()
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }

}