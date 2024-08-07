package de.lucas.cookingrecipes.profile.di

import de.lucas.cookingrecipes.profile.data.ProfileRepositoryImpl
import de.lucas.cookingrecipes.profile.domain.ProfileRepository
import de.lucas.cookingrecipes.profile.presentation.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val profileModule = module {
    single<ProfileRepository> { ProfileRepositoryImpl(get(), get()) }
    viewModelOf(::ProfileViewModel)
}