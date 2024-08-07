package de.lucas.cookingrecipes.recipe.data

import kotlinx.serialization.Serializable

@Serializable
data class Rating(
    val id: String = "",
    val rating: Float = 0f,
)