package de.lucas.cookingrecipes.recipe.data

import de.lucas.cookingrecipes.core.data.serialization.UrlEncoder
import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    val username: String = "",
    @Serializable(with = UrlEncoder::class)
    val picture: String? = "",
    val timestamp: Long = 0,
    val comment: String = "",
)
