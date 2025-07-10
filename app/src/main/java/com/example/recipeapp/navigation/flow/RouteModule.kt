package com.example.recipeapp.navigation.flow

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RouteModule {

    @Binds
    fun bindOnboardingRoute(impl: OnboardingRouteImpl): OnboardingRoute

    @Binds
    fun bindAuthenticationRoute(impl: AuthenticationRouteImpl): AuthenticationRoute
}