package de.lucas.cookingrecipes.details.di

import de.lucas.cookingrecipes.details.data.RecipeDetailRepositoryImpl
import de.lucas.cookingrecipes.details.domain.RecipeDetailRepository
import de.lucas.cookingrecipes.details.presentation.RecipeDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val recipeDetailModule = module {
    single<RecipeDetailRepository> { RecipeDetailRepositoryImpl(get()) }
    viewModelOf(::RecipeDetailViewModel)
}