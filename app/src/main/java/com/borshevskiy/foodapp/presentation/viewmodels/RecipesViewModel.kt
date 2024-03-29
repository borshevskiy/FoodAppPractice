package com.borshevskiy.foodapp.presentation.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.borshevskiy.foodapp.BuildConfig
import com.borshevskiy.foodapp.data.DataStoreRepository
import com.borshevskiy.foodapp.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.borshevskiy.foodapp.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.borshevskiy.foodapp.util.Constants.Companion.DEFAULT_RECIPES_NUMBER
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository ): AndroidViewModel(application) {

    private var mealType = DEFAULT_MEAL_TYPE
    private var dietType = DEFAULT_DIET_TYPE

    var networkStatus = false
    var backOnline = false

    val readMealAndDietType by lazy { dataStoreRepository.readMealAndDietType }
    val readBackOnline = dataStoreRepository.readBackOnline.asLiveData()

    fun saveMealAndDietType(mealType:String, mealTypeId:Int, dietType:String, dietTypeId:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveMealAndDietType(mealType, mealTypeId, dietType, dietTypeId)
        }
    }

    fun saveBackOnline(backOnline: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveBackOnline(backOnline)
        }
    }

    fun applyQueries(): HashMap<String,String> {
        val queries: HashMap<String,String> = HashMap()

        viewModelScope.launch {
            readMealAndDietType.collect { value ->
                mealType = value.selectedMealType
                dietType = value.selectedDietType
            }
        }

        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY] = BuildConfig.API_KEY
        queries[QUERY_TYPE] = mealType
        queries[QUERY_DIET] = dietType
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }

    fun applySearchQuery(searchQuery: String): HashMap<String,String> {
        val queries: HashMap<String,String> = HashMap()

        queries[QUERY_SEARCH] = searchQuery
        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY] = BuildConfig.API_KEY
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }

    fun showNetworkStatus() {
        if(!networkStatus) {
            Toast.makeText(getApplication(), "No Internet Connetion.",Toast.LENGTH_SHORT).show()
            saveBackOnline(true)
        } else if(networkStatus) {
            if (backOnline) {
                Toast.makeText(getApplication(),"We're back online.", Toast.LENGTH_SHORT).show()
                saveBackOnline(false)
            }
        }
    }

    companion object {
        private const val QUERY_SEARCH = "query"
        private const val QUERY_NUMBER = "number"
        private const val QUERY_API_KEY = "apiKey"
        private const val QUERY_TYPE = "type"
        private const val QUERY_DIET = "diet"
        private const val QUERY_ADD_RECIPE_INFORMATION = "addRecipeInformation"
        private const val QUERY_FILL_INGREDIENTS = "fillIngredients"
    }
}