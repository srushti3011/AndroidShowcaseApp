package com.example.recipeapp.homeflow.model

enum class MealType {
    MAINCOURSE, BREAKFAST, SIDEDISH;

    override fun toString(): String {
        return when(this) {
            MAINCOURSE -> {
                "main course"
            }
            BREAKFAST -> {
                "breakfast"
            }
            SIDEDISH -> {
                "side dish"
            }
        }
    }
}