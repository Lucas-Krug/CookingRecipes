package de.lucas.cookingrecipes.create.domain

import de.lucas.cookingrecipes.recipe.data.Recipe
import de.lucas.cookingrecipes.auth.data.user.UserData

interface CreateRecipeRepository {
    suspend fun createRecipe(userData: UserData, recipe: Recipe): String
}