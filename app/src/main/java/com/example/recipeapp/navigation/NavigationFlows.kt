package com.example.recipeapp.navigation

import android.content.Context
import com.example.recipeapp.authenticationflow.view.LoginActivity
import com.example.recipeapp.authenticationflow.view.SignUpActivity
import com.example.recipeapp.onboardingflow.view.OnboardingActivity

sealed class NavigationFlows(val navigation: Navigator) {

    class OnBoardingFlow(navigation: Navigator): NavigationFlows(navigation) {
        inner class ToOnboarding {
            fun navigate(context: Context) {
                navigation.toActivity(context, OnboardingActivity::class.java)
            }
        }
    }

    class AuthenticationFlow(navigation: Navigator): NavigationFlows(navigation) {
        inner class ToSignUp {
            fun navigate(context: Context) {
                navigation.toActivity(context, SignUpActivity::class.java)
            }
        }

        inner class ToLogin {
            fun navigate(context: Context) {
                navigation.toActivity(context, LoginActivity::class.java)
            }
        }
    }

    class RecipeFlow(navigation: Navigator): NavigationFlows(navigation) {
        inner class ToRecipeHome {}
        inner class ToSearchRecipe {}
        inner class ToRecipeList {}
        inner class ToRecipeDetail {}
    }
}
