package com.example.recipeapp.network

import com.example.recipeapp.network.networkrepository.RecipeRepository
import com.example.recipeapp.network.networkrepository.RecipeRepositoryImpl
import com.example.recipeapp.network.networkrepository.UserRepositoryImpl
import com.example.recipeapp.network.networkrepository.UserRespository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun bindUserRepo(impl: UserRepositoryImpl): UserRespository

    @Binds
    fun bindRecipeRepo(impl: RecipeRepositoryImpl): RecipeRepository
}