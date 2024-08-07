package de.lucas.cookingrecipes.create.di

import de.lucas.cookingrecipes.create.data.CreateRecipeRepositoryImpl
import de.lucas.cookingrecipes.create.domain.CreateRecipeRepository
import de.lucas.cookingrecipes.create.presentation.CreateRecipeViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val createRecipeModule = module {
    single<CreateRecipeRepository> { CreateRecipeRepositoryImpl(get()) }
    viewModelOf(::CreateRecipeViewModel)
}