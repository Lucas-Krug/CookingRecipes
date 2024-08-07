package de.lucas.cookingrecipes.recipe.di

import de.lucas.cookingrecipes.recipe.data.RecipeRepositoryImpl
import de.lucas.cookingrecipes.recipe.domain.RecipeRepository
import de.lucas.cookingrecipes.recipe.presentation.RecipeViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val recipeModule = module {
    single<RecipeRepository> { RecipeRepositoryImpl(get()) }
    viewModelOf(::RecipeViewModel)
}