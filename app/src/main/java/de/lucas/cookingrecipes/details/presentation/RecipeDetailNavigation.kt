package de.lucas.cookingrecipes.details.presentation

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.lucas.cookingrecipes.auth.data.user.UserData
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@Serializable
data class RecipeDetail(val key: String, val category: String)

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.RecipeDetailNavigation(
    key: String,
    userData: UserData? = null,
    category: String,
    onClickBack: () -> Unit,
    showLoginSheet: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    val viewModel: RecipeDetailViewModel = koinViewModel()
    viewModel.getRecipeDetails(key)
    val recipeState by viewModel.recipe.collectAsStateWithLifecycle()
    recipeState?.let { recipe ->
        RecipeDetailsScreen(
            isLoggedIn = userData != null,
            recipe = recipe,
            favorite = userData?.favorites?.contains(recipe.id) ?: false,
            ratingList = userData?.ratings ?: emptyList(),
            category = category,
            onClickFavorite = { isFavorite ->
                viewModel.onSetFavorite(
                    recipeId = recipe.id,
                    lastKey = userData?.favorites?.size?.minus(1) ?: 0,
                    key = userData?.favorites?.indexOf(recipe.id) ?: 0,
                    isFavorite = isFavorite,
                )
                Timber.e(isFavorite.toString())
            },
            onRatingChanged = { userRating ->
                viewModel.onRating(
                    recipeId = recipe.id,
                    recipeKey = key,
                    userRatingKey = userData?.ratings?.indexOfFirst { it.id == recipe.id },
                    lastUserKey = userData?.ratings?.size?.minus(1) ?: 0,
                    rating = userRating,
                    ratingSum = recipe.ratingSum,
                    ratedNumber = recipe.ratedNumber,
                )
            },
            onComment = { comment ->
                viewModel.onComment(
                    key = key,
                    comment = comment,
                    commentCount = recipe.comments?.size ?: 0,
                )
            },
            onClickBack = onClickBack,
            showLoginSheet = showLoginSheet,
            animatedVisibilityScope = animatedVisibilityScope,
        )
    }
}
