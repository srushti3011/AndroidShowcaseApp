package com.example.recipeapp.homeflow.model

enum class DietType {
    VEG, VEGAN;

    override fun toString(): String {
        return when(this) {
            VEG -> {
                "vegetarian"
            }
            VEGAN -> {
                "vegan"
            }
        }
    }
}