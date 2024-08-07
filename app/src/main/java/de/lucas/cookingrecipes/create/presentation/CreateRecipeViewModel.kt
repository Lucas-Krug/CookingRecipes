package de.lucas.cookingrecipes.create.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.lucas.cookingrecipes.recipe.data.Recipe
import de.lucas.cookingrecipes.auth.data.user.UserData
import de.lucas.cookingrecipes.create.domain.CreateRecipeRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class CreateRecipeViewModel(
    private val repository: CreateRecipeRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<CreateRecipeState>(CreateRecipeState.Idle)
    val uiState = _uiState.asStateFlow()

    fun createRecipe(userData: UserData, recipe: Recipe) = viewModelScope.launch {
        _uiState.update { CreateRecipeState.Loading }
        delay(1000)
        try {
            repository.createRecipe(userData = userData, recipe = recipe)
            _uiState.update { CreateRecipeState.Success }
        } catch (e: Exception) {
            Timber.e(e)
            _uiState.update { CreateRecipeState.Error(e.message) }
        }
    }
}

sealed class CreateRecipeState {
    data object Idle : CreateRecipeState()
    data object Loading : CreateRecipeState()
    data object Success : CreateRecipeState()
    data class Error(val message: String?) : CreateRecipeState()
}