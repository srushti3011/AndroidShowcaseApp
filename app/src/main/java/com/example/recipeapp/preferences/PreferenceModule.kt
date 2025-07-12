package com.example.recipeapp.preferences

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface PreferenceModule {

    @Binds
    fun bindUserPreference(impl: UserPreferenceManagerImpl): UserPreferenceManager
}