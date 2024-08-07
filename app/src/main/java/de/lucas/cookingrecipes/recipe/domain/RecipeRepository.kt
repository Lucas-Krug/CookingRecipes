package de.lucas.cookingrecipes.recipe.domain

import de.lucas.cookingrecipes.recipe.data.Recipe

interface RecipeRepository {
    suspend fun getRecipes(onSuccess: (List<Recipe>) -> Unit, onError: () -> Unit)
}