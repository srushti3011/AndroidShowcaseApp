package com.example.recipeapp.navigation

import com.example.recipeapp.navigation.route.AuthenticationRoute
import com.example.recipeapp.navigation.route.AuthenticationRouteImpl
import com.example.recipeapp.navigation.route.OnboardingRoute
import com.example.recipeapp.navigation.route.OnboardingRouteImpl
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