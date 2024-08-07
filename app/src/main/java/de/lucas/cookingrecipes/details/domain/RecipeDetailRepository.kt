package de.lucas.cookingrecipes.details.domain

import de.lucas.cookingrecipes.recipe.data.Comment
import de.lucas.cookingrecipes.recipe.data.Recipe
import de.lucas.cookingrecipes.auth.data.user.UserData

interface RecipeDetailRepository {
    suspend fun getRecipeDetails(key: String, onSuccess: (Recipe) -> Unit)

    suspend fun onRating(
        userId: String,
        userRatingKey: Int?,
        lastUserRatingKey: Int,
        recipeKey: String,
        recipeId: String,
        userRating: Float,
        ratingSum: Float,
        ratedNumber: Int,
    )

    suspend fun onSetFavorite(
        userId: String,
        recipeId: String,
        newKey: Int,
        key: Int?,
        isFavorite: Boolean,
    )

    suspend fun onSendComment(
        key: String,
        user: UserData,
        comment: Comment,
        commentCount: Int,
    )
}