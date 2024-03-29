package com.borshevskiy.foodapp.data

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import com.borshevskiy.foodapp.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.borshevskiy.foodapp.util.Constants.Companion.DEFAULT_MEAL_TYPE
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

@ActivityRetainedScoped
class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context) {
    companion object {
        private const val PREFERENCES_NAME = "foodApp_preferences"
        private const val PREFERENCES_MEAL_TYPE = "mealType"
        private const val PREFERENCES_MEAL_TYPE_ID = "mealTypeId"
        private const val PREFERENCES_DIET_TYPE = "dietType"
        private const val PREFERENCES_DIET_TYPE_ID = "dietTypeId"
        private const val PREFERENCES_BACK_ONLINE = "backOnline"
    }

    private object PreferenceKeys {
        val selectedMealType = preferencesKey<String>(PREFERENCES_MEAL_TYPE)
        val selectedMealTypeId = preferencesKey<Int>(PREFERENCES_MEAL_TYPE_ID)
        val selectedDietType = preferencesKey<String>(PREFERENCES_DIET_TYPE)
        val selectedDietTypeId = preferencesKey<Int>(PREFERENCES_DIET_TYPE_ID)
        val backOnline = preferencesKey<Boolean>(PREFERENCES_BACK_ONLINE)
    }

    private val dataStore: DataStore<Preferences> = context.createDataStore(PREFERENCES_NAME)

    suspend fun saveMealAndDietType(mealType: String, mealTypeId: Int, dietType: String, dietTypeId: Int) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.selectedMealType] = mealType
            preferences[PreferenceKeys.selectedMealTypeId] = mealTypeId
            preferences[PreferenceKeys.selectedDietType] = dietType
            preferences[PreferenceKeys.selectedDietTypeId] = dietTypeId
        }
    }

    suspend fun saveBackOnline(backOnline: Boolean) {
        dataStore.edit { it[PreferenceKeys.backOnline] = backOnline }
    }

    val readMealAndDietType: Flow<MealAndDietType> = dataStore.data.catch { exception ->
        if (exception is IOException) emit(emptyPreferences()) else throw exception }.map { preferences ->
        MealAndDietType(
            preferences[PreferenceKeys.selectedMealType] ?: DEFAULT_MEAL_TYPE,
            preferences[PreferenceKeys.selectedMealTypeId] ?: 0,
            preferences[PreferenceKeys.selectedDietType] ?: DEFAULT_DIET_TYPE,
            preferences[PreferenceKeys.selectedDietTypeId] ?: 0)
    }

    val readBackOnline: Flow<Boolean> = dataStore.data.catch { exception ->
        if (exception is IOException) emit(emptyPreferences()) else throw exception }.map {
            preferences -> preferences[PreferenceKeys.backOnline] ?: false }
}

data class MealAndDietType(
    val selectedMealType: String,
    val selectedMealTypeId: Int,
    val selectedDietType: String,
    val selectedDietTypeId: Int
)