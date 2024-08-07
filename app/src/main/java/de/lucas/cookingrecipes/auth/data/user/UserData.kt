package de.lucas.cookingrecipes.auth.data.user

import de.lucas.cookingrecipes.core.data.serialization.UrlEncoder
import de.lucas.cookingrecipes.recipe.data.Rating
import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val id: String? = null,
    val name: String? = null,
    val email: String? = null,
    @Serializable(with = UrlEncoder::class)
    val picture: String = "",
    val myRecipes: List<String> = emptyList(),
    val ratings: List<Rating> = emptyList(),
    val favorites: List<String> = emptyList(),
    val commentCount: Int = 0,
)