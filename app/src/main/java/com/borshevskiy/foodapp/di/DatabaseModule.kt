package com.borshevskiy.foodapp.di

import android.content.Context
import androidx.room.Room
import com.borshevskiy.foodapp.data.database.RecipesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDAO(@ApplicationContext context: Context) = Room.databaseBuilder(context,
        RecipesDatabase::class.java, "recipes_database").build().recipesDao()
}