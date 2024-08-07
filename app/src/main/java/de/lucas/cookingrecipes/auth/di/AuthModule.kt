package de.lucas.cookingrecipes.auth.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import de.lucas.cookingrecipes.auth.data.user.UserRepositoryImpl
import de.lucas.cookingrecipes.auth.domain.user.UserRepository
import de.lucas.cookingrecipes.core.data.db.Preferences
import de.lucas.cookingrecipes.auth.presentation.user.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val authModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseDatabase.getInstance() }
    single { Preferences(get()) }
    single<UserRepository> { UserRepositoryImpl(get(), get(), get()) }
    viewModelOf(::UserViewModel)
}