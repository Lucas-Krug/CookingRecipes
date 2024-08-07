package de.lucas.cookingrecipes.details.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.lucas.cookingrecipes.recipe.data.Comment
import de.lucas.cookingrecipes.recipe.data.Recipe
import de.lucas.cookingrecipes.auth.domain.user.UserRepository
import de.lucas.cookingrecipes.details.domain.RecipeDetailRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecipeDetailViewModel(
    private val recipeDetailRepository: RecipeDetailRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _recipe = MutableStateFlow<Recipe?>(null)
    val recipe = _recipe.asStateFlow()

    fun getRecipeDetails(key: String) = viewModelScope.launch {
        recipeDetailRepository.getRecipeDetails(
            key = key,
            onSuccess = { recipe -> _recipe.value = recipe },
        )
    }

    fun onRating(
        recipeId: String,
        recipeKey: String,
        userRatingKey: Int? = null,
        lastUserKey: Int,
        rating: Float,
        ratingSum: Float,
        ratedNumber: Int,
    ) = viewModelScope.launch {
        val user = userRepository.getUserFromDb()
        if (user != null) {
            recipeDetailRepository.onRating(
                userId = user.id ?: "",
                userRatingKey = userRatingKey,
                lastUserRatingKey = lastUserKey + 1,
                recipeKey = recipeKey,
                recipeId = recipeId,
                userRating = rating,
                ratingSum = ratingSum,
                ratedNumber = ratedNumber,
            )
        }
    }

    fun onSetFavorite(recipeId: String, lastKey: Int, key: Int?, isFavorite: Boolean) =
        viewModelScope.launch {
            val user = userRepository.getUserFromDb()
            if (user != null) {
                recipeDetailRepository.onSetFavorite(
                    userId = user.id ?: "",
                    recipeId = recipeId,
                    newKey = lastKey + 1,
                    key = key,
                    isFavorite = isFavorite,
                )
            }
        }

    fun onComment(key: String, comment: String, commentCount: Int) = viewModelScope.launch {
        val user = userRepository.getUserFromDb()
        if (user != null) {
            recipeDetailRepository.onSendComment(
                key = key,
                user = user,
                comment = Comment(
                    username = user.name ?: "",
                    picture = user.picture,
                    timestamp = System.currentTimeMillis(),
                    comment = comment,
                ),
                commentCount = commentCount,
            )
        }
    }
}
