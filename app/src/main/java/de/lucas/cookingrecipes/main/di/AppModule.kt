package de.lucas.cookingrecipes.main.di

import de.lucas.cookingrecipes.auth.di.authModule
import de.lucas.cookingrecipes.create.di.createRecipeModule
import de.lucas.cookingrecipes.details.di.recipeDetailModule
import de.lucas.cookingrecipes.profile.di.profileModule
import de.lucas.cookingrecipes.recipe.di.recipeModule

fun appModules() = listOf(
    authModule,
    profileModule,
    recipeModule,
    recipeDetailModule,
    createRecipeModule,
)