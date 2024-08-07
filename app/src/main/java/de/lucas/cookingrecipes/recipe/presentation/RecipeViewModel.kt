package de.lucas.cookingrecipes.recipe.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.lucas.cookingrecipes.recipe.data.Recipe
import de.lucas.cookingrecipes.recipe.domain.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecipeViewModel(private val recipeRepository: RecipeRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<RecipeState>(RecipeState.Idle)
    val uiState = _uiState.asStateFlow()

    init {
        if (uiState.value != RecipeState.Success()) {
            getRecipes()
        }
    }

    fun getRecipes() = viewModelScope.launch {
        _uiState.value = RecipeState.Loading
        recipeRepository.getRecipes(
            onSuccess = { _uiState.value = RecipeState.Success(it) },
            onError = { _uiState.value = RecipeState.Error(null) }
        )
    }

    fun recommendationRecipes(): List<Recipe> = uiState.value.let {
        if (it is RecipeState.Success) {
            val recipes = it.recipes.filter { recipe -> recipe.top10 != 0 }
                .sortedBy { recipe -> recipe.top10 }
            return recipes
        } else {
            emptyList()
        }
    }

    fun userRecipes(userRecipeIds: List<String>): List<Recipe> = uiState.value.let {
        if (it is RecipeState.Success && userRecipeIds.isNotEmpty()) {
            return it.recipes.filter { recipe -> recipe.id in userRecipeIds }
        } else {
            emptyList()
        }
    }

    fun favoriteRecipes(favoriteListIds: List<String>): List<Recipe> = uiState.value.let {
        if (it is RecipeState.Success && favoriteListIds.isNotEmpty()) {
            return it.recipes.filter { recipe -> recipe.id in favoriteListIds }
        } else {
            emptyList()
        }
    }

    fun getRecipesByTag(): Map<String, List<Recipe>> = uiState.value.let { uiState ->
        val recipes = mutableMapOf<String, List<Recipe>>()
        val tags = MainTags.entries
        if (uiState is RecipeState.Success) {
            tags.forEach { tag ->
                recipes[tag.tag] = uiState.recipes.filter { recipe ->
                    recipe.tags.contains(tag.tag)
                }
            }
        }
        return recipes
    }

    fun getRecipeWithoutTag(): List<Recipe> = uiState.value.let { uiState ->
        val recipes = mutableListOf<Recipe>()
        val tags = MainTags.entries.map { it.tag }
        if (uiState is RecipeState.Success) {
            uiState.recipes.forEach { recipe ->
                if (recipe.tags.none { it in tags }) {
                    recipes.add(recipe)
                }
            }
        }
        return recipes
    }
}

enum class MainTags(val tag: String) {
    VEGAN("Vegan"),
    VEGETARIAN("Vegetarian"),
    ITALIAN("Italian"),
    GREEK("Greek"),
    FRENCH("French"),
    SPANISH("Spanish"),
    JAPANESE("Japanese"),
    CUBAN("Cuban"),
    GERMAN("German")
}

sealed class RecipeState {
    data object Idle : RecipeState()
    data object Loading : RecipeState()
    data class Success(val recipes: List<Recipe> = emptyList()) : RecipeState()
    data class Error(val message: String?) : RecipeState()
}