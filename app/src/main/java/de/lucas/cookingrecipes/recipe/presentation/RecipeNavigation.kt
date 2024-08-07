package de.lucas.cookingrecipes.recipe.presentation

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.lucas.cookingrecipes.recipe.data.Recipe
import de.lucas.cookingrecipes.auth.presentation.user.UserState
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
object RecipeScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.RecipeNavigation(
    onClickRecipe: (Recipe, String) -> Unit,
    userState: UserState,
    snackbarHostState: SnackbarHostState,
    userRecipes: List<String>,
    favoriteListIds: List<String>,
    onCreateRecipe: () -> Unit,
    navigateToProfile: () -> Unit,
    showLoginSheet: () -> Unit,
    logout: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    val viewModel: RecipeViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var tagRecipeList by rememberSaveable { mutableStateOf<Map<String, List<Recipe>>>(emptyMap()) }

    if (tagRecipeList.isEmpty()) {
        tagRecipeList = viewModel.getRecipesByTag()
    }

    RecipeScreen(
        uiState = uiState,
        recommendationList = viewModel.recommendationRecipes(),
        userRecipes = viewModel.userRecipes(userRecipes),
        favoriteList = viewModel.favoriteRecipes(favoriteListIds),
        tagLists = tagRecipeList,
        noTagList = viewModel.getRecipeWithoutTag(),
        onClickReload = viewModel::getRecipes,
        onClickRecipe = onClickRecipe,
        userState = userState,
        snackbarHostState = snackbarHostState,
        onCreateRecipe = onCreateRecipe,
        navigateToProfile = navigateToProfile,
        showLoginSheet = showLoginSheet,
        logout = logout,
        animatedVisibilityScope = animatedVisibilityScope,
    )
}