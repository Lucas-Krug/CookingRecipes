package de.lucas.cookingrecipes.create.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.lucas.cookingrecipes.auth.data.user.UserData
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
object CreateRecipe

@Composable
fun CreateRecipeNavigation(
    userData: UserData?,
    onCancelCreation: () -> Unit,
    onSuccess: (String) -> Unit,
    onError: (String) -> Unit,
) {
    val viewModel: CreateRecipeViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    if (userData != null) {
        CreateRecipeScreen(
            uiState = uiState,
            userData = userData,
            onCreateRecipe = { recipe ->
                viewModel.createRecipe(
                    userData = userData,
                    recipe = recipe
                )
            },
            onCancelCreation = onCancelCreation,
            onSuccess = onSuccess,
            onError = onError,
        )
    }
}